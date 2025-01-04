package org.spc.demos.basic2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings(value = {"unchecked", "rawtypes", "hiding"})
public class MyHashMap<K, V> implements MyMap<K, V> {

    /**
     * Entry数组的默认初始化长度为16；通过位移运算向左移动四位，得到二进制码 "00010000",转换为十进制是16
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    /**
     * 负载因子默认为0.75f；负载因子是用来标志当使用容量占总容量的75%时，就需要扩充容量了，
     * 扩充Entry数组的长度为原来的两倍，并且重新对所存储的key-value键值对进行散列。
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    /**
     * 可设置的负载因子
     */
    private final float defaultLoadFactor;
    /**
     * 可设置的初始容量
     */
    private int defaultInitSize;
    /**
     * 当前已存入的元素的数量
     */
    private int entryUseSize;

    /**
     * 存放key-value键值对对象的数组
     */
    private Entry<K, V>[] table = null;


    /**
     * 无参构造，数组初始大小为16，负载因子大小为0.75f
     */
    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * 有参构造，自己设置数组初始大小和负载因子大小
     *
     * @param defaultInitialCapacity 数组初始大小
     * @param defaultLoadFactor2     负载因子
     */
    public MyHashMap(int defaultInitialCapacity, float defaultLoadFactor2) {
        //判断初始容量参数是否合法
        if (defaultInitialCapacity < 0) {
            //抛出非法参数异常
            throw new IllegalArgumentException("输入的初始容量参数是非法的  ：" + defaultInitialCapacity);
        }
        //判断负载因子参数是否合法，Float.isNaN()方法是判断数据是否符合 0.0f/0.0f
        if (defaultLoadFactor2 < 0 || Float.isNaN(defaultLoadFactor2)) {
            throw new IllegalArgumentException("输入的负载因子参数是非法的  ：" + defaultLoadFactor2);
        }
        this.defaultInitSize = defaultInitialCapacity;
        this.defaultLoadFactor = defaultLoadFactor2;
        //初始化数组
        table = new Entry[this.defaultInitSize];
    }

    /**
     * @param k
     * @param v
     * @return 如是更新则返回key的旧value值，如是插入新的key-value则返回null
     * @Description: 集合中的put方法
     * @date: 2019年7月13日 下午6:29:47
     */
    @Override
    public V put(K k, V v) {
        V oldValue = null;
        //是否需要扩容？
        //扩容完毕后一定会需要重新进行散列
        if (entryUseSize >= defaultInitSize * defaultLoadFactor) {
            //扩容并重新散列,扩容为原来的两倍
            resize(2 * defaultInitSize);
        }
        //根据key获取的HASH值、数组长度减1，两者做'与'运算，计算出数组中的位置
        int index = hash(k) & (defaultInitSize - 1);
        //如果数组中此下标位置没有元素的话，就直接放到此位置上
        if (table[index] == null) {
            table[index] = new Entry(k, v, null);
            //总存入元素数量+1
            ++entryUseSize;
        } else {
            //遍历数组下边的链表
            Entry<K, V> entry = table[index];
            Entry<K, V> e = entry;
            while (e != null) {
                if (k == e.getKey() || k.equals(e.getKey())) {
                    oldValue = e.getValue();
                    //key已存在，直接更新value
                    e.value = v;
                    return oldValue;
                }
                //获取数组此下标位置上链表的下个元素
                e = e.next;
            }
            //JDK1.7中的链表头插法，直接占据数组下标位置
            table[index] = new Entry<K, V>(k, v, entry);
            //总存入元素数量+1
            ++entryUseSize;
        }
        return oldValue;
    }


    /**
     * @param k
     * @return
     * @Description: 根据key获取value值
     * @date: 2019年7月13日 下午6:34:49
     */
    @Override
    public V get(K k) {
        //通过hash函数和数组元素容量做  【与】运算得到数组下标
        int index = hash(k) & (defaultInitSize - 1);
        if (table[index] == null) {
            return null;
        } else {
            //获取到数组下标位置元素
            Entry<K, V> entry = table[index];
            Entry<K, V> e = entry;
            do {
                if (k.equals(e.getKey())) {
                    return e.getValue();
                }
                //获取数组下标位置对应链表中的下一个元素
                e = e.next;
            } while (entry != null);
        }
        return null;
    }


