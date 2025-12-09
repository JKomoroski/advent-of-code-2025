package ski.komoro.aoc.utils;

import java.util.List;

public record Rectangle(Point2 p1, Point2 p2) {

    public long cellArea() {
        long width = Math.abs(p1.x() - p2.x()) + 1;
        long height = Math.abs(p1.y() - p2.y()) + 1;
        return width * height;
    }

    public long area() {
        return Math.abs(p1.x() - p2.x()) * Math.abs(p1.y() - p2.y());
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
            if (!polygon.isPointOnOrInside(corner)) {
                return false;
            }
        }

        return edges().stream()
                .noneMatch(rectEdge -> polygon.edges().stream().anyMatch(rectEdge::edgesCross));
    }


}
