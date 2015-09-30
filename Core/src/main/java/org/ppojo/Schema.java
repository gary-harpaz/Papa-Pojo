package org.ppojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GARY on 9/23/2015.
 */
public class Schema implements ISchema {
    public Schema() {

    }
    private final Map<String,SchemaField> _fields=new HashMap<>();


    public void setField(SchemaField field) {
        _fields.put(field.getName(),field);
    }
    public void removeField(SchemaField field) {
        removeField(field.getName());
    }
    public void removeField(String name) {
        _fields.remove(name);
    }


    @Override
    public Iterable<SchemaField> getFields() {
        return _fields.values();
    }

    public SchemaField getField(String fieldName) {
        return _fields.get(fieldName);
    }
}