    /**
     * @param size 扩容后的大小
     * @Description:扩容并重新将元素进行散列
     * @date: 2019年7月13日 下午5:06:06
     */
    public void resize(int size) {
        Entry<K, V>[] newTable = new Entry[size];
        //改变数组的初始大小
        defaultInitSize = size;
        //将已存放键值对数量置为0
        entryUseSize = 0;
        //将已存的元算根据最新的数组的大小进行散列
        rehash(newTable);
    }


    /**
     * @param newTable
     * @Description: 重新进行散列
     * @date: 2019年7月13日 下午5:10:07
     */
    public void rehash(Entry<K, V>[] newTable) {
        List<Entry<K, V>> entryList = new ArrayList<>();
        for (Entry<K, V> entry : table) {
            if (entry != null) {
                do {
                    //将原来数组中的元素放到list集合中
                    entryList.add(entry);
                    //如果此数组下标的位置存在链表的话，需要遍历下列表，将列表中的键值对数据取出来放到集合中
                    entry = entry.next;
                } while (entry != null);
            }
        }

        //将旧的数组引用覆盖，让引用指向堆中新开辟的数组
        if (newTable.length > 0) {
            table = newTable;
        }

        //所谓重新的散列hash，就是将元素重新放入到扩容后的集合中
        for (Entry<K, V> entry : entryList) {
            //重新put
            put(entry.getKey(), entry.getValue());
        }
    }


    /**
     * @param key
     * @return
     * @Description: 根据key获取hashcod码值
     * @date: 2019年7月13日 下午5:52:22
     */
    public int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }


    /**
     * @param k key键
     * @return
     * @Description: 判断是否存在此key
     * @date: 2019年7月23日 下午4:52:22
     */
    public boolean containsKey(K k) {
        boolean flag = false;

        int index = hash(k) & (defaultInitSize - 1);
        if (table[index] == null) {
            return flag;
        } else {
            //获取到数组下标位置元素
            Entry<K, V> entry = table[index];
            Entry<K, V> e = entry;
            do {
                if (k.equals(e.getKey())) {
                    flag = true;
                    return flag;
                }
                //获取数组下标位置对应链表中的下一个元素
                e = e.next;
            } while (e != null);
        }

        return flag;
    }


    /**
     * @return
     * @Description: 获取map集合所有的key
     * @date: 2019年7月23日 下午5:52:22
     */
    @Override
    public Set<K> keySet() {
        if (entryUseSize == 0) {
            return null;
        }
        Set<K> entrySet = new HashSet<K>();
        for (Entry<K, V> entry : table) {
            if (entry != null) {
                do {
                    //将原来数组中的元素的key放到set集合中
                    entrySet.add(entry.getKey());
                    //如果此数组下标的位置存在链表的话，需要遍历下列表，将列表中元素的key取出来放到集合中
                    entry = entry.next;
                } while (entry != null);
            }
        }
        return entrySet;
    }


//----------------------------------------内部类 Entry（存放key-value）----------------


    /**
     * @Title: Entry
     * @Description: 实现了key-value简直对接口的java类
     * @date: 2019年7月13日 下午6:12:16
     */
    class Entry<K, V> implements MyMap.Entry<K, V> {
        /**
         * 键值对对象的key
         */
        private K key;
        /**
         * 键值对对象的value
         */
        private volatile V value;
        /**
         * 键值对对象指向下一个键值对对象的指针
         */
        private Entry<K, V> next;

        /**
         * 无参构造
         */
        public Entry() {

        }


        /**
         * 有参构造
         *
         * @param key
         * @param value
         * @param next
         */
        public Entry(K key, V value, Entry<K, V> next) {
            super();
            this.key = key;
            this.value = value;
            this.next = next;
        }


        /**
         * 获取key
         */
        @Override
        public K getKey() {
            return key;
        }

        /**
         * 获取value
         */
        @Override
        public V getValue() {
            return value;
        }
    }

}
