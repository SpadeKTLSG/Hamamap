package org.spc.impl;

import org.spc.api.IHamaEntryEx;

/**
 * HamaNode
 * <p>
 * Hamamap的简单节点
 */
public class HamaNode<K, V> implements IHamaEntryEx<K, V> {


    public HamaNode() {
    }

    @Override
    public K getKey() {
        return null;
    }

    @Override
    public V getValue() {
        return null;
    }

    @Override
    public V setValue(V value) {
        return null;
    }
}
