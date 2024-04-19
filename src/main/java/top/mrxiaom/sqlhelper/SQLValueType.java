package top.mrxiaom.sqlhelper;

import top.mrxiaom.sqlhelper.SQLValueTypeImpl.*;

@SuppressWarnings({"unused"})
public interface SQLValueType {
    String getType();

    String toSQLType();

    /**
     * longtext 长字符串类型
     */
    SQLValueType LongText = new ValueFixed("LONGTEXT");
    /**
     * date 日期类型 YYYY-MM-DD YYYYMMDD
     */
    SQLValueType DATE = new ValueFixed("DATE");
    /**
     * time 时间类型
     */
    SQLValueType TIME = new ValueFixed("TIME");
    /**
     * year 年份类型
     */
    SQLValueType YEAR = new ValueFixed("YEAR");
    /**
     * datetime 日期加时间类型
     */
    SQLValueType DATETIME = new ValueFixed("DATETIME");
    /**
     * timestamp 时间戳类型
     */
    SQLValueType TIMESTAMP = new ValueFixed("TIMESTAMP");

    /**
     * char 类型，固定字符
     */
    static SQLValueType finalString(int size) {
        return char_(size);
    }

    /**
     * char 类型，固定字符
     */
    static SQLValueType char_(int size) {
        return ValueFinalString.of(size);
    }

    /**
     * varchar 类型，可变字符
     */
    static SQLValueType string(int size) {
        return varchar(size);
    }

    /**
     * varchar 类型，可变字符
     */
    static SQLValueType varchar(int size) {
        return ValueString.of(size);
    }

    /**
     * float 类型，单精度浮点数，取值范围同 java
     */
    static SQLValueType float_(int size) {
        return ValueFloat.of(size);
    }

    /**
     * float 类型，单精度浮点数，取值范围同 java
     */
    static SQLValueType float_() {
        return float_(-1);
    }

    /**
     * double 类型，双精度浮点数，取值范围同 java
     */
    static SQLValueType double_(int size) {
        return ValueDouble.of(size);
    }

    /**
     * double 类型，双精度浮点数，取值范围同 java
     */
    static SQLValueType double_() {
        return double_(-1);
    }

    /**
     * tinyint 类型，取值范围同 java 中的 byte
     */
    static SQLValueType tinyInt(int size) {
        return ValueTinyInt.of(size);
    }

    /**
     * tinyint 类型，取值范围同 java 中的 byte
     */
    static SQLValueType tinyInt() {
        return tinyInt(-1);
    }

    /**
     * smallint 类型，取值范围同 java 中的 short
     */
    static SQLValueType smallInt(int size) {
        return ValueSmallInt.of(size);
    }

    /**
     * smallint 类型，取值范围同 java 中的 short
     */
    static SQLValueType smallInt() {
        return smallInt(-1);
    }

    /**
     * int 类型，取值范围同 java
     */
    static SQLValueType int_(int size) {
        return ValueInt.of(size);
    }

    /**
     * int 类型，取值范围同 java
     */
    static SQLValueType int_() {
        return int_(-1);
    }

    /**
     * bigint 类型，取值范围同 java 中的 long
     */
    static SQLValueType bigInt(int size) {
        return ValueBigInt.of(size);
    }

    /**
     * bigint 类型，取值范围同 java 中的 long
     */
    static SQLValueType bigInt() {
        return bigInt(-1);
    }

    /**
     * decimal 类型，缩的“严格”定点数
     */
    static SQLValueType decimal(int m, int d) {
        return ValueDecimal.of(m, d);
    }

    /**
     * decimal 类型，缩的“严格”定点数
     */
    static SQLValueType decimal() {
        return decimal(-1, -1);
    }

    static SQLValueType numeric(int m, int d) {
        return ValueNumeric.of(m, d);
    }

    static SQLValueType numeric() {
        return numeric(-1, -1);
    }

    /**
     * 自定义类型。只需要输入类型及其参数即可，如 INT(10)。PRIMARY KEY 等无需输入
     */
    static SQLValueType custom(String type) {
        return new ValueFixed(type);
    }

    class ValueFixed implements SQLValueType {
        private final String sql;
        public ValueFixed(String sql) {
            this.sql = sql;
        }
        @Override
        public String getType() {
            return sql;
        }

        @Override
        public String toSQLType() {
            return sql;
        }
    }
}
