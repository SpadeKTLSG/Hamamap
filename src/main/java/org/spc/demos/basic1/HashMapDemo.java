package org.spc.demos.basic1;

import javax.swing.tree.TreeNode;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 手撕HashMap
 *
 * @param <K>
 * @param <V>
 * @author azheng
 */
public class HashMapDemo<K, V> implements Map<K, V> {

    /**
     * 使用TreeNode的临界值
     */
    static final int TREEIFY_THRESHOLD = 8;

    /**
     * 默认的负载因子大小
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 初始化数组长度
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    /**
     * 数组最大容量
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;
    /**
     * 负载因子，默认为0.75
     */
    final float loadFactor;
    transient Node<K, V>[] table;
    transient volatile Set<K> keySet;
    transient volatile Collection<V> values;
    transient Set<Map.Entry<K, V>> entrySet;
    /**
     * 包含的键值映射数
     */
    transient int size;
    /**
     * 记录在结构上被修改的次数
     */
    transient int modCount;
    /**
     * 数组调节临界值
     * 要调整数组大小的下一个数组长度临界值(数组容量 * 负载因子)
     * 默认初始化为0，插入一个数据之后值就是 12（16 * 0.75）
     * 当size达到12之后数组进行两倍扩容,此值也进行两倍扩容成24
     */
    int threshold;

    /**
     * @param initialCapacity 最大的entry数量， 默认为1 << 30;
     * @param loadFactor      负载因子
     */
    public HashMapDemo(int initialCapacity, float loadFactor) {
        this.loadFactor = loadFactor;
        this.threshold = 0;
    }

    public HashMapDemo() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
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

    /**
     * V firstEntry = table[(length-1) & hash];
     *
     * @param hash
     * @param key
     * @return
     */
    final Node<K, V> getNode(int hash, Object key) {
        Node<K, V>[] tab;
        Node<K, V> first, e;
        int n;
        K k;
        //保证有对应的值才做处理，杜绝空指针
        if ((tab = table) != null && (n = tab.length) > 0 && (first = tab[(n - 1) & hash]) != null) {
            if (first.hash == hash && // 先校验第一个节点
                    ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                if (first instanceof TreeNode) {
                    //返回红黑树节点TreeNode
                    //return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                }
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
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

    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K, V>[] tab;
        Node<K, V> p;
        int n, i;
        //如果数组为空或者数组长度为0,则数组进行初始化
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }
        //如果此索引下为空，直接赋值
        if ((p = tab[i = (n - 1) & hash]) == null) {
            tab[i] = new Node<>(hash, key, value, null);
            //如果此索引下不为空，则增加到链尾（不增加到链头，是为了统计链表长度）
        } else {
            for (int binCount = 0; ; ++binCount) {
                if (p.next == null) {
                    p.next = new Node<>(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1) {
                        //如果链表长度超过8就转化为红黑树TreeNode
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
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return entrySet;
    }

    @SuppressWarnings("unchecked")
    final Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) { //容量超过最大值时，临界值设置为最大容量值并返回
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            // 容量在正常范围且超过负载临界值时（在调用方法之前已经做判断），做两倍扩容
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY) {
                newThr = oldThr << 1; // 两倍threshold
            }
        } else if (oldThr > 0) {
            newCap = oldThr;
        } else {               // 容量为0时 全部初始化为默认值
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        threshold = newThr;
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        table = newTab;

        //扩容后数据重新添加
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

    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
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
            if (o == this)
                return true;
            if (o instanceof Map.Entry<?, ?> e) {
                return Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue());
            }
            return false;
        }
    }

}
