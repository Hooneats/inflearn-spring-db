package org.example.stream;

import java.util.Optional;
import org.example.stream.code.Dish;

public class Match {

    public static void main(String[] args) {
        boolean isKal400 = Dish.menu.stream().allMatch(dish -> dish.getKal() < 400);
        boolean isExistsKal250 = Dish.menu.stream().anyMatch(dish -> dish.getKal() == 250);
        boolean isNoneKal250 = Dish.menu.stream().noneMatch(dish -> dish.getKal() == 250);
        Optional<Dish> findFirst = Dish.menu.stream().findFirst(); // 옵셔널 반환 nullable --> 병렬 스트림에서 쓸 수 없다.
        Optional<Dish> findAny = Dish.menu.stream().findAny(); // 임의의 요소(병렬 스트림에서 사용 가능) 옵셔널 반환 nullable

        System.out.println("findFirst. = " + findFirst.get());
        System.out.println("findAny =  " + findAny.get());

    }

}
