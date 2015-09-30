package org.ppojo;

import org.ppojo.data.ArtifactData;
import org.ppojo.utils.Helpers;
import org.ppojo.utils.ValidationResult;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static org.ppojo.utils.Helpers.*;

/**
 * Created by GARY on 9/25/2015.
 */
public class ArtifactParser {

    ArtifactParser(TemplateFileParser parentTemplate,ArtifactData rawData) {
        _parentTemplate=parentTemplate;
        _rawData=rawData;
        if (IsNullOrEmpty(_rawData.name)) {
            //deduce artifact name from template file name
            _isArtifactNameDeducedFromTemplateFile=true;
            Path path = Paths.get(_parentTemplate.getFilePath());
            _rawData.name=removeFilenameExtension(path.getFileName().toString());
        }
        if (_rawData.path==null)
            _rawData.path="";
        Path templateFolder=Paths.get(_parentTemplate.getFilePath()).getParent();
        Path artifactFolder=templateFolder.resolve(_rawData.path);
        if (!isNested()) {
            _artifactTargetFile=artifactFolder.resolve(_rawData.name+".java").toString();
            _artifactKey=_artifactTargetFile+"@"+_rawData.name;
        }
        else {
            _artifactTargetFile=artifactFolder.resolve(_rawData.nestInArtifact +".java").toString();
            _artifactKey=_artifactTargetFile+"@"+_rawData.nestInArtifact +"."+_rawData.name;
        }
        _options=new ArtifactOptions("Artifact "+_rawData.name,_rawData.options,_parentTemplate.getOptions());
    }
/*
    private static HashMap<String,Class> _validOptionDefinitions;
    static  {
        _validOptionDefinitions =new HashMap<>();
        for (ArtifactOptions.Fields field : ArtifactOptions.Fields.values()) {
            _validOptionDefinitions.put(field.toString(), String.class);
        }
        _validOptionDefinitions.put(ArtifactOptions.Fields.imports.toString(), ArrayList.class); //currently the only non string option

    }
    */

    private final TemplateFileParser _parentTemplate;
    private final ArtifactData _rawData;
    private boolean _isValid=false;
    private boolean _wasValidationInvoked=false;
    private List<ArtifactParser> _nestedArtifact;
    private ArtifactTypes _artifactType;
    private boolean _isArtifactNameDeducedFromTemplateFile=false;
    private String _parentSourceFolder;
    private String _artifactTargetFile;
    private String _artifactKey;
    private ArtifactParser _artifactParentOfNestedArtifact;
    private ArtifactFile _artifactFile;
    private ArtifactBase _artifactBase;
    private ArtifactOptions _options;


    public String getArtifactTargetFile() {
        return _artifactTargetFile;
    }
    public boolean isNested() {
        return !Helpers.IsNullOrEmpty(_rawData.nestInArtifact);
    }
    public ArtifactParser getArtifactParentOfNestedArtifact() {
        return _artifactParentOfNestedArtifact;
    }
    public Iterable<ArtifactParser> getNestedArtifacts() {
        return _nestedArtifact;
    }
    public boolean isArtifactNameDeducedFromTemplateFile() {
        return _isArtifactNameDeducedFromTemplateFile;
    }
    public boolean isValid() {
        return _isValid;
    }
    public ArtifactTypes getArtifactType() {
        return _artifactType;
    }
    public ArtifactData getRawData() {
        return _rawData;
    }
    public String getParentSourceFolder() {
        return _parentSourceFolder;
    }
    public ArtifactFile getArtifactFile() {
        return _artifactFile;
    }
    public Schema getSchema() {
        return _parentTemplate.getSchema();
    }
    public ArtifactBase getArtifactBase() {
        return _artifactBase;
    }
    public void setArtifactBase(ArtifactBase artifactBase) {
        _artifactBase = artifactBase;
    }
    public ArtifactOptions getOptions() { return _options; }


    public boolean validateArtifact(SchemaGraphParser schemaGraphParser, ValidationResult validationResult) {
        if (_wasValidationInvoked)
            return _isValid;
        _wasValidationInvoked=true;
        int initialErrorSize=validationResult.getErrors().size();
        validateKeyUniqueness(schemaGraphParser, validationResult);
        checkInvalidCharacters(validationResult,this::checkForInvalidCharInName);
        checkInvalidCharacters(validationResult,this::checkForInvalidCharInNestedIn);
        validateArtifactType(validationResult);
        validateTargetFileInSourceFolder(schemaGraphParser, validationResult);
      //  validateOptions(validationResult);
        if (validationResult.getErrors().size()==initialErrorSize) {
            resolveArtifactFile();
            _isValid=true;
            return false;
        }
        return true;
    }

