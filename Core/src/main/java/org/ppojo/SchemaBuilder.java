package org.ppojo;

import javax.annotation.Nonnull;
/**
 * Created by GARY on 9/23/2015.
 */
public class SchemaBuilder {
    private Schema _schema=new Schema();
    public static @Nonnull SchemaBuilder newBuilder() {
        return new SchemaBuilder();
    }
    public SchemaBuilder addField(SchemaField field) {
        _schema.setField(field);
        return this;
    }

    public Schema create() {
        return _schema;
    }
}
