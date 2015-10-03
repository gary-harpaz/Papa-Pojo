package org.ppojo;

import org.ppojo.data.CopyStyleData;
import org.ppojo.utils.EmptyArray;
import org.ppojo.utils.EnumParser;
import org.ppojo.utils.MapChain;
import org.ppojo.utils.MapChainValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by GARY on 9/23/2015.
 */
public class ArtifactOptions extends MapChain {

    static  {
        Map<String,Object> defaultProperties=new HashMap<>();
        defaultProperties.put(ArtifactOptions.Fields.privateFieldPrefix.toString(),"_");
        defaultProperties.put(ArtifactOptions.Fields.encapsulateFields.toString(),true);
        defaultProperties.put(ArtifactOptions.Fields.propertyCapitalization.toString(),CapitalizationTypes.camelCase);
        defaultProperties.put(ArtifactOptions.Fields.publicFieldCapitalization.toString(),CapitalizationTypes.camelCase);
        defaultProperties.put(ArtifactOptions.Fields.getterPrefix.toString(),"get");
        defaultProperties.put(ArtifactOptions.Fields.setterPrefix.toString(),"set");
        defaultProperties.put(ArtifactOptions.Fields.privateFieldName.toString(),"data");
        defaultProperties.put(ArtifactOptions.Fields.imports.toString(), EmptyArray.get(String.class));
        defaultProperties.put(ArtifactOptions.Fields.enumCapitalization.toString(),CapitalizationTypes.camelCase);
        defaultProperties.put(ArtifactOptions.Fields.undefinedMember.toString(),"");
        defaultProperties.put(ArtifactOptions.Fields.constantMemberCapitalization.toString(),CapitalizationTypes.ALL_CAPS);
        defaultProperties.put(ArtifactOptions.Fields.constantValueCapitalization.toString(),CapitalizationTypes.camelCase);
        defaultProperties.put(ArtifactOptions.Fields.indentString.toString(),"    ");
        defaultProperties.put(ArtifactOptions.Fields.pojoCopyStyles.toString(),EmptyArray.get(CopyStyleData.class));
        _defaultOptions=new ArtifactOptions("Default",defaultProperties,null);
    }
    public ArtifactOptions(String name,@Nullable Map<String,Object> localProperties, @Nullable ArtifactOptions parent) {
        super(name,localProperties, parent);
        if (parent==null) {
            if (localProperties==null)
                throw new NullPointerException("All default options must have values");
            for (Fields fields : Fields.values()) {
                Object value=localProperties.get(fields.toString());
                if (value==null && fields!=Fields.Unknown)
                    throw new NullPointerException("Default value for "+fields+" can not be null");
            }
        }
        else {
            if (localProperties!=null) {
                localProperties.forEach((k,v)->{
                    if (v==null)
                        throw new NullPointerException("Null value properties are not allowed "+k.toString()+" value is null");
                });
            }
        }

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

    public CapitalizationTypes getPublicFieldCapitalization() {
        return getValue(Fields.publicFieldCapitalization);
    }

    public String getPrivateFieldName() {
        return getValue(Fields.privateFieldName);
    }

    public String getIndentString() {
        return getValue(Fields.indentString);
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
        publicFieldCapitalization(CapitalizationTypes.class),
        undefinedMember(String.class),
        privateFieldName(String.class),
        constantMemberCapitalization(CapitalizationTypes.class),
        constantValueCapitalization(CapitalizationTypes.class),
        indentString(String.class),
        pojoCopyStyles(CopyStyleData[].class)
        ;


        private final Class _optionType;
        public Class getOptionType() {
            return _optionType;
        }


        Fields(Class optionType) {
            _optionType =optionType;
            if (_optionType==null || _optionType.isPrimitive() || _optionType.isEnum() || _optionType==String.class
                    || _optionType==CopyStyleData[].class) //TODO define formatter for this
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
