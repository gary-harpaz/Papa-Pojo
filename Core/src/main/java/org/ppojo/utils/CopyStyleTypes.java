package org.ppojo.utils;

/**
 * Created by GARY on 10/3/2015.
 */
public enum CopyStyleTypes {
    staticMethod("copy"),
    staticFactory("copy"),
    copyConstructor(""),
    memberFactory("copy"),
    memberCopyTo("copyTo"),
    memberCopyFrom("copyFrom");

    CopyStyleTypes(String defaultMethodName) {
        _defaultMethodName=defaultMethodName;
    }

    private final String _defaultMethodName;
    public String getDefaultMethodName() {
        return _defaultMethodName;
    }


    private static EnumParser<CopyStyleTypes> _parser=new EnumParser<>(CopyStyleTypes.class,null);

    public static CopyStyleTypes Parse(String capitalizationType) {
        return _parser.Parse(capitalizationType);
    }



}
