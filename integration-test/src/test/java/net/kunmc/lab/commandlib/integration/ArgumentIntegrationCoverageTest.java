package net.kunmc.lab.commandlib.integration;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ArgumentIntegrationCoverageTest {
    private static final Set<String> EXCLUDED_ARGUMENTS = Set.of(
            // Structural command node, already exercised indirectly by command tree tests.
            "LiteralArgument");

    @Test
    void bukkit_family_public_arguments_have_integration_cases() throws IOException {
        Path rootDir = Path.of(System.getProperty("commandlib.rootDir", "."));
        Set<String> spigotArguments = publicArgumentClassNames(rootDir.resolve(
                "spigot/src/main/java/net/kunmc/lab/commandlib/argument"));
        Set<String> paperArguments = publicArgumentClassNames(rootDir.resolve(
                "paper/src/main/java/net/kunmc/lab/commandlib/argument"));
        Set<String> commonBukkitArguments = new HashSet<>(spigotArguments);
        commonBukkitArguments.retainAll(paperArguments);
        commonBukkitArguments.removeAll(EXCLUDED_ARGUMENTS);

        String argumentTestSource = Files.readString(rootDir.resolve(
                "integration-test/shared/bukkit-test-plugin/src/main/java/net/kunmc/lab/testplugin/ArgumentTest.java"));
        Set<String> missing = commonBukkitArguments.stream()
                                                   .filter(argument -> !instantiatesArgument(argumentTestSource,
                                                                                             argument))
                                                   .collect(Collectors.toCollection(java.util.TreeSet::new));

        assertThat(missing).as("Add integration coverage in shared Bukkit ArgumentTest or document an exclusion")
                           .isEmpty();
    }

    private Set<String> publicArgumentClassNames(Path argumentDir) throws IOException {
        try (Stream<Path> files = Files.list(argumentDir)) {
            return files.filter(path -> path.getFileName()
                                            .toString()
                                            .endsWith("Argument.java"))
                        .filter(path -> !path.getFileName()
                                             .toString()
                                             .equals("Argument.java"))
                        .filter(path -> sourceContains(path, "public class "))
                        .map(path -> path.getFileName()
                                         .toString()
                                         .replace(".java", ""))
                        .collect(Collectors.toSet());
        }
    }

    private boolean sourceContains(Path path, String needle) {
        try {
            return Files.readString(path)
                        .contains(needle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean instantiatesArgument(String source, String argumentClassName) {
        return Pattern.compile("\\bnew\\s+" + Pattern.quote(argumentClassName) + "\\s*(?:\\(|<)")
                      .matcher(source)
                      .find();
    }
}
