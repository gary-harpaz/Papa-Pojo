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

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Helper enum for classifying {@link JsonElement} types used in Gson library.
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

