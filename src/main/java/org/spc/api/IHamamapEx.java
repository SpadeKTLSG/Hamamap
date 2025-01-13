package org.spc.api;


import org.spc.impl.KVHolder;
import org.spc.tool.Collect;

import java.util.Objects;

/**
 * IHamamap 自带方法扩展接口
 */
public interface IHamamapEx<K, V> extends IHamamap<K, V> {


    /**
     * Returns an unmodifiable entry containing the KV
     * <p>
     * 返回一个不可修改的包含KV的entry (Actually not)
     */
    static <K, V> IHamaEntryEx<K, V> entry(K k, V v) {
        return new KVHolder<>(k, v);
    }

    /**
     * Returns an unmodifiable map containing no KV
     * <p>
     * 返回一个不可修改的没有KV的map (Actually not)
     */
    @SuppressWarnings("unchecked")
    static <K, V> IHamamapEx<K, V> of() {
        return (IHamamapEx<K, V>) Collect.EMPTY_MAP;
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
     * If the K not linked to V, link it with V and return null, else returns the current V
     * <p>
     * 若K没有关联V, 则关联他们并返回null, 否则返回当前V
     */
    default V putIfAbsent(K key, V value) {
        V v;
        if ((v = get(key)) == null) {
            put(key, value);
            return null;
        }
        return v;
    }


    /**
     * Removes the entry for K only if it's mapped to the V, returns T if removed but F if not
     * <p>
     * 只有K映射到V时才移除K, 返回T移除, F未移除
     */
    default boolean removeIfAbsent(K key, V value) {
        V curValue = get(key);
        if (!Objects.equals(curValue, value) || (curValue == null && !containsKey(key))) {
            return false;
        }
        remove(key);
        return true;
    }


}
