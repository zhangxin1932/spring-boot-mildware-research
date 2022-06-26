#1.solr 简介

## 1.1 Solr是什么

```
Solr是Apache旗下基于Lucene开发的全文检索的服务。用户可以通过http请求，向Solr服务器提交一定格式的数据(XMLJSON)，完成索引库的索引。也可以通过Http请求查询索引库获取返回结果(XMLJSON)。
```

## 1.2 Solr和Lucene的区别

```Lucene是一个开放源代码的全文检索引擎工具包，它不是一个完整的全文检索引擎，Lucene提供了完整的
查询引擎和索引引擎，目的是为软件开发人员提供一个简单易用的工具包，以方便的在目标系统中实现全文检索的功能，或者以Lucene为基础构建全文检索引擎。
Solr的目标是打造一款企业级的搜索引擎系统，它是一个搜索引擎服务，可以独立运行，通过Solr可以非常快速的构建企业的搜索引擎，通过Solr也可以高效的完成站内搜索功能。
```

## 1.3 solr 的发展历程

```
2004年，CNET NetWorks公司的Yonik Seeley工程师为公司网站开发搜索功能时完成了Solr的雏形。起初Solr只是CNET公司的内部项目。
2006年1月，CNET公司决定将Solr源码捐赠给Apache软件基金会。
2008年9月，Solr1.3发布了新功能，其他包括分布式搜索和性能增强等功能。
2009年11月，Solr1.4版本发布，此版本对索引，搜索，Facet等方面进行优化，提高了对PDF，HTML等富文本文件处理能力，还推出了许多额外的插件;
2010年3月，Lucene和Solr项目合并，自此,Solr称为了Lucene的子项目，产品现在由双方的参与者共同开发。
2011年，Solr改变了版本编号方案，以便与Lucene匹配。为了使Solr和Lucene有相同的版本号，Solr1.4下一版的版本变为3.1。
2012年10月，Solr4.0版本发布，新功能Solr Cloud也随之发布。
```

## 1.4 solr 的功能

```
灵活的查询语法;
支持各种格式文件(Word，PDF)导入索引库;
支持数据库数据导入索引库;
分页查询和排序
Facet维度查询;
自动完成功能;
拼写检查;
搜索关键字高亮显示;
Geo地理位置查询;
Group 分组查询;
solr Cloud (即 solr 集群模式);
```

## 1.5 Schema文件设计的注意事项
```
indexed属性︰
设置了indexed=true的域要比indxed=false的域，索引时占用更多的内存空间。
而且设置了index=true的域所占用磁盘空间也比较大，所以对于一些不需要搜索的域，
我们需要将其indexed属性设置为false。比如:图片地址。

omitNorms属性:
如果我们不关心词在文档中出现总的次数，影响最终的相关度评分可以设置
omitNorms=true。它可以减少磁盘和内存的占用，也可以加快索引时间。

omitPosition属性:
如果我们不需要根据该域进行高亮，可以设置成true，也可以减少索引体积。

omitTermFreqAndPositions属性:
如果我们只需要利用倒排索引结构根据指定Term找到相应
Document，不需要考虑词的频率，词的位置信息，可以将该属性设置为true，也可以减少索引体积。

stored属性︰
如果某个域的值很长，比如想要存储一本书的信息，
首先我们要考虑该域要不要存储。如果我们确实想要在Solr查询中能够返回该域值，此时可以考虑使用ExtemalFileField域类型。
如果我们存储的域值长度不是很大，但是希望降低磁盘的IO。
可以设置compressed=true即启用域值数据压缩，域值数据压缩可以降低磁盘IO，同时会增加CPU执行开销。
如果你的域值很大很大，比如是一个几十MB PDF文件的内容，
此时如果你将域的stored属性设置为true存储在Solr中，它不仅会影响你的索引创建性能，
还会影响我们的查询性能，如果查询时，f1参数里返回该域，那对系统性能打击会很大，
如果了使用ExtemalFileField域类型，该域是不支持查询的，只支持显示。
通常我们的做法是将该域的信息存储到Redis,Memcached缓存数据库。
当我们需要该域的数据时，我们可以根据solr中文档的主键在缓存数据库中查询。

multiValued属性:
如果我们某个域的数据有多个时，通常可以使用multiValued属性。
eg:鞋子的尺码，鞋子的尺码可能会有多个36，38，39，40，41...。
就可以设置multiValued属性为true。
但是有些域类型是不支持multiValued属性，eg:地理位置查询LatLonType;
Group查询也不支持multiValued属性为true的域。
我们可以考虑使用内嵌的Document来解决次问题。可以为该文档添加多个子文档。

#日期类型
对于日期类型的数据，在Solr中我们强烈建议使用solr中提供的日期类型域类来存储。
不建议使用string域类型来存储。否则是无法根据范围进行查询。

#其余类型
Solr官方提供的schema示例文件中定义了很多
<dynamicField>、<copyField>以及<fieldType>，
上线后这里建议你将它们都清理掉，<fieldType>只保留一些基本的域类型
string、booleanpint plong pfloat pdate即可，
一定保留内置的_version_,_root_域，否则一些功能就不能使用。
比如删除_root_就无法创建内嵌的document;
```

#2.solr 基础知识

## 2.1 solr core

```
#引子
solr部署启动成功之后，需要创建core才可以使用的，才可以使用solr;类似于我们安装完毕MySQL以后，需要创建数据库一样

#概念
在Solr中、每一个Core、代表一个索引库、里面包含索引数据及其配置信息。
Solr中可以拥有多个Core、也就是可以同时管理多个索引库、就像mysql中可以有多个数据库一样。所以SolrCore可以理解成MySQL中的数据库;

#solr目录结构
conf:存储SolrCore相关的配置文件;
data:SolrCore的索引数据;
core.properties: SolrCore的名称,name=SolrCore名称;
所以搭建一个SolrCore只需要创建2个目录和一个properties文件即可;
```

# 3.solr 的安装及配置

```安装使用```

https://solr.apache.org/guide/8_11/solr-tutorial.html

```环境准备```

```
前置条件: 
>> JDK8及以上
```

```下载对应的安装包 ```

https://solr.apache.org/downloads.html

## 3.1 linux 上安装 solr

### 3.1.1 上传到 linux 服务器并解压

```
~$ unzip -q solr-8.11.0.zip

~$ cd solr-8.11.0/
```

### 3.1.2 启动 solr

```
>> 如果是非root用户, 直接执行
./bin/solr start

>> 如果是 root 用户, 则执行命令:
./bin/solr start -force
```

### 3.1.3 通过 solr 管理台访问界面

http://127.0.0.1:8983/solr/#/

## 3.2 windows 上安装 solr

```
solr的启动、停止、查看命令：
1. 启动：bin\solr.cmd start
2. 停止：bin\solr.cmd stop 或bin\solr.cmd stop -all
3. 查看：bin\solr.cmd status
```

## 3.99 对 core 进行管理
### 3.99.1 新建 core

