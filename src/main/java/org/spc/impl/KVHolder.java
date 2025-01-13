package org.spc.impl;


import org.spc.api.IHamaEntry;
import org.spc.api.IHamaEntryEx;

import java.util.Objects;


/**
 * Immutable container for a KV
 * suitable when creating and populating IHamaMap instances
 * A value-based class
 * <p>
 * 一个KV的不可变容器
 * 适用于创建和填充IHamaMap实例
 * 基于值的类
 */
public final class KVHolder<K, V> implements IHamaEntryEx<K, V> {


    final K key;

    final V value;

    public KVHolder(K k, V v) {
        key = Objects.requireNonNull(k);
        value = Objects.requireNonNull(v);
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        throw new UnsupportedOperationException("not supported | 不支持");
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof IHamaEntry<?, ?> e && key.equals(e.getKey()) && value.equals(e.getValue());
    }


    @Override
    public int hashCode() {
        return key.hashCode() ^ value.hashCode();
    }


    @Override
    public String toString() {
        return key + "=" + value;
    }
}
