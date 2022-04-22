package top.mrxiaom.sqlhelper;

public class TableColumn extends TriplePair<SQLValueType, String, EnumConstraints> {
    TableColumn(SQLValueType key, String value, EnumConstraints attribute) {
        super(key, value, attribute);
    }

    public static TableColumn create(SQLValueType type, String name) {
        return create(type, name, EnumConstraints.NONE);
    }

    public static TableColumn create(SQLValueType type, String name, EnumConstraints constraint) {
        return new TableColumn(type, name, constraint);
    }
}
