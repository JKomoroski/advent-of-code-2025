package ski.komoro.aoc.utils;

import java.util.stream.LongStream;

public record Range(String startS, String endS, Long start, Long end) {

    public Range(String startS, String endS) {
        this(startS, endS, Long.parseLong(startS), Long.parseLong(endS));
    }

    public LongStream closedRange() {
        return LongStream.rangeClosed(start(), end());
    }

}
