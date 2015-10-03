package org.ppojo.utils;

import java.util.Map;

/**
 * Created by GARY on 10/2/2015.
 */
public interface ImmutableMap<K,V> {
    V get(K key);
    Iterable<Map.Entry<K,V>> entries();
    Iterable<V> values();
    int size();
}
