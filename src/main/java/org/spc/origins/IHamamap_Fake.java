package org.spc.origins;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface IHamamap_Fake<K, V> {

    //? JavaDoc Simplify: KVmapping(KV) == Key-Value mapping 键值对映射

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


    /**
     * A map entry (key-value pair). The Entry may be unmodifiable, or the
     * value may be modifiable if the optional {@code setValue} method is
     * implemented. The Entry may be independent of any map, or it may represent
     * an entry of the entry-set view of a map.
     * <p>
     * Instances of the {@code Map.Entry} interface may be obtained by iterating
     * the entry-set view of a map. These instances maintain a connection to the
     * original, backing map. This connection to the backing map is valid
     * <i>only</i> for the duration of iteration over the entry-set view.
     * During iteration of the entry-set view, if supported by the backing map,
     * a change to a {@code Map.Entry}'s value via the
     * {@link Map.Entry#setValue setValue} method will be visible in the backing map.
     * The behavior of such a {@code Map.Entry} instance is undefined outside of
     * iteration of the map's entry-set view. It is also undefined if the backing
     * map has been modified after the {@code Map.Entry} was returned by the
     * iterator, except through the {@code Map.Entry.setValue} method. In particular,
     * a change to the value of a mapping in the backing map might or might not be
     * visible in the corresponding {@code Map.Entry} element of the entry-set view.
     *
     * @apiNote It is possible to create a {@code Map.Entry} instance that is disconnected
     * from a backing map by using the {@link Map.Entry#copyOf copyOf} method. For example,
     * the following creates a snapshot of a map's entries that is guaranteed not to
     * change even if the original map is modified:
     * <pre> {@code
     * var entries = map.entrySet().stream().map(Map.Entry::copyOf).toList()
     * }</pre>
     * @see Map#entrySet()
     * @since 1.2
     */
    interface Entry<K, V> {


        /**
         * Returns the key corresponding to this entry.
         *
         * @return the key corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *                               required to, throw this exception if the entry has been
         *                               removed from the backing map.
         */
        K getKey();

        /**
         * Returns the value corresponding to this entry.  If the mapping
         * has been removed from the backing map (by the iterator's
         * {@code remove} operation), the results of this call are undefined.
         *
         * @return the value corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *                               required to, throw this exception if the entry has been
         *                               removed from the backing map.
         */
        V getValue();

        /**
         * Replaces the value corresponding to this entry with the specified
         * value (optional operation).  (Writes through to the map.)  The
         * behavior of this call is undefined if the mapping has already been
         * removed from the map (by the iterator's {@code remove} operation).
         *
         * @param value new value to be stored in this entry
         * @return old value corresponding to the entry
         * @throws UnsupportedOperationException if the {@code put} operation
         *                                       is not supported by the backing map
         * @throws ClassCastException            if the class of the specified value
         *                                       prevents it from being stored in the backing map
         * @throws NullPointerException          if the backing map does not permit
         *                                       null values, and the specified value is null
         * @throws IllegalArgumentException      if some property of this value
         *                                       prevents it from being stored in the backing map
         * @throws IllegalStateException         implementations may, but are not
         *                                       required to, throw this exception if the entry has been
         *                                       removed from the backing map.
         */
        V setValue(V value);

        /**
         * Compares the specified object with this entry for equality.
         * Returns {@code true} if the given object is also a map entry and
         * the two entries represent the same mapping.  More formally, two
         * entries {@code e1} and {@code e2} represent the same mapping
         * if<pre>
         *     (e1.getKey()==null ?
         *      e2.getKey()==null : e1.getKey().equals(e2.getKey()))  &amp;&amp;
         *     (e1.getValue()==null ?
         *      e2.getValue()==null : e1.getValue().equals(e2.getValue()))
         * </pre>
         * This ensures that the {@code equals} method works properly across
         * different implementations of the {@code Map.Entry} interface.
         *
         * @param o object to be compared for equality with this map entry
         * @return {@code true} if the specified object is equal to this map
         * entry
         */
        boolean equals(Object o);

        /**
         * Returns the hash code value for this map entry.  The hash code
         * of a map entry {@code e} is defined to be: <pre>
         *     (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
         *     (e.getValue()==null ? 0 : e.getValue().hashCode())
         * </pre>
         * This ensures that {@code e1.equals(e2)} implies that
         * {@code e1.hashCode()==e2.hashCode()} for any two Entries
         * {@code e1} and {@code e2}, as required by the general
         * contract of {@code Object.hashCode}.
         *
         * @return the hash code value for this map entry
         * @see Object#hashCode()
         * @see Object#equals(Object)
         * @see #equals(Object)
         */
        int hashCode();

    }


}
