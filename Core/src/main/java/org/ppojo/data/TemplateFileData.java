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

package org.ppojo.data;

import org.ppojo.utils.Helpers;
import org.ppojo.TemplateSchemaRelationTypes;

import java.util.List;
import java.util.Map;

/**
 * Represents the raw template data after being deserialized.
 */
public class TemplateFileData {

    /**
     * For a template which defines a {@link TemplateSchemaRelationTypes#SchemaSource} represents the fields of the schema.
     * The map key corresponds to field names and the values usually correspond to the field type in the generated JAVA source file.
     * In future versions it may be possible to specify additional field properties other then the data type using JSON Objects.
     */
    public Map<String,Object> fields;
    /**
     * For a template which defines a {@link TemplateSchemaRelationTypes#SubSchema} represents the subset of fields included in the sub schema.
     * The values must match to existing field names defined in the linked source schema.
     * Additionally the {@link TemplateFileData#linkTo} field must be set to the relative path to the source schema file.
     */
    public String[] subTemplateFields;
    /**
     * Holds the deserialized artifacts defined in the schema file. Note the field is transient.
     * Customized logic for deserializing this data is defined in {@link ArtifactSerializer}.
     */
    public transient ArtifactData[] artifacts;
    /**
     * The value of linkTo is relevant for {@link TemplateSchemaRelationTypes#SubSchema} and for {@link TemplateSchemaRelationTypes#SchemaLink}.
     * It must specify a relative path to the linked schema file.
     */
    public String linkTo;
    /**
     * Options to configure artifacts that are specified at the template level which affect all artifacts defined in the template file.
     * When specified at this level they will override options defined at the default options level.
     * Note that this field is transient. The values are deserialized manually in {@link TemplateSerializer#deserialize}.*
     */
    public transient Map<String,Object> options;

    public TemplateSchemaRelationTypes getSchemaRelationTypes() {
        if (fields!=null)
            return TemplateSchemaRelationTypes.SchemaSource;
        else
            if (subTemplateFields!=null)
                return TemplateSchemaRelationTypes.SubSchema;
            else
                if (!Helpers.IsNullOrEmpty(linkTo))
                    return TemplateSchemaRelationTypes.SchemaLink;
                else
                  if (options!=null)
                      return TemplateSchemaRelationTypes.OptionsConfig;
        return TemplateSchemaRelationTypes.Unknown;
    }

    /**
     * Default file extension for papa pojo template files.
     */
    public static final String FILE_EXTENSION = "pppj";
    public static class Fields {
        public  static final String FIELDS="fields";
        public  static final String SUB_TEMPLATEFIELDS="subTemplateFields";
        public  static final String ARTIFACTS="artifacts";
        public  static final String LINK_TO="linkTo";
        public  static final String OPTIONS="options";
    }
}
