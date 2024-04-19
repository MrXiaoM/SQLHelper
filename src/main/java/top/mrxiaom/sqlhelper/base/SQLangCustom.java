package top.mrxiaom.sqlhelper.base;

import top.mrxiaom.sqlhelper.Pair;
import top.mrxiaom.sqlhelper.SQLang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQLangCustom extends ArrayList<Object> implements SQLang {
    private String sql;
    private SQLangCustom(String sql) {
        this.sql = sql;
    }

    public static SQLangCustom of(String sql) {
        return new SQLangCustom(sql);
    }

    public static SQLangCustom of(String sql, Object... objects) {
        SQLangCustom lang = new SQLangCustom(sql);
        Collections.addAll(lang, objects);
        return lang;
    }

    public static SQLangCustom of(String sql, Iterable<Object> objects) {
        SQLangCustom lang = new SQLangCustom(sql);
        for (Object obj : objects) {
            lang.add(obj);
        }
        return lang;
    }

    public String getSQL() {
        return sql;
    }

    public void setSQL(String sql) {
        this.sql = sql;
    }

    @Override
    public Pair<String, List<Object>> generateSQL() {
        return Pair.of(sql, new ArrayList<>(this));
    }
}