### 3.99.2 删除 core
```
删除 core 时, 不能只删除目录文件, 最好命令行删除.
 cd /usr/local/solr-8.10.1/bin
 ./solr delete -c new_core

否则, solr 管理台, 可能出现如下错误:
new_core: org.apache.solr.common.SolrException:org.apache.solr.common.SolrException: Could not load conf for core new_core: Error loading solr config from /data/solr/data/new_core/conf/solrconfig.xml
```

https://www.jianshu.com/p/4570d647c9a3

# 4.solr-admin 的使用

```
1.DashBoard:solr的版本信息、jvm的相关信息还有一些内存信息。
2.Logging:日志信息，也有日志级别，刚进入查看的时候肯定是有几个警告(warn）信息的，因为复制solr的时候路径发生了变化导致找不到文件，但是并不影响。
3.Core Admin:SolrCore的管理页面。可以使用该管理界面完成SolrCore的卸载。也可以完成SolrCore的添加能添加的前提，SolrCore在solr_home中目录结构是完整的。
4.Java Properties:顾名思义，java的相关配置，比如类路径，文件编码等。
5.Thread Dump:solr服务器当前活跃的一些线程的相关信息。

以上的5个了解一下就行。
通过core admin 创建了一个 core 之后, 左侧菜单栏就会出现一个core selector的选择框, 当选择某一个solrCore以后，又会出现一些菜单，这些菜单就是对选择的SolrCore进行操作的，这些菜单的解读如下文.
```

## 4.1 Documents

```
作用:向SolrCore中添加数据，删除数据，更新数据(索引)。
document是lucene进行索引创建以及搜索的基本单元，我们要把数据添加到Lucene的索引库中，数据结构就是document，
如果我从Lucene的索引库中进行数据的搜索，搜索出来的结果的数据结构也document;

文档的结构:
一个文档是由多个域(Field)组成，每个域包含了域名和域值;如果数据库进行类比，文档相单于数据库中的一行记录，域(Field）则为一条记录的字段。
```

##  4.2 Analyse

```
作用:测试域/域类型分词效果;
当我们向Solr中添加一个文档，底层首先要对文档域中的数据进行分词。建立词和文档关系。

思考的问题?
id为什么不分词,name域为什么可以分词?name域可以对中文进行分词吗?
添加文档的时候，域名可以随便写吗?
```

## 4.3 Solr的配置-Field

```
在Solr中我们需要学习其中4个配置文件:
SolrHome中solr.xml
SolrCore/conf中solrconfig.xml
SolrCore/confSolrCore中managed-schema
SolrCore/conf/data-config.xml
其中我们最常用的一个配置文件，managed-schema。
在Solr中进行索引时，文档中的域需要提前在managed-schema文件中定义，在这个文件中，solr已经提前定义了一些域，比如我们之前使用的id,price,title域。通过管理界面查看已经定义的域;
```

### 4.3.1 基本属性

```
#field标签:定义一个域;
#name属性:域名
#indexed:是否索引，是否可以根据该域进行搜索;一般哪些域不需要搜索，图片路径。
#stored:是否存储，将来查询的文档中是否包含该域的数据;商品的描述。

举例:将图书的信息存储到Solr中; Description域。indexed设置为true，store设置成false
可以根据商品描述域进行查询，但是查询出来的文档中是不包含description域的数据;

#multiValued:是否多值，该域是否可以存储一个数组;图片列表;
#required:是否必须创建文档的时候，该域是否必须
#type:域类型，决定该域使用的分词器。分词器可以决定该域的分词效果(分词，不分词
是否支持中文分词)。域的类型在使用之前必须提前定义;在solr中已经提供了很多的域类型(fieldType)
```

## 4.4 Solr的配置-FieldType介绍

```
每个域都需要指定域类型，而且域类型必须提前定义。域类型决定该域使用的索引和搜索的分词器，影响分词效果。
Solr中已经提供好了一些域类型;
text_general:支持英文分词，不支持中文分词;
string:不分词;适合于id,订单号等。
pfloat:适合小数类型的域，特殊分词，支持大小比较;
pdate:适合日期类型的域，特殊分词，支持大小比较;
pint:适合整数类型的域，特殊分词，支持大小比较;
plong:适合长整数类型的域，特殊分词，支持大小比较;

可以参考text_general是如何定义FiledType来自定义FieldType的.
```

### 4.4.1 相关属性

```
name:域类型名称，定义域类型的必须指定，并且要唯一;将来定义域的时候需要指定通过域名称来指定域类型;
class:域类型对应的java类，必须指定，如果该类是solr中的内置类，使用solr.类名指定即可。如果该类是第三方的类，需要指定全类名。
如果class是TextField，我们还需要使用<analyzer>子标签来配置分析器;
positionIncrementGap:用于多值字段，定义多值间的间隔，来阻止假的短语匹配（了解
autoGeneratePhraseQueries:用于文本字段，如果设为true，solr会自动对该字段的查询生成短语查询，即使搜索文本没带""
enableGraphQueries:是否支持图表查询
docValuesFormat: docValues字段的存储格式化器: schema-aware codec，配置在solrconfig.xml中的
postingsFormat:词条格式器: schema-aware codec，配置在solrconfig.xml中的
```

### 4.4.2 Solr自带的FieldType类

```
solr除了提供了TextField类，我们也可以查看它提供的其他的FiledType类，我们可以通过官网查看其他的FieldType类的作用
```

## 4.5 分析器

```
分析器就是将用户输入的一串文本分割成一个个token，一个个token组成了tokenStream,然后遍历
tokenStream对其进行过滤操作，比如去除停用词,特殊字符，标点符号和统一转化成小写的形式等。
分词的准确的准确性会直接影响搜索的结果，从某种程度上来讲，分词的算法不同，都会影响返回的结果
因此分析器是搜索的基础;

一个分析器只能有一个分词器, 但可以包含多个过滤器.

分析器的工作流程:
>> 分词
>> 过滤
```

# 5.全量导入与增量导入数据

## 5.1 创建solr-core

```
1.先点击Add Core，如果报错:  Can't find resource 'solrconfig.xml'。

2.首先去目录 D:\solr-8.11.1\server\solr\ 下 创建一个名字为 first_core 的文件夹

3.然后拷贝 D:\solr-8.11.1\server\solr\configsets\_default 下的conf目录拷贝到 first_core 目录下

4.然后再点击创建即可
```

## 5.2 

# 6. solr 的常用命令

## 6.1 查询命令
http://localhost:8983/solr/my_first_core/select?indent=true&q.op=OR&q=*%3A*

上述链接地址中:
my_first_core: 表示 solrcore 或 collection
select: 表示请求处理器
?indent=... : 表示查询参数

如果加了双引号, 即表示为短语搜索, 不分词


