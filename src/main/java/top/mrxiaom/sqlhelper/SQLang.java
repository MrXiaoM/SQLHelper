package top.mrxiaom.sqlhelper;

import top.mrxiaom.sqlhelper.base.*;
import top.mrxiaom.sqlhelper.utils.BuildUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface SQLang {

    /**
     * 预编译语句
     *
     * @param helper 数据库连接
     * @return 预编译完成的语句
     */
    default PreparedStatement build(SQLHelper helper) throws SQLException {
        return build(helper.getConnection());
    }

    /**
     * 预编译语句
     *
     * @param conn 数据库连接
     * @return 预编译完成的语句
     */
    default PreparedStatement build(Connection conn) throws SQLException {
        return BuildUtils.defaultBuild(conn, this);
    }

    /**
     * 生成未编译的预编译语句以及参数列表
     */
    Pair<String, List<Object>> generateSQL();

    /**
     * 创建语句列表
     */
    static SQLangList listOf(Iterable<SQLang> values) {
        return SQLangList.of(values);
    }

    /**
     * 创建语句列表
     */
    static SQLangList listOf(Iterable<SQLang> values, SQLang... ext) {
        return SQLangList.of(values, ext);
    }

    /**
     * 创建语句列表
     */
    static SQLangList listOf(SQLang... values) {
        return SQLangList.of(values);
    }

    /**
     * 切换数据库
     * 【此为拼接语句，不防注入】
     */
    static SQLangCustom use(String database) {
        return SQLangCustom.of("USE " + database + ";");
    }

    /**
     * 创建 SELECT 语句，用于查询表
     */
    static SQLangSelect select(String table) {
        return SQLangSelect.from(table);
    }

    /**
     * 创建 UPDATE 语句，用于更新表
     */
    static SQLangUpdate update(String table) {
        return SQLangUpdate.table(table);
    }

    /**
     * 创建 INSERT INTO 语句，用于插入数据到表中
     */
    static SQLangInsertInto insertInto(String table) {
        return SQLangInsertInto.table(table);
    }

    /**
     * 创建 DELETE 语句，用于从表中删除数据
     */
    static SQLangDelete delete(String table) {
        return SQLangDelete.from(table);
    }

    /**
     * 创建 DROP TABLE 语句，用于删除表。
     * 【此为拼接语句，不防注入】
     */
    static SQLangCustom dropTable(String table) {
        return SQLangCustom.of("DROP TABLE " + table + ";");
    }

    /**
     * 立即删除表
     * 【此为拼接语句，不防注入】
     */
    static void dropTable(Connection conn, String table) {
        try (PreparedStatement ps = dropTable(table).build(conn)) {
            ps.executeUpdate();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 创建 TRUNCATE TABLE 语句，用于清空表
     * 【此为拼接语句，不防注入】
     */
    static SQLangCustom truncateTable(String table) {
        return SQLangCustom.of("TRUNCATE TABLE " + table + ";");
    }

    /**
     * 立即清空表
     * 【此为拼接语句，不防注入】
     */
    static void truncateTable(Connection conn, String table) {
        try (PreparedStatement ps = truncateTable(table).build(conn)) {
            ps.executeUpdate();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        createTable("student", true,
                TableColumn.create(SQLValueType.bigInt(), "id", EnumConstraints.PRIMARY_KEY),
                TableColumn.create(SQLValueType.string(4), "name"),
                TableColumn.create(SQLValueType.string(1), "gender"),
                TableColumn.create(SQLValueType.string(32), "class"));
    }

    /**
     * 创建 CREATE TABLE 语句，用于创建表
     * 【此为拼接语句，不防注入】
     */
    static SQLangCustom createTable(String table, boolean ifNotExists, TableColumn... columns) {
        StringBuilder s = new StringBuilder("CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS " : "") + table + " (");
        for (int i = 0; i < columns.length; i++) {
            TableColumn column = columns[i];
            s.append(column.getValue()).append(" ").append(column.getKey().toSQLType());
            if (!column.getAttribute().equals(EnumConstraints.NONE)) s.append(column.getAttribute().toSQL());
            if (i < columns.length - 1) s.append(", ");
        }
        s.append(");");
        return SQLangCustom.of(s.toString());
    }

    /**
     * 立即创建表
     * 【此为拼接语句，不防注入】
     */
    static void createTable(Connection conn, String table, TableColumn... columns) {
        createTable(conn, table, false, columns);
    }

    /**
     * 立即创建表
     * 【此为拼接语句，不防注入】
     */
    static void createTable(Connection conn, String table, boolean ifNotExists, TableColumn... columns) {
        try (PreparedStatement ps = createTable(table, ifNotExists, columns).build(conn)) {
           ps.executeUpdate();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 获取当前数据库的表
     */
    static List<String> getTables(Connection conn) {
        List<String> tables = new ArrayList<>();
        try {
            Statement stat = conn.createStatement();
            ResultSet result = stat.executeQuery(stat.getClass().getName().contains("sqlite") ? "SELECT Name FROM sqlite_master WHERE type='table'" : "SHOW TABLES;");
            while (result.next()) {
                tables.add(result.getString(1));
            }
            stat.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return tables;
    }
}
