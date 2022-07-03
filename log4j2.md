#1.pom依赖引入
```
<properties> 
    <log4j2.version>2.7</log4j2.version>
</properties> 

<dependencies> 
    <dependency> 
        <groupId>org.apache.logging.log4j</groupId> 
        <artifactId>log4j-api</artifactId>
        <version>${log4j2.version}</version> 
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>${log4j2.version}</version>
    </dependency> 
</dependencies> 
```



#2.src目录下加入log4j2.xml，配置好相关属性；
需用的代码中加入static Logger sLogger = LogManager.getLogger(LogManage.class.getName()); sLogger.debug(strLog);即可调用使用。

#3.log4j2.xml注释说明：
```
root标签为log的默认输出形式，如果一个类的log没有在loggers中明确指定其输出lever与格式，那么就会采用root中定义的格式。
Appenders标签，其实就是输出，有各种扩展组件，主要类型有：
ConsoleAppender   输出结果到控制台
FileAppender  输出结果到指定文件
RollingFileAppender   同样输出结果到文件，区别是用一个buffer，因此速度会快点
这里介绍下RollingRandomessFile 的相关属性：
name：表示该appender的名称
fileName：表示输出的文件的路径
append：是否追加，true表示追加内容到所在的日志，false表示每次都覆盖
filePattern：表示当日志到达指定的大小或者时间，产生新日志时，旧日志的命名路径。
PatternLayout：和log4j1一样，指定输出日志的格式
Policies：策略，表示日志什么时候应该产生新日志，可以有时间策略和大小策略等
ThresholdFilter ：过滤器，如果你要选择控制台只能输出ERROR以上的类别，你就用ThresholdFilter，把level设置成ERROR，onMatch="ACCEPT" onMismatch="DENY" 的意思是匹配就接受，否则直接拒绝
```


#4.log4j2.xml 示例配置文件
```
<!-- web项目 （${sys:catalina.home} 是tomcat下的根目录） -->
<?xml version="1.0" encoding="UTF-8"?>  
<Configuration>  
    <Appenders>  
    <!-- 输出到控制台 -->  
        <Console name="CONSOLE" target="SYSTEM_OUT">  
            <!-- 输出格式 -->  
            <PatternLayout pattern="ROOT:%d %-5p %C %L - %m%n" />  
        </Console>  
        <!-- 按天备份一个日志 -->  
        <!-- fileName为生成的文件名，x为路径，也可以采用相对路径模式，filePattern为时间到达后产生新日志，旧日志的文件名 -->  
        <RollingFile name="TASK" fileName="x:/xxxx/logs.log"  
            filePattern="x:/xxxx/logs_%d{yyyy-MM-dd}.log" >   
            <!-- 输出格式 -->  
            <PatternLayout pattern="%d %-5p %m%n" />  
            <Policies>  
            <!-- 每1天更新一次，此处查阅网上和官方示例中，都是以小时出现，我测试是以天为单位。（官方文档中说明按item类型是否是小时，但没找到在哪里设置item类型）另有其他各类型策略，请参阅官方文档 --> 
<!-- TimeBasedTriggeringPolicy需要和filePattern配套使用，由于filePattern配置的时间最小粒度是dd天，所以表示每一天新建一个文件保存日志。SizeBasedTriggeringPolicy表示当文件大小大于指定size时，生成新的文件保存日志。 --> 
                <TimeBasedTriggeringPolicy modulate="true"  
                    interval="1" />  
                <!-- 此处为每个文件大小策略限制，使用它一般会在文件中filePattern采用%i模式 -->  
                <!-- <SizeBasedTriggeringPolicy size="128KB" /> -->  
            </Policies>  
            <!-- 最多备份30天以内的日志，此处为策略限制，Delete中可以按自己需要用正则表达式编写 -->  
        <!-- DefaultRolloverStrategy字段中加入max=“30”经测试是配合SizeBasedTriggeringPolicy限制%i的存在数量，并没有发现是网上流传的是最多保存多少个文件的限制，也或许是我写的有问题 -->  
         <DefaultRolloverStrategy>  
        <Delete basePath="x:/xxxx" maxDepth="1">  
                     <IfFileName glob="logs_*.log" />  
                     <IfLastModified age="30d" />  
            </Delete>  
            </DefaultRolloverStrategy>  
        </RollingFile>  
    </Appenders>  
    <!-- 链接到Console和RollingFile标签实现debug以上等级的输出 -->  
    <loggers>    
    <root level="debug">    
        <appender-ref ref="CONSOLE"/>    
        <AppenderRef ref="TASK" />  
    </root>    
    </loggers>    
</Configuration>  
```
 