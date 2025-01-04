package org.spc.origins;

public interface IHamamap_Fake<K, V> {

    //! Query Operations 查询操作

    /**
     * Returns number of KVs. If it's more than {@code Integer.MAX_VALUE}, just returns that
     * <p>
     * 返回键值对数, 若超整型上限 {@code Integer.MAX_VALUE} 返回上限
     *
     * @return KVs' amount 键值对数
     */
    int size();

    /**
     * If it's empty
     * <p>
     * 是否没有键值对
     *
     * @return {@code true} if this map contains nothing
     */
    boolean isEmpty();

    /**
     * If it got a KV for that key
     * <p>
     * 是否有这个键的键值对
     *
     * @param key K represents KV wanted 代表需要的键值对的键
     * @return {@code true} if it contains a KVmapping for key 是否包含有这个键的键值对
     * @throws ClassCastException   if key's type inappropriate 如果key类型不合适
     * @throws NullPointerException if key's null 如果key为空
     */
    boolean containsKey(Object key);

    /**
     * Returns {@code true} if this map maps one or more keys to the
     * specified value.  More formally, returns {@code true} if and only if
     * this map contains at least one mapping to a value {@code v} such that
     * {@code Objects.equals(value, v)}.  This operation
     * will probably require time linear in the map size for most
     * implementations of the {@code Map} interface.
     *
     * @param value value whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more keys to the
     * specified value
     * @throws ClassCastException   if the value is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified value is null and this
     *                              map does not permit null values
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    boolean containsValue(Object value);

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that
     * {@code Objects.equals(key, k)},
     * then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     *
     * <p>If this map permits null values, then a return value of
     * {@code null} does not <i>necessarily</i> indicate that the map
     * contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to {@code null}.  The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     * {@code null} if this map contains no mapping for the key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    V get(Object key);


    //!  Modification Operations 修改操作

}
