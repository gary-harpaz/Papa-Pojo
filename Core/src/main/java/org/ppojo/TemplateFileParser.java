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

import org.ppojo.data.TemplateFileData;
import org.ppojo.utils.Helpers;
import org.ppojo.utils.ValidationResult;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by GARY on 9/25/2015.
 */
public class TemplateFileParser {

    public TemplateFileParser(String filePath,TemplateFileData rawData,ArtifactOptions defaultOptions) {
        _filePath=filePath;
        _rawData=rawData;
        _schemaRelationType=_rawData.getSchemaRelationTypes();
        _defaultOptions=defaultOptions;
        switch (rawData.getSchemaRelationTypes()) {
            case SchemaLink:
            case SubSchema:
                //delay the creation of options until the linked template is resolved
                break;
            default:
                createOptions();
                break;

        }
    }

    private final ArtifactOptions _defaultOptions;
    private final String _filePath;
    private final TemplateFileData _rawData;
    private TemplateFileParser _linkedTemplate;
    private TemplateSchemaRelationTypes _schemaRelationType;
    private boolean _isValid=false;
    private boolean _wasValidateInvoked=false;
    private List<ArtifactParser> _artifactParsers;
    private Schema _schema;
    private ArtifactOptions _options;

    public String getFilePath() {
        return _filePath;
    }
    public TemplateFileData getRawData() {
        return _rawData;
    }
    public TemplateFileParser getLinkedTemplate() {
        return _linkedTemplate;
    }
    public TemplateSchemaRelationTypes getSchemaRelationType() {
        return _schemaRelationType;
    }
    public boolean isValid() {
        return _isValid;
    }
    public boolean wasValidateInvoked() {
        return _wasValidateInvoked;
    }
    public List<ArtifactParser> getArtifactParsers() {
        return _artifactParsers;
    }
    public Schema getSchema() {
        return  _schema;
    }
    public ArtifactOptions getOptions() {
        return _options;
    }

    public boolean validateTemplate(SchemaGraphParser schemaGraphParser, ValidationResult validationResult) {
        int initialErrorSize = validationResult.getErrors().size();
        if (_wasValidateInvoked)
            return !_isValid;
        _wasValidateInvoked = true;
        if (_rawData.artifacts == null || _rawData.artifacts.length == 0)
            validationResult.addWarning("Template file contains not artifacts to produce in " + _filePath);
        switch (_schemaRelationType) {
            case SchemaSource:
                validateSchemaSource(validationResult);
                break;
            case SchemaLink:
                validateSchemaLink(schemaGraphParser, validationResult);
                break;
            case SubSchema:
                validateSubSchema(schemaGraphParser, validationResult);
                break;
            case OptionsConfig:
                //TODO how to validate options config
                break;
            case Unknown:
                validateUnknown(validationResult);
                break;
            default:
                throw new RuntimeException("Not implemented " + _schemaRelationType);
        }
        if (validationResult.getErrors().size() == initialErrorSize) {
            if (_options==null)
                createOptions();
            resolveSchema();
            _isValid = true;
            return false;
        }
        return true;
    }

    private void createOptions() {
        ArtifactOptions parentOptions = _defaultOptions;
        if (_linkedTemplate != null)
            parentOptions = _linkedTemplate.getOptions();
        _options = new ArtifactOptions("Template file " + _filePath, _rawData.options, parentOptions);
    }

    private void resolveSchema() {
        if (_schema!=null)
            return;
        switch (_schemaRelationType) {
            case SchemaSource:
                resolveSchemaSource();
                break;
            case SubSchema:
                resolveSubSchema();
                break;
            case SchemaLink:
                resolveSchemaLink();
                break;
            case OptionsConfig:
                //TODO resolve options config
                break;
            default:
                throw new RuntimeException("Not implemented "+_schemaRelationType);
        }
    }

