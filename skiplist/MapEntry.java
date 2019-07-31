package slist;

public class MapEntry<K,V> {
	K key;
	V value;
	public MapEntry(K k,V v) {
		key=k;
		value=v;
	}
	public K getKey() {
		return key;
	}
	public V getValue() {
		return value;
	}

}
