package org.spc.tool;

import lombok.Getter;
import lombok.Setter;
import org.spc.impl.HamaNode;

import java.util.Objects;

/**
 * 包装器类, 用于包装HamaNode(TreeNode)对象
 *
 * @param <K>
 * @param <V>
 */
@Getter
@Setter
public class Wrapper<K, V> {

    /**
     * Node(Treenode)
     * <p>
     * 包装的节点对象
     */
    public HamaNode<K, V> node; //public 调试用

    /**
     * hashHelper
     * <p>
     * 哈希辅助值
     */
    public int hashHelper;  //public 调试用


    public Wrapper(HamaNode<K, V> node, int hashHelper) {
        this.node = node;
        this.hashHelper = hashHelper;
    }

    public Wrapper(HamaNode<K, V> node) {
        this(node, 1);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked") Wrapper<K, V> wrapper = (Wrapper<K, V>) o;

        //hashHelper不参与比较
        return Objects.equals(node, wrapper.node);
    }

    @Override
    public int hashCode() {
        //hashHelper参与hash计算
        return Objects.hash(node, hashHelper);
    }

    public final String toString() {
        return this.node.getKey() + "=" + this.node.getValue();
    }


    //? 特定功能重写 (其实没必要)
    public final K getKey() {
        return this.node.getKey();
    }

    public final V getValue() {
        return this.node.getValue();
    }

    public final V setValue(V newValue) {
        V oldValue = this.node.getValue();
        this.node.setValue(newValue);
        return oldValue;
    }
}
