package org.spc.impl;

import org.jetbrains.annotations.NotNull;
import org.spc.api.IHamaEntryEx;
import org.spc.api.IHamamap;
import org.spc.api.IHamamapEx;
import org.spc.tool.Constants;
import org.spc.tool.Toolkit;
import org.spc.tool.Wrapper;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

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
     * Default max insert retry
     * <p>
     * 默认初始插入重试次数
     */
    final int initialRetry;
    /**
     * Default max insert retry
     * <p>
     * 默认最大插入重试次数
     */
    final int maxRetry;
    /**
     * Default max trash (in a bucket)
     * <p>
     * 默认最大垃圾数量（在一个桶内）
     */
    final int maxTrash;
    /**
     * Hash Buckets
     * <p>
     * 哈希桶数组 - 包装器实现
     */
    transient Wrapper<K, V>[] table;
    /**
     * Bucket Identifier
     * <p>
     * "垃圾桶", 一一对应上方的哈希桶
     */
    transient int[] trashTable;

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

    public Hamamap(int initialCapacity, float loadFactor, int maxRetry, int initialRetry, int maxTrash) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity 非法初始容量: " + initialCapacity);
        }
        if (initialCapacity > Constants.MAXIMUM_CAPACITY) {
            initialCapacity = Constants.MAXIMUM_CAPACITY;
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: 非法负载因子 " + loadFactor);
        }
        if (maxRetry < 0) {
            throw new IllegalArgumentException("Illegal max retry: 非法最大重试次数 " + maxRetry);
        }
        if (initialRetry < 0) {
            throw new IllegalArgumentException("Illegal initial retry: 非法初始重试次数 " + initialRetry);
        }
        if (maxTrash < 0) {
            throw new IllegalArgumentException("Illegal max trash: 非法最大垃圾数量 " + maxTrash);
        }
        this.loadFactor = loadFactor;
        this.maxRetry = maxRetry;
        this.initialRetry = initialRetry;
        this.maxTrash = maxTrash;
        this.threshold = Toolkit.tableSizeFor(initialCapacity);

    }

    public Hamamap(int initialCapacity, float loadFactor, int maxRetry, int initialRetry) {
        this(initialCapacity, loadFactor, maxRetry, initialRetry, Constants.DEFAULT_MAX_TRASH);
    }

    public Hamamap(int initialCapacity, float loadFactor, int maxRetry) {
        this(initialCapacity, loadFactor, maxRetry, Constants.DEFAULT_MAX_TRASH);
    }

    public Hamamap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, Constants.DEFAULT_MAX_RETRY, Constants.DEFAULT_MAX_TRASH);
    }

    public Hamamap(int initialCapacity) {
        this(initialCapacity, Constants.DEFAULT_LOAD_FACTOR);
    }

    public Hamamap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Constructs a new Hamamap with the same mappings as the specified Hamamap
     * <p>
     * 使用指定的Hamamap构造一个新的Hamamap, 具有相同的映射
     */
    @SuppressWarnings("rawtypes")
    public Hamamap(IHamamapEx<? extends K, ? extends V> m) {

        Hamamap fake;
        try {
            fake = (Hamamap) m;
        } catch (Exception e) {
            throw new ClassCastException("Illegal Hamamap: 非法Hamamap");
        }
        this.loadFactor = fake.loadFactor;
        this.maxRetry = fake.maxRetry;
        this.initialRetry = fake.initialRetry;
        this.maxTrash = fake.maxTrash;

        putMapEntries(m);
    }

    /**
     * Constructs a new Hamamap with the same mappings as the specified HashMap or so
     * <p>
     * 使用指定的HashMap或其他映射构造一个新的Hamamap (兼容 - 未完成)
     */
