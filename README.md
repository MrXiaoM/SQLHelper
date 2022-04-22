# SQLHelper
新人学习数据库时制作的工具库

## 示例

```java
// conn 是数据库连接
ResultSet resultSet = SQLang.select("websites").column("*").build(conn).get().executeQuery();
```
