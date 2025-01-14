package org.spc.tool;

/**
 * Constants
 * <p>
 * 常量
 */
public interface Constants {


    //! Reborn

    /**
     * Default initial retry
     * <p>
     * 默认初始插入重试次数 = 1
     */
    int DEFAULT_INITIAL_RETRY = 1;

    /**
     * Default max retry
     * <p>
     * 默认最大重试次数 = 3
     */
    int DEFAULT_MAX_RETRY = 3;

    /**
     * Default max trash (in bucket)
     * <p>
     * (桶内)默认最大垃圾数量 = 16
     */
    int DEFAULT_MAX_TRASH = 1 << 4; // aka 16
    /**
     * Default hash helper field value
     * <p>
     * 默认哈希辅助值
     */
    int DEFAULT_HASH_HELPER_VALUE = 1;

    //! Original
    /**
     * Default initial capacity - MUST be a power of two
     * <p>
     * 默认初始容量 - 必须是2的幂
     */
    int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16


    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments
     * MUST be a power of two <= 1<<30
     * <p>
     * 最大容量，如果通过带参数的构造函数隐式指定了更高的值，则使用该值
     * 必须是小于等于1<<30的2的幂。
     */
    int MAXIMUM_CAPACITY = 1 << 30; // aka 1,073,741,824


    /**
     * The load factor used when none specified in constructor
     * <p>
     * 在构造函数中未指定时使用的 负载因子
     */
    float DEFAULT_LOAD_FACTOR = 0.75f; // aka ((75%)=> resize)


    /**
     * The bin count threshold for using a tree rather than list
     * <p>
     * 链树化的阈值
     */
    int TREEIFY_THRESHOLD = 8;


    /**
     * The bin count threshold for untreeifying a (split) bin during a
     * resize operation. Should be less than TREEIFY_THRESHOLD, and at
     * most 6 to mesh with shrinkage detection under removal.
     * <p>
     * 在调整大小操作期间取消树化（拆分）的阈值
     * 应小于TREEIFY_THRESHOLD，最多为6，以便与删除时的缩小检测相匹配
     */
    int UNTREEIFY_THRESHOLD = 6;


    /**
     * The smallest table capacity for which bins may be treeified.
     * Should be at least (4 - 8) * TREEIFY_THRESHOLD to avoid conflicts
     * <p>
     * 可以将链转换为树的最小表容量
     * 应至少为(4 - 8) * TREEIFY_THRESHOLD以避免冲突, 取8
     */
    int MIN_TREEIFY_CAPACITY = 8 * TREEIFY_THRESHOLD; //aka 64

}
