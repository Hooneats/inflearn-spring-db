package org.example.code;

import java.util.HashMap;
import java.util.Map;

public class RepositoryTemp {

    Map<String, String> map = Map.of("1", "ok");

    public String findById(String itemId) {
        String result = map.get(itemId);
        return result;
    }
}
