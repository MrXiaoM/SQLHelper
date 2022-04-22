package top.mrxiaom.sqlhelper.conditions;

public enum EnumOperators {
    EQUALS("="), NOT_EQUALS("<>"),
    GREATER_THAN(">"), GREATER_THAN_OR_EQUALS(">="),
    LESS_THAN("<"), LESS_THAN_OR_EQUALS("<=");
    final String s;

    EnumOperators(String s) {
        this.s = s;
    }

    public String toSQL() {
        return s;
    }
}
