package ski.komoro.aoc.twentyfive;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

public final class Day07 extends AOCBase {

    @Override
    String folder() {
        return "day-07";
    }

    @Override
    protected String part1(final Stream<String> fileInput) throws Exception {
        char[][] grid = fileInput.map(String::toCharArray).toArray(char[][]::new);
        int splits = 0;
        var beams = new HashSet<Integer>();
        beams.add(findStart(grid[0]));


        for (int y = 0; y < grid.length; y++) {
            var newBeams = new HashSet<Integer>();
            var removeBeams = new HashSet<Integer>();

            for (final Integer x : beams) {
                var current = grid[y][x];
                if(current == '^') {
                    removeBeams.add(x);
                    newBeams.add(x - 1);
                    newBeams.add(x + 1);
                    splits++;
                }

            }
            beams.addAll(newBeams);
            beams.removeAll(removeBeams);
        }

        return String.valueOf(splits);

    }

    @Override
    protected String part2(final Stream<String> fileInput) throws Exception {
        char[][] grid = fileInput.map(String::toCharArray).toArray(char[][]::new);
        var beams = new HashSet<Integer>();
        long[] timelineCounts = new long[grid.length];
        final int start = findStart(grid[0]);
        beams.add(start);
        timelineCounts[start] = 1;

        for (int y = 0; y < grid.length; y++) {

            var newBeams = new HashSet<Integer>();
            var removeBeams = new HashSet<Integer>();

            for (final Integer x : beams) {
                var current = grid[y][x];
                if(current == '^') {
                    removeBeams.add(x);
                    newBeams.add(x - 1);
                    newBeams.add(x + 1);
                    //split the timeline
                    timelineCounts[x - 1] += timelineCounts[x];
                    timelineCounts[x + 1] += timelineCounts[x];
                    //kill the old timeline
                    timelineCounts[x] = 0;
                }
            }
            beams.addAll(newBeams);
            beams.removeAll(removeBeams);
        }

        return String.valueOf(Arrays.stream(timelineCounts).sum());
    }

    int findStart(char[] row) {
        for (int i = 0; i < row.length; i++) {
            if(row[i] == 'S'){
                return i;
            }
        }
        throw new IllegalArgumentException();
    }
}
