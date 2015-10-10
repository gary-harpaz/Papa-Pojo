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

import java.util.function.Supplier;

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
