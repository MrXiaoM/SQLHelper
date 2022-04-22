package top.mrxiaom.sqlhelper.base;

import top.mrxiaom.sqlhelper.Pair;
import top.mrxiaom.sqlhelper.SQLang;
import top.mrxiaom.sqlhelper.conditions.ICondition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLSyntaxErrorException;
import java.util.*;
import java.util.stream.Collectors;

public class SQLangUpdate implements SQLang {
    private final String table;
    private boolean isAllowUnsafe = false;
    private final Map<String, Object> sets = new HashMap<>();
    private final List<ICondition> conditions = new ArrayList<>();

    private SQLangUpdate(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }

    public boolean isAllowUnsafe() {
        return isAllowUnsafe;
    }

    public Map<String, Object> getSets() {
        return sets;
    }

    public List<ICondition> getConditions() {
        return conditions;
    }

    /**
     * 开始准备 UPDATE 语句
     * @param table 表名
     * @return 语句
     */
    public static SQLangUpdate table(String table) { return new SQLangUpdate(table); }

    /**
     * 预编译语句
     * @param conn 数据库连接
     * @return 预编译完成的语句
     */
    @Override
    public Optional<PreparedStatement> build(Connection conn) {
        try {
            if (sets.isEmpty()) return Optional.empty();
            if (!isAllowUnsafe && conditions.isEmpty())
                throw new SQLSyntaxErrorException("It is not allowed to UPDATE without \"WHERE\" condition! " +
                        "If you know what you are doing, use `.allowUnsafe()` to continue.");
            StringBuilder sql = new StringBuilder("UPDATE " + table + " SET");
            List<Object> params = new ArrayList<>();
            int size = sets.size();
            List<String> keySet = sets.keySet().parallelStream().collect(Collectors.toList());
            for (int i = 0; i < size; i++) {
                String key = keySet.get(i);
                params.add(sets.get(key));
                sql.append(key).append("=?").append(i < size - 1 ? "," : " ").append("\n");
            }
            if (!conditions.isEmpty()) {
                sql.append(" WHERE");
                for (ICondition c : conditions) {
                    sql.append(" ").append(c.toSQL());
                    if (!c.getParams().isEmpty())
                        params.addAll(c.getParams());
                }
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
     * 允许不安全操作 (允许不填 WHERE)
     * @return 语句
     */
    public SQLangUpdate allowUnsafe() {
        return allowUnsafe(true);
    }

    /**
     * 设置是否允许不安全操作
     * @param isAllowUnsafe 值
     * @return 语句
     */
    public SQLangUpdate allowUnsafe(boolean isAllowUnsafe) {
        this.isAllowUnsafe = isAllowUnsafe;
        return this;
    }

    /**
     * 添加要设置表的中对应列的值
     * @param values Pairs.of(列名称, 值)
     * @return 语句
     */
    @SafeVarargs
    public final SQLangUpdate set(Pair<String, Object>... values) {
        for (Pair<String, Object> s : values) {
            sets.put(s.getKey(), s.getValue());
        }
        return this;
    }

    /**
     * 添加筛选条件
     * @param condition 条件，详见包 top.mrxiaom.sqlhelper.conditions
     * @return 语句
     */
    public SQLangUpdate where(ICondition... condition) {
        conditions.addAll(Arrays.asList(condition));
        return this;
    }
}