```
solr的查询解析是通过queryParser来配置的（solrconfig.xml），一般我们用默认的即可。其各参数含义与用法简单解释如下：
q：查询输入，必须。可以使用运算符
fq：过滤查询。可以使用运算符
sort：排序的字段，格式为field score，多个字段之间用逗号或空格隔开，比如sum(x_f, y_f) desc, price asc，默认是score desc
start：从哪一行开始
rows：获取多少行
fl：查询要输出的字段，字段之间用逗号隔开，比如title,price,seller，星号代表所有，默认就是星号。
df:定义查询时默认的查询field, 默认的查询字段，一般默认指定。
wt：返回的数据类型，可以是JSON、XML、python、ruby、php、csv等格式。
indent：true/false，返回的XML格式是否需要缩进(格式化展示)，默认为false
debugQuery：调试查询，会输出查询过程的一些参数。
qt:（query type）指定那个类型来处理查询请求，一般不用指定，默认是standard。
 
高亮相关：
高亮是通过searchComponent来配置的，在solrconfig.xml中配置名为highlight的searchComponent即可，默认的实现是solr.HighlightComponent。
hl：true/false,是否高亮显示
hl.fl：高亮显示的字段
hl.example.pre：高亮显示的前缀
hl.exapmle.post：高亮显示的后缀
hl.requireFieldMatch：是否只在查询指定的field（只有当hl.usePhraseHighlighter为true时才生效）高亮显示，默认是在所有field都高亮
hl.usePhraseHighlighter：true/false,使用SpanScorer高亮查询短语
hl.highlightMultiTerm：true/false,如果SpanScorer被启用，这个参数针对前缀/模糊/范围/通配符等开启高亮显示
 
facet：true/false
facet是solr的高级搜索功能之一，可以给用户提供更友好的搜索体验（类似于面包屑导航的功能）。在搜索关键字的同时,能够按照 facet指定的字段进行分组统计。比如商品的分类、商品的规格等。facet的字段必须被索引，无须分词（分词意义不大），也无须存储。详细可参考《Solr的facet查询》
facet的查询结果返回字段为facet_counts，与responseHeader、response同级。
facet.query：类似于filter的语法，对任意字段进行筛选
facet.field：需要进行facet的字段
facet.prefix：对facet字段的前缀过滤
facet.sort：true/false，对facet以哪种顺序返回，true为按照count值从大到小排序，默认为true
 
spellcheck：拼写检查
spellcheck是通过component的方式实现的，你可以在solrconfig.xml文件中配置searchComponent来完成拼写检查的功能，默认的实现是solr.SpellCheckComponent，具体的配置参数和实现原理可以看这里《spellCheckComponent》
 
spatial：空间搜索
spatial是专门针对空间数据进行搜索的，空间位置的索引和存储fieldType是LatLonType或者SpatialRecursivePrefixTreeFieldType，通过使用空间搜索，你可以对点、面等数据建立索引，以圆形、方形或其他形状进行范围搜索，以及对搜索结果按距离排序等等，具体的配置参数和实现原理可以看这里《SpatialSearch》
 
 
检索运算符：
冒号":"： field:value结构查询，表示字段field值为value的查询。
通配符：？(任意一个字符) *(任意字符)
布尔操作：AND(并且，同&&) OR(或者，同||) +(包含) -(不包含) NOT(同!)，注意AND、OR、NOT均为大写
范围：[A TO B](从A到B之间，包含A和B,注意TO大写)，{A TO B}（从A到B之间，不包含A和B,注意TO大写）
子运算：()括号跟数学表达式上的差不多，比如：(瓜 OR 傻) AND 男人
模糊检索：~表示模糊检索，比如：roam~将找到形如foam和roams的单词；roam~0.8，检索返回相似度在0.8以上的记录
控制相关度：^表示相关度，如检索jakarta apache，同时希望让”jakarta”的相关度更加好，那么在其后加上”^”符号和增量值，即jakarta^4 apache
?	通配符，替代任意单个字符(不能在检索的项开始使用*或者?符号)
*	通配符，替代任意多个字符(不能在检索的项开始使用*或者?符号)
~	表示相似度查询，如查询类似于"roam"的词，我们可以这样写: roam~将找到形如foam和roams的单词;roam~0.8，检索返回相似度在0.8以上的文档。邻近检索，如检索相隔10个单词的"apache"和"jakarta","jakarta apache"~10
AND	表示且，等同于“&&"
OR	表示或，等同于“||"
NOT	表示否
()	用于构成子查询
[]	范围查询，包含头尾
{}	范围查询，不包含头尾
+	存在运算符，表示文档中必须存在“+”号后的项
-	不存在运算符，表示文档中不包含“”号后的项

[?]查询book_name中包含c?的词。
http://localhost:8080/solr/co1lection1/select?q=book_name:c?

[*]查询book_name总含spring*的词
http://localhost:8080/solr/co1lection1/select?q=book_name:spring*

[~]模糊查询book_name中包含和java及和java比较像的词，相似度0.75以上
http://localhost:8080/solr/co1lection1/select?q=book_name:java~0.75
java和jave相似度就是0.75.4个字符中3个相同。

[AND]查询book_name域中既包含servlet又包含jsp的文档;
方式1:使用and
q=book_name:servlet AND book_name:jsp
q=book_name:servlet && book_name:jsp
方式2:使用+
q=+book_name:servlet +book_name:jsp

[OR]查询book_name域中既包含servlet或者包含jsp的文档;
q=book_name:servlet OR book_name:jsp
q=book_name:servlet || book_name:jsp

[NOT]查询boo_name域中包含spring，并且id不是44的文档
book_name:spring AND NOT id:44
+book_name:spring -id:44

[范围查询]
查询商品数量>=4并且<=10
http://loca1host:8080/so1r/co1lection1/select?q=book_num:[4 To 10]
查询商品数量>4并且<10,
http://loca7host:8080/solr/co1lection1/select?q=book_num:{4 To 10}
查询商品数量大于125
http://1oca7host:8080/solr/collection1/select?q=book_num:{125 To *]
```

## 6.2 常见查询

