package ski.komoro.aoc.utils;

public record Tuple<T, U>(T first, U second) {

    public static <T, U> Tuple<T, U> of(T first, U second) {
        return new Tuple<>(first, second);
    }

    public static <T> Tuple<T, T> of(T[] arr) {
        if (arr.length != 2) {
            throw new IllegalArgumentException("Unexpected array length: " + arr.length);
        }
        return of(arr[0], arr[1]);
    }

    public T one() {
        return first;
    }

    public U two() {
        return second;
    }

    public T _1() {
        return first;
    }

    public U _2() {
        return second;
    }

    public T key() {
        return first;
    }

    public U value() {
        return second;
    }

    public T left() {
        return first;
    }

    public U right() {
        return second;
    }

}
