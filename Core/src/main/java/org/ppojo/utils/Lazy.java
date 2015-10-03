package org.ppojo.utils;

import java.util.function.Supplier;

/**
 * Created by GARY on 10/2/2015.
 */
public class Lazy<T> implements Supplier<T> {

    private Supplier<T> _getter;
    private Supplier<T> _valueSupplier;
    private boolean _isInitialized;
    private T _value;
    Lazy(Supplier<T> valueSupplier) {
        _valueSupplier=valueSupplier;
    }

    @Override
    public T get() {
        if (!_isInitialized) {
            synchronized (this) {
                if (!_isInitialized) {
                    _value=_valueSupplier.get();
                    _valueSupplier=null;
                    _isInitialized=true;
                }
            }
        }
        return _getter.get();
    }
}
