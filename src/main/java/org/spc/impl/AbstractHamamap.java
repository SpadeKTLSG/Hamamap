package org.spc.impl;

import org.spc.api.IHamaEntryEx;
import org.spc.api.IHamamapEx;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Hamamap Abstract Class
 * <p>
 * Hamamap 抽象类
 *
 * @author SpadeKTLSG 玄桃K
 */
public abstract class AbstractHamamap<K, V> implements IHamamapEx<K, V> {

    //域

    protected AbstractHamamap() {
    }


    //* IHamamapEx接口抽象实现: 原生HashMap内容不做修改, 直接位于此处

    //! IHamamap接口抽象实现: 位于此处

    //! 1 Query Operations 查询操作

    @Override
    public int size() {
        return entrySet().size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        Objects.requireNonNull(key); //key不可为null

        for (IHamaEntryEx<K, V> e : entrySet()) {
            if (key.equals(e.getKey()))
                return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<IHamaEntryEx<K, V>> entrySet() {
        return null; //延迟到子类实现
    }

    //! IHamamapEx接口扩展实现: 位于此处

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return IHamamapEx.super.getOrDefault(key, defaultValue);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return IHamamapEx.super.putIfAbsent(key, value);
    }

    @Override
    public boolean removeIfAbsent(K key, V value) {
        return IHamamapEx.super.removeIfAbsent(key, value);
    }
}