```
1.查询所有
http://localhost:8080/solr/primary/select?q=*:*

2.限定返回字段, 表示：查询所有记录，只返回productId字段
http://localhost:8080/solr/primary/select?q=*:*&fl=productId

3.分页. 表示：查询前六条记录，只返回productId字段
http://localhost:8080/solr/primary/select?q=*:*&fl=productId&rows=6&start=0


4. 增加限定条件
表示：查询category=2002、en_US_city_i=110以及namespace=d的前六条记录，只返回productId和category字段
http://localhost:8080/solr/primary/select?q=*:*&fl=productId&rows=6&start=0&fq=category:2002&fq=namespace:d&fl=productId+category&fq=en_US_city_i:1101

5.添加排序
表示：查询category=2002以及namespace=d并按category_2002_sort_i升序排序的前六条记录，只返回productId字段
http://localhost:8080/solr/primary/select?q=*:*&fl=productId&rows=6&start=0&fq=category:2002&fq=namespace:d&sort=category_2002_sort_i+asc


6. facet查询, 现实分组统计结果
http://localhost:8080/solr/primary/select?q=*:*&fl=productId&fq=category:2002&facet=true&facet.field=en_US_county_i&facet.field=en_US_hotelType_s&facet.field=price_p&facet.field=heatRange_i

http://localhost:8080/solr/primary/select?q=*:*&fl=productId&fq=category:2002&facet=true&facet.field=en_US_county_i&facet.field=en_US_hotelType_s&facet.field=price_p&facet.field=heatRange_i&facet.query=price_p:[300.00000+TO+*]

7.fl 也可以返回伪域并给域起别名.
将返回结果中价格转化为分。product 是Solr中的一个函数，表示乘法。
http://locaThost:8080/so1r/collection1/select?q=*:*&fl=*,aliasMoney:product(book_price,100)

8.在Solr中我们是通过sort参数进行排序。
http://loca1host:8080/so1r/co1lection1/select?q=*:*&sort=book_price+asc&wt=json&rows=50
特殊情况:某些文档book_price域值为null,null值排在最前面还是最后面。
定义域类型的时候需要指定2个属性sortMissingLast,sortMissingFirst
sortMissingLast=true,无论升序还是降序，null值都排在最后
sortMissingFirst=true,无论升序还是降序，null值都排在最前
<fieldtype name="fieldName" class="xx" sortMissingLast="true" sortMissingFirst="false"/>
```

```
q和fq的区别
1.应用场景
q 一般是用户输入的关键字如 keyword:海尔空调 
fq 一般是其他过滤条件如 category:电器 onsale:1

2.参数个数 
q 参数必选有且只有1个 
fq 参数可选且可以有多个如：
fq=category:电器&fq=onsale:1, 结果等价于 
fq=category:电器 AND onsale:1
但是缓存不同.上边的写法solr会分别缓存category:电器和onsale:1的结果

3.对得分的影响 
q 影响搜索结果评分 
fq 不会影响搜索结果的评分
```

## 6.3 facet 查询
```
进行 Facet查询需要在请求参数中加入"facet=on”或者"facet=true”只有这样Facet组件才起作用

# 概述
facet是solr的高级搜索功能之一，可以给用户提供更友好的搜索体验。
作用:可以根据用户搜索条件,按照指定域进行分组并统计,类似于关系型数据库中的group by分组查询;
eg:查询title中包含手机的商品，按照品牌进行分组，并且统计数量。
select brand,COUNT(*) from tb_item where title like '%手机%’ group by brand
# 适合场景︰
在电商网站的搜索页面中，我们根据不同的关键字搜索。对应的品牌列表会跟着变化。这个功能就可以基于Facet来实现;

# Facet比较适合的域
一般代表了实体的某种公共属性的域，如商品的品牌，商品的分类，商品的制造厂家，书籍的出版商等等;

# Facet域的要求
Facet的域必须被索引.一般来说该域无需分词，无需存储,无需分词是因为该域的值代表了一个整体概念，如商品的品牌”联想”代表了一个整体概念，如果拆成"联""想”两个字都不具有实际意义.
另外该字段的值无需进行大小写转换等处理，保持其原貌即可.无需存储是因为查询到的文档中不需要该域的数据，而是作为对查询结果进行分组的一种手段，用户一般会沿着这个分组进一步深入搜索.

# Facet查询的分类
facet_queries:表示根据条件进行分组统计查询。
facet_fields:代表根据域分组统计查询。
facet_ranges:可以根据时间区间和数字区间进行分组统计查询。
facet_intervals:可以根据时间区间和数字区间进行分组统计查询。
```

### 6.3.1 facet_fields
```
需求:对item_title中包含手机的文档，按照品牌域进行分组，并且统计数量;
1.进行 Facet查询需要在请求参数中加入"facet=on”或者"facet=true”只有这样Facet组件才起作用
2.分组的字段通过在请求中加入"facet.field”参数加以声明
http://loca7host:8080/so1r/co1lection1/select?q=item_title:手机&facet=on&facet.field=item_brand
3.如果需要对多个字段进行Facet查询,那么将该参数声明多次;各个分组结果互不影响eg:还想对分类进行分组统计.
http://1oca7host:8080/solr/co1lection1/select?q=item_title:手机&facet=on&facet.field=item_brand&facet.fie1d=item_category

其他参数的使用。
在facet中，还提供的其他的一些参数，可以对分组统计的结果进行优化处理;

facet.prefix	表示Facet域值的前缀﹒比如"facet.field=item_brand&facet.prefix=中国",那么对item_brand字段进行Facet查询，只会分组统计以中国为前缀的品牌。
facet.sort		表示Facet字段值以哪种顺序返回.可接受的值为true| false. true表示按照count值从大到小排列. false表示按照域值的自然顺序(字母,数字的顺序)排列﹒默认情况下为true.
facet.limit		限制Facet域值返回的结果条数﹒默认值为100.如果此值为负数，表示不限制.
facet.offset	返回结果集的偏移量，默认为0.它与facet.limit配合使用可以达到分页的效果
facet.mincount	限制了Facet最小count,默认为0.合理设置该参数可以将用户的关注点集中在少数比较热门的领域.
facet.missing	默认为""，如果设置为true或者on,那么将统计那些该Facet字段值为null的记录.
facet.method	取值为enum或fc,默认为fc.该参数表示了两种Facet的算法, 与执行效率相关.enum适用于域值种类较少的情况，比如域类型为布尔型.fc适合于域值种类较多的情况。

# 查询示例
[facet.prefix]分组统计以中国前缀的品牌
&facet=on
&facet.field=item_brand
&facet.prefix=中国

[facet.sort]按照品牌值进行字典排序
&facet=on
&facet.field=item_brand
&facet.sort=false

[facet.limit]限制分组统计的条数为5
&facet=on
&facet.field=item_brand
&facet.limit=10

[facet.offset]结合facet.limit对分组结果进行分页
&facet=on
&facet.field=item_brand
&facet.offset=5
&facet.limit=5

[facet.mincount]搜索标题中有手机的商品，并且对品牌进行分组统计查询，排除统计数量为0的品牌
q=item_title:手机
&facet=on
&facet.field=item_brand
&facet.mincount=1 I
```

