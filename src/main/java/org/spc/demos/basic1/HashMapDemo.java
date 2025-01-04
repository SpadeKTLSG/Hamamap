package org.spc.demos.basic1;


import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class HashMapDemo<K, V> implements Map<K, V> {
    final float loadFactor;
    transient Node<K, V>[] table;
    transient volatile Set<K> keySet;
    transient volatile Collection<V> values;
    transient Set<Map.Entry<K, V>> entrySet;
    transient int size;
    transient int modCount;
    int threshold;

    public HashMapDemo(int initialCapacity, float loadFactor) {
        this.loadFactor = loadFactor;
        this.threshold = 0;
    }

    public HashMapDemo() {
        this.loadFactor = HashMapConstants.DEFAULT_LOAD_FACTOR;
    }

    static final int hash(Object var0) {
        int var1;
        return var0 == null ? 0 : (var1 = var0.hashCode()) ^ var1 >>> 16;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object var1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean containsValue(Object var1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public V get(Object key) {
        Node<K, V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    final Node<K, V> getNode(int hash, Object key) {
        Node<K, V>[] tab;
        Node<K, V> first, e;
        int n;
        K k;
        if ((tab = table) != null && (n = tab.length) > 0 && (first = tab[(n - 1) & hash]) != null) {
            if (first.hash == hash && ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                do {
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
        Node<K, V>[] tab;
        Node<K, V> p;
        int n, i;
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }
        if ((p = tab[i = (n - 1) & hash]) == null) {
            tab[i] = new Node<>(hash, key, value, null);
        } else {
            for (int binCount = 0; ; ++binCount) {
                if (p.next == null) {
                    p.next = new Node<>(hash, key, value, null);
                    if (binCount >= HashMapConstants.TREEIFY_THRESHOLD - 1) {
                        // TODO: Convert to TreeNode
                    }
                    break;
                }
                p = p.next;
            }
        }
        ++modCount;
        if (++size > threshold) resize();
        return value;
    }

    @Override
    public V remove(Object var1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> var1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public Collection<V> values() {
        return values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return entrySet;
    }

    @SuppressWarnings("unchecked")
    final Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= HashMapConstants.MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < HashMapConstants.MAXIMUM_CAPACITY &&
                    oldCap >= HashMapConstants.DEFAULT_INITIAL_CAPACITY) {
                newThr = oldThr << 1;
            }
        } else if (oldThr > 0) {
            newCap = oldThr;
        } else {
            newCap = HashMapConstants.DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (HashMapConstants.DEFAULT_LOAD_FACTOR * HashMapConstants.DEFAULT_INITIAL_CAPACITY);
        }
        threshold = newThr;
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        table = newTab;

        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K, V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null) {
                        newTab[e.hash & (newCap - 1)] = e;
                    } else {
                        Node<K, V> loHead = null, loTail = null;
                        Node<K, V> hiHead = null, hiTail = null;
                        Node<K, V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            } else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return table;
    }
}
