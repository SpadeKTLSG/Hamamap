package org.spc.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Wrapper Class, used to wrap HamaNode(TreeNode) object, contain actual headNode and location infos
 * <p>
 * 包装器类, 用于包装HamaNode(TreeNode)对象, 包含具体的头结点和定位信息
 *
 * @note: 方法内部调用
 */
@Getter
@Setter
@AllArgsConstructor
public class AllLocate<K, V> {

    /**
     * Located headNode Wrapper
     * <p>
     * 定位的头节点
     */
    private Wrapper<K, V> head;

    /**
     * Actual Wrapper
     * <p>
     * 实际对象
     */
    private Wrapper<K, V> data;

    /**
     * sit which headNode Wrapper in
     * <p>
     * 头结点在table位置
     */
    private int sit;

}
