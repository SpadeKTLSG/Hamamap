package org.spc.impl;

import org.spc.api.IHamaEntryEx;
import org.spc.api.IHamamap;
import org.spc.tool.Constants;
import org.spc.tool.Toolkit;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        HashMap.Node<K, V> e;
        return (e = removeNode(hash(key), key, null, false, true)) == null ?
                null : e.value;
    }

    /**
     * Clear Reborn
     * <p>
     * 清空 -> 见Hamamap具体实现
     */
    @Override
    public void clear() {
        HashMap.Node<K, V>[] tab;
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
        HashMap.Node<K, V>[] tab;
        HashMap.Node<K, V> p;
        int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            HashMap.Node<K, V> e;
            K k;
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof HashMap.TreeNode)
                e = ((HashMap.TreeNode<K, V>) p).putTreeVal(this, tab, hash, key, value);
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
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
        ++modCount;
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }

    final HashMap.Node<K, V>[] resize() {
        HashMap.Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        } else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
                    (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes", "unchecked"})
        HashMap.Node<K, V>[] newTab = (HashMap.Node<K, V>[]) new HashMap.Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                HashMap.Node<K, V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof HashMap.TreeNode)
                        ((HashMap.TreeNode<K, V>) e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        HashMap.Node<K, V> loHead = null, loTail = null;
                        HashMap.Node<K, V> hiHead = null, hiTail = null;
                        HashMap.Node<K, V> next;
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


    final HashMap.Node<K, V> removeNode(int hash, Object key, Object value,
                                        boolean matchValue, boolean movable) {
        HashMap.Node<K, V>[] tab;
        HashMap.Node<K, V> p;
        int n, index;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (p = tab[index = (n - 1) & hash]) != null) {
            HashMap.Node<K, V> node = null, e;
            K k;
            V v;
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;
            else if ((e = p.next) != null) {
                if (p instanceof HashMap.TreeNode)
                    node = ((HashMap.TreeNode<K, V>) p).getTreeNode(hash, key);
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
                if (node instanceof HashMap.TreeNode)
                    ((HashMap.TreeNode<K, V>) node).removeTreeNode(this, tab, movable);
                else if (node == p)
                    tab[index] = node.next;
                else
                    p.next = node.next;
                ++modCount;
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
        HashMap.Node<K, V>[] tab;
        V v;
        if ((tab = table) != null && size > 0) {
            for (HashMap.Node<K, V> e : tab) {
                for (; e != null; e = e.next) {
                    if ((v = e.value) == value ||
                            (value != null && value.equals(v)))
                        return true;
                }
            }
        }
        return false;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        putMapEntries(m, true);
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
            ks = new HashMap.KeySet();
            keySet = ks;
        }
        return ks;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        HashMap.Node<K, V> e;
        return (e = getNode(key)) == null ? defaultValue : e.value;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return putVal(hash(key), key, value, true, true);
    }


    @Override
    public Object clone() {
        HashMap<K, V> result;
        try {
            result = (HashMap<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
        result.reinitialize();
        result.putMapEntries(this, false);
        return result;
    }

    // These methods are also used when serializing HashSets
    final float loadFactor() {
        return loadFactor;
    }

    final int capacity() {
        return (table != null) ? table.length :
                (threshold > 0) ? threshold :
                        DEFAULT_INITIAL_CAPACITY;
    }


    //! 树相关功能

    final void treeifyBin(HashMap.Node<K, V>[] tab, int hash) {
        int n, index;
        HashMap.Node<K, V> e;
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
            resize();
        else if ((e = tab[index = (n - 1) & hash]) != null) {
            HashMap.TreeNode<K, V> hd = null, tl = null;
            do {
                HashMap.TreeNode<K, V> p = replacementTreeNode(e, null);
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


    //TreeNode todo

    //! LHM

    /*
     * The following package-protected methods are designed to be
     * overridden by LinkedHashMap, but not by any other subclass.
     * Nearly all other internal methods are also package-protected
     * but are declared final, so can be used by LinkedHashMap, view
     * classes, and HashSet.
     */

    // Create a regular (non-tree) node
    HashMap.Node<K, V> newNode(int hash, K key, V value, HashMap.Node<K, V> next) {
        return new HashMap.Node<>(hash, key, value, next);
    }

    // For conversion from TreeNodes to plain nodes
    HashMap.Node<K, V> replacementNode(HashMap.Node<K, V> p, HashMap.Node<K, V> next) {
        return new HashMap.Node<>(p.hash, p.key, p.value, next);
    }

    // Create a tree bin node
    HashMap.TreeNode<K, V> newTreeNode(int hash, K key, V value, HashMap.Node<K, V> next) {
        return new HashMap.TreeNode<>(hash, key, value, next);
    }

    // For treeifyBin
    HashMap.TreeNode<K, V> replacementTreeNode(HashMap.Node<K, V> p, HashMap.Node<K, V> next) {
        return new HashMap.TreeNode<>(p.hash, p.key, p.value, next);
    }

    /**
     * Reset to initial default state.  Called by clone and readObject.
     */
    void reinitialize() {
        table = null;
        entrySet = null;
        keySet = null;
        values = null;
        modCount = 0;
        threshold = 0;
        size = 0;
    }

    // Callbacks to allow LinkedHashMap post-actions
    void afterNodeAccess(HashMap.Node<K, V> p) {
    }

    void afterNodeInsertion(boolean evict) {
    }

    void afterNodeRemoval(HashMap.Node<K, V> p) {
    }

    // Called only from writeObject, to ensure compatible ordering.
    void internalWriteEntries(java.io.ObjectOutputStream s) throws IOException {
        HashMap.Node<K, V>[] tab;
        if (size > 0 && (tab = table) != null) {
            for (HashMap.Node<K, V> e : tab) {
                for (; e != null; e = e.next) {
                    s.writeObject(e.key);
                    s.writeObject(e.value);
                }
            }
        }
    }


}
