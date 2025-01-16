package org.spc.tool;

import lombok.Getter;
import lombok.Setter;
import org.spc.impl.HamaNode;

import java.util.Objects;

/**
 * Wrapper Class, used to wrap HamaNode(TreeNode) object
 * <p>
 * 包装器类, 用于包装HamaNode(TreeNode)对象
 *
 * @note: 方法内部调用
 */
@Getter
@Setter
public class Wrapper<K, V> {

    /**
     * Node(Treenode)
     * <p>
     * 包装的节点对象
     */
    private HamaNode<K, V> node;

    /**
     * hashHelper
     * <p>
     * 哈希辅助值
     */
    private int hashHelper;


    public Wrapper(Wrapper<K, V> wrapper) {
        this(wrapper.node, wrapper.hashHelper);
    }

    public Wrapper(HamaNode<K, V> node, int hashHelper) {
        this.node = node;
        this.hashHelper = hashHelper;
        this.node.setWrapper(this);
    }

    @Deprecated
    public Wrapper(HamaNode<K, V> node) {
        this(node, Constants.DEFAULT_HASH_HELPER_VALUE);
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
        //hashHelper参与hash计算, 必须用自己的Hash计算方法
        return Toolkit.hash(node.getKey()) + Toolkit.hash(hashHelper);
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
