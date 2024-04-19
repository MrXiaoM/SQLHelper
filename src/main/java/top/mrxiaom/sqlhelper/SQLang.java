package top.mrxiaom.sqlhelper;

import top.mrxiaom.sqlhelper.base.SQLangDelete;
import top.mrxiaom.sqlhelper.base.SQLangInsertInto;
import top.mrxiaom.sqlhelper.base.SQLangSelect;
import top.mrxiaom.sqlhelper.base.SQLangUpdate;

import java.sql.*;
import java.util.ArrayList;
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

    }

    public static SQLangSelect select(String table) {
        return SQLangSelect.from(table);
    }

    public static SQLangUpdate update(String table) {
        return SQLangUpdate.table(table);
    }

    public static SQLangInsertInto insertInto(String table) {
        return SQLangInsertInto.table(table);
    }
    public static SQLangDelete delete(String table) {
        return SQLangDelete.from(table);
    }
    public static void dropTable(Connection conn, String table) {
        try {
            Statement s = conn.createStatement();
            s.executeUpdate("DROP TABLE " + table + ";");
            s.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void truncateTable(Connection conn, String table) {
        try {
            Statement s = conn.createStatement();
            s.executeUpdate("TRUNCATE TABLE " + table + ";");
            s.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void createTable(Connection conn, String table, TableColumn... columns) {
        createTable(conn, table, false, columns);
    }

    public static void createTable(Connection conn, String table, boolean ifNotExists, TableColumn... columns) {
        try {
            Statement stat = conn.createStatement();
            StringBuilder s = new StringBuilder("CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS " : "") + table + " (");
            for (int i = 0; i < columns.length; i++) {
                TableColumn column = columns[i];
                s.append(column.getValue()).append(" ").append(column.getKey().toSQLType());
                if (!column.getAttribute().equals(EnumConstraints.NONE)) s.append(column.getAttribute().toSQL());
                if (i < columns.length - 1) s.append(", ");
            }
            s.append(");");
            stat.executeUpdate(s.toString());
            stat.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static List<String> getTables(Connection conn) {
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
