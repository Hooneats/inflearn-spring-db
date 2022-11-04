package org.example.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FlatMap {

    static List<Integer> number1 = Arrays.asList(1, 2, 3);
    static List<Integer> number2 = Arrays.asList(3, 4);

    public static void main(String[] args) {
        // TODO : List<Stream<String>> ----> List<String> 으로
        // [1,2,3] , [3,4] -> [(1,3)(1,4)(2,3)(2,4)(3,3)(3), 4)]
        List<String> result1 = number1.stream()
            .flatMap(i -> number2.stream().map(j -> "(" + i + ", " + j + ")"))
            .collect(Collectors.toList());

        System.out.println("result1 = " + result1);

        // [1,2,3] , [3,4] -> 숫자 쌍 합이 3으로 나누어 떨어지는 집합
        List<String> result2 = number1.stream()
            .flatMap(i -> number2.stream()
                .filter(j -> (i + j) % 3 == 0)
                .map(j -> "(" + i + ", " + j + ")"))
            .collect(Collectors.toList());

        System.out.println("result2 = " + result2);
    }



}
