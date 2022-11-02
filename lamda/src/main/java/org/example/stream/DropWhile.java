package org.example.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.example.stream.code.Dish;

public class DropWhile {



    public static void main(String[] args) {
        List<Dish> filteredList = Dish.menu.stream()
            .dropWhile(dish -> dish.getKal() < 320)
            .collect(Collectors.toList());

        System.out.println("filteredList = " + filteredList); // rice, chicken, fries
    }

}
