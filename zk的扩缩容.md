> zk迁移(扩容/缩容)实践

#1.背景
```
由于一些历史原因，目前zookeeper集群（3个节点）和elasticsearch、hadoop部署在一起，导致几个组件相互影响，性能也逐渐下降。
甚至出现某个组件异常（例如oom）导致了其他组件不可用。
由于大量项目使用dubbo且依赖zookeeper作为注册中心，zookeeper的不稳定可能是致命的，
所以计划先将zookeeper迁出到3台独立的节点，在此记录下迁移方案。 
```

#2.zookeeper选举原理
```
在迁移前有必要了解zookeeper的选举原理，以便更科学的迁移。
```

##2.1 快速选举FastLeaderElection
```
zookeeper默认使用快速选举，在此重点了解快速选举：
-- 向集群中的其他zk建立连接，并且只有myid比对方大的连接才会被接受（也就是每2台只会有1个连接，避免连接浪费）
-- 每台zk默认先投自己，然后向集群广播自己的选票
-- 收到对方的选票时，依次比较epoch（选举轮数）、zxid（事务id）、myid，较大者胜出，更新选票并广播
-- 如果收到的选票中有某个节点超过集群半数，则胜出当选为leader，其他节点为follower
```

##2.2 注意事项
###2.2.1 zookeeper集群的数量应为奇数：
```
因为根据paxos理论，只有集群中超过半数的节点还存活才能保证集群的一致性。
假如目前集群有5个节点，我们最多允许2个节点不可用，因为3>5\2。
当集群扩容到6个节点的时候，我们仍然只能最多允许2个节点不可用，
到3个节点不可用时，将不满足paxos理论，因为3>6\2不成立。
也就是说当集群节点数n为偶数时，其可用性与n-1是一样的，那我们何必多浪费一台机器呢？
```

###2.2.2 尽量按照 myid 配置的有小到大依次启动
```
由于zookeeper只允许mid大的节点连接到mid小的节点，
我们启动zookeeper的顺序应该按照myid小的到myid大的，最后再启动leader节点！
```

###2.2.3 滚动升级
```
迁移过程中要保证原zookeeper集群还是能提供服务，
新zookeeper集群同步老集群的数据，
将zookeeper url指向新集群的3个节点，
停掉老zookeeper集群。

相当于先扩容zookeeper,然后缩容zookeeper…
```

#3.迁移步骤
```
原有zookeeper集群(server1、server2、server3)zoo.cfg配置如下:
# 省略其他配置
dataDir=/data
server.1=node1:3181:4181
server.2=node2:3181:4181
server.3=node3:3181:4181

使用命令：echo srvr | nc node{?} 2181检查谁是leader({?}依次替换为1、2、3)
ps：也可以用echo stat | nc node{?} 2181显示更详细信息
这里假设leader为node2.（按照正常情况，leader也理应是node2）
```

##3.1 步骤1：新增节点4
```
1.在/data目录创建myid文件，内容为4

2.配置zoo.cfg,内容如下：
# 省略其他配置
dataDir=/data
server.1=node1:3181:4181
server.2=node2:3181:4181
server.3=node3:3181:4181
server.4=node4:3181:4181

3.启动zookeeper：{zookeeperDir}/bin/zkServer.sh start

4.检查所有节点是否提供服务，且集群中只有一个leader，例如以下命令:
$ echo srvr | nc node1 2181
Zookeeper version: 3.4.5-cdh5.7.0--1, built on 03/23/2016 18:30 GMT
Latency min/avg/max: 0/6/190
Received: 16002
Sent: 19874
Connections: 1
Outstanding: 0
Zxid: 0x3b00004872
Mode: leader
Node count: 334
可以看到Mode表示该节点的角色为leader。依次检查每一个节点,如果没有响应，或者出现多个leader，需要还原整个集群！
```

##3.2 新增节点5
```
1.在/data目录创建myid文件，内容为5

2.配置zoo.cfg,内容如下：
# 省略其他配置
dataDir=/data
server.1=node1:3181:4181
server.2=node2:3181:4181
server.3=node3:3181:4181
server.4=node4:3181:4181
server.5=node5:3181:4181

3.启动zookeeper：{zookeeperDir}/bin/zkServer.sh start

4.检查所有节点是否提供服务，且集群中只有一个leader，例如以下命令:
$ echo srvr | nc node1 2181
...
$ echo srvr | nc node2 2181
...
$ echo srvr | nc node3 2181
...
$ echo srvr | nc node4 2181
...
$ echo srvr | nc node5 2181
...
```

