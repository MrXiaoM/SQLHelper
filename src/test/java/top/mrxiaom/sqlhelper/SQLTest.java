package top.mrxiaom.sqlhelper;

import java.util.Optional;

public class SQLTest {
    public static void log(Object... obj) {
        StringBuilder text = new StringBuilder();
        for(int i = 0; i< obj.length;i++){
            text.append(obj[i].toString()).append(i < obj.length - 1 ? " " : "");
        }
        System.out.println(text);
    }
    public static void main(String[] args) {
        try {
            Optional<SQLHelper> sql = SQLHelper.connectTo("localhost", 3306, "database", "root", "123456");
            if (!sql.isPresent()) {
                log("无法连接到数据库");
                return;
            }
            SQLang.insertInto("players")
                    .addValues(
                            Pair.of("name", "test"),
                            Pair.of("imageId", "1919810"),
                            Pair.of("imageContent", "image"),
                            Pair.of("materials", "[]"),
                            Pair.of("enable", 1)
                    )
                    .build(sql.get()).get().executeUpdate();
            log("已插入一条记录");
        }catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
