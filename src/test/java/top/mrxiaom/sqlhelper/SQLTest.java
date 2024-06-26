package top.mrxiaom.sqlhelper;

import java.io.File;
import java.sql.PreparedStatement;

public class SQLTest {
    public static void log(Object... obj) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < obj.length; i++) {
            text.append(obj[i].toString()).append(i < obj.length - 1 ? " " : "");
        }
        System.out.println(text);
    }

    public static void main(String[] args) {
        try {
            SQLHelper sql = SQLHelper.connectToSQLite(new File("./test.db")).orElse(null);
            if (sql == null) {
                log("无法连接到数据库");
                return;
            }
            // 其实可以用 CREATE TABLE IF NOT EXISTS，只需在表名后面加参数 true
            if (!SQLang.getTables(sql.getConnection()).contains("players")) {
                log("表players不存在，正在创建");
                SQLang.createTable(sql.getConnection(), "players",
                        TableColumn.create(SQLValueType.string(32), "name"),
                        TableColumn.create(SQLValueType.string(32), "imageId"),
                        TableColumn.create(SQLValueType.string(32), "imageContent"),
                        TableColumn.create(SQLValueType.string(32), "materials"),
                        TableColumn.create(SQLValueType.int_(1), "enable"));
            } else log("表players存在");
            try (PreparedStatement s = SQLang.insertInto("players")
                    .addValues(
                            Pair.of("name", "test"),
                            Pair.of("imageId", "1919810"),
                            Pair.of("imageContent", "image"),
                            Pair.of("materials", "[]"),
                            Pair.of("enable", 1)
                    )
                    .build(sql)) {
                s.executeUpdate();
                log("已插入一条记录");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
