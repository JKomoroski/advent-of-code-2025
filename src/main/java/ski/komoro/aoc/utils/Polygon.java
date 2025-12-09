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

    public boolean isPointOnOrInside(Point2 p) {
        // First check if point is ON any edge
        for (Line edge : edges()) {
            if (edge.isPointOnLine(p)) {
                return true;
            }
        }

        // Ray casting for interior points
        int crossings = 0;
        for (Line edge : edges()) {
            if ((edge.p1().y() <= p.y() && edge.p2().y() > p.y()) ||
                    (edge.p2().y() <= p.y() && edge.p1().y() > p.y())) {

                double xIntersect = edge.p1().x() +
                        (p.y() - edge.p1().y()) * (edge.p2().x() - edge.p1().x()) /
                                (edge.p2().y() - edge.p1().y());

                if (p.x() < xIntersect) {
                    crossings++;
                }
            }
        }

        return crossings % 2 == 1;
    }

}
