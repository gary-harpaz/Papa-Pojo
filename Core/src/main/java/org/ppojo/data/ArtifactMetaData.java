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

import org.ppojo.ArtifactOptions;
import org.ppojo.ArtifactTypes;
import org.ppojo.exceptions.ArtifactElementTypeMismatch;
import org.ppojo.exceptions.ArtifactTypeMetaDataNotFound;
import org.ppojo.exceptions.UnsupportedArtifactProperty;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Contains information about each artifact type, used for validation of properties and options specified in templates.
 */
public class ArtifactMetaData {

    private static Map<ArtifactTypes,ArtifactMetaData> _metaDatas;
    private static Map<String,Field> _baseClassMembers;
    private static Map <String,Field> _allExtendedOptionFields;
    static  {

        _metaDatas =new HashMap<>();
        _baseClassMembers=new HashMap<>();
        _allExtendedOptionFields=new HashMap<>();
        for (Field field : ArtifactData.class.getFields()) {
            _baseClassMembers.put(field.getName(),field);
            Serializer.validateJsonSupportedField(field);
        }


        Set<ArtifactOptions.Fields> supportsOptions=new HashSet<>();
        supportsOptions.add(ArtifactOptions.Fields.indentString);
        supportsOptions.add(ArtifactOptions.Fields.propertyCapitalization);
        supportsOptions.add(ArtifactOptions.Fields.publicFieldCapitalization);
        supportsOptions.add(ArtifactOptions.Fields.encapsulateFields);
        supportsOptions.add(ArtifactOptions.Fields.getterPrefix);
        supportsOptions.add(ArtifactOptions.Fields.imports);
        supportsOptions.add(ArtifactOptions.Fields.privateFieldPrefix);
        supportsOptions.add(ArtifactOptions.Fields.setterPrefix);
        supportsOptions.add(ArtifactOptions.Fields.pojoCopyStyles);
        NewMetaData(ArtifactTypes.Pojo, ClassArtifactData.class, supportsOptions);

        supportsOptions=new HashSet<>();
        supportsOptions.add(ArtifactOptions.Fields.indentString);
        supportsOptions.add(ArtifactOptions.Fields.propertyCapitalization);
        supportsOptions.add(ArtifactOptions.Fields.getterPrefix);
        supportsOptions.add(ArtifactOptions.Fields.imports);
        supportsOptions.add(ArtifactOptions.Fields.setterPrefix);
        NewMetaData(ArtifactTypes.Interface, InterfaceArtifactData.class,supportsOptions);

        supportsOptions=new HashSet<>();
        supportsOptions.add(ArtifactOptions.Fields.indentString);
        supportsOptions.add(ArtifactOptions.Fields.enumCapitalization);
        supportsOptions.add(ArtifactOptions.Fields.undefinedMember);
        supportsOptions.add(ArtifactOptions.Fields.imports);
        NewMetaData(ArtifactTypes.FieldEnum,FieldEnumArtifactData.class,supportsOptions);

        supportsOptions=new HashSet<>();
        supportsOptions.add(ArtifactOptions.Fields.indentString);
        supportsOptions.add(ArtifactOptions.Fields.propertyCapitalization);
        supportsOptions.add(ArtifactOptions.Fields.getterPrefix);
        supportsOptions.add(ArtifactOptions.Fields.imports);
        supportsOptions.add(ArtifactOptions.Fields.privateFieldPrefix);
        supportsOptions.add(ArtifactOptions.Fields.privateFieldName);
        supportsOptions.add(ArtifactOptions.Fields.immutableCopyDataMember);
        supportsOptions.add(ArtifactOptions.Fields.immutableDefensiveCopy);
        NewMetaData(ArtifactTypes.ImmutableClass,ImmutableClassData.class,supportsOptions);

        supportsOptions=new HashSet<>();
        supportsOptions.add(ArtifactOptions.Fields.indentString);
        supportsOptions.add(ArtifactOptions.Fields.propertyCapitalization);
        supportsOptions.add(ArtifactOptions.Fields.setterPrefix);
        supportsOptions.add(ArtifactOptions.Fields.imports);
        supportsOptions.add(ArtifactOptions.Fields.privateFieldPrefix);
        supportsOptions.add(ArtifactOptions.Fields.privateFieldName);
        supportsOptions.add(ArtifactOptions.Fields.fluentNewBuilderName);
        supportsOptions.add(ArtifactOptions.Fields.fluentGetDataName);
        supportsOptions.add(ArtifactOptions.Fields.fluentResetBuilderName);
        NewMetaData(ArtifactTypes.FluentBuilder,FluentBuilderData.class,supportsOptions);
    }

