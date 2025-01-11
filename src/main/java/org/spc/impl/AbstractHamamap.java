package org.spc.impl;

import org.spc.api.IHamaEntryEx;
import org.spc.api.IHamamapEx;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Hamamap Abstract Class
 * <p>
 * Hamamap 抽象类
 *
 * @author SpadeKTLSG 玄桃K
 */
public abstract class AbstractHamamap<K, V> implements IHamamapEx {

    //* IHamamapEx接口抽象实现: 原生HashMap内容不做修改, 直接位于此处

    //todo

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

    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return IHamamapEx.super.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer action) {
        IHamamapEx.super.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction function) {
        IHamamapEx.super.replaceAll(function);
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        return IHamamapEx.super.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return IHamamapEx.super.remove(key, value);
    }

    @Override
    public boolean replace(Object key, Object oldValue, Object newValue) {
        return IHamamapEx.super.replace(key, oldValue, newValue);
    }

    @Override
    public Object replace(Object key, Object value) {
        return IHamamapEx.super.replace(key, value);
    }

    @Override
    public Object computeIfAbsent(Object key, Function mappingFunction) {
        return IHamamapEx.super.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public Object computeIfPresent(Object key, BiFunction remappingFunction) {
        return IHamamapEx.super.computeIfPresent(key, remappingFunction);
    }

    @Override
    public Object compute(Object key, BiFunction remappingFunction) {
        return IHamamapEx.super.compute(key, remappingFunction);
    }

    @Override
    public Object merge(Object key, Object value, BiFunction remappingFunction) {
        return IHamamapEx.super.merge(key, value, remappingFunction);
    }
}
