package org.example.stream;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.example.stream.code.Dish;

public class Grouping {

    public static void main(String[] args) {
        // 400 이상은 그룹화 되지 않음
        Map<Integer, List<Dish>> map = Dish.menu.stream()
            .filter(dish -> dish.getKal() < 400)
            .collect(Collectors.groupingBy(Dish::getKal));
        System.out.println("map = " + map);

        // 400 이상도 그룹화되어 빈 리스트 반환 []
        Map<Integer, List<Dish>> map1 = Dish.menu.stream()
            .collect(
                Collectors.groupingBy(
                    Dish::getKal,
                    Collectors.filtering(dish -> dish.getKal() < 400, Collectors.toList())
                )
            );
        System.out.println("map1 = " + map1);

        // kal 별로 그룹화 하는데 이름만 추출해 리스트로
        Map<Integer, List<String>> map2 = Dish.menu.stream()
            .collect(
                Collectors.groupingBy(
                    Dish::getKal,
                    Collectors.mapping(Dish::getName, Collectors.toList())
                )
            );
        System.out.println("map2 = " + map2);

        // name 으로 그룹화해 kal 전부 더함
        Map<String, Integer> map3 = Dish.menu.stream()
            .collect(Collectors.groupingBy(Dish::getName,
                Collectors.summingInt(Dish::getKal)));
        System.out.println("map3 = " + map3);

        // name 으로 그룹화해 관련 데이터 정보들 다뽑아줌 sum , min , count, max ,average
        Map<String, IntSummaryStatistics> map4 = Dish.menu.stream()
            .collect(Collectors.groupingBy(Dish::getName,
                Collectors.summarizingInt(Dish::getKal)));

        IntSummaryStatistics ggule = map4.get("ggule");
        BigDecimal decimal = BigDecimal.valueOf(ggule.getAverage()).setScale(3);
        System.out.println(decimal);
        System.out.println("map4 = " + map4);

        // maxBy 는 optional 반환
        Map<String, Optional<Dish>> map5 = Dish.menu.stream()
            .collect(Collectors.groupingBy(Dish::getName,
                Collectors.maxBy(Comparator.comparing(Dish::getKal))));
        System.out.println("map5 = " + map5);

        // maxBy 는 optional 반환 -> Optional.empty() 가 될리 없으므로 collectingAndThen 로 해결
        Map<String, Dish> map6 = Dish.menu.stream()
            .collect(Collectors.groupingBy(Dish::getName,
                Collectors.collectingAndThen(
                    Collectors.maxBy(Comparator.comparing(Dish::getKal)),
                    Optional::get
                )
            ));
        System.out.println("map6 = " + map6);

        // true false 로 분할
        Map<Boolean, List<Dish>> map7 = Dish.menu.stream()
            .collect(Collectors.partitioningBy(dish -> dish.getKal() < 400));
        System.out.println("map7 = " + map7);

        // true false 로 분할 후 그룹화
        Map<Boolean, Map<Integer, List<Dish>>> map8 = Dish.menu.stream()
            .collect(
                Collectors.partitioningBy(
                    dish -> dish.getKal() < 400,
                    Collectors.groupingBy(Dish::getKal)
                ));
        System.out.println("map8 = " + map8);

        // true false 로 분할 후 그룹화 후 최고의 칼로리만
        Map<Boolean, Dish> map9 = Dish.menu.stream()
            .collect(
                Collectors.partitioningBy(
                    dish -> dish.getKal() < 400,
                    Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparing(Dish::getKal)),
                        Optional::get
                    )
                ));
        System.out.println("map9 = " + map9);
    }
}