### 6.3.2 facet_range
```
除了字段分组查询外，还有日期区间，数字区间分组查询。作用:将某一个时间区间(数字区间), 按照指定大小分割,统计数量;

facet.range			该参数表示需要进行分组的字段名，与facet.field一样，该参数可以被设置多次，表示对多个字段进行分组。
facet.range.start	起始时间/数字，时间的一般格式为”1995-12-31T23:59:59z",另外可以使用"NOW","YEAR""MONTH”等等，具体格式可以参考org.apache.solr.schema.DateField 的java doc.
facet.range.end		结束时间数字
facet.range.gap		时间间隔.如果start为2019-1-1,end为2020-1-1.gap设置为"+1MONTH”表示间隔1个月，那么将会把这段时间划分为12个间隔段．注意"+”因为是特殊字符所以应该用“%2B”代替
facet.range.hardend	取值可以为true | false它表示gap迭代到end处采用何种处理.举例说明start为2019-1-1,end为2019-12-25,gap为"+1MONTH",hardend为false的话最后一个时间段为2019-12-1至2020-1-1;hardend为true的话最后一个时间段为2019-12-1至2019-12-25. 举例start为0,end为1200，gap为500，hardend为false,最后一个数字区间[1000,1500] ,hardend为true最后一个数字区间[1000,1200]
facet.range.other	取值范围为before |after | between |none| all,默认为none.
					before会对start之前的值做统计.
					after会对end之后的值做统计.
					between会对start至end之间所有值做统计.如果hardend为true的话,那么该值就是各个时间段统计值的和.
					none表示该项禁用.
					all表示before,after,between都会统计.


需求:分组查询2015年，每一个月添加的商品数量;
facet=on&
facet.range=item_createtime&
facet.range.start=2015-01-01T00:00:00Z
&facet.range.end=2016-01-01T00:00:00Z
&facet.range.gap=%2B1MONTH
&facet.range.other=al1

需求:分组统计价格在0~1000,1000~2000,2000~3000…19000~20000及20000以上每个价格区间商品数量;
facet=on
&facet.range=item_price
&facet.range.start=O
&facet.range.end=20000
&facet.range.gap=1000
&facet.range.hardend=true
&facet.range.other=al1
```


### 6.3.3 facet_queries
```
在facet中还提供了第三种分组查询的方式facet query。
提供了类似filter query (fq)的语法可以更为灵活的对任意字段进行分组统计查询.

需求:查询,分类是平板电视的商品数量,品牌是华为的商品数量,品牌是三星的商品数量;
facet=on
&facet.query=item_category:平板电视
&facet.query=item_brand:华为
&facet.query=item_brand:三星

我们会发现统计的结果中对应名称tem_category:平板电视和item_brand:华为，我们也可以起另别名
facet=on
&facet.query={!key=平板电视}item_category:平板电视
&facet.query={!key=华为品牌}item_brand:华为
&facet.query={!key=三星品牌}item_brand:三星
浏览器传输需要编码: { 编码为 %7B,  } 编码为 %7D
这样可以让字段名统一起来，方便我们拿到请求数据后，封装成自己的对象;
facet=on
&facet.query=%7B!key=平板电视%7Ditem_category:平板电视
&facet.query=%7B!key=华为品牌%7Ditem_brand:华为
&facet.query=%7B!key=三星品牌%7Ditem_brand:三星
```

### 6.3.4 facet_interval
```
在Facet中还提供了一种分组查询方式facet_interval。功能类似于facet_ranges。
facet interval通过设置一个区间及域名，可以统计可变区间对应的文档数量;
通过facet_ranges和facet_interval都可以实现范围查询功能，但是底层实现不同，性能也不同.

facet.interval			此参数指定统计查询的域。它可以在同一请求中多次使用以指示多个字段。
facet.interval.set		指定区间。如果统计的域有多个，可以通过f. <fie1dname> .facet.interval.set语法指定不同域的区间。
						区间语法. 区间必须以'('或代'开头，然后是逗号('，')，最终值，最后是)或T。例如: 
						(1,10)-将包含大于1且小于10的值.
						[1,10])-将包含大于或等于1且小于10的值.
						[1,10]-将包含大于等于1且小于等于10的值.


需求:统计 item_price 在[0-10]及[1000-2000]的商品数量和 item_createtime 在2019年~现在添加的商品数量
&facet=on
&facet.interva1=item_price
&f.item_price.facet.interval.set=[0,10]
&f.item_price.facet.interva7.set=[1000 , 2000]
&facet.interval=item_createtime
&f.item_createtime.facet.interval.set=[2019-01-01T0:0:Oz,NOw]
由于有特殊符号需要进行URL编码[-——->%5B]---->%5D
http://localhost:8080/solr/co7lection1/select?q=*:*
&facet=on
&facet.interval=item_price
&f.item_price.facet.interval.set=%5B0,10%5D
&f.item_price.facet.interval.set=%5B1000,2000%5D
&facet.interval=item_createtime
&f.item_createtime.facet.interva1.set=%5B2019-01-01TO:0:0Z,NOW%5D
```

### 6.3.5 facet中其他的用法: tag操作符和ex操作符
```
当查询使用q或者fq指定查询条件的时候,查询条件的域刚好是facet的域,分组的结果就会被限制,其他的分组数据就没有意义。
          
如: 下述查询将导致分组结果中只有三星品牌有数量，其他品牌都没有数量
q=item_title:手机
&fq=item_brand:三星
&facet=on
&facet.fie1d=item_brand

如果想查看其他品牌手机的数量。给过滤条件定义标记，在分组统计的时候，忽略过滤条件;查询文档是2个条件，分组统计只有一个条件
&q=item_title:手机
&fq=%7B!tag=brandTag%7Ditem_brand:三星
&facet=on
&facet.fie1d=%7B!ex=brandTag%7Ditem_brand
```

### 6.3.6 facet.pivot
```
多维度分组查询。听起来比较抽象。

举例:统计每一个品牌和其不同分类商品对应的数量;

&facet=on
&facet.pivot=item_brand,item_category
```

## 6.4 group 查询
```
solr group作用:
将具有相同字段值的文档分组，并返回每个组的顶部文档。
Group和Facet的概念很像，都是用来分组,但是分组的结果是不同。

group				布尔值				设为true，表示结果需要分组
group.field			字符串				需要分组的字段，字段类型需要是StrField或TextField
group.func			查询语句			可以指定查询函数
group.query			查询语句			可以指定查询语句
rows				整数				返回多少组结果，默认10
start				整数				指定结果开始位置/偏移量
group.limit			整数				每组返回多数条结果,默认1
group.offset		整数				指定每组结果开始位置/偏移量
sort				排序算法			控制各个组的返回顺序
group.sort			排序算法			控制每一分组内部的顺序
group.format		grouped/simple	设置为simple可以使得结果以单一列表形式返回
group.main			布尔值			设为true时，结果将主要由第一个字段的分组命令决定
group.ngroups		布尔值			设为true时，Solr将返回分组数量，默认fasle;
group.truncate		布尔值			设为true时，facet数量将基于group分组中匹相关性高的文档，默认fasle
group.cache.percent	整数0-100		设为大于0时，表示缓存结果，默认为0。该项对于布尔查询，通配符查询，模糊查询有改善，却会减慢普通词查询。


需求:查询item_title中包含手机的文档，按照品牌对文档进行分组;
q=item_title:手机
&group=true
&group.field=item_brand
group分组结果和Fact分组查询的结果完全不同，
group分组把同组的文档放在一起，显示该组文档数量，仅仅展示了第一个文档。

在group查询中除了支持Field分组，Query分组。还支持函数分组。
需求:按照价格范围对商品进行分组。0~1000属于第1组，1000~2000属于第二组，否则属于第3组。
q=*:*
&group=true
&group.func=map(item_price,0,1000,1,map(lem_price,1000,2000,2,3))
map(x,10,100,1,2）在函数参数中的x如果落在[10,100)之间，则将x的值映射为1，否则将其值映射为2
```


