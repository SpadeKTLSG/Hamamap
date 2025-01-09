package org.spc.origins;

import java.io.Serializable;
import java.util.*;

public interface IHamamap_Fake<K, V> {

    //? JavaDoc Simplify:
    // KVmapping(KV) == Key-Value mapping 键值对映射
    // hash code (value) = HC 哈希码(值)

    //! Query Operations 查询操作

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


    //!  Modification Operations 修改操作

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


    //! Bulk Operations 批处理操作


    /**
     * Add all mappings from m to this map
     * <p>
     * 将m的所有KV添加到这个map
     *
     * @throws ClassCastException   if one key/value's type inappropriate
     * @throws NullPointerException if m is null or one key/value's null
     */
    void putAll(Map<? extends K, ? extends V> m);

    /**
     * Removes all mappings
     * <p>
     * 移除所有KV
     */
    void clear();

    //! Views Operations 视图操作

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
    Set<IHamamap_Fake.Entry<K, V>> entrySet();
    // Set 隐含 KV 不重复特性


    //! Comparison / hashing Operations 比较/哈希操作

    /**
     * Compares two maps for equality.
     * <p>
     * 比较两个map是否相等
     *
     * @return T if o is also a map and the two represent the same KVs
     */
    boolean equals(Object o);

    /**
     * Returns the hash code value for this map.  The hash code of a map is
     * defined to be the sum of the hash codes of each entry in the map's
     * {@code entrySet()} view.  This ensures that {@code m1.equals(m2)}
     * implies that {@code m1.hashCode()==m2.hashCode()} for any two maps
     * {@code m1} and {@code m2}, as required by the general contract of
     * {@link Object#hashCode}.
     *
     * @return the hash code value for this map
     * @see Map.Entry#hashCode()
     * @see Object#equals(Object)
     * @see #equals(Object)
     */
    int hashCode();


    /**
     * A KVpair, can be a view of a map, or a snapshot of a map, or a modifiable KVpair.
     * <p>
     * 一个KV对, 可以是map的视图, 或map的快照, 或可修改的KV对
     */
    interface Entry<K, V> {


        /**
         * Returns a comparator that compares {@link Map.Entry} in natural order on key.
         *
         * <p>The returned comparator is serializable and throws {@link
         * NullPointerException} when comparing an entry with a null key.
         *
         * @param <K> the {@link Comparable} type of then map keys
         * @param <V> the type of the map values
         * @return a comparator that compares {@link Map.Entry} in natural order on key.
         * @see Comparable
         * @since 1.8
         */
        static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K, V>> comparingByKey() {
            return (Comparator<Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> c1.getKey().compareTo(c2.getKey());
        }

        /**
         * Returns a comparator that compares {@link Map.Entry} in natural order on value.
         *
         * <p>The returned comparator is serializable and throws {@link
         * NullPointerException} when comparing an entry with null values.
         *
         * @param <K> the type of the map keys
         * @param <V> the {@link Comparable} type of the map values
         * @return a comparator that compares {@link Map.Entry} in natural order on value.
         * @see Comparable
         * @since 1.8
         */
        static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K, V>> comparingByValue() {
            return (Comparator<Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> c1.getValue().compareTo(c2.getValue());
        }

        /**
         * Returns a comparator that compares {@link Map.Entry} by key using the given
         * {@link Comparator}.
         *
         * <p>The returned comparator is serializable if the specified comparator
         * is also serializable.
         *
         * @param <K> the type of the map keys
         * @param <V> the type of the map values
         * @param cmp the key {@link Comparator}
         * @return a comparator that compares {@link Map.Entry} by the key.
         * @since 1.8
         */
        static <K, V> Comparator<Map.Entry<K, V>> comparingByKey(Comparator<? super K> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
        }

        /**
         * Returns a comparator that compares {@link Map.Entry} by value using the given
         * {@link Comparator}.
         *
         * <p>The returned comparator is serializable if the specified comparator
         * is also serializable.
         *
         * @param <K> the type of the map keys
         * @param <V> the type of the map values
         * @param cmp the value {@link Comparator}
         * @return a comparator that compares {@link Map.Entry} by the value.
         * @since 1.8
         */
        static <K, V> Comparator<Map.Entry<K, V>> comparingByValue(Comparator<? super V> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
        }

        /**
         * Returns a copy of the given {@code Map.Entry}. The returned instance is not
         * associated with any map. The returned instance has the same characteristics
         * as instances returned by the {@link Map#entry Map::entry} method.
         *
         * @param <K> the type of the key
         * @param <V> the type of the value
         * @param e   the entry to be copied
         * @return a map entry equal to the given entry
         * @throws NullPointerException if e is null or if either of its key or value is null
         * @apiNote An instance obtained from a map's entry-set view has a connection to that map.
         * The {@code copyOf}  method may be used to create a {@code Map.Entry} instance,
         * containing the same key and value, that is independent of any map.
         * @implNote If the given entry was obtained from a call to {@code copyOf} or {@code Map::entry},
         * calling {@code copyOf} will generally not create another copy.
         * @since 17
         */
        @SuppressWarnings("unchecked")
        static <K, V> Map.Entry<K, V> copyOf(Map.Entry<? extends K, ? extends V> e) {
            Objects.requireNonNull(e);
            if (e instanceof KeyValueHolder) {
                return (Map.Entry<K, V>) e;
            } else {
                return Map.entry(e.getKey(), e.getValue());
            }
        }

        /**
         * Returns the K
         * <p>
         * 返回K
         *
         * @return the key
         */
        K getKey();

        /**
         * Returns the V
         * <p>
         * 返回V
         *
         * @return the value
         */
        V getValue();

        /**
         * Set V to this entry, returns the old value; but if original KV has been removed, throws IllegalStateException
         * <p>
         * 设置V到这个entry, 返回原值; 但如果原KV已被移除, 则抛出IllegalStateException
         *
         * @return old value
         * @throws ClassCastException    if value's type inappropriate
         * @throws NullPointerException  if value is null
         * @throws IllegalStateException original one has been removed
         */
        V setValue(V value);

        /**
         * Compares object with this entry for equality
         * <p>
         * 与这个entry比较相等
         *
         * @return true if object is also a KV entry and the two represent the same KV
         */
        boolean equals(Object o);

        /**
         * Returns the HC for this entry
         *
         * <p>
         * 返回这个entry的哈希码
         *
         * <p>
         * <pre>
         *     (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
         *     (e.getValue()==null ? 0 : e.getValue().hashCode())
         * </pre>
         * <p>
         *
         * @return the HC for this map entry
         */
        int hashCode();
    }


}
