package top.mrxiaom.sqlhelper;

public enum EnumOrder {
    DEFAULT(""),
    ASC("ASC"),
    DESC("DESC");
    final String s;
    EnumOrder(String s) {
        this.s = s;
    }

    public String toSQL() {
        return (this.equals(DEFAULT) ? "" : " ") + s;
    }
}
