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
import org.ppojo.*;
import org.ppojo.ArtifactTypes;
import org.ppojo.exceptions.EnumParseException;
import org.ppojo.exceptions.InvalidArtifactType;
import org.ppojo.exceptions.JsonArrayMixedItemTypes;
import org.ppojo.exceptions.RequiredPropertyMissing;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for deserializing and validating JSON elements of artifacts to corresponding {@link ArtifactData}
 * @see Serializer
 * @see TemplateSerializer
 * @see CopyStyleDataSerializer
 */
public class ArtifactSerializer  implements JsonDeserializer<ArtifactData> {


    public ArtifactSerializer(Serializer serializer) {
        _serializer=serializer;
    }


    private final Serializer _serializer;
    private Gson _gson;

    public void setGson(Gson gson) {
        _gson = gson;
    }

    private String getDeserializeFilePath() {
        return _serializer.getDeserializeFilePath();
    }

    @Override
    public ArtifactData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if (!json.isJsonObject())
            throw new JsonParseException("Invalid element type for artifact, expected JsonObject got "
                    + JsonElementTypes.getType(json)+", in "+ getDeserializeFilePath());
        String artifactName=readStringProperty(json,"","name");
        String type=readStringProperty(json,artifactName,"type");
        ArtifactTypes artifactTypes=ArtifactTypes.Parse(type);
        if (artifactTypes==ArtifactTypes.Unknown)
            throw new InvalidArtifactType("Invalid artifact type "+type+" at "+artifactName+" in template "+ getDeserializeFilePath());
        ArtifactMetaData artifactMetaData=ArtifactMetaData.getArtifactMetaData(artifactTypes);
        Map<String,Object> localOptions=null;
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
            JsonElementTypes elementType=JsonElementTypes.getType(entry.getValue());
            String propertyName=entry.getKey();
            ArtifactOptions.Fields optionsProperty=ArtifactOptions.Fields.Parse(propertyName);
            if (optionsProperty!=ArtifactOptions.Fields.Unknown) {
                ArtifactMetaData.validateOptionsProperty(artifactMetaData, optionsProperty, elementType, artifactName,  getDeserializeFilePath());
                Object optionValue=context.deserialize(entry.getValue(),optionsProperty.getOptionType());
                if (optionsProperty.getOptionType().isEnum() && optionValue==null) {
                    throw new EnumParseException(entry.getValue().getAsString(),propertyName,artifactName, getDeserializeFilePath());
                }
                if (localOptions==null)
                    localOptions=new HashMap<>();
                localOptions.put(optionsProperty.toString(),optionValue);
            }
            else
                ArtifactMetaData.validateArtifactProperty(artifactMetaData, propertyName,elementType,artifactName, getDeserializeFilePath());

            if (elementType==JsonElementTypes.JsonArray)
            {
                for (JsonElement arrayElement : entry.getValue().getAsJsonArray()) {
                    JsonElementTypes arrayElementType=JsonElementTypes.getType(arrayElement);
                    if (arrayElementType!=JsonElementTypes.String && arrayElementType!=JsonElementTypes.JsonObject)
                        throw new JsonArrayMixedItemTypes("Invalid json array property "+entry.getKey()+" of artifact "+artifactName+". Item types must all be String o JsonObject items. in "+ getDeserializeFilePath());
                }
            }
        }
        ArtifactData result;
        result=_gson.fromJson(json,artifactMetaData.getArtifactClass());
        result.options=localOptions;
        return result;
    }

    private String readStringProperty(JsonElement json,String artifact_name,String propertyName) {
        return readStringProperty(json,artifact_name,propertyName,true,null);
    }

    private String readStringProperty(JsonElement json,String artifact_name,String propertyName,boolean isRequired,String defaultValue) {
        JsonElement typeElement = json.getAsJsonObject().get(propertyName);
        if (typeElement==null)
            if (isRequired)
                throw new RequiredPropertyMissing("Required property "+propertyName+" is missing in artifact "+artifact_name+" declaration in "+ getDeserializeFilePath());
            else
                return  defaultValue;
        if (!typeElement.isJsonPrimitive() || !typeElement.getAsJsonPrimitive().isString())
            throw new JsonParseException("Invalid element type for property: "+propertyName+" in artifact "+artifact_name+", expected String got "
                    + JsonElementTypes.getType(typeElement)+", in "+ getDeserializeFilePath());
        return typeElement.getAsString();
    }
}
