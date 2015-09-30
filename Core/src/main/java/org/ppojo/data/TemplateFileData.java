package org.ppojo.data;

import org.ppojo.utils.Helpers;
import org.ppojo.TemplateSchemaRelationTypes;

import java.util.List;
import java.util.Map;

/**
 * Created by GARY on 9/23/2015.
 */
public class TemplateFileData {

    public Map<String,Object> fields;
    public String[] subTemplateFields;
    public transient ArtifactData[] artifacts;
    public String linkTo;
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
        return TemplateSchemaRelationTypes.Unknown;
    }
    public static final String FILE_EXTENSION = "pppj";
    public static class Fields {
        public  static final String FIELDS="fields";
        public  static final String SUB_TEMPLATEFIELDS="subTemplateFields";
        public  static final String ARTIFACTS="artifacts";
        public  static final String LINK_TO="linkTo";
        public  static final String OPTIONS="options";
    }
}
