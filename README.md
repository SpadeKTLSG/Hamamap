# Hamamap

蛤蟆Map - hashmap 重构, 源自 "拐角垃圾桶狂想曲"

hashmap refactored , from "Rhapsody of Corner Trash Cans"

## 缩写 Simplified Docs

JavaDoc Simplify:

- KVmapping(KV) == Key-Value mapping 键值对映射
- hash code (value) = HC 哈希码(值)

## 使用 How to use

打包: Package

```shell
mvn clean package
```

引用: Refer in project
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

优化: Install to maven
> 你可以把他导入到本地maven仓库, 便于使用

```mvn
mvn install:install-file -Dfile=D:\${yourPath}\Hamamap-2.0.jar  -DgroupId=org.spc -DartifactId=Hamamap  -Dversion=2.0 -Dpackaging=jar
```

## 笔记 Note

其实我知道, 这已经基本上是失败了, 性能上的劣势已经不太能追回来了

经过与某位大牛ZQ的探讨, 其实主要提升的性能在 查询; 新增可能并不会提升太多, 毕竟还有扩容操作.
这个重写的主要含义就是空间换时间, 保证每个节点实际上能够插入的串的长度更加的短, 甚至可以不需要红黑树了, 大大优化了查询时候的性能. 因此比较适合那些经常查询, 同时插入又不是要求很快得到结果的情况.

因此比较适合胜任缓存的工作, 但是不适合那种需要快速插入的场景.

In fact, I know, this has been basically a failure, performance disadvantage has not been able to recover

After a discussion with some Master ZQ, in fact, the main improvement in performance, Query; add may not improve too much, after all, there are expansion operations.

The main meaning of this rewrite is space for time, to ensure that each node can actually insert the string length is shorter, and even do not need a red-black tree, greatly optimizing the performance of the query. This makes it ideal for frequent queries where inserts don't require quick results.

So it's good for caching, but not for the kind of scenario that requires fast insertion.

## 对比 Compare

性能方面还是输了, 没让HashMap大人尽兴真是抱歉呐

```text
Hamamap insert time is 1.71 times of HashMap
Hamamap delete time is 1.53 times of HashMap
Hamamap query time is 1.55 times of HashMap
```

## 感言 What can I say?

I said, I did, I won. 我承诺过, 我做了, 我胜利.

2025/01/16 完结, 一场酣畅淋漓的试炼
