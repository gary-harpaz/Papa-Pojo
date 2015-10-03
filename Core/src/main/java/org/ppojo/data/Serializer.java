package org.ppojo.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ppojo.ArtifactOptions;
import org.ppojo.CapitalizationTypes;
import org.ppojo.exceptions.UnsupportedJsonElementType;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GARY on 9/30/2015.
 */
public class Serializer {

    public Serializer() {
        _templateSerializer=new TemplateSerializer(this);
        _artifactSerializer=new ArtifactSerializer(this);
        _copyStyleDataSerializer=new CopyStyleDataSerializer(this);

        CreateNewSerializer();
    }
    private final TemplateSerializer _templateSerializer;
    private final ArtifactSerializer _artifactSerializer;
    private final CopyStyleDataSerializer _copyStyleDataSerializer;
    private Gson _gsonCustomized;
    private Gson _gsonRegular;


    private static Map<Class,JsonElementTypes> _classToJsonElementType;

    static {
        _classToJsonElementType=new HashMap<>();
        _classToJsonElementType.put(String.class,JsonElementTypes.String);
        _classToJsonElementType.put(boolean.class,JsonElementTypes.Boolean);
        _classToJsonElementType.put(Map.class,JsonElementTypes.JsonObject);
        _classToJsonElementType.put(String[].class,JsonElementTypes.JsonArray);
        _classToJsonElementType.put(CapitalizationTypes.class,JsonElementTypes.String);
        _classToJsonElementType.put(ArtifactData[].class,JsonElementTypes.JsonArray);
        _classToJsonElementType.put(CopyStyleData[].class, JsonElementTypes.JsonArray);

    }

    public static JsonElementTypes validateJsonSupportedField(Field field) {
        JsonElementTypes jsonElementTypes=_classToJsonElementType.get(field.getType());
        if (jsonElementTypes==null)
            throw new UnsupportedJsonElementType(field.getType(),field.getName(),field.getDeclaringClass());
        return jsonElementTypes;
    }

    public static JsonElementTypes validateJsonSupportedOption(ArtifactOptions.Fields field,Class appliedClass) {
        JsonElementTypes jsonElementTypes=_classToJsonElementType.get(field.getOptionType());
        if (jsonElementTypes==null)
            throw new UnsupportedJsonElementType(field.getOptionType(),field.toString(),appliedClass);
        return jsonElementTypes;
    }



    public String Serialize(TemplateFileData template)
    {
        return _gsonCustomized.toJson(template);
    }

    private boolean _isPretty =false;


    public boolean getIsPretty() {
        return _isPretty;
    }


    public void setIsPretty(boolean isPretty) {
        _isPretty = isPretty;
        CreateNewSerializer();
    }


    private void CreateNewSerializer() {
        GsonBuilder builder=new GsonBuilder();
        builder.registerTypeAdapter(ArtifactData.class,_artifactSerializer);
        builder.registerTypeAdapter(TemplateFileData.class,_templateSerializer);
        builder.registerTypeAdapter(CopyStyleData[].class,_copyStyleDataSerializer);
        builder= getIsPretty()?builder.setPrettyPrinting():builder;
        _gsonCustomized =builder.create();

        builder=new GsonBuilder();
        builder= getIsPretty()?builder.setPrettyPrinting():builder;
        _gsonRegular =builder.create();
        _artifactSerializer.setGson(_gsonRegular);
        _templateSerializer.setGson(_gsonRegular);
        _copyStyleDataSerializer.setGson(_gsonRegular);




    }


    private String _desrializedFilePath;

    public TemplateFileData deserialize(String json,String filePath) {
        try {
            _desrializedFilePath=filePath;
            return _gsonCustomized.fromJson(json,TemplateFileData.class);
        }
        finally {
            _desrializedFilePath=null;
        }
    }


    public String getDeserializeFilePath() {
        return _desrializedFilePath;
    }
}
