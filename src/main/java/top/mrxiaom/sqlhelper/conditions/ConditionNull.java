package top.mrxiaom.sqlhelper.conditions;

import java.util.ArrayList;
import java.util.List;

public class ConditionNull implements ICondition {
    private final String key;
    private final boolean isNullOrNot;

    public ConditionNull(String key, boolean isNullOrNot) {
        this.key = key;
        this.isNullOrNot = isNullOrNot;
    }

    public static ConditionNull of(String key) {
        return of(key, true);
    }

    public static ConditionNull of(String key, boolean isNullOrNot) {
        return new ConditionNull(key, isNullOrNot);
    }

    @Override
    public String toSQL() {
        return key + " IS " + (isNullOrNot ? "NULL" : "NOT NULL");
    }

    @Override
    public List<Object> getParams() {
        return new ArrayList<>();
    }
}
