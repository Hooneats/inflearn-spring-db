package org.example.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.example.stream.code.Dish;

public class TakeWhile {

    public static void main(String[] args) {
        List<Dish> filteredList = Dish.menu.stream()
            .takeWhile(dish -> dish.getKal() < 320)
            .collect(Collectors.toList());

        System.out.println("filteredList = " + filteredList); // fruit, prawns
    }

}
