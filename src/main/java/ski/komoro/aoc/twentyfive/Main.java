package ski.komoro.aoc.twentyfive;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    static void main(String[] args) throws Exception {
        initInputs(args);
        buildInstance(findDay(args)).run();
    }

    static AOCBase buildInstance(Class<? extends AOCBase> clazz) throws Exception {
        return clazz.getConstructor().newInstance();
    }

    static Class<? extends AOCBase> findDay(String[] args) throws Exception {
        final var subClasses = subClasses();
        return (args == null || args.length == 0 || args[0] == null || args[0].isEmpty())
                ? subClasses.findFirst().orElseThrow()
                : subClasses.filter(c -> c.getSimpleName().contains(args[0])).findFirst().orElseThrow();
    }

    @SuppressWarnings("unchecked")
    static Stream<Class<? extends AOCBase>> subClasses() {
        return Arrays.stream(AOCBase.class.getPermittedSubclasses())
                .sorted(Comparator.comparing(Class::getSimpleName, Comparator.reverseOrder()))
                .map(c -> (Class<? extends AOCBase>) c);
    }

    static void initInputs(String[] args) throws IOException {
        final Path inputs = Path.of("./inputs");
        if(Files.exists(inputs)) {
            return;
        }
        Files.createDirectory(inputs);
        final List<String> suffixes = IntStream.rangeClosed(1, 12)
                .mapToObj(i -> String.format("%02d", i))
                .toList();

        for (final String suffix : suffixes) {
            final Path day = Path.of("./inputs/day-" + suffix);
            final Path in = Path.of("./inputs/day-" + suffix + "/in.txt");
            final Path test = Path.of("./inputs/day-" + suffix + "/test.txt");
            Files.createDirectory(day);
            Files.createFile(in);
            Files.createFile(test);
        }
    }

}
