package ski.komoro.aoc.utils;

public record Line(Point2 p1, Point2 p2) {

    public boolean isPointOnLine(Point2 p) {
        long minX = Math.min(p1.x(), p2().x());
        long maxX = Math.max(p1().x(), p2().x());
        long minY = Math.min(p1().y(), p2().y());
        long maxY = Math.max(p1().y(), p2().y());

        // Check if point is within line bounds
        if (p.x() < minX || p.x() > maxX || p.y() < minY || p.y() > maxY) {
            return false;
        }

        // Check if point is on the line (horizontal or vertical)
        return p1().y() == p2().y() || p.x() == p1().x();
    }

}