/*    @SuppressWarnings("rawtypes")
    public Hamamap(Map<? extends K, ? extends V> m) {

        HashMap fake = (HashMap) m;
        this.loadFactor = Constants.DEFAULT_LOAD_FACTOR;
        this.maxRetry = Constants.DEFAULT_MAX_RETRY;
        this.initialRetry = Constants.DEFAULT_INITIAL_RETRY;
        this.maxTrash =Constants.DEFAULT_MAX_TRASH;
        putMapEntries(m);
    }*/
    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        Hamamap<K, V> result;
        try {
            result = (Hamamap<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        result.reinitialize();
        result.putMapEntries(this);
        return result;
    }

    /**
     * Reinitialize the map
     * <p>
     * 重新初始化映射
     */
    void reinitialize() {
        table = null;
        trashTable = null;
        entrySet = null;
        keySet = null;
        values = null;
        threshold = 0;
        size = 0;
    }


    final float getLoadFactor() {
        return loadFactor;
    }

    final int getInitialRetry() {
        return initialRetry;
    }

    final int getMaxRetry() {
        return maxRetry;
    }

    final int getMaxTrash() {
        return maxTrash;
    }

    final int getCapacity() {
        return (table != null) ? table.length : (threshold > 0) ? threshold : Constants.DEFAULT_INITIAL_CAPACITY;
    }


    //! Functions 核心功能


    /**
     * Add
     * <p>
     * 添加
     */
    @Override
    public V put(K key, V value) {
        //采用试探的方式, 通过hash值和默认的hash盐值进行和. 到时候可以还原并采用别的Hash来改变位置
        int testHash = Toolkit.hash(key) + Toolkit.hash(Constants.DEFAULT_HASH_HELPER_VALUE);
        return putVal(testHash, key, value);
    }


    /**
     * Remove
     * <p>
     * 移除
     */
    @Override
    public V remove(Object key) {
        Wrapper<K, V> e;
        return (e = removeNode(Toolkit.hash(key), key, null, false, true)) == null ? null : e.getNode().getValue();
    }

    /**
     * Clear
     * <p>
     * 清空
     */
    @Override
    public void clear() {
        Wrapper<K, V>[] tab;
        if ((tab = table) != null && size > 0) {
            size = 0;
            //需要对包装节点做处理
            for (Wrapper<K, V> kvWrapper : tab) {
                kvWrapper.setNode(null);
                kvWrapper.setHashHelper(Constants.DEFAULT_HASH_HELPER_VALUE);
            }
            int[] trash = trashTable;
            Arrays.fill(trash, 0);
        }
    }


    /**
     * get a node by key
     * <p>
     * 通过键获取一个节点
     *
     * @note 查询不做修改
     */
    final Wrapper<K, V> getNode(Object key) {
        Wrapper<K, V>[] tab;

        Wrapper<K, V> first;
        HamaNode<K, V> firstNode;

        Wrapper<K, V> e;
        HamaNode<K, V> eNode;
        int n, hash;
        K k;

        if ((tab = table) != null && (n = tab.length) > 0 && (first = tab[(n - 1) & (hash = Toolkit.hash(key))]) != null) {
            //? 包装器处理
            firstNode = first.getNode();
            // 总是检查第一个节点
            if (firstNode.hash == hash && ((k = firstNode.key) == key || (key != null && key.equals(k)))) {
                return first;
            }

            if ((eNode = firstNode.next) != null) {
                do {
                    if (eNode.hash == hash && ((k = eNode.key) == key || (key != null && key.equals(k)))) {
                        return eNode.getWrapper();
                    }
                } while ((eNode = eNode.next) != null);
            }
        }
        return null;
    }

    /**
     * Insert a node by KV
     * <p>
     * 插入一个KV - 主方法
     *
     * @note 插入时候要涉及到 "避让垃圾桶" 的逻辑
     */
    final V putVal(int hash, K key, V value) {
        Wrapper<K, V>[] tab;
        HamaNode<K, V>[] tabNode;

        Wrapper<K, V> p;
        HamaNode<K, V> pNode;

        Boolean success = Boolean.FALSE;
        int n, i;

        //如果哈希表为空, 或者长度为0, 就扩容
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }

        //! 如果这个位置没有节点(Wrapper) - 意味着没有垃圾直接短路, 就直接插入, 不需要处理垃圾桶逻辑
        if ((p = tab[i = (n - 1) & hash]) == null) {
            tab[i] = newNode(hash, key, value, null);

            if (++size > threshold) {
                resize();
            }
            return null;
        }

        //! 否则就要处理垃圾桶逻辑

        Wrapper<K, V> e;
        K k;

        //? 垃圾桶逻辑: 当插入并且当前位置的垃圾桶存在垃圾(int[now] > 0)时, 就要递归重新插入, 用turn代表插入次数
        // {Rehash}方案和{Wrapper}方案, 最终只能选择包装器方案修改节点的对象


        //改变hash实现修改插入位置:此时节点还未初始化, 因此需要手动提前处理hash盐, 初始为 1 + 8
        V tempV = putValRetry(hash, key, value, 0, Constants.DEFAULT_HASH_HELPER_VALUE, success);
        if (success) { //子方法插入成功
            return tempV;
        }


        if ((pNode = p.getNode()).hash == hash && ((k = pNode.key) == key || (key != null && key.equals(k)))) {
            e = p;
        } else {
            for (; ; ) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    break;
                }
                //如果找到了相同的键, 就直接跳出
                if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                    break;
                }
                p = e;
            }
        }
        if (e != null) { //如果对应位置的对象不为空即为 找到了相同的键, 就直接替换值
            V oldValue = e.value;
            e.value = value;
            return oldValue;
        }

        if (++size > threshold) {
            resize();
        }
        return null;
    }

    /**
     * 处理插入的递归调用方法
     *
     * @param turn 插入次数, 初始为0
     * @param salt 当前需要设置的
     */
    private V putValRetry(int hash, K key, V value, int turn, int salt, Boolean success) {
        //失败插入, 需要改变hash进行插入. 使用盐进行重新计算位置

        if (success == Boolean.TRUE) {

        }
        //否则, 判断能否再次插入: 初始重试和最大重试, 使用的Hash盐剧烈性不同
        else if (turn < initialRetry) {

        } else if (turn < maxRetry) {

        } else { //失败, 就在这里插入吧,
//                trashTable[thistable.sit] +=1;
        }


    }


    /**
     * Implements Map.putAll and Map constructor
     * <p>
     * 实现Map.putAll和Map构造函数
     */
    final void putMapEntries(IHamamap<? extends K, ? extends V> m) {
        int s = m.size();

        if (s <= 0) {
            return;
        }

        if (table == null) { // pre-size 如果自己的表为空, 就要预先设置大小
            float ft = ((float) s / loadFactor) + 1.0F;
            int t = ((ft < (float) Constants.MAXIMUM_CAPACITY) ? (int) ft : Constants.MAXIMUM_CAPACITY);
            if (t > threshold) {
                threshold = Toolkit.tableSizeFor(t);
            }
        } else {
            while (s > threshold && table.length < Constants.MAXIMUM_CAPACITY) {
                resize(); // Because of linked-list bucket constraints, we cannot expand all at once, but can reduce total resize effort by repeated doubling now vs later 由于链表桶约束,我们不能一次性扩展所有,但可以通过现在重复加倍而不是以后减少总调整大小的工作量
            }
        }

        //遍历m, 把m的键值对插入到自己的表中
        for (IHamaEntryEx<? extends K, ? extends V> e : m.entrySet()) {
            K key = e.getKey();
            V value = e.getValue();
            //这里同样通过hash值和默认的hash盐值进行和, 同样走试探的方法, 以便还原
            putVal(Toolkit.hash(key), key, value);
        }

    }


    /**
     * Rehash the table when the given capacity is reached
     * <p>
     * 当达到给定容量时重新哈希Hamamap table
     *
     * @note 扩容时, 按照逻辑讲, 应该暂时会变得宽敞起来, 因此我决定直接执行垃圾桶清空操作 (初始化长度)
     */
    final Wrapper<K, V>[] resize() {

        Wrapper<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;

        //如果旧容量大于0
        if (oldCap > 0) {
            if (oldCap >= Constants.MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < Constants.MAXIMUM_CAPACITY && oldCap >= Constants.DEFAULT_INITIAL_CAPACITY) {
                newThr = oldThr << 1; // double threshold 两倍阈值
            }
        } else if (oldThr > 0) {
            newCap = oldThr;    // initial capacity was placed in threshold 初始容量放在阈值中
        } else {               // zero initial threshold signifies using defaults 零初始阈值表示使用默认值
            newCap = Constants.DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (Constants.DEFAULT_LOAD_FACTOR * Constants.DEFAULT_INITIAL_CAPACITY);
        }

        //如果新阈值为0, 计算新阈值
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < Constants.MAXIMUM_CAPACITY && ft < (float) Constants.MAXIMUM_CAPACITY ? (int) ft : Integer.MAX_VALUE);
        }

        threshold = newThr; //更新阈值
        @SuppressWarnings({"unchecked"})
        Wrapper<K, V>[] newTab = (Wrapper<K, V>[]) new Wrapper[newCap];
        table = newTab;

        //摆好垃圾桶
        trashTable = new int[newCap];
        Arrays.fill(trashTable, 0); //赋值垃圾桶全为0

        //如果旧表不为空, 要就把旧表的节点重新插入到新表中
        if (oldTab == null) {
            return newTab;
        }

        //遍历旧表
        for (int j = 0; j < oldCap; ++j) {
            HamaNode<K, V> e;

            if ((e = oldTab[j].getNode()) == null) { //如果这个位置没有节点, 就跳过
                continue;
            }

            oldTab[j] = null;

            if (e.next == null) { //如果这个位置只有一个节点, 就直接插入
                newTab[e.hash & (newCap - 1)] = new Wrapper<>(e, e.getWrapper().getHashHelper());
            } else { // 遍历链表, 重新插入
                HamaNode<K, V> loHead = null, loTail = null;
                HamaNode<K, V> hiHead = null, hiTail = null;
                HamaNode<K, V> next;

                do {
                    next = e.next;
                    if ((e.hash & oldCap) == 0) {
                        if (loTail == null) {
                            loHead = e;
                        } else {
                            loTail.next = e;
                        }
                        loTail = e;
                    } else {
                        if (hiTail == null) {
                            hiHead = e;
                        } else {
                            hiTail.next = e;
                        }
                        hiTail = e;
                    }
                } while ((e = next) != null);

                if (loTail != null) {
                    loTail.next = null;
                    newTab[j] = loHead.getWrapper();
                }

                if (hiTail != null) {
                    hiTail.next = null;
                    newTab[j + oldCap] = hiHead.getWrapper();
                }

            }

        }

        return newTab;
    }


    /**
     * Remove a node
     * <p>
     * 移除一个节点
     *
     * @note 删除需要带走该位置垃圾桶的一个垃圾对象, 就是把该位置的数组元素减一 todo
     */
    final Wrapper<K, V> removeNode(int hash, Object key, Object value, boolean matchValue, boolean movable) {
        Wrapper<K, V>[] tab;
        Wrapper<K, V> p;
        HamaNode<K, V> pNode;
        int n, index;

        if ((tab = table) != null && (n = tab.length) > 0 && (p = tab[index = (n - 1) & hash]) != null) {

            HamaNode<K, V> node = null;
            HamaNode<K, V> e;
            K k;
            V v;

            if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k)))) {
                node = p;
            } else if ((e = p.next) != null) {

                do {
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                        node = e;
                        break;
                    }
                    p = e;
                } while ((e = e.next) != null);

            }
            if (node != null && (!matchValue || (v = node.value) == value || (value != null && value.equals(v)))) {
                if (node == p) {
                    tab[index] = node.next;
                } else {
                    p.next = node.next;
                }
                --size;
                return node;
            }
        }
        return null;
    }


    //! Side Functions 辅助功能

    @Override
    public Set<IHamaEntryEx<K, V>> entrySet() {
        Set<IHamaEntryEx<K, V>> es;
        return (es = entrySet) == null ? (entrySet = new AbstractSet<>() {

            @Override
            public int size() {
                return size;
            }

            @Override
            public @NotNull Iterator<IHamaEntryEx<K, V>> iterator() {
                return new EntryIterator();
            }

            public boolean contains(Object o) {
                if (!(o instanceof IHamaEntryEx<?, ?> e)) return false;
                Object key = e.getKey();
                Wrapper<K, V> candidate = getNode(key);
                return candidate != null && candidate.getNode().equals(e);
            }

            public boolean remove(Object o) {
                if (o instanceof IHamaEntryEx<?, ?> e) {
                    Object key = e.getKey();
                    Object value = e.getValue();
                    return removeNode(Toolkit.hash(key), key, value, true, true) != null;
                }
                return false;
            }

        }) : es;

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
    public V get(Object key) {
        Wrapper<K, V> e;
        return (e = getNode(key)) == null ? null : e.getNode().getValue();
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        Wrapper<K, V>[] tab;
        V v;
        if ((tab = table) == null || size <= 0) {
            return false;
        }

        for (Wrapper<K, V> e : tab) {
            //对一个桶中的元素进行深入
            for (HamaNode<K, V> ek = e.getNode(); ek != null; ek = ek.next) {
                if ((v = ek.value) == value || (value != null && value.equals(v))) {
                    return true;
                }
            }
        }

        return false;
    }


    @Override
    public V getOrDefault(Object key, V defaultValue) {
        Wrapper<K, V> e;
        return (e = getNode(key)) == null ? defaultValue : e.getNode().value;
    }


    //!  节点相关

    /**
     * Create a regular (non-tree) node
     * <p>
     * 创建一个常规（非树）节点
     */

    Wrapper<K, V> newNode(int hash, K key, V value, HamaNode<K, V> next) {
        return new Wrapper<>(new HamaNode<>(hash, key, value, next));
    }


//! 迭代器 内部类

    abstract class HamaIterator {
        HamaNode<K, V> next;        // next entry to return 下一个要返回的条目
        HamaNode<K, V> current;     // current entry    当前条目
        int index;             // current slot    当前插槽

        HamaIterator() {
            HamaNode<K, V>[] t = table;
            current = next = null;
            index = 0;
            if (t != null && size > 0) { // advance to first entry  前进到第一个条目
                do {
                } while (index < t.length && (next = t[index++]) == null);
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Wrapper<K, V> nextNode() {
            HamaNode<K, V>[] t;
            HamaNode<K, V> e = next;

            if (e == null) {
                throw new NoSuchElementException();
            }
            if ((next = (current = e).next) == null && (t = table) != null) {
                do {
                } while (index < t.length && (next = t[index++]) == null);
            }
            return e;
        }

        public final void remove() {
            HamaNode<K, V> p = current;
            if (p == null) {
                throw new IllegalStateException();
            }
            current = null;
            removeNode(p.hash, p.key, null, false, false);

        }
    }

    final class KeyIterator extends HamaIterator implements Iterator<K> {
        public K next() {
            return nextNode().key;
        }
    }

    final class ValueIterator extends HamaIterator implements Iterator<V> {
        public V next() {
            return nextNode().value;
        }
    }

    final class EntryIterator extends HamaIterator implements Iterator<IHamaEntryEx<K, V>> {
        public IHamaEntryEx<K, V> next() {
            return nextNode();
        }
    }


}
