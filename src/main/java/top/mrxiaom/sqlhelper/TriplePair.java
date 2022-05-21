package top.mrxiaom.sqlhelper;

public class TriplePair<K, V, A> {
    private K key;
    private V value;
    private A attribute;

    public TriplePair(K key, V value, A attribute) {
        this.key = key;
        this.value = value;
        this.attribute = attribute;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public A getAttribute() {
        return attribute;
    }

    public void setAttribute(A attribute) {
        this.attribute = attribute;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TriplePair)) return false;
        TriplePair<?, ?, ?> one = (TriplePair<?, ?, ?>) obj;
        if (this.getKey() != null) {
            if (!this.getKey().equals(one.getKey())) return false;
        } else if (one.getKey() != null) return false;
        if (this.getValue() != null) {
            if (!this.getValue().equals(one.getValue())) return false;
        } else if (one.getValue() != null) return false;
        if (this.getAttribute() != null) {
            return this.getAttribute().equals(one.getAttribute());
        } else return one.getAttribute() == null;
    }

    public static <K, V, A> TriplePair<K, V, A> of(K key, V value, A attribute) {
        return new TriplePair<>(key, value, attribute);
    }
}
