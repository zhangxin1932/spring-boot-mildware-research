<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Freemarker 测试</title>
</head>
<body>
    <#--1.${}插值； 只能输出数值、日期或者字符串，其它类型不能输出。-->
    <h1>${name}； ${message}</h1>
    <hr>
    <#--2.assign指令用于在页面上定义一个变量-->
    <#assign linkman="李四"/>
    联系人：${linkman}
    <#assign info={"tel":"110","sex":"男"}/>
    电话：${info.tel};性别：${info.sex}
    <hr>
    <#--3.if
        关键字： gt ：比较运算符“大于”； gte ：比较运算符“大于或等于”； lt ：比较
        运算符“小于”； lte ：比较运算符“小于或等于”
        ??是判断对象是否存在
        ?函数调用
    -->
    <#assign bool=false/>
    <#if bool>
        bool为true
    <#else>
        bool为false
    </#if>
    <hr>
    <#--4.list <#list sequence as item>
    sequence ：表达式将被算作序列或集合
    item ：循环变量（不是表达式）的名称
    如果想在循环中得到索引，使用循环变量+_index 就可以得到。如上述语法中则可以使用 item_index 可以得到循环变量
    -->
    <#list goodsList as fruit>
        索引：${fruit_index},
        水果：${fruit.name};
        价格：${fruit.price}<br>
    </#list>
    <hr>
    <#--5.内建函数: 使用 size 函数来实现对于集合大小的获取-->
    总共${goodsList?size}条记录
    <#--集合中索引为1的name属性-->
    ${goodsList[1].name}
    <#--集合中第一个元素的name属性-->
    ${goodsList?first.name}
    <hr>
    <#--6.内建函数: 可以使用 eval 将 json 字符串转换为对象-->
    <#assign str="{'id':'123','text':'文本'}"/>
    <#assign jsonObj=str?eval/>
    id为：${jsonObj.id};text为：${jsonObj.text}<p></p>
    <hr>
    <#--7.内建函数: 日期格式化-->
    当前日期：${today?date}<br>
    <hr>
    当前时间：${today?time}<br>
    <hr>
    当前日期+时间：${today?datetime}
    <hr>
    格式化显示当前日期时间：${today?string('yyyy年MM月dd日 HH:mm:ss')}
    <hr>
    <#--8.内建函数: 数字转换为字符串-->
    ${number}
    <hr>
    ${number?c}<p></p>
    <hr>
    ${strs!"strs空值的默认显示值"}
    <hr>
    <#if strs??>
        str变量存在
    <#else >
        strs变量不存在
    </#if>
<#--

    9.空值处理
    在 FreeMarker 中对于空值必须手动处理。
    ${emp.name!} 表示 name 为空时什么都不显示
    ${emp.name!(“名字为空”)} 表示 name 为空时显示 名字为空
    ${(emp.company.name)!} 表示如果 company 对象为空则什么都不显示， !只用
    在最近的那个属性判断；所以如果遇上有自定义类型（导航）属性时，需要使用括号
    ${bool???string} 表示：首先??表示判断 bool 变量是否存在，存在返回 true 否则 false，然后对返回的值调用其内置函数 string
    <#if str??> 表示去判断 str 变量是否存在，存在则 true，不存在为 false


    10.运算符
    10.1 算数运算符
    FreeMarker 表达式中完全支持算术运算,FreeMarker 支持的算术运算符包括:+, - , *, / , %
    10.2 逻辑运算符
    逻辑运算符有如下几个:
    逻辑与:&&
    逻辑或:||
    逻辑非:!
    逻辑运算符只能作用于布尔值,否则将产生错误
    10.3. 比较运算符
    表达式中支持的比较运算符有如下几个:
    1 =或者==:判断两个值是否相等.
    2 !=:判断两个值是否不等.
    3 >或者 gt:判断左边值是否大于右边值
    4 >=或者 gte:判断左边值是否大于等于右边值
    5 <或者 lt:判断左边值是否小于右边值
    6 <=或者 lte:判断左边值是否小于等于右边值

    =和!=可以用于字符串,数值和日期来比较是否相等,但=和!=两边必须是相同类型的值,否则会产生错误,
    而且 FreeMarker 是精确比较,"x","x ","X"是不等的.
    其它的运行符可以作用于数字和日期,但不能作用于字符串,
    大部分的时候,使用 gt 等字母运算符代替>会有更好的效果,
    因为 FreeMarker 会把>解释成 FTL 标签的结束字符,当然,也可以使用括号来避免这种情况,如:<#if (x>y)>

-->

</body>
</html>