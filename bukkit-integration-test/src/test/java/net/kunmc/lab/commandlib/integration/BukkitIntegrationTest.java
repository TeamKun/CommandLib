package net.kunmc.lab.commandlib.integration;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.BitSet;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Runs the historical Test Plugin command cases against a real Minecraft server.
 *
 * <p>The important behavior here is not plain Java parsing. CommandLib touches Bukkit, Brigadier, and NMS
 * through the server runtime, so this test keeps a real server process in the loop and only automates the
 * manual "start server, join player, inspect result file" workflow.</p>
 */
class BukkitIntegrationTest {
    private static final String TEST_PLAYER_NAME = "Maru32768";
    private static final Logger LOGGER = LoggerFactory.getLogger(BukkitIntegrationTest.class);

    @Test
    void commandlib_test_plugin_succeeds_on_target_bukkit_version_with_a_real_player() throws Exception {
        Assumptions.assumeTrue(Boolean.getBoolean("commandlib.runMinecraftIntegration"),
                               "Minecraft integration test is disabled. Enable an integration test task.");
        assertDockerAvailable();

        String fixtureName = System.getProperty("commandlib.fixtureName");
        String reportFileName = System.getProperty("commandlib.reportFileName",
                                                   "TEST-commandlib-" + fixtureName + ".xml");
        String serverDirectory = System.getProperty("commandlib.serverDirectory", "server");
        String serverJarName = System.getProperty("commandlib.serverJarName", "server.jar");
        int javaVersion = Integer.getInteger("commandlib.minecraftJavaVersion", 17);

        Path fixturesDir = Path.of(System.getProperty("commandlib.fixturesDir"));
        Path testPluginDir = fixturesDir.resolve(fixtureName);
        Path reportFile = fixturesDir.resolve("test-plugin-common/test-results/" + reportFileName);
        Files.deleteIfExists(reportFile);

        try (GenericContainer<?> container = createBukkitContainer(fixturesDir,
                                                                   testPluginDir,
                                                                   serverDirectory,
                                                                   serverJarName,
                                                                   reportFileName,
                                                                   javaVersion)) {
            container.start();

            BotSession client = connectBot(container);
            try {
                Awaitility.await()
                          .atMost(Duration.ofSeconds(30))
                          .until(client::isConnected);

                waitForPlayerJoin(container);
                client.sendCommand("commandlibtest runTests");

                Awaitility.await()
                          .atMost(Duration.ofMinutes(2))
                          .pollInterval(Duration.ofSeconds(1))
                          .until(() -> Files.exists(reportFile) && Files.size(reportFile) > 0);

                assertJUnitReportSucceeded(reportFile);
            } finally {
                client.disconnect("Test completed");
            }
        }
    }

    @Test
    void player_join_log_detection_accepts_bukkit_and_mohist_formats() {
        assertThat(isPlayerJoinLog("[12:47:42 INFO]: Maru32768 joined the game")).isTrue();
        assertThat(isPlayerJoinLog(
                "[12:47:42 INFO]: Maru32768[/172.17.0.1:52648] logged in with entity id 127 at (-245.5, 63, 153.5)")).isTrue();
        assertThat(isPlayerJoinLog("[12:47:42 INFO]: OtherPlayer joined the game")).isFalse();
    }

    private static void waitForPlayerJoin(GenericContainer<?> container) {
        Awaitility.await()
                  .atMost(Duration.ofSeconds(30))
                  .pollInterval(Duration.ofMillis(500))
                  .until(() -> container.getLogs()
                                        .lines()
                                        .anyMatch(BukkitIntegrationTest::isPlayerJoinLog));
    }

    private static boolean isPlayerJoinLog(String line) {
        if (!line.contains(TEST_PLAYER_NAME)) {
            return false;
        }

        return line.contains("joined the game") || line.contains("logged in with entity id");
    }

    private static void assertJUnitReportSucceeded(Path reportFile) throws ParserConfigurationException, IOException, SAXException {
        Document report = DocumentBuilderFactory.newInstance()
                                                .newDocumentBuilder()
                                                .parse(reportFile.toFile());
        Element testSuite = report.getDocumentElement();
        int tests = Integer.parseInt(testSuite.getAttribute("tests"));
        int failures = Integer.parseInt(testSuite.getAttribute("failures"));
        int errors = Integer.parseInt(testSuite.getAttribute("errors"));

        assertThat(tests).isPositive();
        assertThat(failures).as("JUnit XML failure count in " + reportFile)
                            .isZero();
        assertThat(errors).as("JUnit XML error count in " + reportFile)
                          .isZero();
    }

    private static void assertDockerAvailable() {
        try {
            DockerClientFactory.instance()
                               .client();
        } catch (Throwable t) {
            // Fail instead of skipping: these tasks are explicitly requested integration tests, and a missing
            // Docker daemon should be visible as infrastructure failure in CI and local verification.
            fail("Docker is required for BukkitIntegrationTest but is not available: " + t.getMessage(), t);
        }
    }

