//package org.spc.demos.basic3;
//
//
//import org.spc.demos.basic1.Node;
//
//import java.io.Serializable;
//
///**
// * 手写实现HashMap
// *
// * @Author heyuhua
// * @create 2021/2/9 15:31
// */
//public class HyhHashMap<K, V> implements Myhmap<K, V>, Serializable {
//
//    /**
//     * 默认容量
//     */
//    static final int DEFAULT_CAPACITY = 16;
//    /**
//     * 负载因子
//     */
//    static final float DEFAULT_LOAD_FACTOR = 0.75f;
//    int threshold;
//    /**
//     * 当前key索引位置
//     */
//    int keyIndex;
//    /**
//     * 保存Node<K,V>节点的数组
//     */
//    Node<K, V>[] table;
//    /**
//     * 存储当前Map容量的大小
//     */
//    int size;
//
//    /**
//     * 计算Hash值
//     *
//     * @param key
//     * @return
//     */
//    static final int hash(Object key) {
//        int h;
//        return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
//    }
//
//    @Override
//    public void put(K key, V value) {
//        Node<K, V> node;
//        if (table == null) {
//            table = resize();
//            //table里面为空的情况
//            node = new Node<K, V>(hash(key), key, value, null);
//            table[keyIndex] = node;
//            size++;
//        } else {
//            table = resize();
//            //table不为空时
//            Node<K, V> n;
//            //是否hash冲突
//            boolean hashConflict = false;
//            for (int i = 0; i < table.length; i++) {
//                n = table[i];
//                if (n != null) {
//                    if (n.hash == hash(key)) {
//                        hashConflict = true;
//                        //hash相等时
//                        while (n != null) {
//                            if (n.key.equals(key)) {
//                                //hash相等并且key也相等，直接替换原来的值就行了
//                                n.value = value;
//                                table[i] = n;
//                                size++;
//                            } else {
//                                node = new Node<K, V>(hash(key), key, value, null);
//                                node.next = n;
//                                table[i] = node;
//                                size++;
//                            }
//                            n = n.next;
//                        }
//                    }
//                }
//
//            }
//            if (!hashConflict) {
//                //没有hash冲突，直接put
//                node = new Node<K, V>(hash(key), key, value, null);
//                table[++keyIndex] = node;
//                size++;
//
//            }
//        }
//    }
//
//    @Override
//    public V get(K key) {
//        HyhHashMap.Node<K, V> node;
//        return (node = getNode(key)) == null ? null : node.value;
//    }
//
//    /**
//     * 获取Node
//     *
//     * @param key
//     * @return
//     */
//    final HyhHashMap.Node<K, V> getNode(Object key) {
//        if (table != null) {
//            for (int i = 0; i < table.length; i++) {
//                Node<K, V> node = table[i];
//                if (node != null) {
//                    //hash相等
//                    if (node.hash == hash(key)) {
//                        while (node != null) {
//                            if (node.key.equals(key)) {
//                                //hash和key都相等时`
//                                return node;
//                            }
//                            node = node.next;
//                        }
//                    }
//                }
//
//            }
//        }
//        return null;
//    }
//
//
//}
//
