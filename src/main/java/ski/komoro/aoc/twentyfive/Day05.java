package ski.komoro.aoc.twentyfive;

import java.util.ArrayList;
import java.util.stream.Stream;
import ski.komoro.aoc.utils.Range;

public final class Day05 extends AOCBase {

    @Override
    String folder() {
        return "day-05";
    }

    @Override
    protected String part1(final Stream<String> fileInput) throws Exception {
        var ids = new ArrayList<Range>();
        var available = new ArrayList<Long>();
        fileInput.forEach(s -> {
            if (s.contains("-")) {
                final String[] split = s.split("-");
                ids.add(new Range(split[0], split[1]));
                return;
            }
            if (s.isBlank()) {
                return;
            }
            available.add(Long.parseLong(s));
        });

        final long count = available.stream()
                .filter(l -> ids.stream().anyMatch(r -> r.containsClosed(l)))
                .count();

        return String.valueOf(count);
    }

    @Override
    protected String part2(final Stream<String> fileInput) throws Exception {
        var count = fileInput.filter(s -> s.contains("-"))
                .map(s -> s.split("-"))
                .map(split -> new Range(split[0], split[1]))
                .sorted()
                .collect(Range.merging())
                .stream()
                .mapToLong(Range::sizeClosed)
                .sum();

        return String.valueOf(count);
    }
}