    private static GenericContainer<?> createBukkitContainer(Path fixturesDir,
                                                             Path testPluginDir,
                                                             String serverDirectory,
                                                             String serverJarName,
                                                             String reportFileName,
                                                             int javaVersion) {
        String containerWorkDir = "/workspace/fixtures/" + testPluginDir.getFileName() + "/" + serverDirectory;

        return new GenericContainer<>("eclipse-temurin:" + javaVersion + "-jre").withExposedPorts(25565)
                                                                                // Bind the whole fixtures tree because the plugin writes its JUnit XML report
                                                                                // outside the versioned server directory.
                                                                                .withFileSystemBind(fixturesDir.toAbsolutePath()
                                                                                                               .toString(),
                                                                                                    "/workspace/fixtures",
                                                                                                    BindMode.READ_WRITE)
                                                                                .withWorkingDirectory(containerWorkDir)
                                                                                .withLogConsumer(new Slf4jLogConsumer(
                                                                                        LOGGER))
                                                                                .withCommand("sh",
                                                                                             "-lc",
                                                                                             "printf 'eula=true\n' > eula.txt && "
                                                                                                     // Force offline mode so MCProtocolLib can join as a local fake player
                                                                                                     // without requiring Mojang authentication in CI.
                                                                                                     + "printf 'online-mode=false\nserver-port=25565\nenforce-secure-profile=false\nmotd=CommandLib IT\n' > server.properties && " + "java -Dplugin.env=CI -Dcommandlib.testReportName=" + reportFileName + " -jar " + serverJarName + " nogui")
                                                                                .waitingFor(Wait.forLogMessage(
                                                                                        ".*Done \\(.*\\)! For help, type \"help\".*",
                                                                                        1))
                                                                                .withStartupTimeout(Duration.ofMinutes(8));
    }

    private static BotSession connectBot(GenericContainer<?> container) {
        try {
            return connectWithLegacyProtocol(container);
        } catch (ReflectiveOperationException ignored) {
            try {
                return connectWithModernProtocol(container);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Failed to create an MCProtocolLib client session.", e);
            }
        }
    }

    private static BotSession connectWithLegacyProtocol(GenericContainer<?> container) throws ReflectiveOperationException {
        Class<?> protocolClass = Class.forName("com.github.steveice10.mc.protocol.MinecraftProtocol");

        Object protocol = protocolClass.getConstructor(String.class)
                                       .newInstance(TEST_PLAYER_NAME);
        Object session = createLegacySession(container, protocolClass, protocol);
        Class<?> sessionClass = session.getClass();
        sessionClass.getMethod("connect", boolean.class)
                    .invoke(session, false);
        return createBotSession(session, sessionClass);
    }

    private static Object createLegacySession(GenericContainer<?> container,
                                              Class<?> protocolClass,
                                              Object protocol) throws ReflectiveOperationException {
        Class<?> sessionClass = Class.forName("com.github.steveice10.packetlib.tcp.TcpClientSession");
        Constructor<?> constructor = findNullableSessionConstructor(sessionClass, protocolClass, 3);
        if (constructor != null) {
            return constructor.newInstance(container.getHost(), container.getMappedPort(25565), protocol);
        }

        // Older PacketLib versions create sessions through Client + TcpSessionFactory instead of exposing a
        // direct TcpClientSession(host, port, protocol) constructor.
        Class<?> sessionFactoryClass = Class.forName("com.github.steveice10.packetlib.tcp.TcpSessionFactory");
        Object sessionFactory = sessionFactoryClass.getConstructor()
                                                   .newInstance();
        Class<?> clientClass = Class.forName("com.github.steveice10.packetlib.Client");
        Constructor<?> clientConstructor = findClientConstructor(clientClass, protocolClass, sessionFactoryClass);
        Object client = clientConstructor.newInstance(container.getHost(),
                                                      container.getMappedPort(25565),
                                                      protocol,
                                                      sessionFactory);
        return clientClass.getMethod("getSession")
                          .invoke(client);
    }

    private static BotSession connectWithModernProtocol(GenericContainer<?> container) throws ReflectiveOperationException {
        Class<?> protocolClass = Class.forName("org.geysermc.mcprotocollib.protocol.MinecraftProtocol");
        Object protocol = protocolClass.getConstructor(String.class)
                                       .newInstance(TEST_PLAYER_NAME);

        Class<?> sessionClass = Class.forName("org.geysermc.mcprotocollib.network.tcp.TcpClientSession");
        Constructor<?> constructor = findSessionConstructor(sessionClass, protocolClass);
        Object session = constructor.newInstance(container.getHost(), container.getMappedPort(25565), protocol);
        sessionClass.getMethod("connect", boolean.class)
                    .invoke(session, false);
        return createBotSession(session, sessionClass);
    }

    private static Constructor<?> findSessionConstructor(Class<?> sessionClass, Class<?> protocolClass) {
        Constructor<?> constructor = findNullableSessionConstructor(sessionClass, protocolClass, 3);
        if (constructor != null) {
            return constructor;
        }
        throw new IllegalStateException("Unable to find a compatible TcpClientSession constructor for " + sessionClass.getName());
    }

