package top.mrxiaom.sqlhelper.base;

import top.mrxiaom.sqlhelper.EnumOrder;
import top.mrxiaom.sqlhelper.SQLang;
import top.mrxiaom.sqlhelper.conditions.ICondition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SQLangSelect implements SQLang {
    private final String table;
    private boolean isDistinct = false;
    private final List<String> columns = new ArrayList<>();
    private final List<ICondition> conditions = new ArrayList<>();
    private final List<String> orderColumns = new ArrayList<>();
    private int limit = 0;
    private EnumOrder order;
    private SQLangSelect(String table) {
        this.table = table;
    }

    /**
     * 开始准备 SELECT ... FROM 语句
     * @param table 表名
     * @return 语句
     */
    public static SQLangSelect from(String table) { return new SQLangSelect(table); }

    /**
     * 预编译语句
     * @param conn 数据库连接
     * @return 预编译完成的语句
     */
    @Override
    public Optional<PreparedStatement> build(Connection conn) {
        try {
            if (columns.isEmpty()) columns.add("*");
            StringBuilder sql = new StringBuilder("SELECT ");
            if (isDistinct) sql.append("DISTINCT ");
            int size = columns.size();
            for (int i = 0; i < size; i++) {
                sql.append(columns.get(i)).append(i < size - 1 ? "," : " ").append("\n");
            }
            sql.append("FROM ").append(table);
            List<Object> params = new ArrayList<>();
            if (!conditions.isEmpty()) {
                sql.append(" WHERE");
                for (ICondition c : conditions) {
                    sql.append(" ").append(c.toSQL());
                    if (!c.getParams().isEmpty())
                        params.addAll(c.getParams());
                }
            }
            if (!orderColumns.isEmpty()) {
                int orderSize = orderColumns.size();
                sql.append(" ORDER BY");

                for (int i = 0; i< orderSize; i++) {
                    sql.append(orderColumns.get(i)).append(i < orderSize - 1 ? ",":"");
                }
                sql.append(order.toSQL());
            }
            if (limit > 0) {
                sql.append(" LIMIT ").append(limit);
            }
            sql.append(";");
            PreparedStatement stat = conn.prepareStatement(sql.toString());
            if (!params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    stat.setObject(i + 1, params.get(i));
                }
            }
            return Optional.of(stat);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * 转为动态语句
     * 不推荐使用
     * @return 动态语句
     */
    @Override
    @Deprecated
    public String toString() {
        if (columns.isEmpty()) columns.add("*");
        StringBuilder sql = new StringBuilder("SELECT ");
        if (isDistinct) sql.append("DISTINCT ");
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            sql.append(columns.get(i)).append(i < size - 1 ? "," : " ").append("\n");
        }
        sql.append("FROM ").append(table);
        List<Object> params = new ArrayList<>();
        if (!conditions.isEmpty()) {
            sql.append(" WHERE");
            for (ICondition c : conditions) {
                sql.append(" ").append(c.toSQL());
                if (!c.getParams().isEmpty())
                    params.addAll(c.getParams());
            }
        }
        if (!orderColumns.isEmpty()) {
            int orderSize = orderColumns.size();
            sql.append(" ORDER BY");

            for (int i = 0; i< orderSize; i++) {
                sql.append(orderColumns.get(i)).append(i < orderSize - 1 ? ",":"");
            }
            sql.append(order.toSQL());
        }
        if (limit > 0) {
            sql.append(" LIMIT ").append(limit);
        }
        sql.append(";");
        String str = sql.toString();
        if (str.contains("?")) {
            for(Object p : params) {
                int i = str.indexOf("?");
                if (i < 0) break;
                if (p instanceof String) p = "'" + p + "'";
                str = str.substring(0, i) + p + str.substring(i + 1);
            }
        }
        return str;
    }

    /**
     * 设置返回结果的数量，0为无限制
     * @param limit 数量
     * @return 语句
     */
    public SQLangSelect limit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * 设置排列规则
     * @param order 规则
     * @param columns 按照那些列来排
     * @return 语句
     */
    public SQLangSelect orderBy(EnumOrder order, String... columns){
        if (order != null) this.order = order;
        for (String s : columns) {
            if (!orderColumns.contains(s)) orderColumns.add(s);
        }
        return this;
    }

    /**
     * 设置返回唯一不同的值
     * @return 语句
     */
    public SQLangSelect distinct() {
        return distinct(true);
    }

    /**
     * 设置是否返回唯一不同的值
     * @param isDistinct 值
     * @return 语句
     */
    public SQLangSelect distinct(boolean isDistinct) {
        this.isDistinct = isDistinct;
        return this;
    }

    /**
     * 添加要查询的列，输入*为所有列
     * @param column 列1,列2,列3...
     * @return
     */
    public SQLangSelect column(String... column) {
        for (String s : column) {
            if (s.equals("*")) {
                columns.clear();
                columns.add("*");
            }
            if (!columns.contains(s)) columns.add(s);
        }
        return this;
    }

    /**
     * 添加查询条件
     * @param condition 条件，详见包 top.mrxiaom.sqlhelper.conditions
     * @return 语句
     */
    public SQLangSelect where(ICondition... condition) {
        conditions.addAll(Arrays.asList(condition));
        return this;
    }
}
