package org.spc.api;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * IHamaEntry 自带方法扩展接口
 */
public interface IHamaEntryEx<K, V> extends IHamaEntry<K, V> {


    //! 1 Compare Operations 比较操作

    /**
     * Returns a comparator that compares entry K in natural order on K
     * <p>
     * 返回一个比较器, 用于比较K的Entry
     */
    static <K extends Comparable<? super K>, V> Comparator<IHamaEntryEx<K, V>> comparingByKey() {
        return (Comparator<IHamaEntryEx<K, V>> & Serializable)
                (c1, c2) -> c1.getKey().compareTo(c2.getKey());
    }


    /**
     * Returns a comparator that compares entry V in natural order on V
     * <p>
     * 返回一个比较器, 用于比较V的Entry
     */
    static <K, V extends Comparable<? super V>> Comparator<IHamaEntryEx<K, V>> comparingByValue() {
        return (Comparator<IHamaEntryEx<K, V>> & Serializable)
                (c1, c2) -> c1.getValue().compareTo(c2.getValue());
    }


    /**
     * Returns a comparator that compares entry K by given Comparator
     * <p>
     * 返回一个比较器, 用于比较Entry
     */
    static <K, V> Comparator<IHamaEntryEx<K, V>> comparingByKey(Comparator<? super K> cmp) {
        Objects.requireNonNull(cmp);
        return (Comparator<IHamaEntryEx<K, V>> & Serializable)
                (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
    }


    /**
     * Returns a comparator that compares entry V by given Comparator
     * <p>
     * 返回一个比较器, 用于比较Entry
     */
    static <K, V> Comparator<IHamaEntryEx<K, V>> comparingByValue(Comparator<? super V> cmp) {
        Objects.requireNonNull(cmp);
        return (Comparator<IHamaEntryEx<K, V>> & Serializable)
                (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
    }


    /**
     * Returns a copy which is not associated with any map
     * <p>
     * 返回一个不关联任何map的副本
     */
    static <K, V> IHamaEntryEx<K, V> copyOf(IHamaEntryEx<? extends K, ? extends V> e) {
        Objects.requireNonNull(e);
        return IHamamapEx.entry(e.getKey(), e.getValue());
    }


}
