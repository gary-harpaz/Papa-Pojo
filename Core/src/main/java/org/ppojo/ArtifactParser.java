package org.ppojo;

import org.ppojo.data.ArtifactData;
import org.ppojo.data.FluentBuilderData;
import org.ppojo.data.ImmutableClassData;
import org.ppojo.utils.Helpers;
import org.ppojo.utils.ValidationResult;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.ppojo.utils.Helpers.*;

/**
 * Created by GARY on 9/25/2015.
 */
public class ArtifactParser {

    private boolean _forceCopyMember;

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
        _dependencyResolveIssues=new HashMap<>();
        if (!isNested()) {
            _artifactTargetFile=artifactFolder.resolve(_rawData.name+".java").normalize().toString();
            _artifactKey=_artifactTargetFile+"@"+_rawData.name;
        }
        else {
             newTask(ResolveIssueTypes.NestedClass);
            _artifactTargetFile=artifactFolder.resolve(_rawData.nestInArtifact +".java").normalize().toString();
            _artifactKey=_artifactTargetFile+"@"+_rawData.nestInArtifact +"."+_rawData.name;
        }
        _options=new ArtifactOptions("Artifact "+_rawData.name,_rawData.options,_parentTemplate.getOptions());
        _artifactType=ArtifactTypes.Parse(_rawData.type);
        if (_artifactType==ArtifactTypes.FluentBuilder)
            newTask(ResolveIssueTypes.FluentBuild);
        if (_artifactType==ArtifactTypes.ImmutableClass)
            newTask(ResolveIssueTypes.ImmutableClass);
    }
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
    private ArtifactParser _parentOfNestedArtifact;
    private ArtifactFile _artifactFile;
    private ArtifactBase _artifactBase;
    private ArtifactOptions _options;
    private Map<ResolveIssueTypes,ResolveTask> _dependencyResolveIssues;


    public String getArtifactTargetFile() {
        return _artifactTargetFile;
    }
    public boolean isNested() {
        return !Helpers.IsNullOrEmpty(_rawData.nestInArtifact);
    }
    public ArtifactParser getParentOfNestedArtifact() {
        return _parentOfNestedArtifact;
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
    public String getTemplateFilePath() {
        return _parentTemplate.getFilePath();
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
            return !_isValid;
        _wasValidationInvoked=true;
        int initialErrorSize=validationResult.getErrors().size();
        validateKeyUniqueness(schemaGraphParser, validationResult);
        checkInvalidCharacters(validationResult,this::checkForInvalidCharInName);
        checkInvalidCharacters(validationResult,this::checkForInvalidCharInNestedIn);
        validateTargetFileInSourceFolder(schemaGraphParser, validationResult);
        if (validationResult.getErrors().size()==initialErrorSize) {
            resolveArtifactFile();
            _isValid=true;
            return false;
        }
        return true;
    }

    private void resolveArtifactFile() {
        if (!isNested()) {
            _artifactFile=new ArtifactFile(getArtifactTargetFile(),getParentSourceFolder(), _options);
        }

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

    private boolean getIsResolved() {
        if (_dependencyResolveIssues==null || _dependencyResolveIssues.size()==0)
            return true;
        if (_dependencyResolveIssues.values().stream().allMatch(t->t.dependency!=null))
            return true;
        return false;
    }

    public boolean retryResolve(SchemaGraphParser schemaGraphParser, ValidationResult validationResult) {
        if (getIsResolved())
            return false;
        for (ResolveTask resolveTask : _dependencyResolveIssues.values()) {
            tryResolve(resolveTask,schemaGraphParser,validationResult);
            if (resolveTask.hasResolveFailed)
                return false;
        }
        return !getIsResolved();
    }

    private void tryResolve(ResolveTask resolveTask, SchemaGraphParser schemaGraphParser, ValidationResult validationResult) {
        if (resolveTask.hasResolveFailed || resolveTask.dependency!=null)
            return;
        resolveTask.hasResolveFailed=true;
        switch (resolveTask.issueType) {
            case FluentBuild:
                tryResolveFluentBuilder(resolveTask,validationResult);
                break;
            case ImmutableClass:
                tryResolveImmutableClass(resolveTask, validationResult);
                break;
            case NestedClass:
                throw new RuntimeException("Nested class resolving not implemented");
            default:
                validationResult.addError("Invalid resolve issue type "+resolveTask.issueType);
                break;
        }
        if (resolveTask.dependency!=null)
            resolveTask.hasResolveFailed=false;
    }

    private void tryResolveImmutableClass(ResolveTask resolveTask, ValidationResult validationResult) {

        ImmutableClassData data = (ImmutableClassData) _rawData;
        tryResolveDataArtifact(resolveTask,data.dataArtifact,"Can not resolve ImmutableClass.",validationResult);
        if (resolveTask.dependency!=null) {
            if (!Helpers.IsNullOrEmpty(this.getOptions().getImmutableCopyDataMember())) {
                resolveTask.dependency.setForceCopyMember(true); // force the match to have a copy member for cloning the immutable class data
            }
        }
    }
    private void tryResolveFluentBuilder(ResolveTask resolveTask, ValidationResult validationResult) {

        FluentBuilderData data = (FluentBuilderData) _rawData;
        tryResolveDataArtifact(resolveTask,data.dataArtifact,"Can not resolve Fluent Builder.",validationResult);
    }

    private void tryResolveDataArtifact(ResolveTask resolveTask,String dataArtifact,String errorPrefix, ValidationResult validationResult) {

        if (IsNullOrEmpty(dataArtifact)) {
            validationResult.addError(errorPrefix+"Field dataArtifact is missing " + getArtifactLocation());
            return;
        }
        if (dataArtifact.equals(_rawData.name)) {
            validationResult.addError(errorPrefix+"DataArtifact value can not be the same as the name of the artifact " + getArtifactLocation());
            return;
        }
        ArrayList<ArtifactParser> matches = new ArrayList<>();
        for (ArtifactParser parser : _parentTemplate.getArtifactParsers()) {
            if (parser._rawData.name.equals(dataArtifact))
                matches.add(parser);
        }
        if (_parentTemplate.getSchemaRelationType() == TemplateSchemaRelationTypes.SchemaLink && _parentTemplate.getLinkedTemplate() != null) {
            for (ArtifactParser parser : _parentTemplate.getLinkedTemplate().getArtifactParsers()) {
                if (parser._rawData.name.equals(dataArtifact))
                    matches.add(parser);
            }
        }
        if (matches.size() == 0) {
            validationResult.addError(errorPrefix+"Can not match dataArtifact " + dataArtifact + " to existing artifact" + getArtifactLocation());
            return;
        }
        if (matches.size() > 1) {
            validationResult.addError(errorPrefix+"Multiple matches exist for dataArtifact " + dataArtifact + getArtifactLocation());
            return;
        }
        ArtifactParser potentialMatch = matches.get(0);
        if (!potentialMatch.isValid()) {
            validationResult.addError(errorPrefix+"DataArtifact " + dataArtifact + " is not valid" + getArtifactLocation());
            return;
        }
        if (potentialMatch.getArtifactType() != ArtifactTypes.Pojo) {
            validationResult.addError(errorPrefix+"DataArtifact " + dataArtifact + " is not a Pojo artifact, type is " + potentialMatch.getArtifactType() + getArtifactLocation());
            return;
        }
        if (potentialMatch.getIsResolved()) {
            if (!Helpers.IsNullOrEmpty(this.getOptions().getImmutableCopyDataMember())) {
                potentialMatch.setForceCopyMember(true); // force the match to have a copy member for cloning the immutable class data
            }
            resolveTask.dependency = potentialMatch;
        } else
            resolveTask.hasResolveFailed = false; //The dependency will retry again
    }

    public Iterable<ArtifactParser> getDependantArtifactsNotYetGenerated() {
        if (_dependencyResolveIssues==null || _dependencyResolveIssues.size()==0)
            return null;
        ArrayList<ArtifactParser> result=null;
        for (ResolveTask resolveTask : _dependencyResolveIssues.values()) {
            //all dependencies are assumed to be resolved here
            if (resolveTask.dependency.getArtifactBase()==null) {
                if (result==null)
                    result=new ArrayList<>();
                result.add(resolveTask.dependency);
            }
        }
        return result;
    }

    public PojoArtifact getImmutableDataArtifact() {
        return  (PojoArtifact)_dependencyResolveIssues.get(ResolveIssueTypes.ImmutableClass).dependency.getArtifactBase();
    }
    public PojoArtifact getFluentDataArtifact() {
        return  (PojoArtifact)_dependencyResolveIssues.get(ResolveIssueTypes.FluentBuild).dependency.getArtifactBase();
    }

    public void setForceCopyMember(boolean forceCopyMember) {
        _forceCopyMember = forceCopyMember;
    }

    public boolean isForceCopyMember() {
        return _forceCopyMember;
    }



    private enum ResolveIssueTypes {
        NestedClass,
        ImmutableClass,
        FluentBuild
    }

    private ResolveTask newTask(ResolveIssueTypes issueType) {
        ResolveTask result=new ResolveTask();
        result.issueType=issueType;
        _dependencyResolveIssues.put(issueType,result);
        return result;
    }

    private class ResolveTask {

        public boolean hasResolveFailed;
        public ResolveIssueTypes issueType;
        public ArtifactParser dependency;

    }
}
