package org.spc.impl;

import org.spc.api.IHamaEntryEx;

import java.util.Objects;

/**
 * HamaNode
 * <p>
 * Hamamap的简单节点
 */
public class HamaNode<K, V> implements IHamaEntryEx<K, V> {

    final int hash;
    final K key;
    V value;
    HamaNode<K, V> next;

    public HamaNode(int hash, K key, V value, HamaNode<K, V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public final K getKey() {
        return key;
    }

    public final V getValue() {
        return value;
    }

    public final String toString() {
        return key + "=" + value;
    }

    public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    public final V setValue(V newValue) {
        V oldValue = value;
        value = newValue;
        return oldValue;
    }

    public final boolean equals(Object o) {
        if (o == this)
            return true;

        return o instanceof IHamaEntryEx<?, ?> e
                && Objects.equals(key, e.getKey())
                && Objects.equals(value, e.getValue());
    }
}
