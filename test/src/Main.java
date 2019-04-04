import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        List<Integer> mainList = Stream
                .of(10, 20, 30, 40, 50, 60)
                .collect(Collectors.toList());
        // allMatch
        List<Integer> subList = Stream.of(10,20,30).collect(Collectors.toList());
        boolean allValuesPresent = subList.stream().allMatch(v -> mainList.contains(v));

        System.out.println(mainList);
        System.out.println(subList);
        System.out.printf("result : "+ allValuesPresent);
    }
}
