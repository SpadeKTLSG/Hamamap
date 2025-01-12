package org.spc.impl;

import org.jetbrains.annotations.NotNull;
import org.spc.api.IHamaEntryEx;
import org.spc.api.IHamamapEx;

import java.util.*;

/**
 * Hamamap Abstract Class
 * <p>
 * Hamamap 抽象类
 *
 * @author SpadeKTLSG 玄桃K
 */
public abstract class AbstractHamamap<K, V> implements IHamamapEx<K, V> {

    //Fields 域

    //Transient Views 瞬时视图
    transient Set<K> keySet;
    transient Collection<V> values;


    protected AbstractHamamap() {
    }

    @Override
    public abstract Set<IHamaEntryEx<K, V>> entrySet();


    //? Hamamap...需要实现的方法: 位于此处

    /**
     * Add -> see Hamamap
     * <p>
     * 添加 -> 见Hamamap具体实现
     */
    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove -> see Hamamap
     * <p>
     * 移除 -> 见Hamamap具体实现
     */
    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Clear -> see Hamamap
     * <p>
     * 清空 -> 见Hamamap具体实现
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }


    //! IHamamap接口抽象实现: 位于此处


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
        Objects.requireNonNull(value); //默认: value不可为null

        for (IHamaEntryEx<K, V> e : entrySet()) {
            if (value.equals(e.getValue()))
                return true;
        }
        return false;
    }

    @Override
    public V get(Object key) {
        Objects.requireNonNull(key); //key不可为null

        for (IHamaEntryEx<K, V> e : entrySet()) {
            if (key.equals(e.getKey()))
                return e.getValue();
        }
        return null;
    }


    @Override
    public void putAll(IHamamapEx<? extends K, ? extends V> m) {
        for (IHamaEntryEx<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }


    @Override
    public Set<K> keySet() {
        Set<K> ks = keySet;
        if (ks != null) return ks;

        ks = new AbstractSet<>() {
            public @NotNull Iterator<K> iterator() {
                return new Iterator<>() {
                    private final Iterator<IHamaEntryEx<K, V>> i = entrySet().iterator();

                    public boolean hasNext() {
                        return i.hasNext();
                    }

                    public K next() {
                        return i.next().getKey();
                    }

                    public void remove() {
                        i.remove();
                    }
                };
            }

            public int size() {
                return AbstractHamamap.this.size();
            }

            public boolean isEmpty() {
                return AbstractHamamap.this.isEmpty();
            }

            public boolean contains(Object k) {
                return AbstractHamamap.this.containsKey(k);
            }
        };

        keySet = ks;
        return ks;
    }

    @Override
    public Collection<V> values() {
        Collection<V> vals = values;
        if (vals != null) return vals;


        vals = new AbstractCollection<V>() {
            public @NotNull Iterator<V> iterator() {
                return new Iterator<>() {
                    private final Iterator<IHamaEntryEx<K, V>> i = entrySet().iterator();

                    public boolean hasNext() {
                        return i.hasNext();
                    }

                    public V next() {
                        return i.next().getValue();
                    }

                    public void remove() {
                        i.remove();
                    }
                };
            }

            public int size() {
                return AbstractHamamap.this.size();
            }

            public boolean isEmpty() {
                return AbstractHamamap.this.isEmpty();
            }

            public boolean contains(Object v) {
                return AbstractHamamap.this.containsValue(v);
            }
        };

        values = vals;
        return vals;
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


    //! 基础方法: 位于此处

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractHamamap<?, ?> that;
        try {
            that = (AbstractHamamap<?, ?>) o;
            if (that.size() != size()) return false;
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }

        return Objects.equals(keySet, that.keySet) && Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keySet, values);
    }

    @Override
    public String toString() {
        Iterator<IHamaEntryEx<K, V>> i = entrySet().iterator();
        if (!i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (; ; ) {
            IHamaEntryEx<K, V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            sb.append(key == this ? "(this Map)" : key);
            sb.append('=');
            sb.append(value == this ? "(this Map)" : value);
            if (!i.hasNext())
                return sb.append('}').toString();
            sb.append(',').append(' ');
        }
    }


}
