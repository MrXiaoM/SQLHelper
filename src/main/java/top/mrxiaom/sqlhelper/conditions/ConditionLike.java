package top.mrxiaom.sqlhelper.conditions;

import java.util.Collections;
import java.util.List;

public class ConditionLike implements ICondition {
    String key;
    String pattern;

    private ConditionLike(String key, String pattern) {
        this.key = key;
        this.pattern = pattern;
    }

    public static ConditionLike of(String key, String pattern) {
        return new ConditionLike(key, pattern);
    }

    @Override
    public String toSQL() {
        return key + " LIKE ?";
    }

    @Override
    public List<Object> getParams() {
        return Collections.singletonList(pattern);
    }
}
