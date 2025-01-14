package org.spc.tool;

import org.spc.impl.HamaNode;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Toolkit
 * <p>
 * 工具箱
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public interface Toolkit {

    /**
     * Hash Tool for hamamap
     * <p>
     * Hamamap的哈希工具, 必须结合使用包装器方法
     */
    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * Returns a power of two size for the given target capacity
     * <p>
     * 返回给定目标容量的2的幂大小
     */
    static int tableSizeFor(int cap) {
        int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
        if (n < 0) {
            return 1;
        } else if (n >= Constants.MAXIMUM_CAPACITY) {
            return Constants.MAXIMUM_CAPACITY;
        } else {
            return n + 1;
        }
    }

    /**
     * Returns k.compareTo(x) if x matches kc (k's screened comparable
     * class), else 0
     * <p>
     * 如果x匹配kc（k的筛选可比类），则返回k.compareTo(x)，否则返回0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    static int compareComparables(Class<?> kc, Object k, Object x) {
        return (x == null || x.getClass() != kc ? 0 :
                ((Comparable) k).compareTo(x));
    }

    /**
     * Returns x's Class if it is of the form "class C implements Comparable", else null
     * <p>
     * 如果x的形式为“类C实现Comparable”，则返回x的类，否则返回null
     */
    static Class<?> comparableClassFor(Object x) {
        if (!(x instanceof Comparable)) {
            return null;
        }

        Class<?> c = x.getClass();
        if (c == String.class) // bypass checks
            return c;

        Type[] ts = c.getGenericInterfaces(), as;
        ParameterizedType p;

        for (Type t : ts) {
            if ((t instanceof ParameterizedType) && ((p = (ParameterizedType) t).getRawType() == Comparable.class) && (as = p.getActualTypeArguments()) != null && as.length == 1 && as[0] == c)
                return c;
        }
        return null;
    }

    /**
     * get Wrapped Node
     * <p>
     * 获取包装的节点
     */
    static HamaNode openNode(Wrapper wrapper) {
        return wrapper.getNode();
    }

    /**
     * set Wrapped Node
     * <p>
     * 设置包装的节点
     */
    static void setNode(Wrapper wrapper, HamaNode node) {
        wrapper.setNode(node);
    }

    /**
     * Type check
     * <p>
     * 类型检查
     */
    static boolean isWrapper(Object obj) {
        return obj instanceof Wrapper;
    }

    /**
     * Type check
     * <p>
     * 类型检查
     */
    static boolean isNode(Object obj) {
        return obj instanceof HamaNode;
    }
}
