package top.mrxiaom.sqlhelper.base;

import top.mrxiaom.sqlhelper.Pair;
import top.mrxiaom.sqlhelper.SQLang;

import java.util.*;

public class SQLangList extends ArrayList<SQLang> implements SQLang {
    @Override
    public Pair<String, List<Object>> generateSQL() {
        List<String> sqlList = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        for (SQLang sql : this) {
            Pair<String, List<Object>> pair = sql.generateSQL();
            sqlList.add(pair.getKey());
            objects.addAll(pair.getValue());
        }
        return Pair.of(String.join("\n", sqlList), objects);
    }

    public static SQLangList of(Iterable<SQLang> values) {
        SQLangList list = new SQLangList();
        for (SQLang lang : values) {
            list.add(lang);
        }
        return list;
    }
    public static SQLangList of(Iterable<SQLang> values, SQLang... ext) {
        SQLangList list = new SQLangList();
        for (SQLang lang : values) {
            list.add(lang);
        }
        Collections.addAll(list, ext);
        return list;
    }

    public static SQLangList of(SQLang... values) {
        SQLangList list = new SQLangList();
        Collections.addAll(list, values);
        return list;
    }
}