## 6.5 solr 的高亮查询
```
在Solr中提供了常用的3种高亮的组件(Highlighter)也称为高亮器，来支持高亮查询。

>> Unified Highlighter
Unified Highlighter 是最新的Highlighter (从Solr6.4开始)，它是最性能最突出和最精确的选择。
它可以通过插件/扩展来处理特定的需求和其他需求。官方建议使用该Highlighter，即使它不是默认值。

>> Original Highlighter
Original Highlighter，有时被称为"Standard Highlighter" or "Default Highlighter"，
是Solr最初的Highlighter，提供了一些定制选项，曾经一度被选择。
它的查询精度足以满足大多数需求，尽管它不如 Unified Highlighter完美;

>> FastVector Highlighter
FastVector Highlighter 特别支持多色高亮显示，一个域中不同的词采用不同的html标签作为前后缀。



hl						true | false						通过此参数值用来开启或者禁用高亮.默认值是false.如果你想使用高亮，必须设置成true.
hl.method				unified | original |fastVector		使用哪种哪种高亮组件.接收的值有: unified, origina1 , fastvector.默认值是original.
hl.fl					filed1,filed2...					指定高亮字段列表.多个字段之间以逗号或空格分开.表示那个字段要参与高亮。默认值为空字符串。
hl.q					item_title:三星						高亮查询条件，此参数运行高亮的查询条件和q中的查询条件不同。如果你设置了它,你需要设置h1.qparser .默认值和q中的查询条件一致
hl.tag.pre				<em>								高亮词开头添加的标签，可以是任意字符串，一般设置为HTML标签，默认值为<em>.对于Original Highlighter需要指定hl.simple.pre
hl.tag.post				</em>								高亮词后面添加的标签，可以是任意字符串，一般设置为HTML标签，默认值</em> .对于original Highlighter需要指定hl.simple.post
hl.qparser				lucene | dismax | edismax			用于hl.q查询的查询解析器。仅当hl.q设置时适用。默认值是defType参数的值，而defType参数又默认为lucene。
hl.requireFieldMatch	true | false						默认为fasle.如果置为true，除非该字段的查询结果不为空才会被高亮。它的默认值是false，意味着它可能匹配某个字段却高亮一个不同的字段。如果hl.fl使用了通配符，那么就要启用该参数
hl.usePhraseHighlighter true | false						默认true．如果一个查询中含有短语（引号框起来的）那么会保证一定要完全匹配短语的才会被高亮
hl.highlightMultiTerm	true | false						默认true.如果设置为true,solr将会高亮出现在多terms查询中的短语。
hl.snippets				数值								默认为1.指定每个字段生成的高亮字段的最大数量.
hl.fragsize				数值								每个snippet返回的最大字符数。默认是100.如果为0，那么该字段不会被fragmented且整个字段的值会被返回
hl.encoder				html								如果为空(默认值)，则存储的文本将返回，而不使用highlighter执行任何转义/编码。如果设置为html，则将对特殊的htmI/XML字符进行编码;
hl.maxAnalyzedChars		数值								默认10000.搜索高亮的最大字符,对一个大字段使用一个复杂的正则表达式是非常昂贵的。



需求:查询item_title中包含手机的文档，并且对item_title中的手机关键字进行高亮;
q=item_title:手机
&h1=true
&h1.f1=item_title
&h1.simp1e.pre=<font>&h1.simple.post=</font>

[hl.requireFieldMatch]只有符合对应查询条件的域中参数才进行高亮;只有item_title手机才会高亮。
q=item_title:手机
&h1=true
&h1.f1=item_title,item_category
&h1.simple.pre=<font>
&h1.simple.post=</font>
&h1.requireFie1dMatch=true


[hl.q]默认情况下高亮是基于q中的条件参数。使用fl.q让高亮的条件参数和q的条件参数不一致。比较少见。但是solr提供了这种功能。
需求:查询ltem_tile中包含三星的文档，对item_category为手机的进行高亮;
q=item_title:三星
&h1=true
&hl.q=item_category:手机
&h1.f1=item_category
&h1.simple.pre=<em>
&h7.simple.post=</em>


[hI.highlightMultiTerm]默认为true，如果为true，Solr将对通配符查询进行高亮。如果为false，则根本不会高亮显示它们。
q=item_title:micro* OR item_title:panda&h1=true
&h1.f1=item_title
&h1.simple.pre=<em>
&h7.simple. post=</em>


[hl.usePhraseHighlighter]为true时，将来我们基于短语进行搜索的时候，短语做为一个整体被高亮。为false时，短语中的每个单词都会被单独高亮。在Solr中短语需要用引号引起来;
q=item_title:"老人手机"
&h1=true
&hl.f1=item_title
&h1.simple.pre=<font>
&h1.simple.post=</font>
```


## 6.6 深度分页
```
在Solr中默认分页中，我们需要使用start和rows 参数。一般情况下使用start和rows进行分页不会有什么问题。
但是在极端情况下，假如希望查询第10000页且每页显示10条，意味着Solr需要提取前10000×10=100000条数据，
并将这100000条数据缓存在内存中，然后返回最后10条即用户想要的第10000页数据。

引发问题:
将这100000条数据缓存到内存中，会占用很多内存。页码越靠后，分页查询性能越差。
多数情况下，用户也不会查询第10000页的数据，但是不排除用户直接修改url的参数，查询第10000页的数据。

解决方案:
为此Solr提供了一种全新的分页方式，被称为游标。游标可以返回下一页数据的起始标识。
标识记录着下一页数据在索引库中绝对的索引位置。
一旦我们使用了游标，Solr就不会采用之前的分页方式。
而是根据每一页数据在索引库中绝对索引位置来获取分页数据。
#使用流程:
1.查询数据的时候需要指定一个参数cursorMark=*,可以理解为start=o通过该参数就可以获取第一页数据。在返回的结果中包含另外一个参数nextCursorMark
2.nextCursorMark是下一页数据开始位置。
3.将下一页数据的开始位置作为cursorMark的值来查询下一页数据。
4.如果返回nextCursorMark和cursorMark相同，表示没有下一页
#数据注意点:
1.cursorMark在进行分页的时候，是不能再指定start这个参数。
2使用cursorMark在进行分页的时候，必须使用排序，而且排序字段中必须包含主键。
eg: id asc
eg: id asc name desc


需求: 查询item_title中包含ED的第一页的50条数据
http://localhost:8080/so1r/co1lection1/select?
q=item_title:LED
&cursorMark=*
&rows=50
&sort=id asc
```

