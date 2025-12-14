package ski.komoro.aoc.twentyfive;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public final class Day11 extends AOCBase {

    @Override
    String folder() {
        return "day-11";
    }

    @Override
    protected String part1(final Stream<String> fileInput) throws Exception {
        Map<String, List<String>> map = new HashMap<>();
        fileInput.forEach(s -> {
            var key = s.split(":")[0];
            final List<String> value = Arrays.stream(s.split(":")[1].split("\s+"))
                    .filter(s1 -> !s1.isBlank())
                    .toList();
            map.put(key, value);
        });

        long count = countPathsWithNodes(map, "you", "out", Set.of());

        return String.valueOf(count);
    }

    @Override
    protected String part2(final Stream<String> fileInput) throws Exception {
        Map<String, List<String>> map = new HashMap<>();
        fileInput.forEach(s -> {
            var key = s.split(":")[0];
            final List<String> value = Arrays.stream(s.split(":")[1].split("\s+"))
                    .filter(s1 -> !s1.isBlank())
                    .toList();
            map.put(key, value);
        });

        long count = countPathsWithNodes(map, "svr", "out", Set.of("fft", "dac"));

        return String.valueOf(count);
    }

    public static long countPathsWithNodes(
            Map<String, List<String>> adjacencyMap,
            String start,
            String target,
            Set<String> requiredNodes) {

        // Map required nodes to bit positions
        Map<String, Integer> nodeToBit = new HashMap<>();
        int bitPos = 0;
        for (String node : requiredNodes) {
            nodeToBit.put(node, bitPos++);
        }

        Map<CacheKey, Long> memo = new HashMap<>();
        return countPathsDP(start, target, adjacencyMap, nodeToBit, 0, memo);
    }

    private static long countPathsDP(
            String current,
            String target,
            Map<String, List<String>> adjacencyMap,
            Map<String, Integer> nodeToBit,
            int foundMask,
            Map<CacheKey, Long> memo) {

        CacheKey key = new CacheKey(current, foundMask);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        // Update mask if current is required
        int newMask = foundMask;
        if (nodeToBit.containsKey(current)) {
            newMask |= (1 << nodeToBit.get(current));
        }

        if (current.equals(target)) {
            int allFound = (1 << nodeToBit.size()) - 1;
            return (newMask == allFound) ? 1 : 0;
        }

        long count = 0;
        for (String neighbor : adjacencyMap.getOrDefault(current, List.of())) {
            count += countPathsDP(neighbor, target, adjacencyMap, nodeToBit, newMask, memo);
        }

        memo.put(key, count);
        return count;
    }

    private record CacheKey(String node, int mask) {

    }
}
