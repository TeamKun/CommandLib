package net.kunmc.lab.testplugin;

import com.google.common.collect.Lists;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandLib;
import net.kunmc.lab.commandlib.util.bukkit.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TestMain {
    public static final String TEST_PLAYER_NAME = "Maru32768";
    private final Plugin plugin;
    private final Logger logger;


    public TestMain(Plugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    public void runAllTests() {
        runAllTests(results -> {
            outputResultsToCsv(results);
            errorFailedResults(results);
            logTestResultCount(results);
        });
    }

    public void runAllTests(Consumer<List<TestResult>> consumer) {
        Command mainCommand = new MainCommand();
        ArgumentTest argumentTest = new ArgumentTest(mainCommand);
        CommandSyntaxExceptionTest commandSyntaxExceptionTest = new CommandSyntaxExceptionTest(mainCommand);
        List<TestBase> tests = Lists.newArrayList(argumentTest, commandSyntaxExceptionTest);
        List<String> commands = tests.stream()
                                     .flatMap(x -> x.build()
                                                    .stream())
                                     .collect(Collectors.toList());
        CommandLib.register(plugin, mainCommand);

        Runnable execute = () -> {
            for (String command : commands) {
                Bukkit.getServer()
                      .dispatchCommand(Bukkit.getConsoleSender(), command);
            }

            consumer.accept(tests.stream()
                                 .flatMap(x -> x.results()
                                                .stream())
                                 .collect(Collectors.toList()));

            tests.forEach(TestBase::clearResults);
        };

        mainCommand.addChildren(new Command("rerun") {{
            execute(ctx -> {
                execute.run();
            });
        }});

        Bukkit.getScheduler()
              .runTaskLater(plugin, execute, 4);
    }

    public void outputResultsToCsv(List<TestResult> results) {
        Path path = Paths.get("../../test_plugin_common/test_results/" + BukkitUtil.getMinecraftVersion() + ".csv");
        try (Writer writer = Files.newBufferedWriter(path)) {
            StatefulBeanToCsv<TestResult> csv = new StatefulBeanToCsvBuilder<TestResult>(writer).build();
            csv.write(results);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
