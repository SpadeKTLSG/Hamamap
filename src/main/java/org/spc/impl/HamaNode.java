package org.spc.impl;

import lombok.Getter;
import org.spc.api.IHamaEntryEx;
import org.spc.wrapper.Wrapper;

import java.util.Objects;

/**
 * HamaNode
 * <p>
 * Hamamap的简单节点
 *
 * @note Hash计算被节点包装代理
 */
public class HamaNode<K, V> implements IHamaEntryEx<K, V> {

    /**
     * The key
     */
    final K key;
    /**
     * get the wrapper link
     * <p>
     * 获取包装器 link
     */
    @Getter
    Wrapper<K, V> wrapper;
    /**
     * The value
     */
    V value;
    /**
     * The next
     */
    HamaNode<K, V> next;

    public HamaNode(K key, V value, HamaNode<K, V> next) {
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
        if (o == this) return true;

        return o instanceof IHamaEntryEx<?, ?> e && Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
    }

    /**
     * set the wrapper link
     * <p>
     * 设置包装器 link
     */
    public void setWrapper(Wrapper<K, V> wrapper) {
        this.wrapper = wrapper;
    }

}
