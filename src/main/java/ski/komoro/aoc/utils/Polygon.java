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
        int crossings = 0;

        for (Line edge : edges()) {
            long x1 = edge.p1().x();
            long y1 = edge.p1().y();
            long x2 = edge.p2().x();
            long y2 = edge.p2().y();

            // Check if point is ON edge first
            if (edge.isPointOnLine(p)) {
                return true;
            }

            // Ray casting - standard algorithm
            if ((y1 <= p.y() && y2 > p.y()) || (y2 <= p.y() && y1 > p.y())) {
                double xIntersect = x1 + (p.y() - y1) * (x2 - x1) / (double)(y2 - y1);

                if (p.x() < xIntersect) {
                    crossings++;
                }
            }
        }

        return (crossings & 1) == 1;
    }

}
