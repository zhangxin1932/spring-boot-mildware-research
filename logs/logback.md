
## 1.2 Logback 在启动时，根据以下步骤寻找配置文件：
```
1. 在 classpath 中寻找 logback-test.xml文件
2. 如果找不到 logback-test.xml，则在 classpath 中寻找 logback.groovy 文件
3. 如果找不到 logback.groovy，则在 classpath 中寻找 logback.xml 文件
4. 如果上述的文件都找不到，则 logback 会使用 JDK 的 SPI 机制查找 
META-INF/services/ch.qos.logback.classic.spi.Configurator 中的 logback 配置实现类， 
这个实现类必须实现 Configuration 接口，使用它的实现来进行配置
5. 如果上述操作都不成功，logback 就会使用它自带的 BasicConfigurator 来配置，
并将日志输出到 console
```

## 1.3 logback的变量作用于有三种：local，context，system：
```
1. local 作用域在配置文件内有效；
2. context 作用域的有效范围延伸至 logger context；
3. system 作用域的范围最广，整个 JVM 内都有效

logback 在替换变量时，首先搜索 local 变量，然后搜索 context，然后搜索 system，
在spring项目中，应将变量的作用域设置为context，并交给spring控制
```

