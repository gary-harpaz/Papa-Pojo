package org.ppojo;

import org.ppojo.data.*;
import org.ppojo.utils.Helpers;
import org.ppojo.utils.ValidationResult;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by GARY on 9/25/2015.
 */
public class SchemaGraphParser {

    public static void generateArtifacts(@Nonnull Iterable<String> rootSourceFolders,@Nonnull Iterable<ITemplateFileQuery> templateQueries,@Nonnull ArtifactOptions defaultOptions) {
        if (rootSourceFolders==null)
            throw new NullPointerException("rootSourceFolder");
        if (templateQueries==null)
            throw new NullPointerException("templateQueries");
        if (defaultOptions==null)
            throw new NullPointerException("defaultOptions");
        SchemaGraphParser schemaGraphParser =new SchemaGraphParser(rootSourceFolders,templateQueries,defaultOptions);
        schemaGraphParser.generateArtifacts();
    }
    private SchemaGraphParser(Iterable<String> rootSourceFolders, Iterable<ITemplateFileQuery> templateQueries,ArtifactOptions defaultOptions) {
        _templatesByFilePath=new HashMap<>();
        _allArtifactsByArtifactKey =new HashMap<>();
        _rootSourceFolders=rootSourceFolders;
        _templateQueries=templateQueries;
        _defaultOptions=defaultOptions;
    }

    private final ArtifactOptions _defaultOptions;
    private Iterable<String> _rootSourceFolders;
    private Iterable<ITemplateFileQuery> _templateQueries;
    private Map<String,TemplateFileParser> _templatesByFilePath;
    private Map<String,ArtifactParser> _allArtifactsByArtifactKey;
    private boolean _throwFirstErrorException=true;
    private boolean _allParsersValid=false;

    public Map<String,TemplateFileParser> getAllTemplates() {
        return _templatesByFilePath;
    }
    public Map<String,ArtifactParser> getAllArtifacts() {
        return _allArtifactsByArtifactKey;
    }
    public boolean isThrowFirstErrorException() {
        return _throwFirstErrorException;
    }
    public void setThrowFirstErrorException(boolean throwFirstErrorException) {
        _throwFirstErrorException = throwFirstErrorException;
    }



    private void generateArtifacts() {
        queryTemplates();
        deserializeTemplateFiles();
        ValidationResult validationResult=new ValidationResult();
        validateTemplates(validationResult);
        validateArtifacts(validationResult);
        if (!validationResult.hasErrors()) {
            resolveNestedArtifacts(validationResult);
        }
        if (!validationResult.hasErrors()) {
            _allParsersValid=true;
            SchemaGraph schemaGraph=generateGraph();
            schemaGraph.produceArtifactFiles();
        }
        else
            _allParsersValid=false;
    }

    private SchemaGraph generateGraph() {
        assertAllValid();
        List<ArtifactFile> artifactFiles=new ArrayList<>();
        List<ArtifactParser> artifactsWithNestedChildren=new ArrayList<>();
        for (ArtifactParser artifactParser : _allArtifactsByArtifactKey.values()) {
            if (artifactParser.isNested())
                continue;
            ArtifactFile artifactFile=artifactParser.getArtifactFile();
            artifactFiles.add(artifactFile);
            ArtifactBase artifactBase=newArtifact(artifactParser,artifactFile);
            artifactParser.setArtifactBase(artifactBase);
            if (artifactParser.getNestedArtifacts()!=null)
                artifactsWithNestedChildren.add(artifactParser);
        }
        for (ArtifactParser artifactParser : artifactsWithNestedChildren) {
            for (ArtifactParser nestedArtifact : artifactParser.getNestedArtifacts()) {
                newArtifact(nestedArtifact,artifactParser.getArtifactBase());
            }
        }

        return new SchemaGraph(artifactFiles);

    }

    private void assertAllValid() {
        if (!_allParsersValid)
            throw new RuntimeException("Not all parsers are valid , graph can not be generated");
    }

    private ArtifactBase newArtifact(ArtifactParser artifactParser,IArtifactParent artifactParent) {
        ArtifactBase artifactBase=null;
        switch (artifactParser.getArtifactType()) {
            case Pojo:
                ClassArtifactData classArtifactData=(ClassArtifactData)artifactParser.getRawData();
                artifactBase=new PojoArtifact(artifactParent,artifactParser
              ,classArtifactData.extend,classArtifactData.implement);
                break;
            case Interface:
                InterfaceArtifactData interfaceArtifactData=(InterfaceArtifactData)artifactParser.getRawData();
                artifactBase=new InterfaceArtifact(artifactParent,artifactParser
                ,interfaceArtifactData.isReadOnly,interfaceArtifactData.extend);
                break;
            case FieldEnum:
                FieldEnumArtifactData fieldEnumArtifact=(FieldEnumArtifactData)artifactParser.getRawData();
                artifactBase=new FieldEnumArtifact(artifactParent,artifactParser);
                break;
            default:
                throw new RuntimeException("Artifact type creation does not implement "+artifactParser.getArtifactType()+" in "+artifactParser.getRawData().name);
        }
        artifactParent.addChildArtifact(artifactBase);
        return artifactBase;
    }

    private void resolveNestedArtifacts(ValidationResult validationResult) {
        validateItems(_templatesByFilePath.values(),parser->parser::resolveNestedArtifacts,validationResult);
    }

    private void validateArtifacts(ValidationResult validationResult) {
        validateItems(_templatesByFilePath.values(),parser->parser::validateArtifacts,validationResult);
    }

    private void validateTemplates(ValidationResult validationResult) {
        validateItems(_templatesByFilePath.values(),parser->parser::validateTemplate,validationResult);
    }

    private void deserializeTemplateFiles() {
        Serializer serializer=new Serializer();
        for (String file : _templatesByFilePath.keySet()) {
            String json= Helpers.readTextFile(file);
            TemplateFileData rawData=serializer.deserialize(json,file);
            _templatesByFilePath.put(file,new TemplateFileParser(file,rawData,_defaultOptions));
        }
    }

    private void queryTemplates() {
        for (ITemplateFileQuery templateQuery : _templateQueries) {
            for (String file : templateQuery.getTemplateFiles()) {
                _templatesByFilePath.put(file,null);
            }
        }
    }

    public Iterable<String> getRootSourceFolders() {
        return _rootSourceFolders;
    }

    public  <T> void  validateItems(Iterable<T> items,Function<T,IItemValidator> validationDelegate,ValidationResult validationResult) {
        for (T item : items) {
            boolean hadErrors=validationDelegate.apply(item).validate(this, validationResult);
            if (hadErrors && _throwFirstErrorException)
                throw new RuntimeException(validationResult.getErrors().get(0).getMessage());
        }

    }

    public interface IItemValidator {
        boolean validate(SchemaGraphParser schemaGraphParser,ValidationResult validationResult);
    }
}
