package ski.komoro.aoc.twentyfive;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public class Main {

    static void main(String[] args) throws Exception {
        buildInstance(findDay(args)).run();
    }

    static AOCBase buildInstance(Class<? extends AOCBase> clazz) throws Exception {
        return clazz.getConstructor().newInstance();
    }

    static Class<? extends AOCBase> findDay(String[] args) throws Exception {
        final var subClasses = subClasses();
        return (args == null || args.length == 0 || args[0] == null || args[0].isEmpty())
                ? subClasses.findFirst().orElseThrow()
                : subClasses.filter(c -> c.getSimpleName().contains(args[0])).findFirst().orElseThrow();
    }

    @SuppressWarnings("unchecked")
    static Stream<Class<? extends AOCBase>> subClasses() {
        return Arrays.stream(AOCBase.class.getPermittedSubclasses())
                .sorted(Comparator.comparing(Class::getSimpleName, Comparator.reverseOrder()))
                .map(c -> (Class<? extends AOCBase>) c);

    }



}
