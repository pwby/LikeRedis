项目详解：https://blog.csdn.net/weixin_43240245/article/details/99407470
一、思路

1.将字节流依照Redis的协议反列化为Java能够识别的对象，分析该对象的指令信息（例如是LPUSH、LPOP等），通过反射机制实例化处理该对象的类，在对应的实现类中完成指令 （例如实现将对象中的数据信息储存在内存中或者从内存中删除对应的信息等），把实现指令的结果依照协议序列化为字节流写入到Redis客户端。

2.使用Socket套接字接受来自客户端的连接，利用固定线程池实现对多个用户的服务

3.使用ObjectInputStream与ObjectOutputStream类对存在内存中的数据读写，实现所储存信息的持久化功能

二、过程

第一阶段：

使用Socket套接字实现服务器与客户端的连接

字节流对象遵循BSD协议规则反序列化为Java对象（Object）Protocol：ProtocolDecode：字节流-------->java

对象确认对象类型并转换为所属类型
通过反射机制找到其相应的类该类从数据库（并非磁盘中的数据库，是存放数据的一个类）中得到数据

在该类中对所收到的数据与数据库中的数据信息结合处理把处理完毕信息状态（Java对象）遵循BSD协议规则序列化为字节流响应给client

第二阶段：

使用线程池来处理多个用户请求。每连接到一个用户，就会有一个线程去执行任务。在我写的服务器上把ServerSocker的端口号固定为Redis的端口号，
下载一个redis-2.4.5-win32-win64，使用该客户端连接我的服务器，检测各个功仍然能正常实现。

第三阶段：

将数据库中存在的信息写入到磁盘中，使得信息能够持久化。创建一个Permament类来专门管理数据加载与存储过程

该类以单例模式创建，只能被创建一次。

使用ObjectInputStream和ObjectOutputStream来读写数据库中的信息

当向内存中插入/更新数据/删除数据时，先把数据插入到database中，从database中获取信息再将其写入到文件中

当启动服务器时，先从文件中加载信息到database中


使用ObjectInputStream和ObjectOutputStream都是在try的方括号里面写的，因为在处理完之后，可以自动释放资源。不用手动关闭。
1.test-git-config
2.test--modify issue
3.test--modify issue
