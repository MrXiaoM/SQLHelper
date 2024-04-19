package top.mrxiaom.sqlhelper;

class SQLValueTypeImpl {
    static abstract class ValueWithSize implements SQLValueType {
        long size;
        String type;

        public ValueWithSize(String type, long size) {
            this.type = type;
            this.size = size;
        }

        public String getType() {
            return type;
        }

        /**
         * 显示宽度，如 INT(4) 显示4位数字
         */
        public long getSize() {
            return size;
        }

        public String toSQLType() {
            return this.getType() + (this.getSize() > 0 ? ("(" + this.getSize() + ")") : "");
        }
    }

    static class ValueFinalString extends ValueWithSize {
        private ValueFinalString(int size) {
            super("char", size);
        }

        public static ValueFinalString of(int size) {
            return new ValueFinalString(size);
        }
    }

    static class ValueString extends ValueWithSize {
        private ValueString(int size) {
            super("varchar", size);
        }

        public static ValueString of(int size) {
            return new ValueString(size);
        }
    }

    static class ValueFloat extends ValueWithSize {
        private ValueFloat(int size) {
            super("float", size);
        }

        public static ValueFloat of(int size) {
            return new ValueFloat(size);
        }
    }

    static class ValueDouble extends ValueWithSize {
        private ValueDouble(int size) {
            super("double", size);
        }

        public static ValueDouble of(int size) {
            return new ValueDouble(size);
        }
    }

    static class ValueInt extends ValueWithSize {
        private ValueInt(int size) {
            super("int", size);
        }

        public static ValueInt of(int size) {
            return new ValueInt(size);
        }
    }

    static class ValueSmallInt extends ValueWithSize {
        private ValueSmallInt(int size) {
            super("smallint", size);
        }

        public static ValueSmallInt of(int size) {
            return new ValueSmallInt(size);
        }
    }

    static class ValueTinyInt extends ValueWithSize {
        private ValueTinyInt(int size) {
            super("tinyint", size);
        }

        public static ValueTinyInt of(int size) {
            return new ValueTinyInt(size);
        }
    }

    static class ValueBigInt extends ValueWithSize {
        private ValueBigInt(long size) {
            super("bigint", size);
        }

        public static ValueBigInt of(int size) {
            return new ValueBigInt(size);
        }
    }

    static class ValueDecimal extends ValueWithSize {
        int d;

        private ValueDecimal(int m, int d) {
            super("decimal", m);
            this.d = d;
        }

        public int getD() {
            return d;
        }

        public String toSQLType() {
            return this.getType() + ((this.getSize() >= 0 && getD() >= 0) ? ("(" + this.getSize() + "," + this.getD() + ")") : "");
        }

        public static ValueDecimal of(int m, int d) {
            return new ValueDecimal(m, d);
        }
    }

    static class ValueNumeric extends ValueWithSize {
        int d;

        private ValueNumeric(int m, int d) {
            super("numeric", m);
            this.d = d;
        }

        public int getD() {
            return d;
        }

        public String toSQLType() {
            return this.getType() + ((this.getSize() >= 0 && getD() >= 0) ? ("(" + this.getSize() + "," + this.getD() + ")") : "");
        }

        public static ValueNumeric of(int m, int d) {
            return new ValueNumeric(m, d);
        }
    }

}
