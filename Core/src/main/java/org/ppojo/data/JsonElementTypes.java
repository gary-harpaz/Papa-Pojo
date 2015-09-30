package org.ppojo.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by GARY on 9/30/2015.
 */
public enum JsonElementTypes {
    Unknown,
    JsonObject,
    JsonArray,
    JsonNull,
    Boolean,
    Number,
    String;

    public static JsonElementTypes getType(JsonElement json) {
        if (json.isJsonObject())
            return JsonElementTypes.JsonObject;
        if (json.isJsonArray())
            return JsonElementTypes.JsonArray;
        if (json.isJsonNull())
            return JsonElementTypes.JsonNull;
        if (json.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive=json.getAsJsonPrimitive();
            if (jsonPrimitive.isBoolean())
                return JsonElementTypes.Boolean;
            if (jsonPrimitive.isNumber())
                return JsonElementTypes.Number;
            if (jsonPrimitive.isString())
                return JsonElementTypes.String;
        }
        return JsonElementTypes.Unknown;
    }
}

