package ski.komoro.aoc.twentyfive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Day01 extends AOCBase {

    @Override
    String folder() {
        return "day-01";
    }

    @Override
    protected String part1(final Stream<String> fileInput) throws Exception {
        var list = fileInput.map(Turn::parse)
                .toList();
        Dial dial = new Dial(50, 100);
        int isZero = 0;
        for (final Turn turn : list) {
            dial = dial.move(turn);

            if (dial.pos() == 0) {
                isZero++;
            }
        }

        return String.valueOf(isZero);
    }

    @Override
    protected String part2(final Stream<String> fileInput) throws Exception {
        var list = fileInput.map(Turn::parse)
                .toList();
        Dial dial = new Dial(50, 100);
        for (final Turn turn : list) {
            dial = dial.move(turn);
        }

        return String.valueOf(dial.pastZero());
    }

    record Turn(int distance){
        static Turn parse(String s) {
            final int i = Integer.parseInt(s.substring(1));
            return s.contains("L") ? new Turn(i * -1) : new Turn(i);
        }
    }

    record Dial(int pos, CircularList<Integer> points, int pastZero) {

        Dial(int pos, int size) {
            this(pos, new CircularList<Integer>(IntStream.range(0, size).boxed().toList()), 0);
        }
        Dial(int pos, int size, int pastZero) {
            this(pos, new CircularList<Integer>(IntStream.range(0, size).boxed().toList()), 0);
        }

        Dial move(Turn turn) {
            final int pos = this.pos + turn.distance;
            final int turnsPastZero = calculateTurnsPastZero(turn);
            final Integer pos2 = points.get(pos);
            final int pastZero = this.pastZero + turnsPastZero + (this.pos == 0 ? -1 : 0);
            return new Dial(pos2, this.points, pastZero);
        }

        private int calculateTurnsPastZero(Turn turn) {
            int result = this.pos + turn.distance();
            return (Math.abs(result) / 100) + (Integer.signum(result) != Integer.signum(this.pos) ? 1 : 0);
        }

    }

    static class CircularList<E> extends ArrayList<E> {

        public CircularList(Collection<E> list) {
            super(list);
        }

        @Override
        public E get(int index) {
            int idx = index % size();
            final int circularIdx = (idx < 0) ? idx + size() : idx;
            return super.get(circularIdx);
        }
    }
}
