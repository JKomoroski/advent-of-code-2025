package ski.komoro.aoc.twentyfive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Day10 extends AOCBase {

    @Override
    String folder() {
        return "day-10";
    }

    @Override
    protected String part1(final Stream<String> fileInput) throws Exception {
        var least = fileInput.map(Machine::of)
                .mapToInt(Machine::minPressesLights)
                .sum();
        return String.valueOf(least);
    }

    @Override
    protected String part2(final Stream<String> fileInput) throws Exception {
        final List<Machine> machines = fileInput.map(Machine::of).toList();
        var least = machines.stream()
                .mapToInt(Machine::minPressesJoltage)
                .sum();
        return String.valueOf(least);
    }

    record Machine(IndicatorLights lights, List<Button> buttons, Joltage joltage) {

        static Machine of(String s) {
            final String[] split = s.split(" ");
            final IndicatorLights indicatorLights = IndicatorLights.of(split[0]);
            final Joltage joltage = Joltage.of(split[split.length - 1]);
            final List<Button> buttons = Arrays.stream(split).skip(1)
                    .limit(Math.max(0, split.length - 2))
                    .map(Button::of)
                    .toList();
            return new Machine(indicatorLights, buttons, joltage);
        }

        int minPressesLights() {
            int n = lights.lights().size();
            int m = buttons.size();

            // Build augmented matrix [A|b]
            int[][] mat = new int[n][m + 1];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    mat[i][j] = buttons.get(j).list().contains(i) ? 1 : 0;
                }
                // Target: lights start at false, want them to be lights.lights().get(i)
                mat[i][m] = lights.lights().get(i) ? 1 : 0;
            }
            return minGF2(mat, n, m);
        }

        private int minGF2(int[][] mat, int rows, int cols) {
            int[] pivot = new int[rows];
            Arrays.fill(pivot, -1);

            // Gaussian elimination
            for (int col = 0, row = 0; col < cols && row < rows; col++) {
                int pivotRow = -1;
                for (int i = row; i < rows; i++) {
                    if (mat[i][col] == 1) {
                        pivotRow = i;
                        break;
                    }
                }
                if (pivotRow == -1) {
                    continue;
                }

                if (pivotRow != row) {
                    int[] temp = mat[row];
                    mat[row] = mat[pivotRow];
                    mat[pivotRow] = temp;
                }

                pivot[row] = col;

                for (int i = 0; i < rows; i++) {
                    if (i != row && mat[i][col] == 1) {
                        for (int j = 0; j <= cols; j++) {
                            mat[i][j] ^= mat[row][j];
                        }
                    }
                }
                row++;
            }

            // Check consistency
            for (int i = 0; i < rows; i++) {
                boolean allZero = true;
                for (int j = 0; j < cols; j++) {
                    if (mat[i][j] == 1) {
                        allZero = false;
                        break;
                    }
                }
                if (allZero && mat[i][cols] == 1) {
                    return -1;
                }
            }

            // Find free variables
            Set<Integer> basicVars = new HashSet<>();
            for (int p : pivot) {
                if (p != -1) {
                    basicVars.add(p);
                }
            }

            List<Integer> freeVars = new ArrayList<>();
            for (int i = 0; i < cols; i++) {
                if (!basicVars.contains(i)) {
                    freeVars.add(i);
                }
            }

            // Enumerate all solutions
            int minPresses = Integer.MAX_VALUE;
            int numFree = freeVars.size();

            for (int mask = 0; mask < (1 << numFree); mask++) {
                int[] solution = new int[cols];

                for (int i = 0; i < numFree; i++) {
                    solution[freeVars.get(i)] = (mask >> i) & 1;
                }

                for (int i = rows - 1; i >= 0; i--) {
                    if (pivot[i] == -1) {
                        continue;
                    }

                    int sum = mat[i][cols];
                    for (int j = pivot[i] + 1; j < cols; j++) {
                        sum ^= mat[i][j] * solution[j];
                    }
                    solution[pivot[i]] = sum;
                }

                int presses = Arrays.stream(solution).sum();
                minPresses = Math.min(minPresses, presses);
            }

            return minPresses;
        }

        int minPressesJoltage() {
            int m = buttons.size();
            int k = joltage.jolt().size();

            // Build matrix A
            int[][] A = new int[k][m];
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < m; j++) {
                    A[i][j] = buttons.get(j).list().contains(i) ? 1 : 0;
                }
            }

            int[] b = joltage.jolt().stream().mapToInt(Integer::intValue).toArray();

            return solveIntegerSystem(A, b, k, m);
        }

        private int solveIntegerSystem(int[][] A, int[] b, int rows, int cols) {
            // Create augmented matrix [A|b]
            int[][] mat = new int[rows][cols + 1];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(A[i], 0, mat[i], 0, cols);
                mat[i][cols] = b[i];
            }

            // Gaussian elimination
            int[] pivot = new int[rows];
            Arrays.fill(pivot, -1);

            for (int col = 0, row = 0; col < cols && row < rows; col++) {
                int pivotRow = -1;
                for (int i = row; i < rows; i++) {
                    if (mat[i][col] != 0) {
                        pivotRow = i;
                        break;
                    }
                }
                if (pivotRow == -1) continue;

                if (pivotRow != row) {
                    int[] temp = mat[row];
                    mat[row] = mat[pivotRow];
                    mat[pivotRow] = temp;
                }

                pivot[row] = col;

                // Integer elimination
                for (int i = 0; i < rows; i++) {
                    if (i != row && mat[i][col] != 0) {
                        // Use GCD-based elimination to avoid fractions
                        int g = gcd(mat[row][col], mat[i][col]);
                        int mult1 = mat[i][col] / g;
                        int mult2 = mat[row][col] / g;

                        for (int j = 0; j <= cols; j++) {
                            mat[i][j] = mult2 * mat[i][j] - mult1 * mat[row][j];
                        }
                    }
                }
                row++;
            }

            // Find free variables
            Set<Integer> basicVars = new HashSet<>();
            for (int p : pivot) {
                if (p != -1) basicVars.add(p);
            }

            List<Integer> freeVars = new ArrayList<>();
            for (int i = 0; i < cols; i++) {
                if (!basicVars.contains(i)) {
                    freeVars.add(i);
                }
            }

            // Find reasonable bounds for free variables
            int maxTarget = Arrays.stream(b).max().orElse(100);
            int maxFreeVal = maxTarget; // Heuristic upper bound

            return enumerateIntegerSolutions(mat, pivot, freeVars, rows, cols, maxFreeVal);
        }

        private int gcd(int a, int b) {
            a = Math.abs(a);
            b = Math.abs(b);
            while (b != 0) {
                int t = b;
                b = a % b;
                a = t;
            }
            return a;
        }

        private int enumerateIntegerSolutions(int[][] mat, int[] pivot,
                List<Integer> freeVars, int rows, int cols, int maxFreeVal) {
            int minPresses = Integer.MAX_VALUE;

            // Enumerate free variable combinations
            int[] freeVals = new int[freeVars.size()];
            minPresses = searchSolutions(mat, pivot, freeVars, freeVals, 0, rows, cols, maxFreeVal, minPresses);

            return minPresses == Integer.MAX_VALUE ? -1 : minPresses;
        }

        private int searchSolutions(int[][] mat, int[] pivot, List<Integer> freeVars,
                int[] freeVals, int idx, int rows, int cols, int maxFreeVal, int minPresses) {
            if (idx == freeVars.size()) {
                // Try this combination
                int[] solution = new int[cols];

                for (int i = 0; i < freeVars.size(); i++) {
                    solution[freeVars.get(i)] = freeVals[i];
                }

                // Back-substitute
                for (int i = rows - 1; i >= 0; i--) {
                    if (pivot[i] == -1) continue;

                    int sum = mat[i][cols];
                    for (int j = pivot[i] + 1; j < cols; j++) {
                        sum -= mat[i][j] * solution[j];
                    }

                    if (mat[i][pivot[i]] == 0 || sum % mat[i][pivot[i]] != 0) {
                        return minPresses; // Invalid solution
                    }

                    int val = sum / mat[i][pivot[i]];
                    if (val < 0) return minPresses; // Negative not allowed

                    solution[pivot[i]] = val;
                }

                int total = Arrays.stream(solution).sum();
                return Math.min(minPresses, total);
            }

            // Try values for this free variable
            for (int val = 0; val <= maxFreeVal; val++) {
                freeVals[idx] = val;
                minPresses = searchSolutions(mat, pivot, freeVars, freeVals, idx + 1, rows, cols, maxFreeVal, minPresses);
            }

            return minPresses;
        }
    }

    record Joltage(List<Integer> jolt) {

        static Joltage of(String s) {
            final List<Integer> list = Arrays.stream(s.replaceAll("[^0-9,]", "").split(","))
                    .filter(str -> !str.isEmpty())
                    .map(Integer::parseInt)
                    .toList();
            return new Joltage(list);
        }
    }

    record IndicatorLights(List<Boolean> lights) {

        static IndicatorLights of(String s) {
            final List<Boolean> list = s.chars().filter(c -> c == '.' || c == '#')
                    .mapToObj(c -> c == '#')
                    .collect(Collectors.toList());
            return new IndicatorLights(list);
        }

        void toggle(int i) {
            final Boolean b = lights.get(i);
            lights.set(i, !b);
        }

        void press(Button b) {
            for (final Integer i : b.list()) {
                toggle(i);
            }
        }

        boolean allOff() {
            return lights.stream().noneMatch(b -> b);
        }

        boolean allOn() {
            return lights.stream().allMatch(b -> b);
        }

    }

    record Button(List<Integer> list) {

        static Button of(String s) {
            final List<Integer> list = s.chars()
                    .filter(Character::isDigit)
                    .mapToObj(Character::getNumericValue)
                    .toList();
            return new Button(list);
        }
    }
}
