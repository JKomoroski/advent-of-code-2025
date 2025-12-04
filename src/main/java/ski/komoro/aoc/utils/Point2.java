package ski.komoro.aoc.utils;

import java.util.List;
import java.util.stream.Stream;

public record Point2(int x, int y) {

    // Bound checking is for nerds
    public List<Point2> neighbors() {
        return Stream.of(
                new Point2(x, y - 1),
                new Point2(x, y + 1),
                new Point2(x - 1, y),
                new Point2(x + 1, y),
                new Point2(x - 1, y - 1),
                new Point2(x + 1, y - 1),
                new Point2(x - 1, y + 1),
                new Point2(x + 1, y + 1)
        ).toList();
    }

}
