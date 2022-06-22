https://www.jianshu.com/p/231b2777cc12
https://www.jianshu.com/p/9a8def022836
https://blog.csdn.net/liao0801_123/article/details/89413922
https://blog.csdn.net/div_java/article/details/109089795


#1.基本概念与语法
```
在Mongodb中存在3个概念：
数据库 	数据库是一个仓库，在仓库中可以存放集合
集合 	集合类似于数组，在集合中可以存放文档
文档 	文档是数据库中最小单位，我们存储和操作的内容都是文档

即一个数据库中存在多个集合，一个集合中存在多个文档。
在mongodb中，数据库和集合都不需要手动创建。在创建文档时，如果文档所在的集合和数据库不存在，将自动创建。
```

#2.常用命令
```
show dbs;  //显示所有的数据库
db;  //显示所在的数据库
use st;  //创建并切换数据库
show collections; //显示数据库中所有的集合
db.st1.insert({"xx":"1232"}); //创建文档st1

// 当向集合中插入文档时，如果没有给指定_id属性，则数据库会自动给文档添加_id。该属性用来作为文档的唯一标识。
db.<collection>.insert(doc); //插入一个或多个文档。
db.<collection>.insertOne(); //插入一个文档。
db.collection.insertMany(); //插入多个文档。

// 默认情况下find()方法无参或{}是查询所有对象。
db.<collection>.find();查询集合中所有符合条件的文档。find可以接受对象作为条件参数。

// 修改操作
语法：db.<collection>.update()(查询条件,修改后的新对象);。
在Mongodb3.2之后，新增3个语法来取代update语法
replaceOne()：替换一个文档
updateOne()：修改一个文档
updateMany()：修改多个文档

// 删除操作, 注意：remove默认情况下删除多个文档。
db.<collection>.remove(); 既可以删除一个文档，也可以删除多个文档
db.<collection>.deleteOne(); 删除一个文档
db.<collection>.deleteMany();删除多个文档

```

## 2.1 插入命令
```
//插入一条数据
db.stus.insert({"name":"小猪",age:28,gender:"难"});
//插入多条数据
db.stus.insert([{"name":"数组1",age:28,gender:"女"},{"name":"数组2",age:29,gender:"女"}]);
//查看数据
db.stus.find();
```

## 2.2 查询命令
```
db.stus.find({name:"小猪"}); // 查询特定属性的对象
db.stus.findOne({name:"小猪"}); // 查询集合中符合条件的第一个文档
findOne直接返回的是一个对象。而find返回的是一个数组。
注意，db.stus.find({name:"小猪"})[0];可以选择数组中具体的对象。
db.stus.find({age:28}).count(); // 统计查询数量
```

## 2.3 更新命令
```
// 将name为小猪的文档使用{age:28}去替换。
db.stus.update({name:"小猪"},{age:28});
db.stus.replaceOne({name:"小猪"},{age:28});

// 修改指定的属性, 修改_id为"5e64cb476ef8da42f1854b0f"的文档的name属性和gender属性。若name或者gender不存在，那么将新增该属性。
db.stus.update(
   {"_id":ObjectId("5e64cb476ef8da42f1854b0f")}, 
   {
    $set:{"name":"小白兔",gender:"女"}
   }
);

// 修改name为"小白兔"的文档的age和gender属性。
db.stus.update(
   {name:"小白兔"}, 
   {
    $set:{"age":12,gender:"女"}
   }
);

// 删除指定属性
db.stus.update(
   {name:"小白兔"}, 
   {
    $unset:{gender:"女"}
   }
);

// 修改多个符合条件的值
// db.<collection>.update()，默认情况下只会修改一个文档（即使存在多个符合条件的值）。
// 但是可以配置db.<collection>.update()去修改多个记录。
db.stus.updateMany(
   {gender:"女"}, 
   {
     $set:{age:13}
   },
   {
      multi:true  //可以修改多条记录
   }
);

// db.<collction>.updateMany()可以修改多个符合条件的文档。
db.stus.updateMany(
   {gender:"女"}, 
   {
    $set:{age:11}
   }
);
```

## 2.4 删除操作
```
db.stus.remove({_id:ObjectId("5e64cb476ef8da42f1854b0f")})
// 第二个参数为true，即删除一个参数。
db.stus.remove({gender:"女"},true);

注意：如果传递{}作为参数，那么会清空集合中的所有文档（性能差，因为先匹配在删除）。
若是清空集合，推荐使用db.<collection>.drop();
若是删除数据库db.dropDatabase()；
```

#文件存储系统


```参考资料```
https://docs.mongoing.com/
https://www.mongodb.com/try/download/community
https://blog.csdn.net/Mrqiang9001/article/details/121352249
https://blog.csdn.net/ai_0922/article/details/105192399 (gridfs)