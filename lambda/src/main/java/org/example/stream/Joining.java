package org.example.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Joining {

    static List<String> ex = Arrays.asList("나는", "밥을", "먹었다");

    public static void main(String[] args) {
        // joining 은 내부적으로 StringBuilder 를 사용한다.
        String result = ex.stream()
            .collect(Collectors.joining(" "));

        System.out.println("result = " + result); // "나는 밥을 먹었다.
    }


}
