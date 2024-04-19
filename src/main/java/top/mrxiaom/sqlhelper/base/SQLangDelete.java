package top.mrxiaom.sqlhelper.base;

import top.mrxiaom.sqlhelper.EnumOrder;
import top.mrxiaom.sqlhelper.Pair;
import top.mrxiaom.sqlhelper.SQLang;
import top.mrxiaom.sqlhelper.conditions.ICondition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SQLangDelete implements SQLang {
    private final String table;
    private final List<ICondition> conditions = new ArrayList<>();

    private SQLangDelete(String table) {
        this.table = table;
    }

    /**
     * 开始准备 DELETE ... FROM 语句
     *
     * @param table 表名
     * @return 语句
     */
    public static SQLangDelete from(String table) {
        return new SQLangDelete(table);
    }

    @Override
    public Pair<String, List<Object>> generateSQL() {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(table);
        List<Object> params = new ArrayList<>();
        if (!conditions.isEmpty()) {
            sql.append(" WHERE");
            for (ICondition c : conditions) {
                sql.append(" ").append(c.toSQL());
                if (!c.getParams().isEmpty())
                    params.addAll(c.getParams());
            }
        }
        sql.append(";");
        return Pair.of(sql.toString(), params);
    }

    /**
     * 转为动态语句
     * 不推荐使用
     *
     * @return 动态语句
     */
    @Override
    @Deprecated
    public String toString() {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(table);
        List<Object> params = new ArrayList<>();
        if (!conditions.isEmpty()) {
            sql.append(" WHERE");
            for (ICondition c : conditions) {
                sql.append(" ").append(c.toSQL());
                if (!c.getParams().isEmpty())
                    params.addAll(c.getParams());
            }
        }
        sql.append(";");
        String str = sql.toString();
        if (str.contains("?")) {
            for (Object p : params) {
                int i = str.indexOf("?");
                if (i < 0) break;
                if (p instanceof String) p = "'" + p + "'";
                str = str.substring(0, i) + p + str.substring(i + 1);
            }
        }
        return str;
    }

    /**
     * 添加查询条件
     *
     * @param condition 条件，详见包 top.mrxiaom.sqlhelper.conditions
     * @return 语句
     */
    public SQLangDelete where(ICondition... condition) {
        conditions.addAll(Arrays.asList(condition));
        return this;
    }
}
