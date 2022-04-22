package top.mrxiaom.sqlhelper;

public interface SQLValueType {
    abstract class ValueWithSize implements SQLValueType {
        int size;
        String type;

        public ValueWithSize(String type, int size) {
            this.type = type;
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public int getSize() {
            return size;
        }

        public String toSQLType() {
            return this.getType() + "(" + this.getSize() + ")";
        }
    }

    class ValueFinalString extends ValueWithSize {
        private ValueFinalString(int size) {
            super("char", size);
        }

        public static ValueFinalString of(int size) {
            return new ValueFinalString(size);
        }
    }

    class ValueString extends ValueWithSize {
        private ValueString(int size) {
            super("varchar", size);
        }

        public static ValueString of(int size) {
            return new ValueString(size);
        }
    }

    class ValueInteger extends ValueWithSize {
        private ValueInteger(int size) {
            super("integer", size);
        }

        public static ValueInteger of(int size) {
            return new ValueInteger(size);
        }
    }

    class ValueInt extends ValueWithSize {
        private ValueInt(int size) {
            super("int", size);
        }

        public static ValueInt of(int size) {
            return new ValueInt(size);
        }
    }

    class ValueSmallInt extends ValueWithSize {
        private ValueSmallInt(int size) {
            super("smallint", size);
        }

        public static ValueSmallInt of(int size) {
            return new ValueSmallInt(size);
        }
    }

    class ValueDecimal extends ValueWithSize {
        int d;

        private ValueDecimal(int size, int d) {
            super("decimal", size);
            this.d = d;
        }

        public int getD() {
            return d;
        }

        public String toSQLType() {
            return this.getType() + "(" + this.getSize() + "," + this.getD() + ")";
        }

        public static ValueDecimal of(int size, int d) {
            return new ValueDecimal(size, d);
        }
    }

    class ValueNumeric extends ValueWithSize {
        int d;

        private ValueNumeric(int size, int d) {
            super("numeric", size);
            this.d = d;
        }

        public int getD() {
            return d;
        }

        public String toSQLType() {
            return this.getType() + "(" + this.getSize() + "," + this.getD() + ")";
        }

        public static ValueNumeric of(int size, int d) {
            return new ValueNumeric(size, d);
        }
    }

    String getType();

    String toSQLType();
}
