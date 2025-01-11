package org.spc.impl;

import net.jcip.annotations.Immutable;
import org.spc.api.IHamaEntryEx;

import java.io.Serializable;
import java.util.*;


/**
 * Empty Hamamap, lazy to modify the content of the mutable HashMap, directly located here
 * <p>
 * 空Hamamap, 偷懒把可变的HashMap的内容不做修改, 直接位于此处
 *
 * @author SpadeKTLSG 玄桃K
 * @see Collections.EmptyMap
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
    public Object get(Object key) {
        return null;
    }

    @Override
    public Object put(Object key, Object value) {
        return null;
    }

    @Override
    public Object remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set keySet() {
        return null;
    }

    @Override
    public Collection values() {
        return null;
    }

    @Override
    public Set<IHamaEntryEx> entrySet() {
        return null;
    }

}