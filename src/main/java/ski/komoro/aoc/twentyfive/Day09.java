package ski.komoro.aoc.twentyfive;

import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import ski.komoro.aoc.utils.Point2;
import ski.komoro.aoc.utils.Polygon;
import ski.komoro.aoc.utils.Rectangle;

public final class Day09 extends AOCBase {

    @Override
    String folder() {
        return "day-09";
    }

    @Override
    protected String part1(final Stream<String> fileInput) throws Exception {
        var points = fileInput.map(s -> s.split(","))
                .map(split -> new Point2(Long.parseLong(split[0]), Long.parseLong(split[1])))
                .toList();

        var rectangles = IntStream.range(0, points.size())
                .boxed()
                .flatMap(i -> IntStream.range(i + 1, points.size()).mapToObj(j -> new Rectangle(points.get(i), points.get(j))))
                .toList();

        final long maxSize = rectangles.stream()
                .mapToLong(Rectangle::cellArea)
                .max()
                .orElseThrow();
        return String.valueOf(maxSize);
    }

    @Override
    protected String part2(final Stream<String> fileInput) throws Exception {
        var points = fileInput.map(s -> s.split(","))
                .map(split -> new Point2(Long.parseLong(split[0]), Long.parseLong(split[1])))
                .toList();

        var polygon = Polygon.of(points);
        var largestRect = IntStream.range(0, points.size())
                .parallel()
                .boxed()
                .flatMap(i -> IntStream.range(i + 1, points.size()).mapToObj(j -> new Rectangle(points.get(i), points.get(j))))
                .filter(r -> r.isRectangleInside(polygon))
                .max(Comparator.comparing(Rectangle::cellArea))
                .orElseThrow();

        return String.valueOf(largestRect.cellArea());
    }
}
