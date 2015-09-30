package org.ppojo;

import org.ppojo.utils.EnumParser;
import org.ppojo.utils.MapChain;
import org.ppojo.utils.MapChainValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by GARY on 9/23/2015.
 */
public class ArtifactOptions extends MapChain {

    static  {
        Map<String,Object> defaultProperties=new HashMap<>();
        defaultProperties.put(Fields.privateFieldPrefix.toString(),"_");
        defaultProperties.put(Fields.encapsulateFields.toString(),true);
        defaultProperties.put(Fields.propertyCapitalization.toString(),CapitalizationTypes.camelCase);
        defaultProperties.put(Fields.getterPrefix.toString(),"get");
        defaultProperties.put(Fields.setterPrefix.toString(),"set");

        _defaultOptions=new ArtifactOptions("Default",defaultProperties,null);
    }
    public ArtifactOptions(String name,@Nullable Map<String,Object> localProperties, @Nullable ArtifactOptions parent) {
        super(name,localProperties, parent);
    }
    private static ArtifactOptions _defaultOptions;


    public static @Nonnull ArtifactOptions getDefaultOptions() {
        return _defaultOptions;
    }

    private <T> T getValue(Fields fieldName) {
        T result=null;
        MapChainValue value=get(fieldName.toString());
        if (value!=null && value.getValue()!=null)
            result=(T)value.getValue();
        return result;
    }

    public String[] getImports() {
        return getValue(Fields.imports);
    }

    public boolean getEncapsulateFields() {
        return getValue(Fields.encapsulateFields);
    }

    public String getPrivateFieldPrefix() {
        return getValue(Fields.privateFieldPrefix);
    }

    public CapitalizationTypes getCapitalization() {
        return getValue(Fields.propertyCapitalization);
    }

    public String getGetterPrefix() {
        return getValue(Fields.getterPrefix);
    }

    public String getSetterPrefix() {
        return getValue(Fields.setterPrefix);
    }


    public CapitalizationTypes getEnumCapitalization() {
        return getValue(Fields.enumCapitalization);
    }

    public String getUndefinedMember() {
        return getValue(Fields.undefinedMember);
    }

    public enum Fields {
        Unknown(null),
        imports(String[].class),
        privateFieldPrefix(String.class),
        encapsulateFields(boolean.class),
        propertyCapitalization(CapitalizationTypes.class),
        getterPrefix(String.class),
        setterPrefix(String.class),
        enumCapitalization(CapitalizationTypes.class),
        undefinedMember(String.class),
        constantMemberCapitalization(CapitalizationTypes.class),
        constantValueCapitalization(CapitalizationTypes.class);

        private final Class _optionType;
        public Class getOptionType() {
            return _optionType;
        }


        Fields(Class optionType) {
            _optionType =optionType;
            if (_optionType==null || _optionType.isPrimitive() || _optionType.isEnum() || _optionType==String.class)
                _valueFormatter=Fields::DefaultFormatter;
            else
                if (_optionType==String[].class)
                    _valueFormatter=Fields::StringArrayFormatter;
                else
                    throw new RuntimeException("Unsupported formatter in "+this.toString()+" for "+_optionType.toString());
        }

        private final Function<Object,String> _valueFormatter;
        public String FormatValue(Object value) {
            return _valueFormatter.apply(value);
        }

        private static final EnumParser<Fields> _parser =new EnumParser<>(Fields.class,Fields.Unknown);

        public static @Nonnull Fields Parse(String option) {
            return _parser.Parse(option);
        }

        private static String DefaultFormatter(Object value) {
            if (value==null)
                return "null";
            return value.toString();
        }

        private static String StringArrayFormatter(Object value) {
            if (value == null)
                return "null";
            String[] array = (String[]) value;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[ ");
            for (int i = 0; i < array.length; i++) {
                if (i > 0)
                    stringBuilder.append(", ");
                stringBuilder.append(array[i]);
            }
            stringBuilder.append(" ]");
            return stringBuilder.toString();
        }
    }
}
