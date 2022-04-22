package top.mrxiaom.sqlhelper.conditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConditionIn implements ICondition {
    private final List<Object> items = new ArrayList<>();

    private ConditionIn(Object... items) {
        this.items.addAll(Arrays.asList(items));
    }

    public static ConditionIn of(Object... items) {
        return new ConditionIn(items);
    }

    @Override
    public String toSQL() {
        StringBuilder base = new StringBuilder("IN (");
        for (int i = 0; i < items.size(); i++) {
            base.append("?").append(i < items.size() - 1 ? "," : "");
        }
        base.append(")");
        return base.toString();
    }

    @Override
    public List<Object> getParams() {
        return items;
    }
}
