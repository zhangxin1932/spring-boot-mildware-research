

## 1.2 log4j2 配置文件名称和加载顺序
```
使用log4j2之前需要在classpath下新建一个配置文件。
如果没有任何配置，log4j2将使用缺省配置。
需要注意的是 log4j 2.x 版本不再支持像 1.x 中的.properties后缀的文件配置方式，
2.x 版本配置文件后缀名只能为".xml",".json“或者”.jsn"。

系统选择配置文件的优先级(从先到后)如下：
classpath下的名为log4j2-test.json 或者log4j2-test.jsn的文件
classpath下的名为log4j2-test.xml的文件
classpath下名为log4j2.json 或者log4j2.jsn的文件
classpath下名为log4j2.xml的文件

我们一般默认使用log4j2.xml进行命名。
如果本地要测试，可以把log4j2-test.xml放到classpath，
而正式环境使用log4j2.xml，则在打包部署的时候不要打包log4j2-test.xml即可。
```

## 1.3 log4j2.xml配置文件说明
```
<configuration status="debug" monitorInterval="30">

根节点Configuration
有两个属性: status和monitorinterval, 
两个子节点: Appenders和Loggers(表明可以定义多个Appender和Logger)。

其中，status用来指定log4j本身的打印日志的级别。
monitorinterval用于指定log4j自动重新配置的监测间隔时间, 是用来设置配置文件的动态加载时间的，
单位是s, 最小是5s。monitorInterval="30"表示每30秒配置文件会动态加载一次。
在程序运行过程中，如果修改配置文件，程序会随之改变。

1.Appenders节点，常见的有三种子节点:Console、RollingFile、File。

● Console节点用来定义输出到控制台的Appender。
　(1) name:指定Appender的名字.
　(2) target:SYSTEM_OUT 或 SYSTEM_ERR,一般只设置默认:SYSTEM_OUT.
　(3) PatternLayout:输出格式，不设置默认为:%m%n.

● File节点用来定义输出到指定位置的文件的Appender.
　(1)name:指定Appender的名字.
　(2)fileName:指定输出日志的目的文件带全路径的文件名.
　(3)PatternLayout:输出格式，不设置默认为: %m%n.

●RollingFile节点用来定义日志文件超过指定大小自动删除旧的创建新的的Appender.
自动追加日志信息到文件中，直至文件达到预定的大小，然后自动重新生成另外一个文件来记录之后的日志。
　(1)name:指定Appender的名字.
　(2)fileName:指定输出日志的目的文件带全路径的文件名.
　(3)PatternLayout:输出格式，不设置默认为:%m%n.
　(4)filePattern:指定新建日志文件的名称格式.
　(5)Policies:指定滚动日志的策略，就是什么时候进行新建日志文件输出日志.
　(6)TimeBasedTriggeringPolicy: Policies子节点，基于时间的滚动策略，interval属性用来指定多久滚动一次，默认是1hour。modulate=true用来调整时间：比如现在是早上3am，interval是4，那么第一次滚动是在4am，接着是8am，12am…而不是7am.
　(7)SizeBasedTriggeringPolicy:Policies子节点，基于指定文件大小的滚动策略，size属性用来定义每个日志文件的大小.
　(8)DefaultRolloverStrategy:用来指定同一个文件夹下最多有几个日志文件时开始删除最旧的，创建新的(通过max属性)。

2.Loggers节点，常见的有两种子节点:Root和Logger.

● Root节点用来指定项目的根日志，如果没有单独指定Logger，那么就会默认使用该Root日志输出
　　○ level:日志输出级别，共有8个级别，按照从低到高为：All < Trace < Debug < Info < Warn < Error < Fatal < OFF.
　　○ appender-ref：Root的子节点，用来指定该日志输出到哪个Appender.

● Logger节点用来单独指定日志的形式，比如要为指定包下的class指定不同的日志级别等。
　　○ level:日志输出级别，共有8个级别，按照从低到高为：All < Trace < Debug < Info < Warn < Error < Fatal < OFF.
　　○ name:用来指定该Logger所适用的类或者类所在的包全路径,继承自Root节点.
　　○ appender-ref：Logger的子节点，用来指定该日志输出到哪个Appender,如果没有指定，就会默认继承自Root.如果指定了，那么会在指定的这个Appender和Root的Appender中都会输出，此时我们可以设置Logger的additivity="false"只在自定义的Appender中进行输出。
```

https://logging.apache.org/log4j/2.x/
https://blog.csdn.net/CPOHUI/article/details/106719309
