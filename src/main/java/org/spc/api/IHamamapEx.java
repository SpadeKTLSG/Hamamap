package org.spc.api;


import org.spc.impl.Hamamap;
import org.spc.impl.KVHolder;
import org.spc.tool.collect;

import java.util.Map;
import java.util.Objects;

/**
 * IHamamap 自带方法扩展接口
 */
public interface IHamamapEx<K, V> extends IHamamap<K, V> {


    /**
     * Returns an unmodifiable map containing no KV
     */
    @SuppressWarnings("unchecked")
    static <K, V> IHamamapEx<K, V> of() {
        return (IHamamapEx<K, V>) collect.EMPTY_MAP;
    }


    /**
     * Returns an unmodifiable map containing keys and values extracted from the given entries.
     * The entries themselves are not stored in the map.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K>     the {@code Map}'s key type
     * @param <V>     the {@code Map}'s value type
     * @param entries {@code Map.Entry}s containing the keys and values from which the map is populated
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if there are any duplicate keys
     * @throws NullPointerException     if any entry, key, or value is {@code null}, or if
     *                                  the {@code entries} array is {@code null}
     * @apiNote It is convenient to create the map entries using the {@link Map#entry Map.entry()} method.
     * For example,
     *
     * <pre>{@code
     *     import static java.util.Map.entry;
     *
     *     Map<Integer,String> map = Map.ofEntries(
     *         entry(1, "a"),
     *         entry(2, "b"),
     *         entry(3, "c"),
     *         ...
     *         entry(26, "z"));
     * }</pre>
     * @see Map#entry Map.entry()
     * @since 9
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    static <K, V> IHamamapEx<K, V> ofEntries(IHamaEntryEx<? extends K, ? extends V>... entries) {

        if (entries.length == 0) {
            return (IHamamapEx<K, V>) collect.EMPTY_MAP;
        } else {
            Object[] kva = new Object[entries.length << 1];
            int a = 0;
            for (IHamaEntryEx<? extends K, ? extends V> entry : entries) {
                kva[a++] = entry.getKey();
                kva[a++] = entry.getValue();
            }
            //todo : 超级赋值
            return new Hamamap<>(kva);
        }
    }


    /**
     * Returns an unmodifiable {@link Map.Entry} containing the given key and value.
     * These entries are suitable for populating {@code Map} instances using the
     * {@link Map#ofEntries Map.ofEntries()} method.
     * The {@code Entry} instances created by this method have the following characteristics:
     *
     * <ul>
     * <li>They disallow {@code null} keys and values. Attempts to create them using a {@code null}
     * key or value result in {@code NullPointerException}.
     * <li>They are unmodifiable. Calls to {@link Map.Entry#setValue Entry.setValue()}
     * on a returned {@code Entry} result in {@code UnsupportedOperationException}.
     * <li>They are not serializable.
     * <li>They are <a href="../lang/doc-files/ValueBased.html">value-based</a>.
     * Programmers should treat instances that are {@linkplain #equals(Object) equal}
     * as interchangeable and should not use them for synchronization, or
     * unpredictable behavior may occur. For example, in a future release,
     * synchronization may fail. Callers should make no assumptions
     * about the identity of the returned instances. This method is free to
     * create new instances or reuse existing ones.
     * </ul>
     */
    static <K, V> IHamaEntryEx<K, V> entry(K k, V v) {
        return new KVHolder<>(k, v);
    }


    /**
     * Returns an <a href="#unmodifiable">unmodifiable Map</a> containing the entries
     * of the given Map. The given Map must not be null, and it must not contain any
     * null keys or values. If the given Map is subsequently modified, the returned
     * Map will not reflect such modifications.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param map a {@code Map} from which entries are drawn, must be non-null
     * @return a {@code Map} containing the entries of the given {@code Map}
     * @throws NullPointerException if map is null, or if it contains any null keys or values
     * @implNote If the given Map is an <a href="#unmodifiable">unmodifiable Map</a>,
     * calling copyOf will generally not create a copy.
     * @since 10
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    static <K, V> IHamamapEx<K, V> copyOf(IHamamapEx<? extends K, ? extends V> map) {
        return IHamamapEx.ofEntries(map.entrySet().toArray(new IHamaEntryEx[0]));
    }


    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @implSpec The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    default V getOrDefault(Object key, V defaultValue) {
        V v;
        return (((v = get(key)) != null) || containsKey(key)) ? v : defaultValue;
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