    private void resolveArtifactFile() {
        if (!isNested()) {
//            _artifactFile=new ArtifactFile(getArtifactTargetFile(),getParentSourceFolder(),_parentTemplate.getFilePath(),_parentTemplate.getRawData().options,null);
            _artifactFile=new ArtifactFile(getArtifactTargetFile(),getParentSourceFolder(), _options);
        }

    }

    public boolean resolveNested(SchemaGraphParser schemaGraphParser, ValidationResult validationResult) {
        if (!isNested())
            return false;
        int initialErrorSize=validationResult.getErrors().size();
        String parentKey=Helpers.removeLastOccurrenceOf(_artifactKey,".");
        _artifactParentOfNestedArtifact= schemaGraphParser.getAllArtifacts().get(parentKey);
        if (_artifactParentOfNestedArtifact==null)
            validationResult.addError("Could not match nested artifact to parent artifact definition. Parent key was : "+parentKey+getArtifactLocation());
        if (validationResult.getErrors().size()>initialErrorSize) {
            _isValid=false;
            return true;
        }
        _artifactParentOfNestedArtifact.addNestedChild(this);
        return false;
    }

    private void addNestedChild(ArtifactParser nestedChildArtifact) {
        if (_nestedArtifact==null)
            _nestedArtifact=new ArrayList<>();
        _nestedArtifact.add(nestedChildArtifact);
    }

    private void validateKeyUniqueness(SchemaGraphParser schemaGraphParser, ValidationResult validationResult) {
        ArtifactParser duplicate= schemaGraphParser.getAllArtifacts().get(_artifactKey);
        if (duplicate==null)
            schemaGraphParser.getAllArtifacts().put(_artifactKey,this);
        else
            if (duplicate!=this) {
                if (duplicate.isArtifactNameDeducedFromTemplateFile() || isArtifactNameDeducedFromTemplateFile())
                    validationResult.addError("Duplicate artifact name detected "+_artifactKey+". Try to set explicit artifact name to solve the collision");
                else
                    validationResult.addError("Duplicate artifact name detected "+getArtifactLocation()+" and "+duplicate.getArtifactLocation());
            }
    }

    /*

    private void validateOptions(ValidationResult validationResult) {
        if (_rawData.options!=null) {
            HashMap<String,Object> validOptions=new HashMap<>();
            _rawData.options.forEach((option,value)-> {
                Class optionsClass= _validOptionDefinitions.get(option);
                if (optionsClass==null)
                    validationResult.addWarning("Invalid option "+option+" value will be ignored "+getArtifactLocation());
                else
                    if (value.getClass()!=optionsClass)
                        validationResult.addError("Invalid option value for "+option+". Expected value of class "+optionsClass.toString()+" got "+value.getClass().toString()+getArtifactLocation());
                    else
                        validOptions.put(option,value);
            });
            _rawData.options=validOptions;
        }
    }
    */

    private void validateTargetFileInSourceFolder(SchemaGraphParser schemaGraphParser, ValidationResult validationResult) {
        boolean inSourceFolder=false;
        for (String sourceFolder : schemaGraphParser.getRootSourceFolders()) {
            if (_artifactTargetFile.startsWith(sourceFolder)) {
                _parentSourceFolder=sourceFolder;
                inSourceFolder=true;
                break;
            }
        }
        if (!inSourceFolder)
            validationResult.addError("Artifact target file "+_artifactTargetFile+" does not reside in any source folder "+getArtifactLocation());
    }

    private void validateArtifactType(ValidationResult validationResult) {
        try {
            _artifactType= ArtifactTypes.valueOf(_rawData.type);
        } catch (Exception ex) {
            validationResult.addError("Invalid artifact type " + _rawData.type + getArtifactLocation());
        }
    }

    private void checkInvalidCharacters(ValidationResult validationResult,BiConsumer<String,ValidationResult> delegate) {
        delegate.accept(".", validationResult);
        delegate.accept("@", validationResult);
        delegate.accept("\\", validationResult);
        delegate.accept("/", validationResult);
    }

    private void checkForInvalidCharInName(String character, ValidationResult validationResult) {
        checkForInvalidCharInField("name", _rawData.name,character,validationResult);
    }
    private void checkForInvalidCharInNestedIn(String character, ValidationResult validationResult) {
        if (isNested()) {
            checkForInvalidCharInField("nestInArtifact", _rawData.nestInArtifact,character,validationResult);
        }
    }

    private void checkForInvalidCharInField(String fieldName,String fieldValue, String character, ValidationResult validationResult) {
        if (fieldValue.contains(character))
            validationResult.addError("Artifact "+fieldName+" contains illegal character "+character+getArtifactLocation());

    }
    public String getArtifactLocation() {
        return " in artifact " + _rawData.name + " template file " + _parentTemplate.getFilePath();
    }


}
