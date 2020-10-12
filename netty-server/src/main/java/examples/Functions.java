package examples;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Functions {

    public static void main(String[] args) {
        Consumer<String> consumer = s -> {
            System.out.println("Hello " + s);
        };
        Consumer<String> consumer1 = System.out::println;
        //consumer.andThen(consumer1).accept("WORLD");
        Predicate<String> predicate = s -> s.length() > 3;
        predicate = predicate.and(s -> s.startsWith("OK"));
        System.out.println(predicate.test("OKLOL2"));

        Supplier<ArrayList<String>> supplier = ArrayList::new;

        Function<String, Integer> function = String::length;
        System.out.println(function.apply("OK"));


    }
}
