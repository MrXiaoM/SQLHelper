package top.mrxiaom.sqlhelper.utils;

import top.mrxiaom.sqlhelper.Pair;
import top.mrxiaom.sqlhelper.SQLang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BuildUtils {

    public static PreparedStatement defaultBuild(Connection conn, SQLang lang) throws SQLException {
        Pair<String, List<Object>> pair = lang.generateSQL();
        String sql = pair.getKey();
        List<Object> params = pair.getValue();
        return setParams(conn.prepareStatement(sql), params);
    }

    public static PreparedStatement setParams(PreparedStatement stat, List<Object> params) throws SQLException {
        if (!params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                stat.setObject(i + 1, params.get(i));
            }
        }
        return stat;
    }
}
