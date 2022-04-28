package top.mrxiaom.sqlhelper;

import top.mrxiaom.sqlhelper.base.SQLangInsertInto;
import top.mrxiaom.sqlhelper.base.SQLangSelect;
import top.mrxiaom.sqlhelper.base.SQLangUpdate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SQLang {
    default Optional<PreparedStatement> build(SQLHelper helper){
        return build(helper.getConnection());
    }

    Optional<PreparedStatement> build(Connection conn);

    static SQLangSelect select(String table) {
        return SQLangSelect.from(table);
    }

    static SQLangUpdate update(String table) {
        return SQLangUpdate.table(table);
    }

    static SQLangInsertInto insertInto(String table) { return SQLangInsertInto.table(table); }

    static void dropTable(Connection conn, String table) {
        try {
            Statement s = conn.createStatement();
            s.executeUpdate("DROP TABLE " + table + ";");
            s.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    static void truncateTable(Connection conn, String table) {
        try {
            Statement s = conn.createStatement();
            s.executeUpdate("TRUNCATE TABLE " + table + ";");
            s.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    static void createTable(Connection conn, String table, TableColumn... columns) {
        try {
            Statement stat = conn.createStatement();
            StringBuilder s = new StringBuilder("CREATE TABLE " + table + " (");
            for(int i = 0; i < columns.length; i++) {
                TableColumn column = columns[i];
                s.append(column.getValue()).append(" ").append(column.getKey().toSQLType());
                if (!column.getAttribute().equals(EnumConstraints.NONE)) s.append(column.getAttribute().toSQL());
                if (i < columns.length - 1) s.append(", ");
            }
            s.append(");");
            stat.executeUpdate(s.toString());
            stat.close();
        }catch (Throwable t){
            t.printStackTrace();
        }
    }

    static List<String> getTables(Connection conn) {
        List<String> tables = new ArrayList<>();
        try {
            Statement stat = conn.createStatement();
            ResultSet result = stat.executeQuery("SHOW TABLES;");
            while(result.next())
            {
                tables.add(result.getString(0));
            }
            stat.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return tables;
    }

}
