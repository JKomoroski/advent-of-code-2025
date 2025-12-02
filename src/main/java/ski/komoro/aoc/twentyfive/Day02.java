package ski.komoro.aoc.twentyfive;

import java.util.Arrays;
import java.util.stream.Stream;
import ski.komoro.aoc.utils.Range;

public class Day02 extends AOCBase {

    String repeatsDigits = "^(\\d+)\\1$";
    String repeatsDigits2OrMore = "^(\\d+)\\1+$";

    public static void main(String[] args) throws Exception {
        new Day02().run();
    }

    @Override
    String folder() {
        return "day-02";
    }

    @Override
    protected void part1(final Stream<String> fileInput) throws Exception {
        final long sum =
                fileInput.flatMap(s -> Arrays.stream(s.split(",")))
                .map(this::range)
                .flatMapToLong(Range::closedRange)
                .filter(this::hasRepeatingDigits)
                .sum();

        IO.println("Part 1: " + sum);

    }

    @Override
    protected void part2(final Stream<String> fileInput) throws Exception {
        final long sum =
                fileInput.flatMap(s -> Arrays.stream(s.split(",")))
                        .map(this::range)
                        .flatMapToLong(Range::closedRange)
                        .filter(this::hasRepeatingDigits2OrMore)
                        .sum();

        IO.println("Part 2: " + sum);
    }

    Range range(String s) {
        final String[] split = s.split("-");
        return new Range(split[0], split[1]);
    }

    boolean hasRepeatingDigits(long id) {
        return String.valueOf(id).matches(repeatsDigits);
    }

    boolean hasRepeatingDigits2OrMore(long id) {
        return String.valueOf(id).matches(repeatsDigits2OrMore);
    }
}
