package org.spc.api;


import org.spc.impl.KVHolder;
import org.spc.tool.collect;

import java.util.Map;
import java.util.Objects;

/**
 * IHamamap 自带方法扩展接口
 */
public interface IHamamapEx<K, V> extends IHamamap<K, V> {


    /**
     * Returns an unmodifiable {@link Map.Entry} containing the given key and value.
     * These entries are suitable for populating {@code Map} instances using the
     * {@link Map#ofEntries Map.ofEntries()} method.
     */
    static <K, V> IHamaEntryEx<K, V> entry(K k, V v) {
        return new KVHolder<>(k, v);
    }

    /**
     * Returns an unmodifiable map containing no KV
     * <p>
     * 返回一个不可修改的没有KV的map
     */
    @SuppressWarnings("unchecked")
    static <K, V> IHamamapEx<K, V> of() {
        return (IHamamapEx<K, V>) collect.EMPTY_MAP;
    }


    /**
     * Returns V for K, if not found, returns default value
     * <p>
     * 返回K对应的V, 若没有则返回默认值
     */
    default V getOrDefault(Object key, V defaultValue) {
        return Objects.nonNull(get(key)) ? get(key) : defaultValue;
    }


    /**
     * If the specified key is not already associated with a value (or is mapped
     * to {@code null}) associates it with the given value and returns
     * {@code null}, else returns the current value.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with the specified key, or
     * {@code null} if there was no mapping for the key.
     * (A {@code null} return can also indicate that the map
     * previously associated {@code null} with the key,
     * if the implementation supports null values.)
     * @throws UnsupportedOperationException if the {@code put} operation
     *                                       is not supported by this map
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException            if the key or value is of an inappropriate
     *                                       type for this map
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified key or value is null,
     *                                       and this map does not permit null keys or values
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @implSpec The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * V v = map.get(key);
     * if (v == null)
     *     v = map.put(key, value);
     *
     * return v;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    default V putIfAbsent(K key, V value) {
        V v = get(key);
        if (v == null) {
            v = put(key, value);
        }

        return v;
    }


    /**
     * Removes the entry for the specified key only if it is currently
     * mapped to the specified value.
     *
     * @param key   key with which the specified value is associated
     * @param value value expected to be associated with the specified key
     * @return {@code true} if the value was removed
     * @throws UnsupportedOperationException if the {@code remove} operation
     *                                       is not supported by this map
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException            if the key or value is of an inappropriate
     *                                       type for this map
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified key or value is null,
     *                                       and this map does not permit null keys or values
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @implSpec The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * if (map.containsKey(key) && Objects.equals(map.get(key), value)) {
     *     map.remove(key);
     *     return true;
     * } else
     *     return false;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    default boolean remove(Object key, Object value) {
        Object curValue = get(key);
        if (!Objects.equals(curValue, value) || (curValue == null && !containsKey(key))) {
            return false;
        }
        remove(key);
        return true;
    }


    /**
     * Replaces the entry for the specified key only if currently
     * mapped to the specified value.
     *
     * @param key      key with which the specified value is associated
     * @param oldValue value expected to be associated with the specified key
     * @param newValue value to be associated with the specified key
     * @return {@code true} if the value was replaced
     * @throws UnsupportedOperationException if the {@code put} operation
     *                                       is not supported by this map
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException            if the class of a specified key or value
     *                                       prevents it from being stored in this map
     * @throws NullPointerException          if a specified key or newValue is null,
     *                                       and this map does not permit null keys or values
     * @throws NullPointerException          if oldValue is null and this map does not
     *                                       permit null values
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException      if some property of a specified key
     *                                       or value prevents it from being stored in this map
     * @implSpec The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * if (map.containsKey(key) && Objects.equals(map.get(key), oldValue)) {
     *     map.put(key, newValue);
     *     return true;
     * } else
     *     return false;
     * }</pre>
     * <p>
     * The default implementation does not throw NullPointerException
     * for maps that do not support null values if oldValue is null unless
     * newValue is also null.
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    default boolean replace(K key, V oldValue, V newValue) {
        Object curValue = get(key);
        if (!Objects.equals(curValue, oldValue) || (curValue == null && !containsKey(key))) {
            return false;
        }
        put(key, newValue);
        return true;
    }


    /**
     * Replaces the entry for the specified key only if it is
     * currently mapped to some value.
     *
     * @param key   key with which the specified value is associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with the specified key, or
     * {@code null} if there was no mapping for the key.
     * (A {@code null} return can also indicate that the map
     * previously associated {@code null} with the key,
     * if the implementation supports null values.)
     * @throws UnsupportedOperationException if the {@code put} operation
     *                                       is not supported by this map
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException            if the class of the specified key or value
     *                                       prevents it from being stored in this map
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified key or value is null,
     *                                       and this map does not permit null keys or values
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     * @implSpec The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * if (map.containsKey(key)) {
     *     return map.put(key, value);
     * } else
     *     return null;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    default V replace(K key, V value) {
        V curValue;
        if (((curValue = get(key)) != null) || containsKey(key)) {
            curValue = put(key, value);
        }
        return curValue;
    }


}
