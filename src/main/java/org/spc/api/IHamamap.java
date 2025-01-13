package org.spc.api;


import java.util.Collection;
import java.util.Set;

/**
 * Hamamap Interface - hashmap refactored ,  from "Rhapsody of Corner Trash Cans"
 * <p>
 * Hamamap 接口
 *
 * @author SpadeKTLSG 玄桃K
 */
public interface IHamamap<K, V> {


    //! 1 Query Operations 查询操作


    /**
     * Returns number of KVs. If it's more than Integer.MAX_VALUE, just returns that
     * <p>
     * 返回KV数, 若超整型上限 Integer.MAX_VALUE 返回上限
     *
     * @return KVs' amount
     */
    int size();

    /**
     * If it's empty
     * <p>
     * 是否没有KV
     *
     * @return T if this contains nothing
     */
    boolean isEmpty();

    /**
     * If got a KV for that key
     * <p>
     * 是否有这个K的KV
     *
     * @return T if contains a KV for key
     * @throws ClassCastException   if key's type inappropriate
     * @throws NullPointerException if key's null
     */
    boolean containsKey(Object key);

    /**
     * Returns T if this map contains one or more KV for value
     * <p>
     * 如果这个map包含有一个或多个value的KV, 返回 T
     *
     * @return T if it contains one or more KV for value
     * @throws ClassCastException   if value's type inappropriate
     * @throws NullPointerException if value's null
     */
    boolean containsValue(Object value);

    /**
     * Returns V to inputed K, or null if no mapping for it
     * <p>
     * 返回键对应的值, 没有就返回null
     *
     * @return V to inputed K, or null if no mapping
     * @throws ClassCastException   if key's type inappropriate
     * @throws NullPointerException if key's null
     */
    V get(Object key);


    //!  2 Modification Operations 修改操作


    /**
     * Add a new KV for key and value; if has one for that key, update it then return the dead value
     * <p>
     * 添加一个新的KV, 若已有这个K的KV, 则更新它并返回原值
     *
     * @return the dead value, or null if no mapping for key
     * @throws ClassCastException   if key/value's type inappropriate
     * @throws NullPointerException if key/value's null
     */
    V put(K key, V value);


    /**
     * Removes a present mapping for a key, then returns the dead value or null if no mapping for that
     * <p>
     * 移除一个K的KV, 返回原值, 没有就返回null
     *
     * @return the dead value, or null if no mapping for key
     * @throws ClassCastException   if key's type inappropriate
     * @throws NullPointerException if key's null
     */
    V remove(Object key);


    //! 3 Bulk Operations 批处理操作


    /**
     * Add all mappings from m to this map
     * <p>
     * 将m的所有KV添加到这个map
     *
     * @throws ClassCastException   if one key/value's type inappropriate
     * @throws NullPointerException if m is null or one key/value's null
     */
    void putAll(IHamamapEx<? extends K, ? extends V> m);

    /**
     * Removes all mappings
     * <p>
     * 移除所有KV
     */
    void clear();


    //! 4 Views Operations 视图操作


    /**
     * Returns a Set view of keys which means changes to map are reflected in the set, and vice-versa
     * <p>
     * 返回键的Set视图, 这意味着对map的更改会反映在set中, 反之亦然
     *
     * @return a set view of the keys contained in this map
     */
    Set<K> keySet();
    // Set 隐含 key 不重复特性

    /**
     * Returns a Collection view of values, "view" see keySet() for more
     * <p>
     * 返回值的Collection视图, "视图" 详见 keySet()
     *
     * @return a collection view of the values contained in this map
     */
    Collection<V> values();
    // Collection 隐含 value 可重复特性


    /**
     * Returns a Set view of the KVs, "view" see keySet() for more
     * <p>
     * 返回KV的Set视图, "视图" 详见 keySet()
     *
     * @return a set view of the KVs contained in this map
     */
    Set<IHamaEntryEx<K, V>> entrySet();
    // Set 隐含 KV 不重复特性


    //! 5 Comparison / hashing Operations 比较/哈希操作


    /**
     * Compares two maps for equality.
     * <p>
     * 比较两个map是否相等
     *
     * @return T if o is also a map and the two represent the same KVs
     */
    boolean equals(Object o);

    /**
     * Returns the HC for this map
     * <p>
     * 返回这个map的哈希码
     *
     * @return HC for this map
     */
    int hashCode();


}
