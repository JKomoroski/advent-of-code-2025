package ski.komoro.aoc.twentyfive;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class Day06 extends AOCBase {

    @Override
    String folder() {
        return "day-06";
    }

    @Override
    protected String part1(final Stream<String> fileInput) throws Exception {
        List<Problem> problems = new ArrayList<>();
        fileInput.forEach(line -> {
            final String[] split = line.trim().split("\\s+");
            for (int i = 0; i < split.length; i++) {
                if (problems.size() <= i) {
                    problems.add(new Problem());
                }
                problems.get(i).update(split[i]);
            }
        });

        final long sum = problems.stream()
                .mapToLong(Problem::compute)
                .sum();

        return String.valueOf(sum);
    }

    @Override
    protected String part2(final Stream<String> fileInput) throws Exception {
        final List<String> lines = fileInput.toList();
        final var interimArgs = new ArrayList<Long>();
        List<Problem> problems = new ArrayList<>();

        for (int i = lines.stream().mapToInt(String::length).max().orElseThrow() - 1; i >= 0; i--) {
            StringBuilder builder = new StringBuilder();
            char sign = 'a';
            for (String line : lines) {
                if (line.length() <= i) {
                    continue;
                }
                char c = line.charAt(i);
                if (Character.isDigit(c)) {
                    builder.append(c);
                } else if (c == '+' || c == '*') {
                    sign = c;
                }
            }

            if (!builder.isEmpty()) {
                interimArgs.add(Long.parseLong(builder.toString()));
            }

            if (sign != 'a') {
                final Problem problem = new Problem();
                problem.setSign(String.valueOf(sign));
                for (var arg : interimArgs) {
                    problem.add(arg);
                }
                interimArgs.clear();
                problems.add(problem);
            }
        }

        final long sum = problems.stream()
                .mapToLong(Problem::compute)
                .sum();

        return String.valueOf(sum);
    }

    static class Problem {

        final ArrayList<Long> n;
        String sign;

        Problem() {
            this.n = new ArrayList<>();
            this.sign = null;
        }

        void update(String s) {
            if(s.contains("+") || s.contains("*")) {
                this.setSign(s);
            }  else {
                this.add(s);
            }
        }

        void add(String s) {
            n.add(Long.parseLong(s));
        }

        void add(long s) {
            n.add(s);
        }

        void setSign(String sign) {
            this.sign = sign;
        }

        long compute() {
            if (n.isEmpty()) {
                return 0;
            }

            return switch (sign) {
                case "*" -> n.stream().mapToLong(Long::longValue).reduce(1, (a, b) -> a * b);
                case "+" -> n.stream().mapToLong(Long::longValue).sum();
                default -> throw new IllegalArgumentException("Unknown sign: " + sign);
            };
        }
    }
}
