package ski.komoro.aoc.twentyfive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public final class Day12 extends AOCBase {

    @Override
    String folder() {
        return "day-12";
    }

    @Override
    protected String part1(final Stream<String> fileInput) throws Exception {
        List<Gift> gifts = new ArrayList<>();
        List<Space> spaces = new ArrayList<>();

        final List<String> input = fileInput.toList();
        for (int i = 0; i < input.size(); i++) {
            final String candidate = input.get(i);
            if (candidate.isBlank()) {
                final String s = input.get(i - 1);
                final String s1 = input.get(i - 2);
                final String s2 = input.get(i - 3);
                char[][] gift = new char[][]{
                        s.toCharArray(),
                        s1.toCharArray(),
                        s2.toCharArray()
                };
                gifts.add(new Gift(gift));
                continue;
            }

            if (candidate.contains("x")) {
                final String[] split = candidate.split(": ");
                final String[] xY = split[0].split("x");
                int[] giftCounts = Arrays.stream(split[1].split(" ")).mapToInt(Integer::parseInt).toArray();
                spaces.add(new Space(Integer.parseInt(xY[0]), Integer.parseInt(xY[1]), giftCounts));
            }
        }

        final long sanity = spaces.stream()
                .filter(s -> s.sanityCheck(gifts))
                .count();

        return String.valueOf(sanity);
    }

    @Override
    protected String part2(final Stream<String> fileInput) throws Exception {
        return "hello world";
    }

    record Gift(char[][] shape) {

        long area() {
            long area = 0;
            for (var line : shape) {
                for (var character : line) {
                    if ('#' == character) {
                        area++;
                    }
                }
            }
            return area;
        }
    }

    record Space(int x, int y, int[] giftCounts) {

        int area() {
            return x * y;
        }

        boolean sanityCheck(List<Gift> gifts) {
            long requiredArea = 0;
            for (int i = 0; i < giftCounts.length; i++) {
                requiredArea += giftCounts[i] * gifts.get(i).area();
            }
            return area() >= requiredArea;
        }
    }
}
