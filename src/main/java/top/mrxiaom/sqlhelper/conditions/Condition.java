package top.mrxiaom.sqlhelper.conditions;

import java.util.Collections;
import java.util.List;

public class Condition<V> implements ICondition {
    @Override
    public String toSQL() {
        return "`" + key + "`" + operator.toSQL() + "?";
    }

    @Override
    public List<Object> getParams() {
        return Collections.singletonList(value);
    }

    private final String key;
    private V value;
    private final EnumOperators operator;

    private Condition(String key, EnumOperators operator, V value) {
        this.key = key;
        this.value = value;
        this.operator = operator;
    }

    public EnumOperators getOperator() {
        return operator;
    }

    public String getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public static <V> Condition<V> of(String key, EnumOperators operator, V value) {
        return new Condition<>(key, operator, value);
    }
}
