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
import org.ppojo.exceptions.EnumParseException;
import org.ppojo.CopyStyleTypes;
import org.ppojo.utils.EmptyArray;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.ppojo.utils.Helpers.EmptyIfNull;

/**
 * Responsible for deserializing an array of copy styles JSON elements to array of {@link CopyStyleData}.
 * @see CopyStyleTypes
 * @see CopyStyleData
 * @see Serializer
 * @see ArtifactSerializer
 * @see TemplateSerializer
 */
public class CopyStyleDataSerializer  implements JsonDeserializer<CopyStyleData[]> {

    public CopyStyleDataSerializer(Serializer serializer) {
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
    public CopyStyleData[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonArray())
            throw new JsonParseException("Invalid element type for option pojoCopyStyles, expected JsonArray got "
                    + JsonElementTypes.getType(json)+", in "+ getDeserializeFilePath());
        JsonArray array=json.getAsJsonArray();
        if (array.size()==0)
            return EmptyArray.get(CopyStyleData.class);
        CopyStyleData[] result=new CopyStyleData[array.size()];
        int memberIndex=0;
        for (JsonElement element : array) {
            result[memberIndex]=ParseJsonArrayElement(element,memberIndex);
            memberIndex++;
        }
        Set<CopyStyleTypes> styleTypes=new HashSet<>();
        for (CopyStyleData copyStyleData : result) {
            if (!styleTypes.contains(copyStyleData.style))
                styleTypes.add(copyStyleData.style);
            else
                throw new JsonParseException("Invalid definition for option pojoCopyStyles, duplicate entries for style type "+copyStyleData.style+" exist. This is not allowed in "+ getDeserializeFilePath());
        }
        return result;
    }

    private CopyStyleData ParseJsonArrayElement(JsonElement element,int memberIndex) {
        CopyStyleData copyStyleData;
        JsonElementTypes elementType=JsonElementTypes.getType(element);
        switch (elementType) {
            case String:
                copyStyleData=ParseStyleStringElement(element.getAsString(),memberIndex);
                break;
            case JsonObject:
                copyStyleData=ParseStyleJsonObject(element.getAsJsonObject(),memberIndex);
                break;
            default:
                throw new JsonParseException("Invalid element type for option pojoCopyStyles["+memberIndex+"], expected String or JsonObject got "
                        + elementType+", in "+ getDeserializeFilePath());
        }
        if (copyStyleData.methodName==null)
            copyStyleData.methodName =copyStyleData.style.getDefaultMethodName();
        else
            if (copyStyleData.methodName.equals("") && copyStyleData.style!=CopyStyleTypes.copyConstructor)
             throw new JsonParseException("Invalid field value option pojoCopyStyles["+memberIndex+"].methodName, value can not be empty, in "+getDeserializeFilePath());
        return copyStyleData;
    }

    private CopyStyleData ParseStyleJsonObject(JsonObject asObject,int memberIndex) {
        CopyStyleData copyStyleData=new CopyStyleData();
        JsonElement styleMember=null;
        JsonElement methodNameMember=null;
        for (Map.Entry<String, JsonElement> elementEntry : asObject.entrySet()) {
            switch (elementEntry.getKey()) {
                case "style":
                    styleMember=elementEntry.getValue();
                    break;
                case "methodName":
                    methodNameMember=elementEntry.getValue();
                    break;
                default:
                    throw new JsonParseException("Invalid member for array element pojoCopyStyles["+memberIndex+"], unsupported field "+elementEntry.getKey()+". Only supports fields style and methodName, in "+ getDeserializeFilePath());
            }
        }
        if (styleMember==null)
            throw new JsonParseException("Invalid element in option pojoCopyStyles["+memberIndex+"], required field: style is missing, in "+ getDeserializeFilePath());
        if (!(styleMember.isJsonPrimitive() && styleMember.getAsJsonPrimitive().isString()))
            throw new JsonParseException("Invalid element type for option pojoCopyStyles["+memberIndex+"] style field, expected String got "
                    + JsonElementTypes.getType(styleMember)+", in "+ getDeserializeFilePath());
        if (methodNameMember!=null && !(methodNameMember.isJsonPrimitive() && methodNameMember.getAsJsonPrimitive().isString()))
            throw new JsonParseException("Invalid element type for option pojoCopyStyles["+memberIndex+"]  methodName field, expected String got "
                    + JsonElementTypes.getType(methodNameMember)+", in "+ getDeserializeFilePath());
        copyStyleData.style= parseStyleType(styleMember.getAsString(), memberIndex);
        if (methodNameMember!=null)
            copyStyleData.methodName=EmptyIfNull(methodNameMember.getAsString());
        return copyStyleData;
    }


    private CopyStyleData ParseStyleStringElement(String styleTypeStr,int memberIndex) {
        CopyStyleData copyStyleData=new CopyStyleData();
        copyStyleData.style=parseStyleType(styleTypeStr,memberIndex);
        return copyStyleData;
    }

    private CopyStyleTypes parseStyleType(String styleTypeStr, int memberIndex) {
        CopyStyleTypes styleType=_gson.fromJson(styleTypeStr,CopyStyleTypes.class);
        if (styleType==null)
            throw new EnumParseException(styleTypeStr,"pojoCopyStyles["+memberIndex+"].style", getDeserializeFilePath());
        return styleType;
    }
}
