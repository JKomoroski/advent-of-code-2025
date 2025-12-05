package ski.komoro.aoc.utils;

import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.LongStream;

public record Range(String startS, String endS, Long start, Long end) implements Comparable<Range> {

    public Range(String startS, String endS) {
        this(startS, endS, Long.parseLong(startS), Long.parseLong(endS));
    }

    public LongStream closedRange() {
        return LongStream.rangeClosed(start(), end());
    }

    public long sizeClosed() {
        return end() - start() + 1;
    }

    public boolean containsClosed(long value) {
        return value >= start() && value <= end();
    }


    public boolean overlapsOrAdjacent(Range other) {
        return this.end >= other.start - 1;
    }

    public Range merge(Range other) {
        long newEnd = Math.max(this.end, other.end);
        return new Range(this.startS, String.valueOf(newEnd), this.start, newEnd);
    }

    public static void accumulate(ArrayList<Range> list, Range range) {
        if (list.isEmpty() || !list.getLast().overlapsOrAdjacent(range)) {
            list.add(range);
            return;
        }
        list.set(list.size() - 1, list.getLast().merge(range));
    }

    public static Collector<Range, ArrayList<Range>, ArrayList<Range>> merging() {
        return Collector.of(
                ArrayList::new,
                (list, range) -> {
                    if (list.isEmpty() || !list.getLast().overlapsOrAdjacent(range)) {
                        list.add(range);
                    } else {
                        list.set(list.size() - 1, list.getLast().merge(range));
                    }
                },
                (list1, list2) -> {
                    for (Range range : list2) {
                        if (list1.isEmpty() || !list1.getLast().overlapsOrAdjacent(range)) {
                            list1.add(range);
                        } else {
                            list1.set(list1.size() - 1, list1.getLast().merge(range));
                        }
                    }
                    return list1;
                }
        );
    }

    @Override
    public int compareTo(final Range o) {
        return this.start().compareTo(o.start());
    }
}
