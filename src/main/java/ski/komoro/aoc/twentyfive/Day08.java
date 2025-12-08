package ski.komoro.aoc.twentyfive;

import static java.util.function.Predicate.not;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ski.komoro.aoc.utils.Point3;
import ski.komoro.aoc.utils.Tuple;

public final class Day08 extends AOCBase {

    @Override
    String folder() {
        return "day-08";
    }

    @Override
    protected String part1(final Stream<String> fileInput) throws Exception {
        final List<Point3> list = fileInput.map(s -> s.split(","))
                .map(l -> new Point3(Long.parseLong(l[0]), Long.parseLong(l[1]), Long.parseLong(l[2])))
                .toList();

        Map<Point3, LinkedHashMap<Point3, Double>> data = buildDistanceMap(list);

        List<Circuit> circuits = new ArrayList<>();
        int connections = 0;
        int maxTry = data.size() == 20 ? 10 : 1000;

        while (connections < maxTry && hasUnconnectedPoints(data)) {
            connections++;
            var connection = findPointWithNearestNeighbor(data);
            removeNearestPoint(connection.left(), data);
            connectPoints(connection.left(), connection.right(), circuits);
        }

        var longest = circuits.stream()
                .sorted(Comparator.comparing(Circuit::size).reversed())
                .limit(3)
                .mapToLong(Circuit::size)
                .reduce(1L, (x, y) -> x * y);
        return String.valueOf(longest);
    }

    @Override
    protected String part2(final Stream<String> fileInput) throws Exception {
        final List<Point3> list = fileInput.map(s -> s.split(","))
                .map(l -> new Point3(Long.parseLong(l[0]), Long.parseLong(l[1]), Long.parseLong(l[2])))
                .toList();

        Map<Point3, LinkedHashMap<Point3, Double>> data = buildDistanceMap(list);

        List<Circuit> circuits = new ArrayList<>();
        Tuple<Point3, Point3> lastConnection = null;

        while (hasUnconnectedPoints(data)) {
            var connection = findPointWithNearestNeighbor(data);
            removeNearestPoint(connection.left(), data);
            connectPoints(connection.left(), connection.right(), circuits);
            if (circuits.size() == 1 && circuits.getFirst().size() == data.size()) {
                lastConnection = connection;
                break;
            }
        }
        if (lastConnection == null) {
            throw new NullPointerException();
        }

        return String.valueOf(lastConnection.left().x() * lastConnection.right().x());
    }

    private Tuple<Point3, Point3> findPointWithNearestNeighbor(Map<Point3, LinkedHashMap<Point3, Double>> data) {
        var p = data.keySet()
                .stream()
                .filter(p1 -> !data.get(p1).isEmpty())
                .min(Comparator.comparingDouble(p1 -> nearestDistance(p1, data)))
                .orElseThrow();
        return Tuple.of(p, nearestPoint(p, data));
    }

    private boolean hasUnconnectedPoints(Map<Point3, LinkedHashMap<Point3, Double>> data) {
        return data.values().stream().anyMatch(m -> !m.isEmpty());
    }

    Point3 nearestPoint(Point3 point, Map<Point3, LinkedHashMap<Point3, Double>> data) {
        return data.get(point).firstEntry().getKey();
    }

    double nearestDistance(Point3 point, Map<Point3, LinkedHashMap<Point3, Double>> data) {
        return data.get(point).firstEntry().getValue();
    }

    void removeNearestPoint(Point3 point, Map<Point3, LinkedHashMap<Point3, Double>> data) {
        final Entry<Point3, Double> nearest = data.get(point).pollFirstEntry();
        data.get(nearest.getKey()).remove(point);
    }

    private Map<Point3, LinkedHashMap<Point3, Double>> buildDistanceMap(List<Point3> points) {
        return points.stream()
                .collect(Collectors.toMap(Function.identity(), point -> buildSortedDistances(point, points)));
    }

    private LinkedHashMap<Point3, Double> buildSortedDistances(Point3 source, List<Point3> allPoints) {
        return allPoints.stream()
                .filter(not(source::equals))
                .collect(Collectors.toMap(Function.identity(), target -> target.computeDistance(source)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private void connectPoints(Point3 p1, Point3 p2, List<Circuit> circuits) {
        Circuit c1 = findCircuit(p1, circuits);
        Circuit c2 = findCircuit(p2, circuits);

        if (c1 == null && c2 == null) {
            circuits.add(new Circuit(p1, p2));
        } else if (c1 != null && c2 != null && c1 != c2) {
            c1.merge(c2);
            circuits.remove(c2);
        } else if (c1 != null) {
            c1.addPoint(p2);
        } else {
            c2.addPoint(p1);
        }
    }

    private Circuit findCircuit(Point3 point, List<Circuit> circuits) {
        return circuits.stream()
                .filter(c -> c.hasPoint(point))
                .findFirst()
                .orElse(null);
    }

    static class Circuit {

        Circuit(Point3 p1, Point3 p2) {
            this.points = new HashSet<>();
            this.points.add(p1);
            this.points.add(p2);
        }

        final Set<Point3> points;

        boolean hasPoint(Point3 other) {
            return points.contains(other);
        }

        boolean addPoint(Point3 other) {
            return points.add(other);
        }

        boolean merge(Circuit other) {
            return this.points.addAll(other.points);
        }

        long size() {
            return points.size();
        }
    }
}
