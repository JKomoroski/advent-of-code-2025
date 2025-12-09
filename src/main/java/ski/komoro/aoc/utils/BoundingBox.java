package ski.komoro.aoc.utils;

import java.util.List;

public record BoundingBox(long minX, long maxX, long minY, long maxY) {
    public boolean contains(Point2 p) {
        return p.x() >= minX && p.x() <= maxX && p.y() >= minY && p.y() <= maxY;
    }

    public static BoundingBox of(List<Point2> points) {
        return new BoundingBox(
                points.stream().mapToLong(Point2::x).min().orElseThrow(),
                points.stream().mapToLong(Point2::x).max().orElseThrow(),
                points.stream().mapToLong(Point2::y).min().orElseThrow(),
                points.stream().mapToLong(Point2::y).max().orElseThrow()
        );
    }
}