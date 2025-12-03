package ski.komoro.aoc.twentyfive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
                .mapToLong(b -> b.maxN(2))
                .sum();

        return String.valueOf(joltage);

    }

    @Override
    protected String part2(final Stream<String> fileInput) throws Exception {
        final long joltage = fileInput.map(Bank::from)
                .mapToLong(b -> b.maxN(12))
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

        int maxTwo() {
            int maxidx1 = 0;
            int maxidx2 = batteries.size() - 1;

            for (int i = 0; i < batteries.size() - 1; i++) {
                if (batteries.get(i) > batteries.get(maxidx1)) {
                    maxidx1 = i;
                }
            }

            for (int i = maxidx1 + 1; i < batteries.size(); i++) {
                if (batteries.get(i) > batteries.get(maxidx2)) {
                    maxidx2 = i;
                }
            }
            final String pos1 = String.valueOf(batteries.get(maxidx1));
            final String pos2 = String.valueOf(batteries.get(maxidx2));
            final String result = pos1 + pos2;

            return Integer.parseInt(result);
        }

        long maxN(int n) {
            List<Integer> maxIndices = new ArrayList<>();

            int maxIdx = 0;
            for (int i = 0; i <= batteries.size() - n; i++) {
                if (batteries.get(i) > batteries.get(maxIdx)) {
                    maxIdx = i;
                }
            }
            maxIndices.add(maxIdx);

            for (int k = 1; k < n; k++) {
                int prevIdx = maxIndices.get(k - 1);
                maxIdx = prevIdx + 1;
                for (int i = prevIdx + 1; i <= batteries.size() - (n - k); i++) {
                    if (batteries.get(i) > batteries.get(maxIdx)) {
                        maxIdx = i;
                    }
                }
                maxIndices.add(maxIdx);
            }

            StringBuilder result = new StringBuilder();
            for (int idx : maxIndices) {
                result.append(batteries.get(idx));
            }

            return Long.parseLong(result.toString());
        }
    }

}
