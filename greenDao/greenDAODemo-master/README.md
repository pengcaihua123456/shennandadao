# 一、关于greenDAO
greenDAO应该算是当前最火的数据库开源框架了，它是一个将对象映射到SQLite数据库中的轻量且快速的ORM（object / relational mapping）解决方案。
![greenDAO-orm](http://p86p0tig8.bkt.clouddn.com/greenDAO-orm.png)
关于greenDAO的其他相关信息可以看官网[greenDAO](http://greenrobot.org/greendao/)、
[githubgreenDAO地址](https://github.com/greenrobot/greenDAO)、[官方的配置GreenDAO英文文档](http://greenrobot.org/greendao/documentation/introduction/)
# 二、greenDAO理解
 DAO的core library中有以下几个核心类，也是后面常用到的，先来大概了解下他们的结构，不然直接看他们的使用会云里雾里。

![Core-Classes](http://p86p0tig8.bkt.clouddn.com/greenDAO-Core-Classes.png)

- **DaoMaster**：Dao中的管理者。它保存了sqlitedatebase对象以及操作DAO classes（注意：不是对象）。其提供了一些创建和删除table的静态方法，其内部类OpenHelper和DevOpenHelper实现了
SQLiteOpenHelper，并创建数据库的框架。

-  **DaoSession**：会话层。操作具体的DAO对象（注意：是对象），比如各种getter方法。

-  **Daos**：实际生成的某某DAO类，通常对应具体的java类，比如NoteDao等。其有更多的权限和方法来操作数据库元素。

-  **Entities**：持久的实体对象。通常代表了一个数据库row的标准java properties。

# 三、greenDAO优势
1、一个精简的库
2、性能最大化
3、内存开销最小化
4、易于使用的 APIs
5、对 Android 进行高度优化
有兴趣的可以看下官网的 [greenDAO Features](http://greenrobot.org/greendao/features/)
# 四、greenDAO使用
greenDAO3.0采用注解的方式来定义实体类，通过gradle插件生成相应的代码。
### 1，首先在`project的gradle`文件中引入greenDAO插件，引入之后如下：
```
// In your root build.gradle file:
buildscript {
    repositories {
        jcenter()
        mavenCentral() // add repository
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.0'
        classpath 'org.greenrobot:greendao-gradle-plugin:3.2.2' // add plugin
    }
}
```
### 2，然后在`module的gradle`文件中添加greenDAO的插件，并引入相关类库，修改之后如下：
```
// In your app projects build.gradle file:
apply plugin: 'com.android.application'
apply plugin: 'org.greenrobot.greendao' // apply plugin

android {  
    ...  
    ...  
  
    greendao{  
        schemaVersion 1  
        daoPackage 'com.greendao.gen'
        targetGenDir 'src/main/java'  
    }  
} 
dependencies {  
    ...  
    ...  
    implementation 'org.greenrobot:greendao:3.2.2' // add library
} 
```
在gradle的根模块中加入上述代码，就完成了我们的基本配置了。
属性介绍：
schemaVersion--> 指定数据库schema版本号，迁移等操作会用到;
daoPackage --> dao的包名，包名默认是entity所在的包；
targetGenDir --> 生成数据库文件的目录;
### 3，创建一个User的实体类

```
@Entity
public class User {
    @Id 
    private Long id; 
    @Property(nameInDb = "NAME")
    private String name; 
    @Transient 
    private int tempUsageCount; // not persisted  
}
```
- **@Entity** 表示这个实体类一会会在数据库中生成对应的表，
- **@Id** 表示该字段是id，注意该字段的数据类型为包装类型Long
- **@Property** 则表示该属性将作为表的一个字段，其中nameInDb看名字就知道这个属性在数据库中对应的数据名称。
- **@Transient** 该注解表示这个属性将不会作为数据表中的一个字段。
- **@NotNull** 表示该字段不可以为空
- **@Unique** 表示该字段唯一。小伙伴们有兴趣可以自行研究。
### 4，MakeProject
编译项目（Android快捷键：Ctrl+F9），User实体类会自动编译，生成get、set方法并且会在com.greendao.gen目录下生成三个文件,并且还会在我们的包下生成DaoMaster和DaoSession。
# 五、greenDAO使用
步骤：即：先创建了一个SQLiteOpenHelper并创建连接到一个具体数据库；再根据具体的datebase创建一个master对象用于；最后通过master创建一个数据库的会话操作。
```
 public class MyApplication extends Application {
 private DaoMaster.DevOpenHelper mHelper;
 private SQLiteDatabase db;
 private DaoMaster mDaoMaster;
 private DaoSession mDaoSession;
 public static MyApplication instances;
 @Override    public void onCreate() {
     super.onCreate();
     instances = this;
     setDatabase();
 }
 public static MyApplication getInstances(){
     return instances;
 }

/**
 * 设置greenDAO
 */
private void setDatabase() {
    // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
    // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO已经帮你做了。
    // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
    // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
    mHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
    db = mHelper.getWritableDatabase();
    // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。 
    mDaoMaster = new DaoMaster(db); 
    mDaoSession = mDaoMaster.newSession();
}
public DaoSession getDaoSession() {
      return mDaoSession;
}
public SQLiteDatabase getDb() {
      return db;
  }
}
```
获取UserDao对象：
```
 UserDao mUserDao = MyApplication.getInstances().getDaoSession().getUserDao();
```
# 六、简单的增删改查实现：
1. 增
```
mUser = new User((long)2,"anye3");
mUserDao.insert(mUser);//添加一个
```
2. 删
```
mUserDao.deleteByKey(id);
```
3. 改
```
mUser = new User((long)2,"anye0803");
mUserDao.update(mUser);
```
4. 查
```
List<User> users = mUserDao.loadAll();
String userName = "";
for (int i = 0; i < users.size(); i++) {
    userName += users.get(i).getName()+",";
}
mContext.setText("查询全部数据==>"+userName);
```
更多的操作就不一一介绍了，大家可以根据需要去查找资料；
# 七、greenDAO中的注解
(一) @Entity 定义实体
@nameInDb 在数据库中的名字，如不写则为实体中类名
@indexes 索引
@createInDb 是否创建表，默认为true,false时不创建
@schema 指定架构名称为实体
@active 无论是更新生成都刷新
(二) @Id
(三) @NotNull 不为null
(四) @Unique 唯一约束
(五) @ToMany 一对多
(六) @OrderBy 排序
(七) @ToOne 一对一
(八) @Transient 不存储在数据库中
(九) @generated 由greenDAO产生的构造函数或方法

# 八、数据库升级
数据库的升级其实就两个步骤我们来看看：
8.1 修改gradle文件
首先在module的gradle文件中修改版本号：
```
//这里改为最新的版本号  
schemaVersion 2  
targetGenDir 'src/main/java'  
```
8.2修改实体类
```
@Entity  
public class User {  
    @Property  
    private int age;  
    @Property  
    private String password;  
    @Id  
    private Long id;  
    @Property(nameInDb = "USERNAME")  
    private String username;  
    @Property(nameInDb = "NICKNAME")  
    private String nickname;  
}
```
重现编译项目运行即可。
一般的数据库升级这样就可以了，特殊情况可能需要自己编写数据库迁移脚本，这种时候可以自定义DBHelper，定义方式如下，注意继承类：
```
public class DBHelper extends DaoMaster.OpenHelper {  
    public static final String DBNAME = "lenve.db";  
  
    public DBHelper(Context context) {  
        super(context, DBNAME, null);  
    }  
  
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
        super.onUpgrade(db, oldVersion, newVersion);  
    }  
}
```

可以在onUpgrade方法中进行数据库的迁移，如果自定义了DBHelper，则数据库的初始化变为如下方式：
```
DBHelper devOpenHelper = new DBHelper(this);  
DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());  
DaoSession daoSession = daoMaster.newSession();  
userDao = daoSession.getUserDao(); 
```

# 九.结束语
总体来说,greenDAO在配置上相对于2.0要简单的多。
本文 Demo 下载链接：[greenDAODemo](https://github.com/YangJiexian/greenDAODemo)，如果喜欢的话可以star一下。
本教程旨在介绍 greenDAO3.0的基本用法与配置，更高级与详细的使用，请参见官网如本文有任何问题欢迎指正。
