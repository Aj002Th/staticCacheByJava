### StaticCache

​	随着计算机的不断发展，cpu的速度提升越来越快，相较而言存储器的读写速度较为缓慢，系统的瓶颈也从cpu转变到了存储器。计算机的设计者通过利用程序的局部性原理对计算机进行了多层的存储结构设计，这一精巧的设计很大程度地提高了计算机系统的吞吐量。在软件这一层，同样是各种场景都有缓存组件的应用身影，项目中，我们也经常实现自己的缓存，比如用 用redis来存储数据库的热点数据以降低数据库的读写压力。缓存经常要面对的一个重要问题，就是缓存更新导致的缓存一致性问题，在本次作业中，我完成了一个特殊的缓存实现——StaticCache，只有 get，没有 set 的缓存，那么自然就没有更新导致的缓存一致性问题，因为你就更新不了。
​	StaticCache 是一个小巧的 kv 存储库，这里一个缓存库，而非是一个开箱即用的 server 进程组件。StaticCache最大的特点是没有删除接口，换句话说，kv 键值一旦设置进去了，那么用户端是没有主动的手段删除这个值的，所以这个值将不能被用户修改， k 的 v 不能修改，那么带来的好处就是没有覆盖更新带来的一致性问题，但正因为如此，所以这个库是有自己的特殊场景的，需要业务自己满足这种场景的使用。
​	看到这里你显然会有点困惑了，我抛出 3 个问题自问自答：

- 问题1：StaticCache 没有更新和删除接口，那么缓存所占用等等空间岂不是会越来越多？这样的话还有实用意义吗？
  其实不是的，StaticCache 没有 set，update，delete 接口只是让用户无法更新和删除已经缓存的内容而已，而不是说设置进去的 kv 要永久保存，缓存空间肯定不是无限的，StaticCache内部是通过 LRU 算法来管理内容的。
- 问题2：StaticCache没有 set 接口，那内容是怎么设置进去的呢？
  初始化的时候，就需要明确当 key miss 的时候，怎么获取到内容的手段，把这个手段配置好是前提；get 调用的时候，当 key miss ，就会调用初始化的获取手段来获取数据，如果 hit 的话，那么就直接返回数据；
- 问题3：这种只能 get ，不能更新 key 的缓存有啥用？有什么适用场景？
  这个其实是具体的使用场景，比如你缓存一些静态文件，用文件 md5 作为 key，value 就是文件。这种场景就很适合用 groupcache 这种缓存，因为 key 对应的 value 不需要改变。

​	看起来似乎有点奇怪，但其实这就是个设计思路比较新奇的内存缓存罢了，而且在单机缓存的实现是可以非常简单的：通过内存中维护一个 cache map，当收到查询请求时，先查询 cache 是否命中，如果命中则直接返回，否则必须到存储系统执行查询，然后缓存一份，再返回结果即可。这也是就是该程序的工作的基本过程。单机系统中实现的简易性也是其一大特点。

​	由于这只是一个缓存组件，想要展现出其功能需要相应地使用它编写应用代码。这里设计了一个很简单了应用例子，代码位于Main.java之中。

​	我预先创建了四个文件分别为 a.txt, b.txt, c.txt, d.txt再通过StaticCache缓存其中的内容。缓存的size我设置为2，这意味着总有两个文件不在缓存之中。

​	在应用程序中，我按顺序设置了四个文件的key和内容读取的方法，接着按顺序访问了四个文件，每进行一个操作之后我都打印出了cache中缓存的key值有哪些，以清楚地展现出内部的LRU是怎么进行工作的。