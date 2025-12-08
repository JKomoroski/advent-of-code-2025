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

        Map<Point3, LinkedHashMap<Point3, Double>> data = list.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        p -> list.stream()
                                .filter(not(p::equals))
                                .collect(Collectors.toMap(Function.identity(), p2 -> p2.computeDistance(p)))
                                .entrySet().stream()
                                .sorted(Map.Entry.comparingByValue())
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (e1, e2) -> e1,
                                        LinkedHashMap::new
                                ))
                        )
                );
        List<Circuit> circuits = new ArrayList<>();
        int connections = 0;
        int maxTry = list.size() == 20 ? 10 : 1000;
        while (connections < maxTry && data.values().stream().anyMatch(m -> !m.isEmpty())) {
            connections++;
            Point3 hasNearest = list.stream()
                    .filter(p -> !data.get(p).isEmpty())
                    .min(Comparator.comparingDouble(p -> nearestDistance(p, data)))
                    .orElseThrow();
            Point3 unconnected = nearestPoint(hasNearest, data);
            removeNearestPoint(hasNearest, data);
            Circuit c1 = circuits.stream()
                    .filter(c -> c.hasPoint(hasNearest))
                    .findFirst()
                    .orElse(null);
            Circuit c2 = circuits.stream()
                    .filter(c -> c.hasPoint(unconnected))
                    .findFirst()
                    .orElse(null);
            if (c1 == null && c2 == null) {
                // Neither in circuit - create new
                circuits.add(new Circuit(hasNearest, unconnected));
            } else if (c1 != null && c2 != null) {
                // Both in circuits - merge if different
                if (c1 != c2) {
                    c1.merge(c2);
                    circuits.remove(c2);
                }
                // else: already connected, skip
            } else if (c1 != null) {
                // Only hasNearest in circuit
                c1.addPoint(unconnected);
            } else {
                // Only unconnected in circuit
                c2.addPoint(hasNearest);
            }
        }

        var longest = circuits.stream()
                .sorted(Comparator.comparing(Circuit::size).reversed())
                .limit(3)
                .mapToLong(Circuit::size)
                .reduce(1L, (x, y) -> x * y);
        return String.valueOf(longest);
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

    @Override
    protected String part2(final Stream<String> fileInput) throws Exception {
        final List<Point3> list = fileInput.map(s -> s.split(","))
                .map(l -> new Point3(Long.parseLong(l[0]), Long.parseLong(l[1]), Long.parseLong(l[2])))
                .toList();

        Map<Point3, LinkedHashMap<Point3, Double>> data = list.stream()
                .collect(Collectors.toMap(
                                Function.identity(),
                                p -> list.stream()
                                        .filter(not(p::equals))
                                        .collect(Collectors.toMap(Function.identity(), p2 -> p2.computeDistance(p)))
                                        .entrySet().stream()
                                        .sorted(Map.Entry.comparingByValue())
                                        .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                Map.Entry::getValue,
                                                (e1, e2) -> e1,
                                                LinkedHashMap::new
                                        ))
                        )
                );
        List<Circuit> circuits = new ArrayList<>();
        Point3 box = null;
        Point3 box2 = null;
        while (data.values().stream().anyMatch(m -> !m.isEmpty())) {
            Point3 hasNearest = list.stream()
                    .filter(p -> !data.get(p).isEmpty())
                    .min(Comparator.comparingDouble(p -> nearestDistance(p, data)))
                    .orElseThrow();
            Point3 unconnected = nearestPoint(hasNearest, data);
            removeNearestPoint(hasNearest, data);
            Circuit c1 = circuits.stream()
                    .filter(c -> c.hasPoint(hasNearest))
                    .findFirst()
                    .orElse(null);
            Circuit c2 = circuits.stream()
                    .filter(c -> c.hasPoint(unconnected))
                    .findFirst()
                    .orElse(null);
            if (c1 == null && c2 == null) {
                // Neither in circuit - create new
                circuits.add(new Circuit(hasNearest, unconnected));
            } else if (c1 != null && c2 != null) {
                // Both in circuits - merge if different
                if (c1 != c2) {
                    c1.merge(c2);
                    circuits.remove(c2);
                }
                // else: already connected, skip
            } else if (c1 != null) {
                // Only hasNearest in circuit
                c1.addPoint(unconnected);
            } else {
                // Only unconnected in circuit
                c2.addPoint(hasNearest);
            }

            if(circuits.size() == 1 && circuits.get(0).size() == list.size()) {
                box = hasNearest;
                box2 = unconnected;
                break;
            }
        }

        return String.valueOf(box.x() * box2.x());
    }

    static class Circuit {
        Circuit(Point3 p1,  Point3 p2) {
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
