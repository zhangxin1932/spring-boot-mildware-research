#1.概述
## 1.1 Disruptor介绍
```
1.Disruptor 是英国外汇交易公司LMAX开发的一个高性能队列，
研发的初衷是解决内存队列的延迟问题（在性能测试中发现竟然与I/O操作处于同样的数量级）。
基于 Disruptor 开发的系统单线程能支撑每秒 600 万订单，2010 年在 QCon 演讲后，获得了业界关注。

2.Disruptor是一个开源的Java框架，它被设计用于在生产者—消费者（producer-consumer problem，简称PCP）
问题上获得尽量高的吞吐量（TPS）和尽量低的延迟。

3.从功能上来看，Disruptor 是实现了“队列”的功能，而且是一个有界队列。
那么它的应用场景自然就是“生产者-消费者”模型的应用场合了。

4.Disruptor是LMAX在线交易平台的关键组成部分，
LMAX平台使用该框架对订单处理速度能达到600万TPS，除金融领域之外，
其他一般的应用中都可以用到Disruptor，它可以带来显著的性能提升。

5.其实Disruptor与其说是一个框架，不如说是一种设计思路，
这个设计思路对于存在“并发、缓冲区、生产者—消费者模型、事务处理”这些元素的程序来说，
Disruptor提出了一种大幅提升性能（TPS）的方案。

6.Disruptor的github主页：
https://github.com/LMAX-Exchange/disruptor

7.高效的原因
Disruptor 是在内存中以队列的方式去实现的，而且是无锁的。
这也是 Disruptor 为什么高效的原因。
```

##1.2 Disruptor 的核心概念
### 1.2.1 Ring Buffer
```
如其名，环形的缓冲区。
曾经 RingBuffer 是 Disruptor 中的最主要的对象，但从3.0版本开始，
其职责被简化为仅仅负责对通过 Disruptor 进行交换的数据（事件）进行存储和更新。
在一些更高级的应用场景中，Ring Buffer 可以由用户的自定义实现来完全替代。
```

### 1.2.2 Sequence Disruptor
```
通过顺序递增的序号来编号管理通过其进行交换的数据（事件），
对数据(事件)的处理过程总是沿着序号逐个递增处理。
一个 Sequence 用于跟踪标识某个特定的事件处理者( RingBuffer/Consumer )的处理进度。
虽然一个 AtomicLong 也可以用于标识进度，但定义 Sequence 来负责该问题还有另一个目的，
那就是防止不同的 Sequence 之间的CPU缓存伪共享(Flase Sharing)问题。
（注：这是 Disruptor 实现高性能的关键点之一，网上关于伪共享问题的介绍已经汗牛充栋，在此不再赘述）。
```

### 1.2.3 Sequencer
```
Sequencer 是 Disruptor 的真正核心。
此接口有两个实现类 SingleProducerSequencer、MultiProducerSequencer ，
它们定义在生产者和消费者之间快速、正确地传递数据的并发算法。
```

### 1.2.4 Sequence Barrier
```
用于保持对RingBuffer的 main published Sequence 和
Consumer依赖的其它Consumer的 Sequence 的引用。
Sequence Barrier 还定义了决定 Consumer 是否还有可处理的事件的逻辑。
```

### 1.2.5 Wait Strategy
```
定义 Consumer 如何进行等待下一个事件的策略。
（注：Disruptor 定义了多种不同的策略，针对不同的场景，提供了不一样的性能表现）
```

### 1.2.6 Event
```
在 Disruptor 的语义中，生产者和消费者之间进行交换的数据被称为事件(Event)。
它不是一个被 Disruptor 定义的特定类型，而是由 Disruptor 的使用者定义并指定。
```

### 1.2.7 EventProcessor
```
EventProcessor 持有特定消费者(Consumer)的 Sequence，
并提供用于调用事件处理实现的事件循环(Event Loop)。
```

### 1.2.8 EventHandler
```
Disruptor 定义的事件处理接口，由用户实现，用于处理事件，是 Consumer 的真正实现。
```

### 1.2.9 Producer
```
即生产者，只是泛指调用 Disruptor 发布事件的用户代码，Disruptor 没有定义特定接口或类型。
```



https://mp.weixin.qq.com/s?__biz=Mzg5NDM1ODk4Mw==&mid=2247541975&idx=2&sn=df26939d3a03894d20be2d5076a18617&chksm=c022a07ff7552969746087bf64b35d7e77986ba098ae5cc855605ee49ee6392eef17ef5119c3&mpshare=1&scene=24&srcid=0709I5Bfar2coYek9pYLcqZt&sharer_sharetime=1657329138276&sharer_shareid=9b286cadc70f5998699efc52a3cf3724#rd
https://blog.csdn.net/lijinqing39/article/details/100575167
https://blog.csdn.net/philip502/article/details/125332082
https://blog.csdn.net/boling_cavalry/article/details/117575447
https://blog.csdn.net/lijinqing39/article/details/100575167