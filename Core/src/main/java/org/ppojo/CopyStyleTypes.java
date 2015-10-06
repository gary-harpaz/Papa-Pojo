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

import org.ppojo.utils.EnumParser;

/**
 * Created by GARY on 10/3/2015.
 */
public enum CopyStyleTypes {
    staticMethod("copy",false),
    staticFactory("copy",true),
    copyConstructor("",true),
    memberFactory("copy",true),
    memberCopyTo("copyTo",false),
    memberCopyFrom("copyFrom",false);

    CopyStyleTypes(String defaultMethodName,boolean isFactory) {
        _defaultMethodName=defaultMethodName;
        _isFacotry=isFactory;
    }

    private final String _defaultMethodName;
    private final boolean _isFacotry;

    public String getDefaultMethodName() {
        return _defaultMethodName;
    }

    public boolean isFactory() {
        return _isFacotry;
    }

    private static EnumParser<CopyStyleTypes> _parser=new EnumParser<>(CopyStyleTypes.class,null);

    public static CopyStyleTypes Parse(String capitalizationType) {
        return _parser.Parse(capitalizationType);
    }



}
