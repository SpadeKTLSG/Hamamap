package org.spc.impl;

import org.spc.api.IHamaEntryEx;
import org.spc.api.IHamamap;
import org.spc.tool.Constants;
import org.spc.tool.Toolkit;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

import static org.spc.tool.Constants.DEFAULT_INITIAL_CAPACITY;

/**
 * Hamamap - hashmap refactored ,  from "Rhapsody of Corner Trash Cans"
 * <p>
 * Hamamap - hashmap 重构, 源自 "拐角垃圾桶狂想曲"
 *
 * @author SpadeKTLSG 玄桃K
 */
public class Hamamap<K, V> extends AbstractHamamap<K, V> implements IHamamap<K, V>, Cloneable, Serializable {


    //! Specials 特殊

    // Serial 序列化
    @Serial
    private static final long serialVersionUID = 1145141919810L;


    //! Fields 域
    /**
     * The load factor for the hash table
     * <p>
     * 哈希表的负载因子
     */
    final float loadFactor;
    /**
     * Hash Buckets
     * <p>
     * 哈希桶数组
     */
    transient HamaNode<K, V>[] table;
    /**
     * Bucket Identifier
     * <p>
     * "垃圾桶"
     */
    transient int[] crushTable;


    //* Params 参数
    /**
     * Holds cached entrySet()
     * <p>
     * 存储缓存的 KV Set
     */
    transient Set<IHamaEntryEx<K, V>> entrySet;
    /**
     * The number of KVs
     * <p>
     * 键值对的数量
     */
    transient int size;

    /**
     * The next size value at which to resize (capacity * load factor)
     * <p>
     * 下一次调整大小的大小值（容量*负载因子）
     */
    int threshold;


    //! Builders 构建器

