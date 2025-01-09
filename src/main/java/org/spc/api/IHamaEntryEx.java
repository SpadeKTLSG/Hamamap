package org.spc.api;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

/**
 * IHamaEntry 自带方法扩展接口
 */
public interface IHamaEntryEx<K, V> extends IHamaEntry<K, V> {


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
        return Map.entry(e.getKey(), e.getValue());
    }


}
