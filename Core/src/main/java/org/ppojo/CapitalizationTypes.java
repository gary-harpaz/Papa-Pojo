package org.ppojo;

import org.ppojo.utils.EnumParser;

/**
 * Created by GARY on 9/23/2015.
 */
public enum CapitalizationTypes {
    None,
    camelCase,
    PascalCase,
    ALL_CAPS
    ;

    private static EnumParser<CapitalizationTypes> _parser=new EnumParser<>(CapitalizationTypes.class,null);

    public static CapitalizationTypes Parse(String capitalizationType) {
        return _parser.Parse(capitalizationType);
    }
}
