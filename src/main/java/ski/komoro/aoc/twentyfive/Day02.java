package ski.komoro.aoc.twentyfive;

import java.util.Arrays;
import java.util.stream.Stream;
import ski.komoro.aoc.utils.Range;

public final class Day02 extends AOCBase {

    String repeatsDigitsOnce = "^(\\d+)\\1$";
    String repeatsDigitsAtLeastOnce = "^(\\d+)\\1+$";

    @Override
    String folder() {
        return "day-02";
    }

    @Override
    protected String part1(final Stream<String> fileInput) throws Exception {
        final long sum =
                fileInput.flatMap(s -> Arrays.stream(s.split(",")))
                .map(this::range)
                .flatMapToLong(Range::closedRange)
                .filter(this::hasRepeatingDigits)
                .sum();
        return String.valueOf(sum);
    }

    @Override
    protected String part2(final Stream<String> fileInput) throws Exception {
        final long sum =
                fileInput.flatMap(s -> Arrays.stream(s.split(",")))
                        .map(this::range)
                        .flatMapToLong(Range::closedRange)
                        .filter(this::hasRepeatingDigitsAtLeastOnce)
                        .sum();

        return String.valueOf(sum);
    }

    Range range(String s) {
        final String[] split = s.split("-");
        return new Range(split[0], split[1]);
    }

    boolean hasRepeatingDigits(long id) {
        return String.valueOf(id).matches(repeatsDigitsOnce);
    }

    boolean hasRepeatingDigitsAtLeastOnce(long id) {
        return String.valueOf(id).matches(repeatsDigitsAtLeastOnce);
    }
}