    public static void NewMetaData(ArtifactTypes artifactTypes,Class<? extends ArtifactData> artifactClass,Set<ArtifactOptions.Fields> supportsOptions) {
        ArtifactMetaData artifactMetaData=new ArtifactMetaData(artifactTypes,artifactClass,supportsOptions);
        _metaDatas.put(artifactTypes, artifactMetaData);
    }

    ArtifactMetaData(ArtifactTypes artifactType,Class<? extends ArtifactData> artifactClass,Set<ArtifactOptions.Fields> supportsOptions) {
        _artifactType = artifactType;
        _artifactClass=artifactClass;
        _supportsOptions=supportsOptions;
        _extendedFields =new HashMap<>();
        for (Field field : artifactClass.getFields()) {
            Field aField=_baseClassMembers.get(field.getName());
            if (aField!=null) // a base class field not an extended options field
                continue;
            _extendedFields.put(field.getName(), field);
            aField=_allExtendedOptionFields.get(field.getName());
            if (aField==null)
                _allExtendedOptionFields.put(field.getName(),field);
            else
            if (aField.getType()!=field.getType())
                throw new RuntimeException("Invalid class definition in class "+field.getDeclaringClass().toString()+", field "+field.getName()+
                        " inconsistent field type "+field.getType().toString()+" with field in class "+aField.getDeclaringClass().toString()+" of type "+aField.getType()
                        +". Fields must be of the same type");
            Serializer.validateJsonSupportedField(field);
        }
        for (ArtifactOptions.Fields supportsOption : _supportsOptions) {
            Serializer.validateJsonSupportedOption(supportsOption, artifactClass);
        }
    }

    private final ArtifactTypes _artifactType;
    private final Class<? extends ArtifactData> _artifactClass;
    private final Map<String,Field> _extendedFields;
    private final Set<ArtifactOptions.Fields> _supportsOptions;

    /**
     * @return The target class for deserializing the {@link ArtifactTypes ArtifactType} returned by {@link ArtifactMetaData#getArtifactType()}
     */
    public Class<? extends ArtifactData> getArtifactClass() {
        return _artifactClass;
    }
    /**
     * Every artifact type supports a different set of options that can be applied to it. During deserialization if unrecognized options are applied to the artifact
     * an exception is thrown.
     * @return The {@link org.ppojo.ArtifactOptions.Fields ArtifactOptions} applicable to the {@link ArtifactTypes ArtifactType} returned by {@link ArtifactMetaData#getArtifactType()}
     */
    public Iterable<ArtifactOptions.Fields> getSupportedOptions() {
        return _supportsOptions;
    }

    public ArtifactTypes getArtifactType() {
        return _artifactType;
    }

    public static void validateArtifactProperty(ArtifactMetaData artifactMetaData, String propertyName, JsonElementTypes elementType
            ,String artifactName,String templateFilePath) {
        Field field=_baseClassMembers.get(propertyName);
        if (field==null) {
            field=artifactMetaData._extendedFields.get(propertyName);
            if (field==null)
                throw new UnsupportedArtifactProperty(propertyName,artifactMetaData.getArtifactType(),artifactName,templateFilePath);
        }
        JsonElementTypes expectedElementType=Serializer.validateJsonSupportedField(field);
        if (expectedElementType!=elementType)
            throw new ArtifactElementTypeMismatch(propertyName,artifactMetaData.getArtifactType(),expectedElementType,elementType,artifactName,templateFilePath);
    }


    public static void validateOptionsProperty(ArtifactMetaData artifactMetaData, ArtifactOptions.Fields optionsProperty, JsonElementTypes elementType,
                                               String artifactName, String templateFilePath) {
        if (!artifactMetaData.isOptionSupported(optionsProperty))
            throw new UnsupportedArtifactProperty(optionsProperty.toString(),artifactMetaData.getArtifactType(),artifactName,templateFilePath);
        JsonElementTypes expectedElementType=Serializer.validateJsonSupportedOption(optionsProperty, artifactMetaData.getClass());
        if (expectedElementType!=elementType)
            throw new ArtifactElementTypeMismatch(optionsProperty.toString(),artifactMetaData.getArtifactType(),expectedElementType,elementType,artifactName,templateFilePath);

    }

    private boolean isOptionSupported(ArtifactOptions.Fields optionsProperty) {
        return _supportsOptions.contains(optionsProperty);
    }

    public static @Nonnull
    ArtifactMetaData getArtifactMetaData(ArtifactTypes artifactTypes) {
        ArtifactMetaData metaData=_metaDatas.get(artifactTypes);
        if (metaData==null)
            throw new ArtifactTypeMetaDataNotFound("Can not find artifact metadata for type "+artifactTypes);
        return metaData;
    }
}
