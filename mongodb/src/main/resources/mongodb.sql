db.students.insert([
{ "_id" : ObjectId("5e64bf9a2a16affa306b8b93"), "name" : "数组1", "age" : "18","gender" : "男","scope" : 77.0},
{"_id" : ObjectId("5e64cc2b6ef8da42f1854b11"), "name" : "数组2","age" : "13", "gender" : "女","scope" : 89},
{ "_id" : ObjectId("5e663e7379bbd40eb81de8eb"),"name" : "数组3","age" : "13","gender" : "男","scope" : 60},
{"_id" : ObjectId("5e6648d179bbd40eb81de8ee"),"name" : "数组4","age" : "14","gender" : "男","scope" : 59},
{"_id" : ObjectId("5e6648d479bbd40eb81de8f0"),"name" : "数组5","age" : "18","gender" : "男","scope" : 68},
{"_id" : ObjectId("5e6648d879bbd40eb81de8f1"),"name" : "数组6","age" : "24","gender" : "男","scope" : 56},
{"_id" : ObjectId("5e6648d979bbd40eb81de8f2"),"name" : "数组7","age" : "25","gender" : "女", "scope" : 90},
{ "_id" : ObjectId("5e6648d979bbd40eb81de8f3"),"name" : "数组8","age" : "24","gender" : "男","scope" : 98},
{ "_id" : ObjectId("5e6648d979bbd40eb81de8f4"),"name" : "数组9", "age" : "18","gender" : "男","scope" : 45},
{ "_id" : ObjectId("5e6648d979bbd40eb81de8f5"),"name" : "数组10", "age" : "14", "gender" : "女", "scope" : 67}
]);



db.items.insert([
{ "_id" : 1, "item" : "abc", "price" : 10, "quantity" : 2, "date" : ISODate("2014-03-01T08:00:00Z") },
{ "_id" : 2, "item" : "jkl", "price" : 20, "quantity" : 1, "date" : ISODate("2014-03-01T09:00:00Z") },
{ "_id" : 3, "item" : "xyz", "price" : 5, "quantity" : 10, "date" : ISODate("2014-03-15T09:00:00Z") },
{ "_id" : 4, "item" : "xyz", "price" : 5, "quantity" : 20, "date" : ISODate("2014-04-04T11:21:39.736Z") },
{ "_id" : 5, "item" : "abc", "price" : 10, "quantity" : 10, "date" : ISODate("2014-04-04T21:23:13.331Z") }
]);
