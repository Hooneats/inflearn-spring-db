package org.example.stream;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerateCollection {


    public static void main(String[] args) {
        // removeIf , removeAll , sort
        List<String> list = Stream.of("1", "2", "3", "4")
            .collect(Collectors.toList());


        Set<String> set = Stream.of("1", "2", "3", "4")
            .collect(Collectors.toSet());

        // forEach , sorted(Entry.comparingByKey) , sorted(Entry.comparingByValue) , getOrDefault ,
        // computeIfAbsent : 값이 없으면 키를 이용해 새값을 추가 ,
        // computeIfPresent : 값이 있으면 새 값을 추가
        // compute : 그냥 꺼내서 새값을 추가
        // remove(key, value)
        // replaceAll(key, value) : 모든 값을 변경한다.
        // replace(key, value) : 키가 존재하면 맵의 값을 바꾼다.
        // merge(key, value, BiFunction) : 중복된 키의 값이 null 이 아닌 존재하면 어떻게 처리해서 넣을지 BiFunction 에 정의
        // entrySet().removeIf(Predicate)
        Map<String, List<String>> map = Stream.of("1", "2", "3", "4")
            .collect(Collectors.groupingBy(s -> s));
    }

}
