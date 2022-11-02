package org.example.stream.code;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(of = "name")
public class Dish {

    private String name;
    private int kal;

    static public List<Dish> menu = Arrays.asList(
        new Dish("fruit", 120),
        new Dish("prawns", 300),
        new Dish("rice", 350),
        new Dish("chicken", 400),
        new Dish("fries", 530)
    );

}
