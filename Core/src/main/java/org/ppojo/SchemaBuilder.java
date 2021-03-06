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

package org.ppojo;

import javax.annotation.Nonnull;
/**
 * Builder class for creating {@link Schema} objects using a fluent API.
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
