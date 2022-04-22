package top.mrxiaom.sqlhelper.conditions;

import java.util.List;

public interface ICondition {
    String toSQL();

    List<Object> getParams();
}
