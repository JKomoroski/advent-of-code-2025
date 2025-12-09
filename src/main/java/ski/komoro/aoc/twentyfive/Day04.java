package ski.komoro.aoc.twentyfive;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import ski.komoro.aoc.utils.Point2;

public final class Day04 extends AOCBase {

    @Override
    String folder() {
        return "day-04";
    }

    @Override
    protected String part1(final Stream<String> fileInput) throws Exception {
        char[][] grid = fileInput.map(String::toCharArray).toArray(char[][]::new);

        final List<Point2> points = IntStream.range(0, grid.length)
                .boxed()
                .flatMap(y -> IntStream.range(0, grid[0].length).mapToObj(x -> new Point2(x, y)))
                .toList();

        int accessible = 0;

        for (final var p : points) {
            if(!isPaper(p, grid)) {
                continue;
            }
            int neighborCount = 0;

            for (final Point2 neighbor : p.neighbors()) {
                if(isPaper(neighbor, grid)) {
                    neighborCount++;
                }
            }

            if(neighborCount < 4 ) {
                accessible++;
            }
        }

        return String.valueOf(accessible);
    }

    // Bound checking is for nerds
    boolean isPaper(Point2 p, char[][] grid) {
        try {
            return grid[Math.toIntExact(p.x())][Math.toIntExact(p.y())] == '@';
        } catch (Exception e) {
            return false;
        }
    }

    void removePaper(Point2 p, char[][] grid) {
        grid[Math.toIntExact(p.x())][Math.toIntExact(p.y())] = '.';
    }

    @Override
    protected String part2(final Stream<String> fileInput) throws Exception {

        char[][] grid = fileInput.map(String::toCharArray).toArray(char[][]::new);

        final List<Point2> points = IntStream.range(0, grid.length)
                .boxed()
                .flatMap(y -> IntStream.range(0, grid[0].length).mapToObj(x -> new Point2(x, y)))
                .toList();

        boolean madeChange = true;
        int removed = 0;

        while (madeChange) {
            madeChange = false;
            for (final var p : points) {
                if (!isPaper(p, grid)) {
                    continue;
                }
                int neighborCount = 0;

                for (final Point2 neighbor : p.neighbors()) {
                    if (isPaper(neighbor, grid)) {
                        neighborCount++;
                    }
                }

                if (neighborCount < 4) {
                    removePaper(p, grid);
                    removed++;
                    madeChange = true;
                }
            }
        }

        return String.valueOf(removed);
    }


}
