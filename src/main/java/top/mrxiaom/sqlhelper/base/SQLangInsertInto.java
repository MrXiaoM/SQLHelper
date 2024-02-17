package top.mrxiaom.sqlhelper.base;

import top.mrxiaom.sqlhelper.Pair;
import top.mrxiaom.sqlhelper.SQLang;
import top.mrxiaom.sqlhelper.conditions.ICondition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

public class SQLangInsertInto implements SQLang {
    private final String table;
    private final List<String> columns = new ArrayList<>();
    private final List<Object> values = new ArrayList<>();
    private final List<ICondition> conditions = new ArrayList<>();
    private final Map<String, Object> dupKeyUpdates = new HashMap<>();
    private SQLangInsertInto(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Object> getValues() {
        return values;
    }

    public List<ICondition> getConditions() {
        return conditions;
    }

    public Map<String, Object> getDuplicateKeyUpdateMap() {
        return dupKeyUpdates;
    }

    /**
     * 开始准备 UPDATE 语句
     *
     * @param table 表名
     * @return 语句
     */
    public static SQLangInsertInto table(String table) {
        return new SQLangInsertInto(table);
    }

    /**
     * 预编译语句
     *
     * @param conn 数据库连接
     * @return 预编译完成的语句
     */
    @Override
    public Optional<PreparedStatement> build(Connection conn) {
        try {
            if (values.isEmpty()) return Optional.empty();
            StringBuilder sql = new StringBuilder("INSERT INTO " + table);
            List<Object> params = new ArrayList<>();
            StringBuilder valueString = new StringBuilder();
            int size = values.size();
            for (int i = 0; i < size; i++) {
                if (!columns.isEmpty()) {
                    sql.append(i == 0 ? " (" : ",").append(columns.get(i)).append(i == size - 1 ? ")" : "");
                }
                params.add(values.get(i));
                valueString.append(i == 0 ? "(" : ",").append("?").append(i == size - 1 ? ")" : "");
            }
            sql.append(" VALUES ").append(valueString).append(";");
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
     * 添加要设置表的中对应列的值
     *
     * @param values Pairs.of(列名称, 值)
     * @return 语句
     */
    @SafeVarargs
    public final SQLangInsertInto addValues(Pair<String, Object>... values) {
        for (Pair<String, Object> s : values) {
            this.columns.add(s.getKey());
            this.values.add(s.getValue());
        }
        return this;
    }

    public final SQLangInsertInto clearValues() {
        this.columns.clear();
        this.values.clear();
        return this;
    }

    /**
     * 添加要设置表的中对应列的值
     * 【注意】执行此操作时若columns有内容将清空所有值
     *
     * @param values 要添加的值
     * @return 语句
     */
    public final SQLangInsertInto addValues(Object... values) {
        if (!this.columns.isEmpty()) {
            this.columns.clear();
            this.values.clear();
        }
        this.values.addAll(Arrays.asList(values));
        return this;
    }

    /**
     * 在主键、唯一键等重复时，只更新某些值
     * @param keysValues 键值列表，第单数个是键，第偶数个是值
     * @return 语句
     */
    public final SQLangInsertInto onDuplicateKeyUpdate(Object... keysValues) {
        for (int i = 0; i < keysValues.length - 1; i += 2) {
            String key = keysValues[i].toString();
            Object value = keysValues[i + 1];
            dupKeyUpdates.put(key, value);
        }
        return this;
    }
}
