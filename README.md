# SQLHelper
新人学习数据库时制作的工具库

## 已实现

* 连接 MySQL 数据库
* 连接 SQLite 数据库
* `SELECT` 语句
* `UPDATE` 语句
* `INSERT` 语句
* `DELETE` 语句
* 条件 (`WHERE` 语句)
* 同时构建多条预编译语句

本依赖在生产环境 (作者的 Minecraft 服务器) 中工作良好。

## 示例

基本用法：[吾爱破解](https://www.52pojie.cn/thread-1629732-1-1.html)

建议配合 [HikariCP](https://github.com/brettwooldridge/HikariCP) 连接池使用

```java
import java.sql.PreparedStatement;
import java.sql.SQLException;
import top.mrxiaom.sqlhelper.EnumConstraints;
import top.mrxiaom.sqlhelper.Pair;
import top.mrxiaom.sqlhelper.SQLang;
import top.mrxiaom.sqlhelper.SQLValueType;

/**
 * 创建表
 */
public void createTableExample() {
    Connection conn = hikariPool.getConnection();
    SQLang.createTable(conn, "student", /*if not exists:*/ true,
            TableColumn.create(SQLValueType.bigInt(), "id", EnumConstraints.PRIMARY_KEY),
            TableColumn.create(SQLValueType.string(4), "name"),
            TableColumn.create(SQLValueType.string(1), "gender"),
            TableColumn.create(SQLValueType.string(32), "class")
    );
    
    // 有多个表时，可以把参数 conn 去掉，使用下面的方法构建多条语句，然后一起执行
}

/**
 * 同时执行多条语句
 */
public void multiStatementsExample() {
    Connection conn = hikariPool.getConnection();
    try (PreparedStatement ps = SQLang.listOf(
            // 实际语句为
            // INSERT INTO student VALUES (?,?,?,?);
            // 构建语句后自动将变量添加到预编译语句中，则执行效果类似于
            // INSERT INTO student VALUES (1, '张三', '男', '23计算机1班');
            SQLang.insertInto("student").addValues(1, "张三", "男", "23计算机1班"),
            // INSERT INTO student VALUES (2, '李四', '男', '23现代通信3班');
            SQLang.insertInto("student").addValues(2, "李四", "男", "23现代通信3班"),
            // INSERT INTO student (id,name,gender,class) VALUES (3, '王五', '女', '22光伏1班');
            SQLang.insertInto("student").addValues(Pair.of("id", 3), Pair.of("name", "王五"), Pair.of("gender", "女"), Pair.of("class", "22光伏1班"))
    ).build(conn)) {
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

/**
 * 主键不存在时插入，存在时更新
 */
public void insertOrUpdateExample() {
    Connection conn = hikariPool.getConnection();
    // 将 李四 转到 23计算机1班
    // 这个例子不太恰当，用 UPDATE 语句更好，但是我找不到更合适的例子了
    try (PreparedStatement ps = SQLang.insertInto("student")
            .addValues(2, "李四", "男", "23计算机1班")
            .onDuplicateKeyUpdate("class", "23计算机1班")
            .build(conn)) {
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}
```

## 在你的项目使用

![jitpack](https://jitpack.io/v/MrXiaoM/SQLHelper.svg)

Gradle:
```buildscript
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    implementation 'com.github.MrXiaoM:SQLHelper:2.0.0'
}
```
