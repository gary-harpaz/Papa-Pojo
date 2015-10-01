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
 * Created by GARY on 9/30/2015.
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
        supportsOptions.add(ArtifactOptions.Fields.propertyCapitalization);
        supportsOptions.add(ArtifactOptions.Fields.publicFieldCapitalization);
        supportsOptions.add(ArtifactOptions.Fields.encapsulateFields);
        supportsOptions.add(ArtifactOptions.Fields.getterPrefix);
        supportsOptions.add(ArtifactOptions.Fields.imports);
        supportsOptions.add(ArtifactOptions.Fields.privateFieldPrefix);
        supportsOptions.add(ArtifactOptions.Fields.setterPrefix);
        NewMetaData(ArtifactTypes.Pojo, ClassArtifactData.class,supportsOptions);

        supportsOptions=new HashSet<>();
        supportsOptions.add(ArtifactOptions.Fields.propertyCapitalization);
        supportsOptions.add(ArtifactOptions.Fields.getterPrefix);
        supportsOptions.add(ArtifactOptions.Fields.imports);
        supportsOptions.add(ArtifactOptions.Fields.setterPrefix);
        NewMetaData(ArtifactTypes.Interface, InterfaceArtifactData.class,supportsOptions);

        supportsOptions=new HashSet<>();
        supportsOptions.add(ArtifactOptions.Fields.enumCapitalization);
        supportsOptions.add(ArtifactOptions.Fields.undefinedMember);
        supportsOptions.add(ArtifactOptions.Fields.imports);
        NewMetaData(ArtifactTypes.FieldEnum,FieldEnumArtifactData.class,supportsOptions);
    }



    public static void Init() {

    }

    public static void NewMetaData(ArtifactTypes artifactTypes,Class<? extends ArtifactData> artifactClass,Set<ArtifactOptions.Fields> supportsOptions) {
        ArtifactMetaData artifactMetaData=new ArtifactMetaData(artifactTypes,artifactClass,supportsOptions);
        _metaDatas.put(artifactTypes, artifactMetaData);
    }

    ArtifactMetaData(ArtifactTypes artifactTypes,Class<? extends ArtifactData> artifactClass,Set<ArtifactOptions.Fields> supportsOptions) {
        _artifactTypes=artifactTypes;
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


    private final ArtifactTypes _artifactTypes;
    private final Class<? extends ArtifactData> _artifactClass;
    private final Map<String,Field> _extendedFields;
    private final Set<ArtifactOptions.Fields> _supportsOptions;
    public Iterable<ArtifactOptions.Fields> getSupportedOptions() {
        return _supportsOptions;
    }

    public ArtifactTypes getArtifactTypes() {
        return _artifactTypes;
    }


    public static void validateArtifactProperty(ArtifactMetaData artifactMetaData, String propertyName, JsonElementTypes elementType
            ,String artifactName,String templateFilePath) {
        Field field=_baseClassMembers.get(propertyName);
        if (field==null) {
            field=artifactMetaData._extendedFields.get(propertyName);
            if (field==null)
                throw new UnsupportedArtifactProperty(propertyName,artifactMetaData.getArtifactTypes(),artifactName,templateFilePath);
        }
        JsonElementTypes expectedElementType=Serializer.validateJsonSupportedField(field);
        if (expectedElementType!=elementType)
            throw new ArtifactElementTypeMismatch(propertyName,artifactMetaData.getArtifactTypes(),expectedElementType,elementType,artifactName,templateFilePath);
    }


    public static void validateOptionsProperty(ArtifactMetaData artifactMetaData, ArtifactOptions.Fields optionsProperty, JsonElementTypes elementType,
                                               String artifactName, String templateFilePath) {
        if (!artifactMetaData.isOptionSupported(optionsProperty))
            throw new UnsupportedArtifactProperty(optionsProperty.toString(),artifactMetaData.getArtifactTypes(),artifactName,templateFilePath);
        JsonElementTypes expectedElementType=Serializer.validateJsonSupportedOption(optionsProperty, artifactMetaData.getClass());
        if (expectedElementType!=elementType)
            throw new ArtifactElementTypeMismatch(optionsProperty.toString(),artifactMetaData.getArtifactTypes(),expectedElementType,elementType,artifactName,templateFilePath);

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