##3.3 新增节点6
```
1.在/data目录创建myid文件，内容为6

2.配置zoo.cfg,内容如下：
# 省略其他配置
dataDir=/data
server.1=node1:3181:4181
server.2=node2:3181:4181
server.3=node3:3181:4181
server.4=node4:3181:4181
server.5=node5:3181:4181
server.6=node6:3181:4181

3.启动zookeeper：{zookeeperDir}/bin/zkServer.sh start

4.检查所有节点是否提供服务，且集群中只有一个leader，例如以下命令:
$ echo srvr | nc node1 2181
...
$ echo srvr | nc node2 2181
...
$ echo srvr | nc node3 2181
...
$ echo srvr | nc node4 2181
...
$ echo srvr | nc node5 2181
...
$ echo srvr | nc node6 2181
...
```

##3.4 更新节点4
```
1.修改节点4的配置如下：
# 省略其他配置
dataDir=/data
server.1=node1:3181:4181
server.2=node2:3181:4181
server.3=node3:3181:4181
server.4=node4:3181:4181
server.5=node5:3181:4181
server.6=node6:3181:4181
  
2.重启节点4的zookeeper：{zookeeperDir}/bin/zkServer.sh start

3.检查所有节点是否提供服务，且集群中只有一个leader，例如以下命令:
$ echo srvr | nc node1 2181
...
$ echo srvr | nc node2 2181
...
$ echo srvr | nc node3 2181
...
$ echo srvr | nc node4 2181
...
$ echo srvr | nc node5 2181
...
$ echo srvr | nc node6 2181
...
```

##3.5 更新节点5
```
同步骤4
```

##3.6 更新老集群节点1
```
1.修改节点1的配置如下：
# 省略其他配置
dataDir=/data
server.1=node1:3181:4181
server.2=node2:3181:4181
server.3=node3:3181:4181
server.4=node4:3181:4181
server.5=node5:3181:4181
server.6=node6:3181:4181

2.重启节点1的zookeeper：{zookeeperDir}/bin/zkServer.sh restart

3.检查所有节点是否提供服务，且集群中只有一个leader:
$ echo srvr | nc node1 2181
...
$ echo srvr | nc node2 2181
...
$ echo srvr | nc node3 2181
...
$ echo srvr | nc node4 2181
...
$ echo srvr | nc node5 2181
...
$ echo srvr | nc node6 2181
...
```

##3.7 更新老集群节点3
```
同步骤6
```

##3.8 更新老集群节点2
```
最后更新leader节点：node2，同步骤6
ps:这时候如果没有读写zookeeper操作，集群的leader将变为节点6（因为节点6的myid最大）
```

##3.9 将原有zookeeper的url指向新的节点
```
运维修改nginx配置，zookeeper url（例如pro1.zookeeper.so、pro2.zookeeper.so、pro3.zookeeper.so）指向node4，node5，node6

相关业务系统重启（避免cdh缓存）
```

##3.10 老zookeeper集群下线
```
这一步需要等待所有的业务系统都重启之后。

这时候还是得一台一台关闭（下线），因为假如同时关闭node1和node2，
那当重启node3的时候集群将不可用（没有超过集群半数的节点存活）
```

###3.10.1 下线zookeeper老集群中的节点1
```
1.关闭node1: {zookeeperDir}/bin/zkServer.sh stop

2.依次修改node2，3，4，5，6的配置，并且重启，配置如下：
# 省略其他配置
dataDir=/data
server.2=node2:3181:4181
server.3=node3:3181:4181
server.4=node4:3181:4181
server.5=node5:3181:4181
server.6=node6:3181:4181

3.重启后检查所有节点是否提供服务，且集群中只有一个leader。
ps:这时候如果没有读写zookeeper操作，leader将变成node5，
因为node6节点重启的时候，集群重新选举，node5的myid最大
```

###3.10.2 下线zookeeper老集群中的节点2
```
1.关闭node2: {zookeeperDir}/bin/zkServer.sh stop

2.依次修改node3，4，5，6的配置，并且重启，配置如下：
# 省略其他配置
dataDir=/data
server.3=node3:3181:4181
server.4=node4:3181:4181
server.5=node5:3181:4181
server.6=node6:3181:4181

3.重启后检查所有节点是否提供服务，且集群中只有一个leader。
ps:这时候如果没有读写zookeeper操作，leader将重新变成node6
```

###3.10.3 下线zookeeper老集群中的节点3
```
1.关闭node3: {zookeeperDir}/bin/zkServer.sh stop

2.依次修改node4，5，6的配置，并且重启，配置如下：
# 省略其他配置
dataDir=/data
server.4=node4:3181:4181
server.5=node5:3181:4181
server.6=node6:3181:4181

3.重启后检查所有节点是否提供服务，且集群中只有一个leader。
ps:这时候如果没有读写zookeeper操作，node5将成为最终的leader
```


```参考资料```
https://zacard.net/2017/08/29/howto-zookeeper-move/
