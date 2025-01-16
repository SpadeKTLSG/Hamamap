package org.spc.impl;

import org.jetbrains.annotations.NotNull;
import org.spc.api.IHamaEntryEx;
import org.spc.api.IHamamap;
import org.spc.api.IHamamapEx;
import org.spc.tool.Constants;
import org.spc.tool.MyBoolean;
import org.spc.tool.Toolkit;
import org.spc.tool.Wrapper;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

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
     * 默认初始插入重试次数 (未来可以扩展, 现在没啥用)
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
     * 键值对的数量 (实际大小)
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
        int testHash = Toolkit.hash(key) + Toolkit.hash(Constants.DEFAULT_HASH_HELPER_VALUE);
        return (e = removeNode(testHash, key)) == null ? null : e.getNode().getValue();
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
     * @note 只给出一个Key, 但是真正存放的时候, 可能存在下面几个可能的位置是真正存放的地方:
     * hash =[ {key} + {hashHelper} * {0~maxRetry}]; 因此要发起多次查询, 以便找到真正的位置
     */
    final Wrapper<K, V> getNode(Object key) { //todo FIXME: 插入了查不到
        int testKeyHash = Toolkit.hash(key);
        if (Constants.USE_THREAD) {
            return getNodeByHashThread(testKeyHash, key);
        } else {
            return getNodeByHash(testKeyHash, key); //normal
        }
    }


    /**
     * Get a node by hash
     * <p>
     * 处理轮询式查询
     */
    private Wrapper<K, V> getNodeByHash(int hash, Object key) {
        int salt = Toolkit.hash(Constants.DEFAULT_HASH_HELPER_VALUE);
        //使用数组初始化为重试次数, 对每种情况进行探索
        for (int i = 0; i < maxRetry; i++) {
            Wrapper<K, V> res = getRealNode(hash + salt * i, key);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    /**
     * handle multi-threaded query
     * <p>
     * 处理多线程式查询
     *
     * @note 不推荐使用, 仅供调试
     */
    private Wrapper<K, V> getNodeByHashThread(int rawHash, Object key) {
        int salt = Toolkit.hash(Constants.DEFAULT_HASH_HELPER_VALUE);

        //通过线程池进行多线程查询, 最后CountdownLatch等待所有线程结束后汇总结果
        ExecutorService executor = Toolkit.CACHE_REBUILD_EXECUTOR;
        CountDownLatch countDownLatch = new CountDownLatch(maxRetry); //3 times retrys, means 4 threads to find the key's possible position
        List<Wrapper<K, V>> res = new ArrayList<>(); //处理查询(以防万一出来多个位置) <- 调试用, 未来可直接返回
        AtomicReference<Wrapper<K, V>> result = new AtomicReference<>();

        for (int i = 0; i < maxRetry; i++) {
            int finalI = i;
            executor.execute(() -> {
                res.add(getRealNode(rawHash + salt * finalI, key));
                countDownLatch.countDown();
            });
        }
        executor.submit(() -> {
            try {
                countDownLatch.await();
                //找到第一个非空的结果
                result.set(res.stream().filter(Objects::nonNull).findFirst().orElse(null));
            } catch (InterruptedException ignored) {
            }
        });
        return result.get();
    }

    /**
     * Get the real node by just hash
     * <p>
     * 通过确定的hash值获取真实节点
     */
    private Wrapper<K, V> getRealNode(int realHash, Object key) {
        Wrapper<K, V>[] tab;
        Wrapper<K, V> first;
        Wrapper<K, V> e;
        int n;
        K k;

        if ((tab = table) != null && (n = tab.length) > 0 && (first = tab[(n - 1) & realHash]) != null) {
            //? 包装器处理:
            //! 正确判断节点的真实Hash值方法, 是通过包装器对象.hashCode()方法获取
            if (first.hashCode() == realHash && ((k = first.getNode().key) == key || (key != null && key.equals(k)))) {
                return first;
            }

            //遍历桶中的链表
            while (first.getNode().next != null && (e = first.getNode().next.getWrapper()) != null) { //小心空指针
                if (e.hashCode() == realHash && ((k = e.getNode().key) == key || (key != null && key.equals(k)))) {
                    return e;
                }
                first = e;
            }
        }
        return null;
    }


    /**
     * Insert a node by KV
     * <p>
     * 插入一个KV - 主方法
     *
     * @param testHash 试探的Hash值
     * @note 插入时候要涉及到 "避让垃圾桶" 的逻辑
     */
    final V putVal(int testHash, K key, V value) {
        Wrapper<K, V>[] tab; //本地哈希表引用
        Wrapper<K, V> p; //当前位置的包装器
        int n; // 记录哈希表的长度length
        int i; // 记录哈希表的位置sit

        //如果哈希表为空, 或者长度为0, 就扩容
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }

        //! 如果这个位置没有节点(Wrapper) - 意味着没有垃圾直接插入, 不需要处理垃圾桶逻辑
        if ((p = tab[i = (n - 1) & testHash]) == null) {
            tab[i] = newNode(testHash, key, value, null);
            //维护链关系: 不需要

            if (++size > threshold) {
                resize();
            }
            return null;
        }

        //! 否则就要处理垃圾桶逻辑
        MyBoolean success = new MyBoolean(false); //记录是否插入成功
        //当插入并且当前位置的垃圾桶存在垃圾(int[now] > 0)时, 就要开始执行递归重新插入, 用turn代表插入次数
        //改变hash实现修改插入位置: 此时节点还未初始化, 因此需要手动提前处理hash盐, 初始为 1 + 8(一次增长)
        int newTestHash = Toolkit.hash(key) + Toolkit.hash(Constants.DEFAULT_HASH_HELPER_VALUE + Constants.DEFAULT_HASH_HELPER_VALUE_GROW);
        V tempV = putValRetry(newTestHash, key, value, 1, success);
        if (success.value) { //子方法重试插入成功
            return tempV;
        }
        return putValbyHash(testHash, p, key, value);//重试失败, 仍然正常插入初始位置 (testHash)
    }


    /**
     * 处理插入的递归调用方法
     *
     * @param turn 插入次数, 初始为0
     * @note 未来可以做非递归的实现, 仿照上面的查询
     */
    private V putValRetry(int nowHash, K key, V value, int turn, MyBoolean success) {
        if (turn >= maxRetry) {
            return null;
        }
        //判断能否再次插入: 初始重试和最大重试, 使用的Hash盐剧烈性可以设置为不同 (未来)
        int i;

        if (table[i = (table.length - 1) & nowHash] == null) { //如果这个位置没有节点(Wrapper) - 意味着没有垃圾直接插入, 不需要处理垃圾桶逻辑
            table[i] = newNode(nowHash, key, value, null);
            //维护链关系: 不需要
            if (++size > threshold) {
                resize();
            }
            success.value = true;
            return null;
        }
        //改变hash重新试探
        int newTestHash = Toolkit.hash(key) + Toolkit.hash(Constants.DEFAULT_HASH_HELPER_VALUE + Constants.DEFAULT_HASH_HELPER_VALUE_GROW * turn);
        return putValRetry(newTestHash, key, value, turn + 1, success);
    }

    /**
     * Use specified hash to insert into the corresponding p's sequence area
     *
     * @param p 对应桶的初始化位置的包装器对象
     *          <p>
     *          采用指定的hash值插入对应p的序列区域
     */
    private V putValbyHash(int realHash, Wrapper<K, V> p, K key, V value) {

        Wrapper<K, V> e = null; //临时位置包装器
        K k; //临时键

        if (p.hashCode() == realHash && ((k = p.getNode().key) == key || (key != null && key.equals(k)))) {
            e = p; //如果对象节点的hash值和key值相同, 就直接赋值
            //由于是取代, 不进行垃圾桶处理
        } else {
            //一旦进行了遍历, 就要处理垃圾桶关系
            for (; ; ) {
                //如果下一个节点为空, 就直接插入

                if (p.getNode().next == null || (e = p.getNode().next.getWrapper()) == null) {//如果下一个节点为空, 就直接插入
                    p.getNode().next = newNode(realHash, key, value, null).getNode(); //维护关系
                    break;
                }
                //如果下一个节点不为空, 就继续遍历; 如果找到相同的key, 就直接替换
                if (e.hashCode() == realHash && ((k = e.getNode().key) == key || (key != null && key.equals(k)))) {
                    break;
                }
                p = e; //前进
            }
            //?在当前hashTable对应的位置的垃圾桶中增加一个垃圾
            trashTable[(table.length - 1) & realHash] += 1;
        }

        if (e != null) { //如果对应位置的对象不为空即为 找到了相同的键, 就直接替换值
            V oldValue = e.getNode().value;
            e.getNode().value = value;
            return oldValue;
        }

        if (++size > threshold) {
            resize();
        }

        return null; //返回空, 说明插入成功并且没有替换
    }


    /**
     * Create a regular (non-tree) node
     * <p>
     * 创建一个常规（非树）节点
     *
     * @note hash已经被提升到了包装类中, 节点的hash尽量不要用
     */
    Wrapper<K, V> newNode(int hash, K key, V value, HamaNode<K, V> next) {
        return new Wrapper<>(new HamaNode<>(key, value, next), hash);
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
            int testHash = Toolkit.hash(key) + Toolkit.hash(Constants.DEFAULT_HASH_HELPER_VALUE);
            //这里同样通过hash值和默认的hash盐值进行和, 同样走试探的方法, 以便还原
            putVal(testHash, key, value);
        }

    }


    /**
     * Rehash the table when the given capacity is reached
     * <p>
     * 当达到给定容量时重新哈希Hamamap table
     *
     * @note 扩容时, 按照逻辑讲, 应该暂时会变得宽敞起来, 因此我决定直接执行垃圾桶清空操作 (初始化长度), 暂时不搬运了
     */
    final Wrapper<K, V>[] resize() {
        Wrapper<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;


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
        @SuppressWarnings({"unchecked"}) Wrapper<K, V>[] newTab = (Wrapper<K, V>[]) new Wrapper[newCap];
        table = newTab;

        //摆好垃圾桶
        trashTable = new int[newCap];
        Arrays.fill(trashTable, 0); //赋值垃圾桶全为0


        if (oldTab == null) {
            return newTab;
        }

        //如果旧表不为空, 要就把旧表的节点重新插入到新表中, 注意Hash计算处理
        for (int j = 0; j < oldCap; ++j) {
            Wrapper<K, V> e;

            if ((e = oldTab[j]) == null) { //如果旧表这个桶位置没有节点, 就跳过
                continue;
            }

            oldTab[j] = null; //释放旧表的桶位置

            if (e.getNode().next == null) { //如果这个位置只有一个节点, 就直接插入
                newTab[e.hashCode() & (newCap - 1)] = new Wrapper<>(e); //保留原来的包装器参数

            } else { // 这个位置有多节点
                Wrapper<K, V> loHead = null, loTail = null;
                Wrapper<K, V> hiHead = null, hiTail = null;
                Wrapper<K, V> next;

                //遍历这个桶中的节点
                do {
                    next = e.getNode().next.getWrapper();
                    if ((e.hashCode() & oldCap) == 0) { //如果hash值不变, 就插入到原来的位置
                        if (loTail == null) {
                            loHead = e;
                        } else {
                            loTail.getNode().next = e.getNode();
                        }
                        loTail = e;
                    } else {
                        if (hiTail == null) {
                            hiHead = e;
                        } else {
                            hiTail.getNode().next = e.getNode();
                        }
                        hiTail = e;
                    }
                } while ((e = next) != null);

                //如果低位链表不为空, 就插入到新表中
                if (loTail != null) {
                    loTail.getNode().next = null;
                    newTab[j] = loHead;
                }

                //如果高位链表不为空, 就插入到新表中
                if (hiTail != null) {
                    hiTail.getNode().next = null;
                    newTab[j + oldCap] = hiHead;
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
     * @note 删除需要带走该位置垃圾桶的一个垃圾对象, 就是把该位置的数组元素减一
     */
    final Wrapper<K, V> removeNode(int testHash, Object key) {
        Wrapper<K, V> p; //该桶位置的包装器头
        Wrapper<K, V> nodeWrapper;
        int index; //本次试探的位置

        if (table == null || size == 0 || table.length == 0) { //合法性检查
            return null;
        }
        if ((p = table[index = (table.length - 1) & testHash]) == null) { //这个桶位置没有节点
            return null;
        }


        //查找节点 - 直接调用查询方法
        nodeWrapper = getNode(key);
        if (nodeWrapper == null) { //没找到哦
            return null;
        }

        if (nodeWrapper == p) { //如果是头节点
            table[index] = nodeWrapper.getNode().next.getWrapper();
        } else { //如果不是头节点, 使用前驱节点短路之
            p.getNode().next = nodeWrapper.getNode().next;
        }

        --size;
        return nodeWrapper;
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

            @Deprecated
            public boolean remove(Object o) {
                //暂时关闭
                throw new UnsupportedOperationException();
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


//! 迭代器 内部类

    abstract class HamaIterator {
        Wrapper<K, V> next;        // next entry to return 下一个要返回的条目
        Wrapper<K, V> current;     // current entry    当前条目
        int index;             // current slot    当前插槽

        HamaIterator() {
            Wrapper<K, V>[] t = table;
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
            Wrapper<K, V>[] t;
            Wrapper<K, V> e = next;

            if (e == null) {
                throw new NoSuchElementException();
            }

            if (e.getNode().next == null || (next = (current = e).getNode().next.getWrapper()) == null) {
                if (table != null) {
                    while (index < table.length && (next = table[index++]) == null) {
                        // Continue to the next non-null entry
                    }
                }
            }
            return e;
        }

        @Deprecated
        public final void remove() {
            //暂时关闭
            throw new UnsupportedOperationException();
        }
    }

    final class KeyIterator extends HamaIterator implements Iterator<K> {
        public K next() {
            return nextNode().getNode().key;
        }
    }

    final class ValueIterator extends HamaIterator implements Iterator<V> {
        public V next() {
            return nextNode().getNode().value;
        }
    }

    final class EntryIterator extends HamaIterator implements Iterator<IHamaEntryEx<K, V>> {
        public IHamaEntryEx<K, V> next() {
            return nextNode().getNode();
        }
    }


}
