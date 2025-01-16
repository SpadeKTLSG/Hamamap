package org.spc.wrapper;

public class AllLocate<K, V> {

    /**
     * 定位的头节点
     */
    private Wrapper<K, V> head;
    /**
     * 实际对象
     */
    private Wrapper<K, V> data;

    /**
     * 头结点在table位置
     */
    private int sit;

}
