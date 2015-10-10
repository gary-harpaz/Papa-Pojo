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

import java.util.ArrayList;

/**
 * Helper class for constructing array lists using a fluent API.
 */
public class ArrayListBuilder<T> {

    private ArrayList<T> _arrayList;

    public ArrayListBuilder(T firstItem) {
        _arrayList=new ArrayList<>();
        if (firstItem!=null)
            _arrayList.add(firstItem);
    }
    public static <S> ArrayListBuilder<S> newArrayList(S firstItem) {
        return new ArrayListBuilder<>(firstItem);
    }

    public static <S> ArrayListBuilder<S> newArrayList() {
        return new ArrayListBuilder<>(null);
    }

    public ArrayListBuilder<T> add(T item) {
        _arrayList.add(item);
        return this;
    }

    public  ArrayList<T> create() {
        return _arrayList;
    }

}
