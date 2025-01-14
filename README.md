# Hamamap

蛤蟆Map - hashmap 重构, 源自 "拐角垃圾桶狂想曲"

hashmap refactored , from "Rhapsody of Corner Trash Cans"

JavaDoc Simplify:

- KVmapping(KV) == Key-Value mapping 键值对映射
- hash code (value) = HC 哈希码(值)

打包:

```shell
mvn clean package
```

temp:

其实我知道, 已经基本上是失败了, 性能上的劣势已经不太能追回来了, 但是: I said, I did, I won.

经过与某位大牛ZQ的探讨, 其实主要提升的是性能位置 在 查询; 新增可能并不会提升太多, 毕竟还有扩容操作.
这个重写的主要含义就是空间换时间, 保证每个节点实际上能够插入的串的长度更加的短, 甚至可以不需要红黑树了, 大大优化了查询时候的性能. 因此比较适合那些经常查询, 同时插入又不是要求很快得到结果的情况.

因此比较适合胜任缓存的工作, 但是不适合那种需要快速插入的场景. 
