package top.mrxiaom.sqlhelper;

public enum EnumConstraints {
    NONE(""),
    NOT_NULL("NOT NULL"),
    UNIQUE("UNIQUE"),
    PRIMARY_KEY("PRIMARY KEY"),
    FOREIGN_KEY("FOREIGN KEY"),
    CHECK("CHECK"),
    DEFAULT("DEFAULT");
    final String s;

    EnumConstraints(String s) {
        this.s = s;
    }

    public String toSQL() {
        return " " + s;
    }
}
