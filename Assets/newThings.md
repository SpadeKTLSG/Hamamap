## 未来的提升方向

来自某人的建议
"既然你都给它定义列表数量, 那是不是可以找到最小的"
"怎么知道主桶的副桶是哪个？在该桶新建一个属性指向副桶，而hash值变化和两者关联"

"然后就是我们不是不确定是在主桶还是副桶吗... 那就可以加层过滤来缓解一部分, 比方说布隆, 通过对应字节筛选出一定不在这里面的;
在一定程度上效率又提高了"
"虽然这样最坏情况没有提高，但平均情况肯定提高了，因为很多都没必要再迭代"

"主要思路是基于 主桶 + 副桶 这样的连接思路"

有问题, 就是如果 一旦动态确认了主副桶 后面一般就不会改变了，因为你需要通过通头去找副桶

...除非再升级架构，改成路由表. 第一次路由，第二次路由 但问题是第一次路由，第二次路由，查询的时候不知道是哪个路由

"那就都查一遍"


---

"反正你现在这个布隆是肯定可以先加的, 100%提升"

是这样的
