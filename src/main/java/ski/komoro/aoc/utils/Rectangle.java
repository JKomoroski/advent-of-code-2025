package ski.komoro.aoc.utils;

import java.util.List;

public record Rectangle(Point2 p1, Point2 p2) {

    public long area() {
        long width = Math.abs(p1.x() - p2.x()) + 1;
        long height = Math.abs(p1.y() - p2.y()) + 1;
        return width * height;
    }

    List<Point2> corners() {
        long minX = Math.min(p1.x(), p2.x());
        long maxX = Math.max(p1.x(), p2.x());
        long minY = Math.min(p1.y(), p2.y());
        long maxY = Math.max(p1.y(), p2.y());
        return List.of(
                new Point2(minX, minY), new Point2(maxX, minY),
                new Point2(minX, maxY), new Point2(maxX, maxY)
        );
    }

    List<Line> edges() {
        var corners = corners();
        // Check no edge crosses polygon boundary
        return List.of(
                new Line(corners.get(0), corners.get(1)), // bottom: (minX,minY) -> (maxX,minY)
                new Line(corners.get(1), corners.get(3)), // right:  (maxX,minY) -> (maxX,maxY)
                new Line(corners.get(3), corners.get(2)), // top:    (maxX,maxY) -> (minX,maxY)
                new Line(corners.get(2), corners.get(0))  // left:   (minX,maxY) -> (minX,minY)
        );

    }

    public boolean isRectangleInside(Polygon polygon) {
        // Check all corners inside
        for (Point2 corner : corners()) {
            if (!isPointInside(corner, polygon)) {
                return false;
            }
        }


        for (Line rectEdge : edges()) {
            for (Line polyEdge : polygon.edges()) {
                if (edgesCross(rectEdge, polyEdge)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean edgesCross(Line l1, Line l2) {
        // Check if lines are identical - NOT a crossing
        if ((l1.p1().equals(l2.p1()) && l1.p2().equals(l2.p2())) ||
                (l1.p1().equals(l2.p2()) && l1.p2().equals(l2.p1()))) {
            return false;
        }

        long l1MinX = Math.min(l1.p1().x(), l1.p2().x());
        long l1MaxX = Math.max(l1.p1().x(), l1.p2().x());
        long l1MinY = Math.min(l1.p1().y(), l1.p2().y());
        long l1MaxY = Math.max(l1.p1().y(), l1.p2().y());

        long l2MinX = Math.min(l2.p1().x(), l2.p2().x());
        long l2MaxX = Math.max(l2.p1().x(), l2.p2().x());
        long l2MinY = Math.min(l2.p1().y(), l2.p2().y());
        long l2MaxY = Math.max(l2.p1().y(), l2.p2().y());

        boolean l1Horizontal = l1.p1().y() == l1.p2().y();
        boolean l2Horizontal = l2.p1().y() == l2.p2().y();

        // Parallel lines - overlap is OK, NOT a crossing
        if (l1Horizontal == l2Horizontal) {
            return false;
        }

        if (l1Horizontal) {
            // l1 horizontal, l2 vertical
            long l1Y = l1.p1().y();
            long l2X = l2.p1().x();

            // Cross only if intersection is in INTERIOR of both segments
            return l2X > l1MinX && l2X < l1MaxX &&
                    l1Y > l2MinY && l1Y < l2MaxY;
        } else {
            // l1 vertical, l2 horizontal
            long l1X = l1.p1().x();
            long l2Y = l2.p1().y();

            return l1X > l2MinX && l1X < l2MaxX &&
                    l2Y > l1MinY && l2Y < l1MaxY;
        }
    }
    private boolean isPointInside(Point2 p, Polygon polygon) {
        // First check if point is ON any edge
        for (Line edge : polygon.edges()) {
            if (edge.isPointOnLine(p)) {
                return true;
            }
        }

        // Ray casting for interior points
        int crossings = 0;
        for (Line edge : polygon.edges()) {
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

    private boolean edgesIntersect(Line l1, Line l2) {
        // Check if lines are identical
        if ((l1.p1().equals(l2.p1()) && l1.p2().equals(l2.p2())) ||
                (l1.p1().equals(l2.p2()) && l1.p2().equals(l2.p1()))) {
            return true;
        }

        long l1MinX = Math.min(l1.p1().x(), l1.p2().x());
        long l1MaxX = Math.max(l1.p1().x(), l1.p2().x());
        long l1MinY = Math.min(l1.p1().y(), l1.p2().y());
        long l1MaxY = Math.max(l1.p1().y(), l1.p2().y());

        long l2MinX = Math.min(l2.p1().x(), l2.p2().x());
        long l2MaxX = Math.max(l2.p1().x(), l2.p2().x());
        long l2MinY = Math.min(l2.p1().y(), l2.p2().y());
        long l2MaxY = Math.max(l2.p1().y(), l2.p2().y());

        boolean l1Horizontal = l1.p1().y() == l1.p2().y();
        boolean l2Horizontal = l2.p1().y() == l2.p2().y();

        // Parallel lines - check for overlap
        if (l1Horizontal == l2Horizontal) {
            if (l1Horizontal) {
                // Both horizontal
                if (l1.p1().y() != l2.p1().y()) return false;
                return l1MaxX >= l2MinX && l1MinX <= l2MaxX;
            } else {
                // Both vertical
                if (l1.p1().x() != l2.p1().x()) return false;
                return l1MaxY >= l2MinY && l1MinY <= l2MaxY;
            }
        }

        if (l1Horizontal) {
            // l1 horizontal, l2 vertical
            long l1Y = l1.p1().y();
            long l2X = l2.p1().x();

            return l2X >= l1MinX && l2X <= l1MaxX &&
                    l1Y >= l2MinY && l1Y <= l2MaxY;
        } else {
            // l1 vertical, l2 horizontal
            long l1X = l1.p1().x();
            long l2Y = l2.p1().y();

            return l1X >= l2MinX && l1X <= l2MaxX &&
                    l2Y >= l1MinY && l2Y <= l1MaxY;
        }
    }

}
