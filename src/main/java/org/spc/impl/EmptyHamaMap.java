package org.spc.impl;

import net.jcip.annotations.Immutable;
import org.spc.api.IHamaEntryEx;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;


/**
 * Empty Hamamap, lazy to modify the content of the mutable HashMap, directly located here
 * <p>
 * 空Hamamap, 偷懒把可变的HashMap的内容不做修改, 直接位于此处
 *
 * @author SpadeKTLSG 玄桃K
 */
@Immutable
public class EmptyHamaMap<K, V> extends AbstractHamamap<K, V> implements Serializable {

    private static final long serialVersionUID = 1L;

    K key;
    V value;

    //todo
    public EmptyHamaMap() {
    }

    public EmptyHamaMap(K k, V v) {
        this.key = Objects.requireNonNull(k);
        this.value = Objects.requireNonNull(v);
    }


    //todo

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
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
    public V put(Object key, Object value) {
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
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
        return null;
    }

}
