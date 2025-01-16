package org.spc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spc.impl.Hamamap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 简单测试
 */
public class HamaEzTest {


    private Hamamap<String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new Hamamap<>();
    }

    /**
     * 测试Hamamap插入功能
     */
    @Test
    void testBasicFunc_Insert() {
        map.put("three", 3);
        map.put("three", 1);
        System.out.println(map.entrySet());
        assertEquals(1, map.get("three"));
    }

    /**
     * 测试Hamamap插入功能 NPE
     */
    @Test
    void testBasicFunc_InsertNPE() {

        map.put("one", 1);
        assertEquals(1, map.get("one"));
        map.put("two", 2);
        assertEquals(2, map.get("two"));
        map.put("three", 3);
        System.out.println(map.entrySet());
        assertEquals(3, map.get("three"));
    }
}