## 6.7 json facet
```
在Solr中对于Facet查询提供了另外一种查询格式。称为JSON Facet。
使用JSON Fact有很多好处，书写灵活,便于自定义响应结果，便于使用分组统计函数等。

基本语法:
json.facet = {
    <facet_name> : {
        type : <fact_type>,
        <other_facet_parameter>
    }
}
>> facet_name:可以任意取值，是为了将来解析结果使用。
>> type:取值terms, query, range.
    terms根据域进行Facet查询, 
    query根据查询解决进行facet查询,
    range根据范围进行facet查询。
>> other_facet_parameter:其他facet的一些参数。
```

### 6.7.1 示例
```
1.需求: 根据商品分类，分组统计每个分类对应的商品数量，并且要求数量大于等于3。

>> 传统的Facet查询
q=*:*
&facet=true
&facet.field=item_category
&facet.limit=3

>> json facet 查询
q=*:*&
json.facet={
    category:{
        type:terms ,
        field:item_category,
        limit:3
    }
}
其中:
category是自定义的一个键。
type:指的是facet的类型
field:分组统计的域
最终编码为: 
http://localhost:8080/so1r/co11ection1/select?
q=*:*&json.facet=%7Bcategory:%7Btype:terms,field:item_category,limit:3%7D%7D


2.需求:分组统计2015年每月添加的商品数量。
q=*:*
&json.facet={
    month_count:{
        type:range,
        filed:item_create_time,
        start:'2015-01-01T00:00:00Z',
        end:'2016-01-01T00:00:00Z',
        gap:'+1MONTH'
    }
}


3.需求:统计品牌是华为的商品数量，分类是平板电视的商品数量。
q=*:*
&json.facet={i}
    brand_dount:{
        type:query,
        q:"item_brand:华为"
    },
    category_count:{
        type:query,
        q:"item_category:平板电视"
    }
}
```

### 6.7.2 SubFacet
```
在JSON Facet中，还支持另外一种Facet查询，叫SubFacet.允许用户在之前分组统计的基础上
再次进行分组
统计,可以实现pivot Facet(多维度分组查询功能)查询的功能。

需求:对商品的数据，按照品牌进行分组统计,获取前5个分组数据。
q=*:*&
json.facet={
    top_brand : {
        type:terms,
        field:item_brand,
        limit:5
    }
}

在以上分组的基础上，统计每个品牌，不同商品分类的数据。eg:统计三星手机多少个，三星平板电视多少?
q=*:*&
json.facet={
    top_brand : {
        type :terms,
        field:item_brand,
        limit:5,
        facet:{
            top_category:{
                type:terms ,
                field:item_category,
                limit: 3
            }
        }
    }
}
```

## 6.8 facet 或 json facet 函数查询
```
聚合函数				例子							描述
sum						sum(sales)						聚合求和
avg						avg(popularity)					聚合求平均值
min						min(salary)						最小值
max						max(mul(price , popularity))	最大值
unique					unique(author)					统计域中唯一值的个数
sumsq					sumsq(rent)						平方和
percentile				percentile(salary,30,60,10)		可以根据百分比进行统计
```

### 6.8.1 示例
```
需求:查询华为手机价格最大值,最小值，平均值.
q=item_title:手机
&fq=item_brand:华为
&json.facet={
    avg_price:"avg(item_price)",
    min_price:"min(item_price)",
    max_price:"max(item_price)"
}


需求:查询每个品牌下，最高的手机的价格.
q=item_title:手机
&json.facet={
    categories:{
        type:terms ,
        field:item_brand ,
        facet:{
            x:"max(item_price)"
        }
    }
}
```


## 6.9 solr 的 join 查询
```
在Solr中的索引文档一般建议扁平化、非关系化.
所谓扁平化即每个文档包含的域个数和类型可以不同，
而非关系型化则表示每个文档之间没有任何关联，是相互独立。
不像关系型数据库中两张表之间可以通过外键建立联系。

案例场景:
需求:需要将员工表和部门表的数据导入到Solr中进行搜索。
通常我们的设计原则, 将员工表和部门表的字段冗余到一个文档中。
将来查询出来的文档中包含员工和部门信息。
但是在Solr中他也提供了Join查询，类似于SQL中子查询，
有时候我们利用join查询也可以完成类似子查询的功能。
// 主查询条件------->根据部门名称查询所有员工
q=*:*& 
// 过滤条件 co11ection2 中 dept_dname:SALES
fq=dept_dname:SALES
// 建立连接关系
{!join fromIndex=dept_collection toIndex=emp_collection from=id to=emp_deptno}
fromIndex: 被连接的collection
toIndex: 主co1lection
from: 指代 fromIndex 中需要和 toIndex 关联的字段
to: 指代 toIndex 中需要和 fromIndex 关联的字段
// 最终:
q=*:*
&fq={!join fromIndex=dept_collection toIndex=emp_collection from=id to=emp_deptno}dept_dname:SALES
```

### 6.9.1 需求1
```
需求:查询 emp_no=7369 员工的部门信息。

1.sql 子查询
select * from dept where deptno = (
    select deptno from emp where empno = 7369
)

2.solr 子查询
主查询条件------->查询所有部门
---------> 子查询条件员工 id 为 7369 使用 join 进行连接
q=*:*
&fq=id:7369
{!join fromIndex=emp_collection toIndex=dept_collection from=emp_deptno to=id}
```

### 6.9.2 需求2
```
需求:查询 empno=7369 的员工的领导的信息。

1.sql查询
select * from emp where empno = (
    select emp_mgr from emp where empno=7369;
)

2.solr查询
q=*:*
&fq={!join fromIndex=emp_collection toIndex=emp_collection from=emp_mgr to=id}empno:7369
```

### 6.9.3 需求3
```
需求:统计SALES部门下，薪水最高的员工

q=*:*
&fq={!join fromIndex=dept_collection toIndex=emp_collection from=id to=emp_deptno}dept_name:SALES
&json.facet={
    x:"max(emp_sal)"
}
```

## 6.10 block join
```
在Solr中还支持一种Join查询，我们称为Block Join。
但前提是我们的Document必须是NestedDocument(内嵌的Document);

内嵌的Document就是我们可以在Document内部再添加一个Document，形成父子关系.
就好比HTML中的div标签内部能够嵌套其他div标签，形成多层级的父子关系，
以员工和部门为例，如果我们采用内嵌Document 来进行索引，
我们使用一个 collection 来存储部门和员工的信息。此时我们的索引数据结构应该是类似这样的:
<add>
    <doc>
        <field name="id">1</field>
        <field name="dept_dname">ACCOUNTING</field>
        <field name="dept_1oc">NEW YORK</fie1d>
        <doc>
            <field name="id">7369</field>
            <field name="emp_ename">SMITH CoCO</field>
            <field name="emp_job">CLERK</fie1d>
            <field name="emp_hiredate">1980-12-17</field>
            <field name="emp_deptno">1</field>
        </doc>
        <doc>
            <fie1d name="id">7566</fie1d>
            <field name="emp_ename">JONES</field>
            <field name="emp_job">MANAGER</field>
            <field name="emp_hiredate">1981-04-02</field>
            <field name="emp_deptno">1</field>
        </doc>
    </doc>
</add>
```

