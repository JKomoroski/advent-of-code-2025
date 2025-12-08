package ski.komoro.aoc.utils;

public record Point3(long x, long y, long z) {

    public double computeDistance(Point3 other) {
        return Math.sqrt(
                Math.powExact(this.x - other.x, 2) +
                Math.powExact(this.y - other.y, 2) +
                Math.powExact(this.z - other.z, 2)
        );
    }

}
