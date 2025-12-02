package ski.komoro.aoc.twentyfive;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public abstract sealed class AOCBase permits Day02, Day03 {
    protected Instant start1 = null;
    protected Instant end1 = null;
    protected Instant start2 = null;
    protected Instant end2 = null;

    abstract String folder();

    protected String file() {
        return "in.txt";
    }

    protected String testFile() {
        return "test.txt";
    }

    void run() throws Exception {
        IO.println("Starting Test");
        final var testInput = Paths.get("", "inputs", folder(), testFile()).toAbsolutePath();
        runPart1(testInput);
        runPart2(testInput);
        IO.println("Test Total Time Elapsed: " + Duration.between(start1, end2).toMillis() + "ms");

        IO.println("Starting Actual");
        final var input = Paths.get("", "inputs", folder(), file()).toAbsolutePath();
        runPart1(input);
        runPart2(input);
        IO.println("Actual Total Time Elapsed: " + Duration.between(start1, end2).toMillis() + "ms");

    }

    private static LocalDateTime fromInstant(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).truncatedTo(ChronoUnit.SECONDS);
    }

    private void runPart1(Path input) throws Exception {
        start1 = Instant.now();
        IO.println("Starting Part 1 @ " + fromInstant(start1));
        part1(Files.lines(input));
        end1 = Instant.now();
        IO.println("Finished Part 1 @ " + fromInstant(end1));
        IO.println("Finished Part 1 in " + Duration.between(start1, end1).toMillis() + "ms");
    }

    private void runPart2(Path input) throws Exception {
        start2 = Instant.now();
        IO.println("Starting Part 2 @ " + fromInstant(start2));
        part2(Files.lines(input));
        end2 = Instant.now();
        IO.println("Finished Part 2 @ " + fromInstant(end2));
        IO.println("Finished Part 2 in " + Duration.between(start2, end2).toMillis() + "ms");
    }


    protected abstract void part1(Stream<String> fileInput) throws Exception;

    protected abstract void part2(Stream<String> fileInput) throws Exception;

}
