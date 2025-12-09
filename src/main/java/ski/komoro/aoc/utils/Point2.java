package ski.komoro.aoc.utils;

import java.util.List;
import java.util.stream.Stream;

public record Point2(long x, long y) {

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

    public boolean isPointOnLine(Line l) {
        return l.isPointOnLine(this);
    }

    public static boolean isCollinear(Point2 a, Point2 b, Point2 c) {
        // Cross product = 0 means collinear
        return (b.y - a.y) * (c.x - b.x) == (c.y - b.y) * (b.x - a.x);
    }

}
