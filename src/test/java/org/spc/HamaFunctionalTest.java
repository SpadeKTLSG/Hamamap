package org.spc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spc.impl.Hamamap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HamaFunctionalTest   功能测试
 */
class HamaFunctionalTest {

    private Hamamap<String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new Hamamap<>();
    }


    //! 以下是一些基本的测试用例

    /**
     * 测试Hamamap插入功能
     */
    @Test
    void testBasicFunc_Insert() {
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        assertEquals(1, map.get("one"));
        assertEquals(2, map.get("two"));
        assertEquals(3, map.get("three"));
    }


    /**
     * 测试Hamamap删除功能
     */
    @Test
    void testBasicFunc_Delete() { //todo 没反应了
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        map.remove("two");

        assertNull(map.get("two"));
        assertEquals(1, map.get("one"));
        assertEquals(3, map.get("three"));
    }


    /**
     * 测试Hamamap查询功能
     */
    @Test
    void testQueryFunc() {
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        assertTrue(map.containsKey("one"));
        assertFalse(map.containsKey("four"));
        assertTrue(map.containsValue(2));
        assertFalse(map.containsValue(4));
    }


    /**
     * 测试Hamamap修改功能
     */
    @Test
    void testModifyFunc() { //todo 没反应了
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        map.put("two", 22);
        assertEquals(22, map.get("two"));

        map.put("three", 33);
        assertEquals(33, map.get("three"));
    }


    /**
     * 测试Hamamap拷贝功能
     */
    @Test
    void testCopyFunc() {
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        @SuppressWarnings("unchecked") Hamamap<String, Integer> copy = (Hamamap<String, Integer>) map.clone();
        assertEquals(3, copy.size());
        assertEquals(1, copy.get("one"));
        assertEquals(2, copy.get("two"));
        assertEquals(3, copy.get("three"));
    }


    //! 以下是一些复杂的测试用例

    /**
     * 测试Hamamap复合功能
     */
    @Test
    void testComplexFunc() {
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);
        map.put("five", 5);
        System.out.println(map.entrySet());
        assertEquals(5, map.size());
        assertTrue(map.containsKey("three"));
        assertTrue(map.containsValue(4));

        map.remove("four");
        assertFalse(map.containsKey("four"));
        assertEquals(4, map.size());
    }

    /**
     * 测试Hamamap多重功能
     */
    @Test
    void testMultipleFunc() {
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        assertEquals(3, map.size());
        assertTrue(map.containsKey("one"));
        assertTrue(map.containsValue(2));

        map.clear();
        assertEquals(0, map.size());
        assertFalse(map.containsKey("one"));
        assertFalse(map.containsValue(2));
    }


    /**
     * 终极测试
     */
    @Test
    void testComplexHashMapOperations() {
        // 插入400个数字
        for (int i = 1; i <= 400; i++) {
            map.put("key" + i, i);
        }
        assertEquals(400, map.size());

        // 删除其中的50个
        for (int i = 1; i <= 50; i++) {
            map.remove("key" + i);
        }
        assertEquals(350, map.size());

        // 再插入1000个
        for (int i = 401; i <= 1400; i++) {
            map.put("key" + i, i);
        }
        assertEquals(1350, map.size());

        // 再查询250次
        for (int i = 1; i <= 250; i++) {
            int keyIndex = (i % 1400) + 1;
            Integer value = map.get("key" + keyIndex);
            if (keyIndex <= 50) {
                assertNull(value);
            } else {
                assertNotNull(value);
            }
        }
    }


}
