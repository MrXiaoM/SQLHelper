package top.mrxiaom.sqlhelper.conditions;

import java.util.ArrayList;
import java.util.List;

public enum EnumConditions implements ICondition {
    LEFT_BRACKET("("), RIGHT_BRACKET(")"),
    NOT("not"), OR("or"), AND("and");
    final String s;

    EnumConditions(String s) {
        this.s = s;
    }

    @Override
    public String toSQL() {
        return s;
    }

    @Override
    public List<Object> getParams() {
        return new ArrayList<>();
    }
}
