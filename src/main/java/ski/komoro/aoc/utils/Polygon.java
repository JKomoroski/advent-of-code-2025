package ski.komoro.aoc.utils;

import java.util.ArrayList;
import java.util.List;

public record Polygon(List<Line> edges) {

    public static Polygon of(List<Point2> points) {
        var edges = new ArrayList<Line>();
        for (int i = 1; i < points.size(); i++) {
            edges.add(new Line(points.get(i - 1), points.get(i)));
        }
        edges.add(new Line(points.getLast(), points.getFirst()));
        return new Polygon(edges);
    }

}
