package top.mrxiaom.sqlhelper;

public class TriplePair<K, V, A>{
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

    public static <K, V, A> TriplePair<K, V, A> of(K key, V value, A attribute) { return new TriplePair<>(key, value, attribute); }
}