    private static Constructor<?> findNullableSessionConstructor(Class<?> sessionClass,
                                                                 Class<?> protocolClass,
                                                                 int parameterCount) {
        for (Constructor<?> constructor : sessionClass.getConstructors()) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length != parameterCount) {
                continue;
            }
            if (parameterTypes[0] != String.class || parameterTypes[1] != int.class) {
                continue;
            }
            // MCProtocolLib changed concrete protocol types between artifacts; assignability lets the same
            // test code work across those incompatible releases.
            if (parameterTypes[2].isAssignableFrom(protocolClass)) {
                return constructor;
            }
        }
        return null;
    }

    private static Constructor<?> findClientConstructor(Class<?> clientClass,
                                                        Class<?> protocolClass,
                                                        Class<?> sessionFactoryClass) {
        for (Constructor<?> constructor : clientClass.getConstructors()) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length != 4) {
                continue;
            }
            if (parameterTypes[0] != String.class || parameterTypes[1] != int.class) {
                continue;
            }
            if (parameterTypes[2].isAssignableFrom(protocolClass) && parameterTypes[3].isAssignableFrom(
                    sessionFactoryClass)) {
                return constructor;
            }
        }
        throw new IllegalStateException("Unable to find a compatible PacketLib Client constructor for " + clientClass.getName());
    }

    private static BotSession createBotSession(Object session,
                                               Class<?> sessionClass) throws ReflectiveOperationException {
        Method isConnected = sessionClass.getMethod("isConnected");
        Method send = findSendMethod(sessionClass);
        Method disconnect = findDisconnectMethod(sessionClass);
        return new BotSession(session, isConnected, send, disconnect);
    }

    private static Method findSendMethod(Class<?> sessionClass) {
        for (Method method : sessionClass.getMethods()) {
            if (method.getName()
                      .equals("send") && method.getParameterCount() == 1) {
                return method;
            }
        }
        throw new IllegalStateException("Unable to find a compatible send(Packet) method for " + sessionClass.getName());
    }

    private static Method findDisconnectMethod(Class<?> sessionClass) throws NoSuchMethodException {
        try {
            return sessionClass.getMethod("disconnect", String.class);
        } catch (NoSuchMethodException ignored) {
            return sessionClass.getMethod("disconnect", String.class, Throwable.class);
        }
    }

    private static final class BotSession {
        private final Object delegate;
        private final Method isConnectedMethod;
        private final Method sendMethod;
        private final Method disconnectMethod;

        private BotSession(Object delegate, Method isConnectedMethod, Method sendMethod, Method disconnectMethod) {
            this.delegate = delegate;
            this.isConnectedMethod = isConnectedMethod;
            this.sendMethod = sendMethod;
            this.disconnectMethod = disconnectMethod;
        }

        boolean isConnected() {
            try {
                return (boolean) isConnectedMethod.invoke(delegate);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Failed to query MCProtocolLib session state.", e);
            }
        }

        void sendCommand(String command) {
            try {
                sendMethod.invoke(delegate, createCommandPacket(command));
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Failed to send MCProtocolLib command: " + command, e);
            }
        }

        private Object createCommandPacket(String command) throws ReflectiveOperationException {
            String normalizedCommand = command.startsWith("/") ? command.substring(1) : command;
            Object modernCommandPacket = tryCreateStringPacket(
                    "org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket",
                    normalizedCommand);
            if (modernCommandPacket != null) {
                return modernCommandPacket;
            }

            Object signedCommandPacket = tryCreateSignedCommandPacket(
                    "com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket",
                    normalizedCommand);
            if (signedCommandPacket != null) {
                return signedCommandPacket;
            }

            // Minecraft 1.16.5 predates the dedicated command packet. Commands are sent as chat text.
            return Class.forName("com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket")
                        .getConstructor(String.class)
                        .newInstance("/" + normalizedCommand);
        }

        private Object tryCreateStringPacket(String className, String command) throws ReflectiveOperationException {
            try {
                return Class.forName(className)
                            .getConstructor(String.class)
                            .newInstance(command);
            } catch (ClassNotFoundException | NoSuchMethodException ignored) {
                return null;
            }
        }

        private Object tryCreateSignedCommandPacket(String className,
                                                    String command) throws ReflectiveOperationException {
            try {
                return Class.forName(className)
                            .getConstructor(String.class,
                                            long.class,
                                            long.class,
                                            java.util.List.class,
                                            int.class,
                                            BitSet.class)
                            .newInstance(command,
                                         System.currentTimeMillis(),
                                         0L,
                                         Collections.emptyList(),
                                         0,
                                         new BitSet());
            } catch (ClassNotFoundException | NoSuchMethodException ignored) {
                return null;
            }
        }

        void disconnect(String reason) {
            try {
                if (disconnectMethod.getParameterCount() == 1) {
                    disconnectMethod.invoke(delegate, reason);
                } else {
                    disconnectMethod.invoke(delegate, reason, null);
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Failed to disconnect MCProtocolLib session.", e);
            }
        }
    }
}
