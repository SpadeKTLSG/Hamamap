package org.spc.demos.basic2;

import java.util.Set;

public interface MyMap<K, V> {

    /**
     * @param k
     * @param v
     * @return
     * @Description: 插入键值对方法
     * @date: 2019年7月13日 下午3:59:16
     */
    V put(K k, V v);

    /**
     * @param k
     * @return
     * @Description:根据key获取value
     * @date: 2019年7月13日 下午3:59:40
     */
    V get(K k);

    /**
     * @param k key键
     * @return
     * @Description: 判断key键是否存在
     * @date: 2019年7月23日 下午4:07:22
     */
    boolean containsKey(K k);


    /**
     * @return
     * @Description: 获取map集合中所有的key，并放入set集合中
     * @date: 2019年7月23日 下午4:24:19
     */
    Set<K> keySet();


//------------------------------内部接口 Entry（存放key-value）---------------------

    /**
     * @Title: Enter
     * @Description: 定义内部接口 Entry，存放键值对的Entery接口
     * @date: 2019年7月13日 下午4:00:33
     */
    interface Entry<K, V> {
        /**
         * @return
         * @Description: 获取key方法
         * @date: 2019年7月13日 下午4:02:06
         */
        K getKey();

        /**
         * @return
         * @Description:获取value方法
         * @date: 2019年7月13日 下午4:02:10
         */
        V getValue();
    }
}