    private void resolveSchemaSource() {
        Schema schema=new Schema();
        if (_rawData.fields!=null) {
            _rawData.fields.forEach((name,fieldProperties)-> {
                if (fieldProperties instanceof String)
                    schema.setField(new SchemaField(name,fieldProperties.toString()));
                else
                    throw new RuntimeException("Multiple field properties not yet implemented in template "+_filePath+" field "+name+". set the value to be a string of the field type");
            });
        }
        _schema=schema;
    }
    private void resolveSubSchema() {
        Schema schema=new Schema();
        if (_rawData.subTemplateFields!=null) {
            if (_linkedTemplate==null)
                throw new RuntimeException("Could not resolve source template in sub template "+_filePath);
            Schema sourceSchema=_linkedTemplate.getSchema();
            if (sourceSchema==null) {
                _linkedTemplate.resolveSchema();
                sourceSchema=_linkedTemplate.getSchema();
                if (sourceSchema==null)
                    throw new RuntimeException("Could not resolve source schema in sub template "+_filePath);
            }
            for (String fieldName : _rawData.subTemplateFields) {
                SchemaField schemaField=sourceSchema.getField(fieldName);
                if (schemaField==null)
                    throw new RuntimeException("Could not resolve source schema field "+fieldName+" in sub template "+_filePath);
                schema.setField(schemaField);
            }
        }
        _schema=schema;
    }
    private void resolveSchemaLink() {
        if (_linkedTemplate==null)
            throw new RuntimeException("Could not resolve source template in sub template "+_filePath);
        Schema schema=_linkedTemplate.getSchema();
        if (schema==null) {
            _linkedTemplate.resolveSchema();
            schema=_linkedTemplate.getSchema();
            if (schema==null)
                throw new RuntimeException("Could not resolve source schema in template link"+_filePath);
        }
        _schema=schema;
    }
    private void validateSubSchema(SchemaGraphParser schemaGraphParser, ValidationResult validationResult) {
        if (_rawData.fields!=null)
            validationResult.addError("Template can not define schema fields and also define sub template fields in "+_filePath);
        if (_rawData.subTemplateFields.length==0)
            validationResult.addError("Sub template must define at least one field in "+_filePath);
        if (Helpers.IsNullOrEmpty(_rawData.linkTo))
            validationResult.addError("Invalid sub template definition in "+_filePath+". Sub template must be linked to source template");
        else {
            _linkedTemplate=resolveLink(schemaGraphParser,validationResult);
            if (_linkedTemplate!=null && _linkedTemplate.isValid()) {
                switch (_linkedTemplate.getSchemaRelationType()) {
                    case SchemaSource:
                        for (String subTemplateField : _rawData.subTemplateFields) {
                            Object parentFieldDefinition=_linkedTemplate.getRawData().fields.get(subTemplateField);
                            if (parentFieldDefinition==null)
                                validationResult.addError("Unable to match sub template field "+subTemplateField+" to source schema field in "+_filePath+"");
                        }
                        break;
                    default:
                        validationResult.addError("Invalid sub schema link in "+_filePath+". Target schema must be a schema source. Link was : "+_linkedTemplate.getFilePath());
                        break;
                }

            }
        }
    }

    private TemplateFileParser resolveLink(SchemaGraphParser schemaGraphParser, ValidationResult validationResult) {
        TemplateFileParser linkedTemplate;
        String absoluteLink= Paths.get(_filePath).getParent().resolve(_rawData.linkTo).normalize().toString();
        linkedTemplate= schemaGraphParser.getAllTemplates().get(absoluteLink);
        if (linkedTemplate==null)
            validationResult.addError("Invalid template link :"+absoluteLink+ " in "+_filePath+". Template could not be found.");
        else {
            if (!linkedTemplate.wasValidateInvoked())
                linkedTemplate.validateTemplate(schemaGraphParser,validationResult);
            if (!linkedTemplate.isValid())
                validationResult.addError("Invalid template in "+_filePath+". Template is indirectly invalid since it link to an invalid template "+linkedTemplate.getFilePath());
        }
        return linkedTemplate;
    }

    private void validateSchemaLink(SchemaGraphParser schemaGraphParser, ValidationResult validationResult) {
        _linkedTemplate=resolveLink(schemaGraphParser,validationResult);
        if (_linkedTemplate!=null && _linkedTemplate.isValid()) {
            switch (_linkedTemplate.getSchemaRelationType()) {
                case SchemaSource:
                case SubSchema:
                    break;
                default:
                    validationResult.addError("Invalid schema link in "+_filePath+". Target schema must be a schema source or sub schema. Link was : "+_linkedTemplate.getFilePath());
            }
        }
    }

    private void validateUnknown(ValidationResult validationResult) {
        validationResult.addError("Unrecognized template schema relation in "+_filePath+". Please specify one of the following: fields, linkTo or subTemplateFields");
    }

    private void validateSchemaSource(ValidationResult validationResult) {
        if (!Helpers.IsNullOrEmpty(_rawData.linkTo))
            validationResult.addError("Template can not define schema fields and also link to external template in "+_filePath);
        if (_rawData.subTemplateFields!=null)
            validationResult.addError("Template can not define schema fields and also define sub template fields in "+_filePath);
        if (_rawData.fields.size()==0)
            validationResult.addError("Template must define at least one field in "+_filePath);
    }

    public boolean validateArtifacts(SchemaGraphParser schemaGraphParser, ValidationResult validationResult) {
        if (!isValid() || _rawData.artifacts==null) //Template itself is not valid so all artifacts are invalid
            return !isValid();
        int initialErrorSize=validationResult.getErrors().size();
        _artifactParsers= Arrays.stream(_rawData.artifacts).map(a->new ArtifactParser(this,a)).collect(Collectors.toList());
        for (ArtifactParser artifactParser : _artifactParsers) {
            artifactParser.validateArtifact(schemaGraphParser,validationResult);
        }
        return validationResult.getErrors().size()>initialErrorSize;
    }

}
