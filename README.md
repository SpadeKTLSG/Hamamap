# Hamamap

蛤蟆Map - hashmap 重构, 源自 "拐角垃圾桶狂想曲"

hashmap refactored , from "Rhapsody of Corner Trash Cans"

## 缩写 Simplified Docs

JavaDoc Simplify:

- KVmapping(KV) == Key-Value mapping 键值对映射
- hash code (value) = HC 哈希码(值)

## 使用 How to use

打包:

```shell
mvn clean package
```

引用:
> 你可以放在项目的libs里面, 或者直接在IDEA里面引用

```mvn
        <!--特殊测试-hamamap-->
        <dependency>
            <groupId>org.spc</groupId>
            <artifactId>Hamamap</artifactId>
            <version>2.0</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/libs/Hamamap-2.0.jar</systemPath>
        </dependency>
```

## 笔记 Note

其实我知道, 这已经基本上是失败了, 性能上的劣势已经不太能追回来了

经过与某位大牛ZQ的探讨, 其实主要提升的性能在 查询; 新增可能并不会提升太多, 毕竟还有扩容操作.
这个重写的主要含义就是空间换时间, 保证每个节点实际上能够插入的串的长度更加的短, 甚至可以不需要红黑树了, 大大优化了查询时候的性能. 因此比较适合那些经常查询, 同时插入又不是要求很快得到结果的情况.

因此比较适合胜任缓存的工作, 但是不适合那种需要快速插入的场景.

## 感言 What I want to say

I said, I did, I won. 我承诺过, 我做了, 我胜利.

2025/01/16 完结, 一场酣畅淋漓的试炼
