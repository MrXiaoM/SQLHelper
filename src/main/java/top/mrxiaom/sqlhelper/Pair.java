package top.mrxiaom.sqlhelper;

public class Pair <K, V>{
    private K key;
    private V value;
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) return false;
        Pair<?, ?> one = (Pair<?, ?>) obj;
        if (this.getKey() != null){
            if (!this.getKey().equals(one.getKey())) return false;
        }
        else if (one.getKey() != null) return false;
        if (this.getValue() != null) {
            return this.getValue().equals(one.getValue());
        }
        else return one.getValue() == null;
    }

    public static <K, V> Pair<K, V> of(K key, V value) { return new Pair<>(key, value); }
}
