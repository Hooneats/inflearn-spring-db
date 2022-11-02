package org.example.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Reduce {

    static List<Integer> ex = Arrays.asList(1, 2, 3, 4, 5, 5, 3, 1, 23, 4, 1, 23, 1, 23, 55, 34, 5, 33, 35,
        66);

    public static void main(String[] args) {
        Optional<Integer> max1 = ex.stream().reduce(Integer::max);
        Optional<Integer> min1 = ex.stream().reduce(Integer::min);
        System.out.println("max1.get() = " + max1.get());
        System.out.println("max1.get() = " + max1.get());

        int size = ex.size();
        System.out.println("size = " + size);
        //mpa reduce => ex.size() * 3 (개당 3점으로다가)
        Integer score = ex.stream()
            .map(i -> 3)
            .reduce(0, (a, b) -> a + b);
        System.out.println("score = " + score);

        Integer sum = ex.stream()
            .collect(Collectors.reducing(0, integer -> integer, Integer::sum));
        System.out.println("sum = " + sum);
        Integer sum1 = ex.stream().map(integer -> integer).reduce(0, Integer::sum);
        System.out.println("sum1 = " + sum1);
        Integer sum2 = ex.stream()
            .collect(Collectors.reducing(0, Integer::sum));
        System.out.println("sum2 = " + sum2);
        Integer sum3 = ex.stream().reduce(0, Integer::sum);
        System.out.println("sum3 = " + sum3);

        /**
         * TODO :내부 상태를 갖는 즉 내부 저장소가 필요한 stream 연산 :
         *          ㄴ sum, max, reduce 등의 연산과 sorted, distinct ==> 이전 상태값을 알고있어야 하거나 연산한 값을 저장하고 있어야한다.
         *       내부 상태를 갖지 않는 연산 :
         *          ㄴ map, filter 등
         */

    }

}
