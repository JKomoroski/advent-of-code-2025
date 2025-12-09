package ski.komoro.aoc.utils;

public record Line(Point2 p1, Point2 p2) {

    public boolean isPointOnLine(Point2 p) {
        long minX = Math.min(p1.x(), p2.x());
        long maxX = Math.max(p1.x(), p2.x());
        long minY = Math.min(p1.y(), p2.y());
        long maxY = Math.max(p1.y(), p2.y());

        // Check if point is within line bounds
        if (p.x() < minX || p.x() > maxX || p.y() < minY || p.y() > maxY) {
            return false;
        }

        // Check if point is on the line (horizontal or vertical)
        return p1.y() == p2.y() || p.x() == p1.x();
    }


    public boolean intersects(Line other) {
        // Check if lines are identical - NOT a crossing
        if ((this.p1.equals(other.p1) && this.p2.equals(other.p2)) ||
                (this.p1.equals(other.p2) && this.p2.equals(other.p1))) {
            return false;
        }

        long thisMinX = Math.min(this.p1.x(), this.p2.x());
        long thisMaxX = Math.max(this.p1.x(), this.p2.x());
        long thisMinY = Math.min(this.p1.y(), this.p2.y());
        long thisMaxY = Math.max(this.p1.y(), this.p2.y());

        long l2MinX = Math.min(other.p1.x(), other.p2.x());
        long l2MaxX = Math.max(other.p1.x(), other.p2.x());
        long l2MinY = Math.min(other.p1.y(), other.p2.y());
        long l2MaxY = Math.max(other.p1.y(), other.p2.y());

        boolean thisHorizontal = this.p1.y() == this.p2.y();
        boolean l2Horizontal = other.p1.y() == other.p2.y();

        // Parallel lines - overlap is OK, NOT a crossing
        if (thisHorizontal == l2Horizontal) {
            return false;
        }

        if (thisHorizontal) {
            // this horizontal, l2 vertical
            long thisY = this.p1.y();
            long l2X = other.p1.x();

            // Cross only if intersection is in INTERIOR of both segments
            return l2X > thisMinX && l2X < thisMaxX &&
                    thisY > l2MinY && thisY < l2MaxY;
        } else {
            // this vertical, l2 horizontal
            long thisX = this.p1.x();
            long l2Y = other.p1.y();

            return thisX > l2MinX && thisX < l2MaxX &&
                    l2Y > thisMinY && l2Y < thisMaxY;
        }
    }

    public boolean edgesIntersect(Line other) {
        // Check if lines are identical
        if ((this.p1.equals(other.p1()) && this.p2.equals(other.p2())) ||
                (this.p1.equals(other.p2()) && this.p2.equals(other.p1()))) {
            return true;
        }

        long l1MinX = Math.min(this.p1.x(), this.p2.x());
        long l1MaxX = Math.max(this.p1.x(), this.p2.x());
        long l1MinY = Math.min(this.p1.y(), this.p2.y());
        long l1MaxY = Math.max(this.p1.y(), this.p2.y());

        long l2MinX = Math.min(other.p1.x(), other.p2.x());
        long l2MaxX = Math.max(other.p1.x(), other.p2.x());
        long l2MinY = Math.min(other.p1.y(), other.p2.y());
        long l2MaxY = Math.max(other.p1.y(), other.p2.y());

        boolean l1Horizontal = this.p1.y() == this.p2.y();
        boolean l2Horizontal = other.p1.y() == other.p2.y();

        // Parallel lines - check for overlap
        if (l1Horizontal == l2Horizontal) {
            if (l1Horizontal) {
                // Both horizontal
                if (this.p1.y() != other.p1.y()) return false;
                return l1MaxX >= l2MinX && l1MinX <= l2MaxX;
            } else {
                // Both vertical
                if (this.p1.x() != other.p1.x()) return false;
                return l1MaxY >= l2MinY && l1MinY <= l2MaxY;
            }
        }

        if (l1Horizontal) {
            // l1 horizontal, l2 vertical
            long l1Y = this.p1.y();
            long l2X = other.p1.x();

            return l2X >= l1MinX && l2X <= l1MaxX &&
                    l1Y >= l2MinY && l1Y <= l2MaxY;
        } else {
            // l1 vertical, l2 horizontal
            long l1X = this.p1.x();
            long l2Y = other.p1.y();

            return l1X >= l2MinX && l1X <= l2MaxX &&
                    l2Y >= l1MinY && l2Y <= l1MaxY;
        }
    }

}
