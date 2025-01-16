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
    @Deprecated
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

    /**
     * Default hash helper field value grow
     * <p>
     * 默认哈希辅助值增长
     */
    int DEFAULT_HASH_HELPER_VALUE_GROW = 8;

    /**
     * The maximum length of queue in thread pool
     * <p>
     * 线程池队列最大长度
     */
    int MAX_THREAD_QUEUE_SIZE = 2048;

    /**
     * The alive time of thread pool's thread
     * <p>
     * 线程池线程生存时间
     */
    int THREAD_KEEPALIVE_TIME = 1;

    /**
     * (in one table[?]) can stand trash count
     * <p>
     * (一个table桶里面) 可以容忍的垃圾数量
     */
    int DEFAULT_CANSTANDED_TRASH_COUNT = 3;


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


}
