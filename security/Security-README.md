#1.xss
```
应对原则:
输入校验, 输出编码
```

#2.CSRF
```
应对原则:
refer 校验, token 鉴权
```

#3.SSRF

#4.XXE
```
应对原则:
禁用外部实体
DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
dbf.setExpandEntityReferences(false);
```

#5.加密算法
```
不安全的加密算法:
DES/3DES/SKIPJACK/RC2/RSA(1024位及以下)
MD2/MD4/MD5/RSAWithSHA1/AES-ECB/SHA1

>> 推荐使用的加密算法: HASH 加密: SHA256,对称加密: AES-CBC, 非对称加密: RSA2048
>> AES 加密有限选择: CBC, GCM, OFB
>> 个人数据的传输, 存储(db, redis)建议使用AES-CBC加密;
>> 有搜索的允许使用AES-ECB或者HMAC
```
