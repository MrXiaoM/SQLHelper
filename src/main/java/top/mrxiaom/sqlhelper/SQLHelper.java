package top.mrxiaom.sqlhelper;

import top.mrxiaom.sqlhelper.conditions.Condition;
import top.mrxiaom.sqlhelper.conditions.EnumOperators;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 不推荐使用该方法连接到数据库
 * 请使用 HikariCP 创建连接池
 */
@Deprecated
public class SQLHelper {
    final Connection conn;

    private SQLHelper(Connection conn, boolean init) {
        this.conn = conn;
        if (init) init();
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }

    /**
     * 初次加载时执行
     */
    public void init() {

    }

    @SafeVarargs
    public final void set(String table, Pair<String, String> findColumnAndValue,
                          Pair<String, String>... setTargetColumnValue) {
        if (setTargetColumnValue.length < 1) return;
        try {
            Statement s = conn.createStatement();
            StringBuilder sql = new StringBuilder("UPDATE " + table + " SET");
            for (int i = 0; i < setTargetColumnValue.length; i++) {
                Pair<String, String> pair = setTargetColumnValue[i];
                sql.append(" ").append(pair.getKey()).append("='").append(pair.getValue()).append("'");
                if (i < setTargetColumnValue.length - 1) sql.append(",");
            }
            sql.append(" WHERE ").append(findColumnAndValue.getKey())
                    .append("='").append(findColumnAndValue.getValue()).append("';");
            s.executeUpdate(sql.toString());
            s.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public Optional<List<Object>> get(String table, Pair<String, String> findColumnAndValue, String targetColumn) {
        try (PreparedStatement s = SQLang.select(table)
                .column(findColumnAndValue.getKey(), targetColumn)
                .where(
                        Condition.of(findColumnAndValue.getKey(), EnumOperators.EQUALS, findColumnAndValue.getValue())
                ).build(conn)
        ) {
            ResultSet result = s.executeQuery();
            List<Object> target = new ArrayList<>();
            while (result.next()) {
                target.add(result.getObject(2));
            }
            result.close();
            if (!target.isEmpty())
                return Optional.of(target);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return Optional.empty();
    }

    private static SQLHelper useOrCreateDatabase(Connection conn, String database) throws SQLException {
        boolean needToCreate = true;
        Statement stat = conn.createStatement();
        ResultSet result = stat.executeQuery("SHOW DATABASES");
        while (result.next()) {
            if (result.getObject(1).equals(database)) {
                needToCreate = false;
                break;
            }
        }
        if (needToCreate) {
            stat.executeUpdate("CREATE DATABASE IF NOT EXISTS " + database);
        }
        stat.executeUpdate("USE " + database);
        stat.close();

        return new SQLHelper(conn, needToCreate);
    }

    public static Optional<SQLHelper> connectToSQLite(File file) {
        return connectToSQLite("org.sqlite.JDBC", file);
    }

    public static Optional<SQLHelper> connectToSQLite(String jdbc, File file) {
        String DB_URL = "jdbc:sqlite:" + file.getAbsolutePath();
        try {
            // 注册 JDBC 驱动
            Class.forName(jdbc);
            // 打开链接
            Connection conn = DriverManager.getConnection(DB_URL);
            return Optional.of(new SQLHelper(conn, false));
        } catch (Throwable t) {
            // 处理 JDBC 错误
            t.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<SQLHelper> connectToMySQL(String host, int port, String database, String user, String password) {
        return connectToMySQL("com.mysql.cj.jdbc.Driver", host, port, database, user, password);
    }

    public static Optional<SQLHelper> connectToMySQL(String jdbc, String host, int port, String database, String user, String password) {
        return connectToMySQL(jdbc, host, port, database, user, password, new String[] { "useSSL=false", "allowPublicKeyRetrieval=true", "serverTimezone=UTC" });
    }
    public static Optional<SQLHelper> connectToMySQL(String jdbc, String host, int port, String database, String user, String password, String[] params) {
        String DB_URL = "jdbc:mysql://" + host + ":" + port + "/?" + String.join("&", params);
        try {
            // 注册 JDBC 驱动
            Class.forName(jdbc);
            // 打开链接
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            return Optional.of(useOrCreateDatabase(conn, database));
        } catch (Throwable t) {
            // 处理 JDBC 错误
            t.printStackTrace();
        }
        return Optional.empty();
    }

    @Deprecated
    public static Optional<SQLHelper> connectTo(String host, int port, String database, String user, String password) {
        return connectToMySQL(host, port, database, user, password);
    }
}
