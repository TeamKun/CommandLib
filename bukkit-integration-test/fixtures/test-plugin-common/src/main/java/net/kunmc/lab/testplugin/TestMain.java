package net.kunmc.lab.testplugin;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandLib;
import net.kunmc.lab.commandlib.util.bukkit.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TestMain {
    private static final String TEST_PLAYER_NAME = "Maru32768";
    private final Plugin plugin;
    private final Logger logger;


    public TestMain(Plugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    public void register() {
        register(results -> {
            outputResultsToJUnitXml(results);
            errorFailedResults(results);
            logTestResultCount(results);
        });
    }

    public void register(Consumer<List<TestResult>> consumer) {
        Command mainCommand = new MainCommand();
        mainCommand.permission(PermissionDefault.TRUE);
        AtomicBoolean running = new AtomicBoolean(false);

        ArgumentTest argumentTest = new ArgumentTest(mainCommand, TEST_PLAYER_NAME);
        CommandSyntaxExceptionTest commandSyntaxExceptionTest = new CommandSyntaxExceptionTest(mainCommand);
        List<TestBase> tests = List.of(argumentTest, commandSyntaxExceptionTest);
        List<String> commands = tests.stream()
                                     .flatMap(x -> x.build()
                                                    .stream())
                                     .collect(Collectors.toList());

        Runnable execute = () -> {
            logger.info("Received CommandLib test run request.");
            if (!running.compareAndSet(false, true)) {
                logger.info("CommandLib test cases are already running.");
                return;
            }

            Bukkit.getScheduler()
                  .runTaskLater(plugin, () -> {
                      try {
                          logger.info("Executing CommandLib test cases.");
                          for (String command : commands) {
                              logger.info("Dispatching test command: " + command);
                              Bukkit.getServer()
                                    .dispatchCommand(Bukkit.getConsoleSender(), command);
                          }

                          consumer.accept(tests.stream()
                                               .flatMap(x -> x.results()
                                                              .stream())
                                               .collect(Collectors.toList()));

                          tests.forEach(TestBase::clearResults);
                      } finally {
                          running.set(false);
                      }
                  }, 1L);
        };

        mainCommand.addChildren(new Command("runTests") {{
            permission(PermissionDefault.TRUE);
            execute(ctx -> {
                execute.run();
            });
        }});
        CommandLib.register(plugin, mainCommand);
    }

    public void outputResultsToJUnitXml(List<TestResult> results) {
        String reportName = System.getProperty("commandlib.testReportName",
                                               "TEST-commandlib-" + BukkitUtil.getMinecraftVersion() + ".xml");
        Path path = Paths.get("../../test-plugin-common/test-results/" + reportName);
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, buildJUnitXml(results));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String buildJUnitXml(List<TestResult> results) {
        long failedCount = results.stream()
                                  .filter(x -> x.status() == TestStatus.FAILED)
                                  .count();
        String testCases = results.stream()
                                  .sorted(Comparator.comparing(TestResult::key))
                                  .map(this::buildTestCaseXml)
                                  .collect(Collectors.joining());
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<testsuite name=\"CommandLib Test Plugin\" tests=\"" + results.size() + "\" failures=\"" + failedCount + "\" errors=\"0\" skipped=\"0\">\n" + testCases + "</testsuite>\n";
    }

    private String buildTestCaseXml(TestResult result) {
        String className = testClassName(result.key());
        String testName = testMethodName(result.key());
        if (result.status() == TestStatus.SUCCEEDED) {
            return "  <testcase classname=\"" + xmlEscape(className) + "\" name=\"" + xmlEscape(testName) + "\"/>\n";
        }
        return "  <testcase classname=\"" + xmlEscape(className) + "\" name=\"" + xmlEscape(testName) + "\">\n" + "    <failure message=\"" + xmlEscape(
                firstLine(result.message())) + "\">" + xmlEscape(result.message()) + "</failure>\n" + "  </testcase>\n";
    }

    private String testClassName(String key) {
        int separator = key.lastIndexOf('.');
        return separator == -1 ? "CommandLibTest" : key.substring(0, separator);
    }

    private String testMethodName(String key) {
        int separator = key.lastIndexOf('.');
        return separator == -1 ? key : key.substring(separator + 1);
    }

    private String firstLine(String value) {
        int newLine = value.indexOf('\n');
        return newLine == -1 ? value : value.substring(0, newLine);
    }

    private String xmlEscape(String value) {
        return value.replace("&", "&amp;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;");
    }

    public void errorFailedResults(List<TestResult> results) {
        for (TestResult result : results) {
            if (result.status() == TestStatus.FAILED) {
                if (result.message()
                          .indexOf('\n') > -1) {
                    logger.log(Level.SEVERE,
                               result.key() + ": " + result.message()
                                                           .substring(0,
                                                                      result.message()
                                                                            .indexOf('\n')));
                } else {
                    logger.log(Level.SEVERE, result.key() + ": " + result.message());
                }
            }
        }
    }

    public void logTestResultCount(List<TestResult> results) {
        long succeededCount = results.stream()
                                     .filter(x -> x.status() == TestStatus.SUCCEEDED)
                                     .count();
        long failedCount = results.stream()
                                  .filter(x -> x.status() == TestStatus.FAILED)
                                  .count();
        logger.log(Level.INFO, String.format("SUCCEEDED: %d  FAILED: %d", succeededCount, failedCount));
    }
}
