package top.mrxiaom.sqlhelper.conditions;

import java.util.Arrays;
import java.util.List;

public class ConditionBetween implements ICondition {
    private final Object one;
    private final Object another;

    private ConditionBetween(Object one, Object another) {
        this.one = one;
        this.another = another;
    }

    public static ConditionBetween of(Object one, Object another) {
        return new ConditionBetween(one, another);
    }

    @Override
    public String toSQL() {
        return "BETWEEN ? AND ?";
    }

    @Override
    public List<Object> getParams() {
        return Arrays.asList(one, another);
    }
}
