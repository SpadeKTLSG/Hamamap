package org.spc.api;

/**
 * Hamamap KVpair, can be a view of a map, or a snapshot of a map, or a modifiable KVpair.
 * <p>
 * 一个KV对接口: 可以是map的视图, 或map的快照, 或可修改的KV对
 *
 * @author SpadeKTLSG 玄桃K
 */

/**
 * Hamamap Interface
 * <p>
 * Hamamap 接口
 *
 * @author SpadeKTLSG 玄桃K
 */
public interface IHamaEntry {

    /**
     * Returns the K
     * <p>
     * 返回K
     *
     * @return the key
     */
    K getKey();

    /**
     * Returns the V
     * <p>
     * 返回V
     *
     * @return the value
     */
    V getValue();

    /**
     * Set V to this entry, returns the old value; but if original KV has been removed, throws IllegalStateException
     * <p>
     * 设置V到这个entry, 返回原值; 但如果原KV已被移除, 则抛出IllegalStateException
     *
     * @return old value
     * @throws ClassCastException    if value's type inappropriate
     * @throws NullPointerException  if value is null
     * @throws IllegalStateException original one has been removed
     */
    V setValue(V value);

    /**
     * Compares object with this entry for equality
     * <p>
     * 与这个entry比较相等
     *
     * @return true if object is also a KV entry and the two represent the same KV
     */
    boolean equals(Object o);

    /**
     * Returns the HC for this entry
     *
     * <p>
     * 返回这个entry的哈希码
     *
     * <p>
     * <pre>
     *     (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
     *     (e.getValue()==null ? 0 : e.getValue().hashCode())
     * </pre>
     * <p>
     *
     * @return the HC for this map entry
     */
    int hashCode();
}