### 6.10.1 内嵌 Document 文档的创建
```
<!--员工相关的域-->
<field name="emp_ename" type="text_ik" indexed="true" stored="true"/>
<field name="emp_job" type="text_ik" indexed="true" stored="true"/>
<field name="emp_mgr" type="string" indexed="true" stored="true"/>
<field name="emp_hiredate" type="pdate" indexed="true" stored="true"/>
<field name="emp_sa7" type="pfloat" indexed="true" stored="true"/>
<field name="emp_comm" type="pfloat" indexed="true" stored="true"/>
<field name="emp_cv" type="text_ik" indexed="true" stored="false"/>

<!--部门表的业务域-->
<field name="dept_dname" type="text_ik" indexed="true" stored="true"/>
<field name="dept_loc" type="text_ik" indexed="true" stored="true"/>
<! --标识那个是父节点-->
<field name="docParent" type="string" indexed="true" stored="true"/>
<!--创建内嵌的document必须有_root_域，该域在schema文件中已经有了，不需要额外添加-->
<fie1d name="_root_" type="string" indexed="true" stored="true"/>
```

### 6.10.2 使用solrJ/spring data solr将部门和员工信息以内嵌Document的形式进行索引
```
1.solr api
solrInputDocument doc = new solrInputDocument();
solrInputDocument subDoc = new solrInputDocument();
//建立父子关系的API方法。
doc.addchildDocument(subDoc);
2.Spring Data Solr
Spring Data Solr可以使用@Field注解和@ChildDocument注解
```

### 6.10.3 基于内嵌的document查询
```
语法:用来查询子文档
allParents:查询所有父文档的条件,someParents过滤条件
q={!child of=<allparents>}<someParents>

语法:用来查询父文档
allParents:查询所有父文档的条件，someChildren子文档的过滤条件
q={!parent which=<a1lParents>}<somechildren>

需求:查询ACCOUNTING部门下面所有的员工。
q={!child of=docParent:isParent}dept_dname:ACCOUNTING

需求:查询CLARK员工所属的部门
q={!parent which=docParent:isParent}emp_ename:CLARK
```

## 6.11 相关度排序
### 6.11.1 Field 权重
```
比如我们有图书相关的文档，需要根据book_name或者book_description搜索图书文档。我们可能认为
book_name域的权重可能要比book_description域的权重要高。
我们就可以在搜索的时候给指定的域设置权重。

演示:默认的相关度排序
q=book_name:java OR book_description:java
http://localhost:8080/so1r/collection1/select?q=book_name:java OR book_description:java


设置域的权重
#方式1:
在schema文件中给指定field指定boost属性值。boost默认值1.0
此方式不建议:原因是需要重新对数据进行索引。
<field name=""book_name" type="text_ik" indexed="true" stored="true" boost="10.0"/>

#方式2:
使用edismax查询解析器。
defType=edismax&q=java
&qf=book_name book_description^10

http://localhost:8080/solr/co1lection1/select?defType=edismax
&q=java
&qf=book_name book_description%5E10
```

### 6.11.2 Term权重
```
有时候我们查询的时候希望为某个域中的词设置权重。
查询book_name中包含java或者spring的，我们希望包含java这个词的权重高一些。

book_name :java OR book_name:spring
book_name :java^10 OR book_name:spring
```

### 6.11.3 Function权重

           
### 6.11.4 临近词权重
```
有时候我们有这种需求，比如搜索"iphone plus"将来包含iphone或者plus的文档都会被返回，
但是我们希望包含iphone plus完整短语的文档权重高一些。

#第一种方式: 使用edismax查询解析器
也可以添加一个参数
pf=域名~slop^boost
http://1ocalhost:8080/solr/co1lection1/select?defType=edismax
&q=iphone plus
&qf=book_name
&pf=book_name~0^10
0^10  表示 book_name 中包含 iphone 没有任何词 plus 文档权重是 10
1^10  表示 book_name 中包含 iphone 一个词/没有词 plus文档权重是 10
2^10  表示 book_name 中包含 "iphone 2个及2个以下词 plus"文档权重是10

#第二种方式: 使用标准解析器中也可以完成l临近词的加权
/select?q=iphone plus OR "iphone plus"~O^10 &df=book_name
```

### 6.11.5 4.3.5 Document权重
```
除了上面的修改相关度评分的操作外，我们也可以在索引的时候，设置文档的权重。
使用SolrJ或者SpringDataSolr完成索引操作的时候调用相关方法来设置文档的权重。
在Solr7以后已经废弃。
```           

```dynamicfiled```

# 7.solr 的更新
## 7.1 solr 的原子更新
```
Solr支持的原子更新:
// 局部更新需要指定文档的 id (在 schema.xml 中配置的主键 uniqueKey), 
// 主键不需要添加 set、add 等信息, 其他需要原子更新的 field 需要构造为 Map
set: 修改指定文档中该 field 的值, 如果这个 field 已经存在, 则更新, 如果不存在, 则追加到这个文档中 —— 可以是单值, 也可以是multi-valued;
add: 向指定文档中的 field 字段添加值, 这个 field 必须是 multi-valued 类型的, 否则将出错 —— 只能是multi-valued;
inc: 对指定文档中数值类型的值进行自增操作 —— 只能是数值类型, 包括int、long、float、double.
remove: 删除一个或多个域值，用于单值域就是删除该域，用于多值域就是删除多值域的其中一个或多个值。
removeregex: 同remove类似，只不过是通过正则表达式来匹配多个域值。
```

## 7.2 索引更新和提交的建议 (commit)
```
在Solr中我们在进行索引更新的时候，一般不建议显式的调用commit()进行硬提交，
建议我们solrconfig.xml 中配置自动提交和软提交;

>> 硬提交:
所谓硬提交是将没有提交的数据flush到硬盘中，并且能够查询到这条数据。
如果我们每次都进行硬提交的话，频繁的和磁盘IO进行交互，会影响索引的性能。
>> 软提交:
所谓软提交是将提交数据到写到内存里面，并且开启一个searcher。
它可以保证数据可以被搜索到，等到一定的时机后再进行自动硬提交,再将数据flush到硬盘上。


在SolrConfig.xml中如何配置软提交
<updateHandler class="solr. DirectupdateHandler2">

    <autocommit>
        <!--表示软提交达到1万条的时候会自动进行一次硬提交-->
        <maxDocs>10000</maxDoCs>
        <!--每10秒执行一次硬提交-->
        <maxTime>10000</maxTime>
    </autocommit>

    <autosoftcommit>
    <! --1秒执行一次软提交-->
        <maxTime>1000</maxTime>
    </autosoftcommit>
</updateHand1er>
```




https://blog.csdn.net/MeiX505/article/details/78469618
https://www.cnblogs.com/xiaostudy/p/12758750.html
https://bbs.csdn.net/topics/391884309
http://www.highersoft.net/mobile/detailnotice_mobile.jsp?id=185