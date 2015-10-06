package org.ppojo;

import org.ppojo.data.*;
import org.ppojo.exceptions.FolderNotFoundException;
import org.ppojo.exceptions.FolderPathNotADirectory;
import org.ppojo.utils.Helpers;
import org.ppojo.utils.ValidationResult;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GARY on 9/25/2015.
 */
public class SchemaGraphParser {

    public static void generateArtifacts(@Nonnull Iterable<String> rootSourceFolders,@Nonnull Iterable<ITemplateFileQuery> templateQueries,
                                         @Nonnull ArtifactOptions defaultOptions) {
        if (rootSourceFolders==null)
            throw new NullPointerException("rootSourceFolder");
        if (templateQueries==null)
            throw new NullPointerException("templateQueries");
        if (defaultOptions==null)
            throw new NullPointerException("defaultOptions");
        SchemaGraphParser schemaGraphParser =new SchemaGraphParser(rootSourceFolders,templateQueries,defaultOptions);
        schemaGraphParser.generateArtifacts();
    }
    public static void listMatchedTemplates(@Nonnull Iterable<String> rootSourceFolders,@Nonnull Iterable<ITemplateFileQuery> templateQueries,
                                         @Nonnull ArtifactOptions defaultOptions) {
        if (rootSourceFolders==null)
            throw new NullPointerException("rootSourceFolder");
        if (templateQueries==null)
            throw new NullPointerException("templateQueries");
        if (defaultOptions==null)
            throw new NullPointerException("defaultOptions");
        SchemaGraphParser schemaGraphParser =new SchemaGraphParser(rootSourceFolders,templateQueries,defaultOptions);
        schemaGraphParser.validateAndResolveInputParams();
        for (String templateFile : schemaGraphParser.getAllTemplates().keySet()) {
            System.out.println(templateFile);
        }
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
        validateAndResolveInputParams();
        deserializeTemplateFiles();
        ValidationResult validationResult=new ValidationResult(_throwFirstErrorException);
        validateTemplates(validationResult);
        validateArtifacts(validationResult);
        if (!validationResult.hasErrors()) {
            resolveArtifacts(validationResult);
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
/*
        for (ArtifactParser artifactParser : _allArtifactsByArtifactKey.values()) {
            if (artifactParser.getArtifactBase()!=null) //artifact already created through recursion skip ahead
                continue;
            ArtifactFile artifactFile=artifactParser.getArtifactFile();
            artifactFiles.add(artifactFile);
            ArtifactBase artifactBase=newArtifact(artifactParser,artifactFile);
            artifactParser.setArtifactBase(artifactBase);
        } */
        generateGraphRecursive(artifactFiles,_allArtifactsByArtifactKey.values());
        return new SchemaGraph(artifactFiles);

    }
    private void generateGraphRecursive(List<ArtifactFile> artifactFiles,Iterable<ArtifactParser> artifactParsers) {
        if (artifactParsers==null)
            return;
        for (ArtifactParser artifactParser : artifactParsers) {
            if (artifactParser.getArtifactBase()!=null) //artifact already created through recursion skip ahead
                continue;
            generateGraphRecursive(artifactFiles,artifactParser.getDependantArtifactsNotYetGenerated());
            ArtifactFile artifactFile=artifactParser.getArtifactFile();
            artifactFiles.add(artifactFile);
            ArtifactBase artifactBase=newArtifact(artifactParser,artifactFile);
            artifactParser.setArtifactBase(artifactBase);
        }

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
                artifactBase=new FieldEnumArtifact(artifactParent,artifactParser);
                break;
            case ImmutableClass:
                ImmutableClassData immutableClassData=(ImmutableClassData)artifactParser.getRawData();
                PojoArtifact pojoArtifact=artifactParser.getImmutableDataArtifact();
                artifactBase=new ImmutableClassArtifact(artifactParent,artifactParser,immutableClassData.extend,immutableClassData.implement,pojoArtifact);
                break;
            case FluentBuilder:
                FluentBuilderData fluentBuilderData=(FluentBuilderData)artifactParser.getRawData();
                PojoArtifact fluentDataArtifact=artifactParser.getFluentDataArtifact();
                artifactBase=new FluentBuilderArtifact(artifactParent,artifactParser,fluentBuilderData.extend,fluentBuilderData.implement,fluentDataArtifact);
                break;
            default:
                throw new RuntimeException("Artifact type creation does not implement "+artifactParser.getArtifactType()+" in "+artifactParser.getRawData().name);
        }
        artifactParent.addChildArtifact(artifactBase);
        return artifactBase;
    }

    private void resolveArtifacts(ValidationResult validationResult) {

        ArrayList<ArtifactParser> unResolvedArtifacts=new ArrayList<>(_allArtifactsByArtifactKey.values());
        do {
            int prevNumUnresolved=_allArtifactsByArtifactKey.size();
            for (int i = unResolvedArtifacts.size() - 1; i >= 0; i--) {
                if (!unResolvedArtifacts.get(i).retryResolve(this,validationResult))
                    unResolvedArtifacts.remove(i);
            }
            if (unResolvedArtifacts.size()==0)
                break;
            if (prevNumUnresolved==unResolvedArtifacts.size()) {
                validationResult.addError("No progress resolving artifact dependencies. Possible cyclical dependencies stopping.");
                break;
            }
        } while (true);




    }

    private void validateArtifacts(ValidationResult validationResult) {
        for (TemplateFileParser templateFileParser : _templatesByFilePath.values()) {
            templateFileParser.validateArtifacts(this,validationResult);
        }
    }

    private void validateTemplates(ValidationResult validationResult) {
        for (TemplateFileParser templateFileParser : _templatesByFilePath.values()) {
            templateFileParser.validateTemplate(this,validationResult);
        }
    }

    private void deserializeTemplateFiles() {
        Serializer serializer=new Serializer();
        for (String file : _templatesByFilePath.keySet()) {
            String json= Helpers.readTextFile(file);
            TemplateFileData rawData=serializer.deserialize(json,file);
            _templatesByFilePath.put(file,new TemplateFileParser(file,rawData,_defaultOptions));
        }
    }

    public void validateAndResolveInputParams() {
        ArrayList<String> normalizedSourceFolders=new ArrayList<>();
        for (String rootSourceFolder : _rootSourceFolders) {
            Path path=Paths.get(rootSourceFolder).normalize();
            if (!path.isAbsolute()) {
                path=path.toAbsolutePath();
            }
            File file=path.toFile();
            if (!file.exists())
                throw new FolderNotFoundException("Sources folder "+file.toString()+" does not exist");
            if (!file.isDirectory())
              throw new FolderPathNotADirectory("Sources folder "+file.toString()+" is not a directory");
            normalizedSourceFolders.add(path.toString());
        }
        _rootSourceFolders=normalizedSourceFolders;
        if (normalizedSourceFolders.size()==0)
            throw new IllegalArgumentException("No sources folder provided. Must specify at least one sources folder.");
        for (ITemplateFileQuery templateQuery : _templateQueries) {
            for (String file : templateQuery.getTemplateFiles()) {
                _templatesByFilePath.put(file,null);
            }
        }
    }

    public Iterable<String> getRootSourceFolders() {
        return _rootSourceFolders;
    }
}