    /**
     * Constructs an empty {@code HashMap} with the specified initial
     * capacity and load factor.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is negative
     *                                  or the load factor is nonpositive
     */
    public Hamamap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        if (initialCapacity > Constants.MAXIMUM_CAPACITY)
            initialCapacity = Constants.MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = Toolkit.tableSizeFor(initialCapacity);
    }

    /**
     * Constructs an empty {@code HashMap} with the specified initial
     * capacity and the default load factor (0.75).
     *
     * @param initialCapacity the initial capacity.
     * @throws IllegalArgumentException if the initial capacity is negative.
     */
    public Hamamap(int initialCapacity) {
        this(initialCapacity, Constants.DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs an empty {@code HashMap} with the default initial capacity
     * (16) and the default load factor (0.75).
     */
    public Hamamap() {
        this.loadFactor = Constants.DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }

    /**
     * Constructs a new {@code HashMap} with the same mappings as the
     * specified {@code Map}.  The {@code HashMap} is created with
     * default load factor (0.75) and an initial capacity sufficient to
     * hold the mappings in the specified {@code Map}.
     *
     * @param m the map whose mappings are to be placed in this map
     * @throws NullPointerException if the specified map is null
     */
    public Hamamap(Map<? extends K, ? extends V> m) {
        this.loadFactor = Constants.DEFAULT_LOAD_FACTOR;
        putMapEntries(m, false);
    }


    //! Main Functions 主要功能

    @Override
    public Set<IHamaEntryEx<K, V>> entrySet() {
        Set<IHamaEntryEx<K, V>> es;
        return (es = entrySet) == null ? (entrySet = new EntrySet()) : es;

    }

    /**
     * Add Reborn
     * <p>
     * 添加 -> 见Hamamap具体实现
     */
    @Override
    public V put(K key, V value) {
        return putVal(Toolkit.hash(key), key, value, false, true);
    }


    /**
     * Remove Reborn
     * <p>
     * 移除 -> 见Hamamap具体实现
     */
    @Override
    public V remove(Object key) {
        HamaNode<K, V> e;
        return (e = removeNode(Toolkit.hash(key), key, null, false, true)) == null ?
                null : e.value;
    }

    /**
     * Clear Reborn
     * <p>
     * 清空 -> 见Hamamap具体实现
     */
    @Override
    public void clear() {
        HamaNode<K, V>[] tab;
        if ((tab = table) != null && size > 0) {
            size = 0;
            for (int i = 0; i < tab.length; ++i)
                tab[i] = null;
        }
    }


    //! Functions 功能

    /**
     * Implements Map.get and related methods.
     *
     * @param key the key
     * @return the node, or null if none
     */
    final HamaNode<K, V> getNode(Object key) {
        HamaNode<K, V>[] tab;
        HamaNode<K, V> first, e;
        int n, hash;
        K k;

        if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & (hash = Toolkit.hash(key))]) != null) {
            if (first.hash == hash && // always check first node
                    ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                if (first instanceof HamaTreeNode<K, V>)
                    return ((HamaTreeNode<K, V>) first).getTreeNode(hash, key);
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        HamaNode<K, V>[] tab;
        HamaNode<K, V> p;
        int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            HamaNode<K, V> e;
            K k;
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof HamaTreeNode)
                e = ((HamaTreeNode<K, V>) p).putTreeVal(this, tab, hash, key, value);
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= Constants.TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }

    /**
     * Implements Map.putAll and Map constructor.
     *
     * @param m     the map
     * @param evict false when initially constructing this map, else
     *              true (relayed to method afterNodeInsertion).
     */
    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
        int s = m.size();
        if (s > 0) {
            if (table == null) { // pre-size
                float ft = ((float) s / loadFactor) + 1.0F;
                int t = ((ft < (float) MAXIMUM_CAPACITY) ?
                        (int) ft : MAXIMUM_CAPACITY);
                if (t > threshold)
                    threshold = tableSizeFor(t);
            } else {
                // Because of linked-list bucket constraints, we cannot
                // expand all at once, but can reduce total resize
                // effort by repeated doubling now vs later
                while (s > threshold && table.length < MAXIMUM_CAPACITY)
                    resize();
            }

            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                putVal(hash(key), key, value, false, evict);
            }
        }
    }


    final HamaNode<K, V>[] resize() {
        HamaNode<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= Constants.MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < Constants.MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        } else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (Constants.DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < Constants.MAXIMUM_CAPACITY && ft < (float) Constants.MAXIMUM_CAPACITY ?
                    (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes", "unchecked"})
        HamaNode<K, V>[] newTab = (HamaNode<K, V>[]) new HamaNode[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                HamaNode<K, V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof HamaTreeNode)
                        ((HamaTreeNode<K, V>) e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        HamaNode<K, V> loHead = null, loTail = null;
                        HamaNode<K, V> hiHead = null, hiTail = null;
                        HamaNode<K, V> next;
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
        return newTab;
    }


    final HamaNode<K, V> removeNode(int hash, Object key, Object value,
                                    boolean matchValue, boolean movable) {
        HamaNode<K, V>[] tab;
        HamaNode<K, V> p;
        int n, index;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (p = tab[index = (n - 1) & hash]) != null) {
            HamaNode<K, V> node = null, e;
            K k;
            V v;
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;
            else if ((e = p.next) != null) {
                if (p instanceof HamaTreeNode)
                    node = ((HamaTreeNode<K, V>) p).getTreeNode(hash, key);
                else {
                    do {
                        if (e.hash == hash &&
                                ((k = e.key) == key ||
                                        (key != null && key.equals(k)))) {
                            node = e;
                            break;
                        }
                        p = e;
                    } while ((e = e.next) != null);
                }
            }
            if (node != null && (!matchValue || (v = node.value) == value ||
                    (value != null && value.equals(v)))) {
                if (node instanceof HamaTreeNode)
                    ((HamaTreeNode<K, V>) node).removeTreeNode(this, tab, movable);
                else if (node == p)
                    tab[index] = node.next;
                else
                    p.next = node.next;
                --size;
                afterNodeRemoval(node);
                return node;
            }
        }
        return null;
    }


    //! Side Functions 辅助功能

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public V get(Object key) {
        HamaNode<K, V> e;
        return (e = getNode(key)) == null ? null : e.value;
    }


    @Override
    public boolean containsKey(Object key) {
        return getNode(key) != null;
    }

    public boolean containsValue(Object value) {
        HamaNode<K, V>[] tab;
        V v;
        if ((tab = table) != null && size > 0) {
            for (HamaNode<K, V> e : tab) {
                for (; e != null; e = e.next) {
                    if ((v = e.value) == value ||
                            (value != null && value.equals(v)))
                        return true;
                }
            }
        }
        return false;
    }


    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own {@code remove} operation), the results of
     * the iteration are undefined.  The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * {@code Iterator.remove}, {@code Set.remove},
     * {@code removeAll}, {@code retainAll}, and {@code clear}
     * operations.  It does not support the {@code add} or {@code addAll}
     * operations.
     *
     * @return a set view of the keys contained in this map
     */
    public Set<K> keySet() {
        Set<K> ks = keySet;
        if (ks == null) {
            ks = new KeySet();
            keySet = ks;
        }
        return ks;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        HamaNode<K, V> e;
        return (e = getNode(key)) == null ? defaultValue : e.value;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return putVal(Toolkit.hash(key), key, value, true, true);
    }


    @Override
    public Object clone() {
        Hamamap<K, V> result;
        try {
            result = (Hamamap<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
        result.reinitialize();
        result.putMapEntries(this, false);
        return result;
    }

    void reinitialize() {
        table = null;
        entrySet = null;
        keySet = null;
        values = null;
        threshold = 0;
        size = 0;
    }


    // These methods are also used when serializing HashSets
    final float loadFactor() {
        return loadFactor;
    }

    final int capacity() {
        return (table != null) ? table.length :
                (threshold > 0) ? threshold :
                        Constants.DEFAULT_INITIAL_CAPACITY;
    }


    // Create a regular (non-tree) node
    HamaNode<K, V> newNode(int hash, K key, V value, HamaNode<K, V> next) {
        return new HamaNode<>(hash, key, value, next);
    }

    // For conversion from TreeNodes to plain nodes
    HamaNode<K, V> replacementNode(HamaNode<K, V> p, HamaNode<K, V> next) {
        return new HamaNode<>(p.hash, p.key, p.value, next);
    }

    // Create a tree bin node
    HamaTreeNode<K, V> newTreeNode(int hash, K key, V value, HamaNode<K, V> next) {
        return new HamaTreeNode<>(hash, key, value, next);
    }

    // For treeifyBin
    HamaTreeNode<K, V> replacementTreeNode(HamaNode<K, V> p, HamaNode<K, V> next) {
        return new HamaTreeNode<>(p.hash, p.key, p.value, next);
    }

    //! 树相关功能

    final void treeifyBin(HamaNode<K, V>[] tab, int hash) {
        int n, index;
        HamaNode<K, V> e;
        if (tab == null || (n = tab.length) < Constants.MIN_TREEIFY_CAPACITY)
            resize();
        else if ((e = tab[index = (n - 1) & hash]) != null) {
            HamaTreeNode<K, V> hd = null, tl = null;
            do {
                HamaTreeNode<K, V> p = replacementTreeNode(e, null);
                if (tl == null)
                    hd = p;
                else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            } while ((e = e.next) != null);
            if ((tab[index] = hd) != null)
                hd.treeify(tab);
        }
    }


    //! 内部类

    final class KeySet extends AbstractSet<K> {
        public int size() {
            return size;
        }

        public void clear() {
            HashMap.this.clear();
        }

        public Iterator<K> iterator() {
            return new HashMap.KeyIterator();
        }

        public boolean contains(Object o) {
            return containsKey(o);
        }

        public boolean remove(Object key) {
            return removeNode(hash(key), key, null, false, true) != null;
        }

        public Spliterator<K> spliterator() {
            return new HashMap.KeySpliterator<>(HashMap.this, 0, -1, 0, 0);
        }

        public Object[] toArray() {
            return keysToArray(new Object[size]);
        }

        public <T> T[] toArray(T[] a) {
            return keysToArray(prepareArray(a));
        }

        public void forEach(Consumer<? super K> action) {
            HamaNode<K, V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (HamaNode<K, V> e : tab) {
                    for (; e != null; e = e.next)
                        action.accept(e.key);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }

    final class EntrySet extends AbstractSet<IHamaEntryEx<K, V>> {
        public int size() {
            return size;
        }

        public void clear() {
            HashMap.this.clear();
        }

        public Iterator<Map.Entry<K, V>> iterator() {
            return new HashMap.EntryIterator();
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry<?, ?> e))
                return false;
            Object key = e.getKey();
            HashMap.Node<K, V> candidate = getNode(key);
            return candidate != null && candidate.equals(e);
        }

        public boolean remove(Object o) {
            if (o instanceof Map.Entry<?, ?> e) {
                Object key = e.getKey();
                Object value = e.getValue();
                return removeNode(hash(key), key, value, true, true) != null;
            }
            return false;
        }

        public Spliterator<Map.Entry<K, V>> spliterator() {
            return new HashMap.EntrySpliterator<>(HashMap.this, 0, -1, 0, 0);
        }

        public void forEach(Consumer<? super Map.Entry<K, V>> action) {
            HashMap.Node<K, V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (HashMap.Node<K, V> e : tab) {
                    for (; e != null; e = e.next)
                        action.accept(e);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }


}
