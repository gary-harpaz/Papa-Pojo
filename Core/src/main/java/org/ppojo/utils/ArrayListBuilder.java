package org.ppojo.utils;

import java.util.ArrayList;

/**
 * Created by GARY on 9/27/2015.
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
