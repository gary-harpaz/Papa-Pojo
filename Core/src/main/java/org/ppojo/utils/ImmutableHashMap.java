/*
 * Copyright (c) 2015.  Gary Harpaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ppojo.utils;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Map;

public class ImmutableHashMap<K,V> implements ImmutableMap<K,V> {

    private Map<K,V> _values;
    public ImmutableHashMap(@Nonnull Map<K, V> values) {
        _values=values;
    }
    @Override
    public V get(K key) {
        return _values.get(key);
    }

    @Override
    public Iterable<Map.Entry<K, V>> entries() {
        return _values.entrySet();
    }

    @Override
    public Iterable<V> values() {
        return _values.values();
    }

    @Override
    public int size() {
        return _values.size();
    }

    public static <C,D> ImmutableMap<C,D> newMap(Map<C,D> values) {
        if (values==null)
            return empty();
        return new ImmutableHashMap(values);
    }

    private static final Object _empty=new Empty<>();
    public static <C,D> ImmutableMap<C,D>  empty() {
        return (ImmutableMap<C,D>)_empty;
    }

    private static class Empty<A,B> implements ImmutableMap<A,B> {

        @Override
        public B get(A key) {
            return null;
        }

        @Override
        public Iterable<Map.Entry<A, B>> entries() {
            return EmptyIterable.empty();
        }

        @Override
        public Iterable<B> values() {
            return EmptyIterable.empty();
        }

        @Override
        public int size() {
            return 0;
        }
    }

    private static class EmptyIterable<T> implements Iterable<T> {

        private static Object _instance=new EmptyIterable<>();
        public static <E> EmptyIterable<E> empty(){
            return (EmptyIterable<E>)_instance;
        }

        private EmptyIterator<T> _iterator=new EmptyIterator<>();

        @Override
        public Iterator<T> iterator() {
            return _iterator;
        }
    }
    private static class EmptyIterator<T> implements  Iterator<T> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            return null;
        }
    }

}
