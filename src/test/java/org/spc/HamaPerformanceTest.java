package org.spc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spc.impl.Hamamap;

import java.util.HashMap;

/**
 * HamaPerformanceTest  性能测试
 */
public class HamaPerformanceTest {

    private Hamamap<String, Integer> map;
    private HashMap<String, Integer> jmap;

    @BeforeEach
    void setUp() {
        map = new Hamamap<>();
        jmap = new HashMap<>();
    }

    /**
     * 测试Hamamap插入性能
     */
    @Test
    void testInsertPerformance() {
        long startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            map.put("key" + i, i);
        }
        long endTime = System.nanoTime();
        System.out.println("Hamamap insert time: " + (endTime - startTime) + " ns");
        long hmapInsertTime = endTime - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            jmap.put("key" + i, i);
        }
        endTime = System.nanoTime();
        System.out.println("HashMap insert time: " + (endTime - startTime) + " ns");
        long jmapInsertTime = endTime - startTime;

        //计算两者差距的百分比: Hamamap的耗时是HashMap的多少倍:
        System.out.println("Hamamap insert time is " + (double) hmapInsertTime / jmapInsertTime + " times of HashMap");
    }

    /**
     * 测试Hamamap查询性能
     */
    @Test
    void testQueryPerformance() {
        for (int i = 0; i < 100000; i++) {
            map.put("key" + i, i);
            jmap.put("key" + i, i);
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            map.get("key" + i);
        }
        long endTime = System.nanoTime();
        System.out.println("Hamamap query time: " + (endTime - startTime) + " ns");
        long hmapQueryTime = endTime - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            jmap.get("key" + i);
        }
        endTime = System.nanoTime();
        System.out.println("HashMap query time: " + (endTime - startTime) + " ns");
        long jmapQueryTime = endTime - startTime;

        //计算两者差距的百分比: Hamamap的耗时是HashMap的多���倍:
        System.out.println("Hamamap query time is " + (double) hmapQueryTime / jmapQueryTime + " times of HashMap");
    }

    /**
     * 测试Hamamap删除性能
     */
    @Test
    void testDeletePerformance() {
        for (int i = 0; i < 100000; i++) {
            map.put("key" + i, i);
            jmap.put("key" + i, i);
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            map.remove("key" + i);
        }
        long endTime = System.nanoTime();
        System.out.println("Hamamap delete time: " + (endTime - startTime) + " ns");
        long hmapDeleteTime = endTime - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            jmap.remove("key" + i);
        }
        endTime = System.nanoTime();
        System.out.println("HashMap delete time: " + (endTime - startTime) + " ns");
        long jmapDeleteTime = endTime - startTime;

        //计算两者差距的百分比: Hamamap的耗时是HashMap的多少倍:
        System.out.println("Hamamap delete time is " + (double) hmapDeleteTime / jmapDeleteTime + " times of HashMap");
    }
}
