package org.spc;

import java.util.HashMap;

public class HashMapExample {

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");

        map.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
