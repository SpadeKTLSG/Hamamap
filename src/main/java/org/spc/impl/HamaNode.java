package org.spc.impl;

import lombok.Getter;
import lombok.Setter;
import org.spc.api.IHamaEntryEx;

import java.util.Objects;

/**
 * HamaNode
 * <p>
 * Hamamap的简单节点
 */
public class HamaNode<K, V> implements IHamaEntryEx<K, V> {

    /**
     * The hash
     */
    final int hash;
    /**
     * The key
     */
    final K key;
    /**
     * The hash helper, used to bypass hash collision
     * <p>
     * 哈希助手，用于绕过哈希冲突
     */
    @Getter
    @Setter
    public int hashHelper;
    /**
     * The value
     */
    V value;
    /**
     * The next
     */
    HamaNode<K, V> next;

    public HamaNode(int hash, K key, V value, HamaNode<K, V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
        this.hashHelper = 1;
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
        //增加hashHelper的hashCode
        return Objects.hashCode(key) ^ Objects.hashCode(value) ^ Objects.hashCode(hashHelper);
    }

    public final V setValue(V newValue) {
        V oldValue = value;
        value = newValue;
        return oldValue;
    }

    public final boolean equals(Object o) {
        if (o == this) return true;

        return o instanceof IHamaEntryEx<?, ?> e && Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
    }

    /**
     * 调试用
     */
    public final int getHash() {
        return hash;
    }

    /**
     * 调试用
     */
    public final HamaNode<K, V> getNext() {
        return next;
    }

}
