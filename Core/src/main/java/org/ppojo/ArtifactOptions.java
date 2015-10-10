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

import org.ppojo.data.CopyStyleData;
import org.ppojo.utils.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.ppojo.*;

/**
 * Extends the {@link MapChain} infrastructure class with logic specific for customizing artifact generation.
 * All available options are enumerated by {@link ArtifactOptions.Fields} and exposed via type safe member methods.
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
        defaultProperties.put(ArtifactOptions.Fields.immutableCopyDataMember .toString(),"");
        defaultProperties.put(ArtifactOptions.Fields.immutableDefensiveCopy.toString(),false);
        defaultProperties.put(ArtifactOptions.Fields.fluentNewBuilderName.toString(),"newBuilder");
        defaultProperties.put(ArtifactOptions.Fields.fluentGetDataName.toString(),"getData");
        defaultProperties.put(ArtifactOptions.Fields.fluentResetBuilderName.toString(),"reset");
        _defaultOptions=new ArtifactOptions("Default",defaultProperties,null);
    }
    public ArtifactOptions(String name,@Nullable Map<String,Object> localProperties, @Nullable ArtifactOptions parent) {
        this(name,localProperties,parent,false);
    }
    public ArtifactOptions(String name,@Nullable Map<String,Object> localProperties, @Nullable ArtifactOptions parent,boolean debugFlag) {
        super(name,localProperties, parent);
        _debugFlag=debugFlag;
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
    private final boolean _debugFlag;
    public boolean isDebugFlag() {
        return _debugFlag;
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

    public CopyStyleData[] getPojoCopyStyles() {
        return getValue(Fields.pojoCopyStyles);
    }

    public String getImmutableCopyDataMember() {
        return getValue(Fields.immutableCopyDataMember);
    }

    public boolean getIsDefensiveCopy() {
        return getValue(Fields.immutableDefensiveCopy);
    }

    public String getFluentResetBuilderName() {
        return getValue(Fields.fluentResetBuilderName);
    }

    public String getFluentNewBuilderName() {
        return getValue(Fields.fluentNewBuilderName);
    }

    public String getFluentGetDataName() {
        return getValue(Fields.fluentGetDataName);
    }

    /**
     * All available options for customizing artifact code generation. See the wiki for details about them.
     */
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
        pojoCopyStyles(CopyStyleData[].class),
        immutableCopyDataMember(String.class),
        immutableDefensiveCopy(boolean.class),
        fluentNewBuilderName(String.class),
        fluentGetDataName(String.class),
        fluentResetBuilderName(String.class)
        ;


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
                    if (_optionType==CopyStyleData[].class)
                        _valueFormatter=Fields::copyStylesFormatter;
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

        private static String copyStylesFormatter(Object value) {
            if (value == null)
                return "null";
            if (!(value instanceof  CopyStyleData[]))
                return value.toString();
            CopyStyleData[] copyStylesArray=(CopyStyleData[])value;
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("[ ");
            for (CopyStyleData copyStyleData : copyStylesArray) {
                stringBuilder.append("{ style : ").append(copyStyleData.style.toString());
                if (!Helpers.IsNullOrEmpty(copyStyleData.methodName)) {
                    stringBuilder.append(", methodName : ").append(copyStyleData.methodName);
                }
                stringBuilder.append(" }");

            }
            stringBuilder.append(" ]");
            return stringBuilder.toString();
        }
    }
}
