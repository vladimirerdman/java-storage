package examples;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Stream;

public class Monads {

    public static void main(String[] args) {
        //flatMap, get, apply
        Stream.of(1,2,3,4,5,6,7,8,9,10)
                .filter(val -> val % 2 == 0)
                //.map("a"::repeat)
                .map(s -> s + " " + s)
                .forEach(System.out::println);

//        List<String> data = Files.lines(Path.of("netty-server/src/main/java/examples/data.txt"))
//                .filter(line -> !line.isEmpty())
//                .flatMap(line -> Arrays.stream(line.split(" +")))
//                .collect(Collectors.toList());
//        System.out.println(data);

    }
}
