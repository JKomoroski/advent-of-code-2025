package ski.komoro.aoc.twentyfive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Day03 extends AOCBase {

    @Override
    String folder() {
        return "day-03";
    }

    @Override
    protected String part1(final Stream<String> fileInput) throws Exception {
        final long joltage = fileInput.map(Bank::from)
                .mapToLong(b -> b.maxOrderedDigits(2))
                .sum();

        return String.valueOf(joltage);

    }

    @Override
    protected String part2(final Stream<String> fileInput) throws Exception {
        final long joltage = fileInput.map(Bank::from)
                .mapToLong(b -> b.maxOrderedDigits(12))
                .sum();

        return String.valueOf(joltage);
    }

    record Bank(List<Integer> batteries) {

        static Bank from(String s) {
            final List<Integer> batteries = Arrays.stream(s.split(""))
                    .map(Integer::parseInt)
                    .toList();
            return new Bank(batteries);
        }

        long maxOrderedDigits(int n) {
            List<Integer> maxIndices = new ArrayList<>();

            int firstMax = IntStream.rangeClosed(0, batteries.size() - n)
                    .reduce(0, this::compareBatteryByIdx);
            maxIndices.add(firstMax);

            // Subsequent indices
            IntStream.range(1, n)
                    .forEach(k -> {
                        int prevIdx = maxIndices.get(k - 1);
                        int maxIdx = IntStream.rangeClosed(prevIdx + 1, batteries.size() - (n - k))
                                .reduce(prevIdx + 1, this::compareBatteryByIdx);
                        maxIndices.add(maxIdx);
                    });

            final String result = maxIndices.stream()
                    .map(batteries::get)
                    .map(String::valueOf)
                    .collect(Collectors.joining());

            return Long.parseLong(result);
        }

        int compareBatteryByIdx(int previousBest, int current) {
            return batteries.get(current) > batteries.get(previousBest) ? current : previousBest;
        }

    }

}
