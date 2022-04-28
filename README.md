# SQLHelper
新人学习数据库时制作的工具库

## 已实现

* `SELECT` 语句
* `UPDATE` 语句
* `INSERT` 语句
* 条件 (`WHERE` 语句)

## 示例

```java
// conn 是数据库连接
ResultSet resultSet = SQLang.select("websites").column("*").build(conn).get().executeQuery();
```

## 在你的项目使用

![jitpack](https://jitpack.io/v/MrXiaoM/SQLHelper.svg)

Gradle:
```buildscript
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    implementation 'com.github.MrXiaoM:SQLHelper:1.1'
}
```
