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

import com.google.gson.*;
import org.ppojo.ArtifactOptions;
import org.ppojo.exceptions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for deserializing and validating the JSON element specified in the papa pojo template file to corresponding {@link TemplateFileData}
 * @see Serializer
 * @see ArtifactSerializer
 * @see CopyStyleDataSerializer
 */
public class TemplateSerializer  implements JsonDeserializer<TemplateFileData> {


    private static Map<String,Field> _templateFileDataMembers;
    static  {
        //ArtifactMetaData.Init();
        _templateFileDataMembers =new HashMap<>();
        for (Field field : TemplateFileData.class.getFields()) {
            _templateFileDataMembers.put(field.getName(), field);
            Serializer.validateJsonSupportedField(field);
        }
    }

    private  final Serializer _serializer;
    private Gson _gson;

    public TemplateSerializer(Serializer serializer)
    {
        _serializer=serializer;

    }

    public static void validateTemplateProperty(String propertyName, JsonElementTypes elementType
            ,String templateFilePath) {
        Field field= _templateFileDataMembers.get(propertyName);
        if (field==null) {
            throw new UnsupportedTemplateProperty(propertyName,templateFilePath);
        }
        JsonElementTypes expectedElementType=Serializer.validateJsonSupportedField(field);
        if (expectedElementType!=elementType)
            throw new TemplateElementTypeMismatch(propertyName,expectedElementType,elementType,templateFilePath);
    }


    @Override
    public TemplateFileData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonParseException("Invalid element type for template, expected JsonObject got "
                    + JsonElementTypes.getType(json)+", in "+ getDeserializeFilePath());
        ArtifactData[] artifacts=null;
        Map<String,Object> options=null;
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
            JsonElementTypes elementType=JsonElementTypes.getType(entry.getValue());
            String propertyName=entry.getKey();
            validateTemplateProperty(propertyName,elementType,getDeserializeFilePath());
            switch (propertyName) {
                case TemplateFileData.Fields.ARTIFACTS:
                    artifacts=context.deserialize(entry.getValue(),ArtifactData[].class);
                    break;
                case TemplateFileData.Fields.OPTIONS:
                    options=deserializeOptions(entry.getValue(),context);
                    break;
            }
        }
        TemplateFileData result=_gson.fromJson(json,TemplateFileData.class);
        result.artifacts=artifacts;
        result.options=options;
        return result;
    }

    private Map<String,Object> deserializeOptions(JsonElement optionsElement,JsonDeserializationContext context) {
        Map<String,Object> optionsResult=new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : optionsElement.getAsJsonObject().entrySet()) {
            String propertyName=entry.getKey();
            ArtifactOptions.Fields optionsProperty=ArtifactOptions.Fields.Parse(propertyName);
            if (optionsProperty==ArtifactOptions.Fields.Unknown)
                throw new UnsupportedTemplateOption(propertyName,getDeserializeFilePath());
            JsonElementTypes elementType=JsonElementTypes.getType(entry.getValue());
            JsonElementTypes expectedElementType=Serializer.validateJsonSupportedOption(optionsProperty,TemplateFileData.class);
            if (expectedElementType!=elementType)
                throw new TemplateElementTypeMismatch(propertyName,expectedElementType,elementType,getDeserializeFilePath());
            Object optionValue=context.deserialize(entry.getValue(),optionsProperty.getOptionType());
            if (optionsProperty.getOptionType().isEnum() && optionValue==null) {
                throw new EnumParseException(entry.getValue().getAsString(),propertyName, getDeserializeFilePath());
            }
            optionsResult.put(optionsProperty.toString(),optionValue);
        }
        return optionsResult;
    }

    private String getDeserializeFilePath() {
        return _serializer.getDeserializeFilePath();
    }

    public void setGson(Gson gson) {
        _gson=gson;
    }
}
