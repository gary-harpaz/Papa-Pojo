package org.ppojo;

import org.ppojo.data.ArtifactMetaData;
import org.ppojo.utils.EmptyArray;
import org.ppojo.utils.MapChainValue;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.ppojo.utils.Helpers.IsNullOrEmpty;
import static org.ppojo.utils.Helpers.as;
import static org.ppojo.utils.Helpers.removeFilenameExtension;

/**
 * Created by GARY on 9/23/2015.
 */
public abstract class ArtifactBase implements IArtifactParent {
    public abstract ArtifactTypes getType();
    private final IArtifactParent _artifactParent;
    private final ArtifactOptions _options;
    private final String _name;
    private final Schema _schema;
    private final String _singleIndent;
    private String _currentIndent="";

    private List<ArtifactBase> _nestedArtifacts;

    public Schema getSchema() {
        return _schema;
    }
    public IArtifactParent getArtifactParent() {
        return _artifactParent;
    }
    public ArtifactOptions getOptions() {
        return _options;
    }
    public String getName() {
        return _name;
    }
    public List<ArtifactBase> getNestedArtifacts() {
        return _nestedArtifacts;
    }

    protected ArtifactBase(@Nonnull IArtifactParent artifactParent,@Nonnull ArtifactParser artifactParser) {
        Schema schema=artifactParser.getSchema();
        if (schema==null)
            throw new NullPointerException("schema");
        _schema=schema;
        if (artifactParent==null)
            throw new NullPointerException("artifactParent");
        _artifactParent=artifactParent;
        if (artifactParser==null)
            throw new NullPointerException("artifactParser");
        String name=artifactParser.getRawData().name;
        if (IsNullOrEmpty(name)) {
            ArtifactFile artifactFile=as(ArtifactFile.class,artifactParent);
            if (artifactFile!=null)
                name= removeFilenameExtension(Paths.get(artifactFile.getArtifactFileName()).getFileName().toString());
        }
        _name=name;
        if (!artifactParser.isValid())
            throw new IllegalStateException("Artifact " + _name + " can not be created with invalid parser. In " + artifactParser.getTemplateFilePath());
        _options=artifactParser.getOptions();
        _singleIndent =_options.getIndentString();
    }


    public void setCurrentIndent(int level) {
        StringBuilder stringBuilder=new StringBuilder();
        for (int i=0; i<level; i++) {
            stringBuilder.append(_singleIndent);
        }
        _currentIndent=stringBuilder.toString();
    }

    public String getIndent() {
        return _currentIndent;
    }

    @Override
    public void addChildArtifact(ArtifactBase artifactBase) {
        if (_nestedArtifacts==null)
            _nestedArtifacts=new ArrayList<>();
        _nestedArtifacts.add(artifactBase);
    }


    public abstract void writeArtifactDeceleration(BufferedWriter bufferedWriter) throws IOException;
    public abstract void writeArtifactContent(BufferedWriter bufferedWriter) throws IOException;


    public void WriteOptionValuesComment(BufferedWriter bufferedWriter) throws IOException {

        //TODO finish

        ArtifactMetaData artifactMetaData=ArtifactMetaData.getArtifactMetaData(getType());

        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("/* Options applied when creating this artifact: %n");
        for (ArtifactOptions.Fields field : artifactMetaData.getSupportedOptions()) {
            MapChainValue mapChainValue = _options.get(field.toString());
        }

    }


    public static String capitalizeName(String prefix, String name, CapitalizationTypes capitalization) {
        StringBuilder stringBuilder=new StringBuilder();
        capitalizeName(prefix, name, capitalization, stringBuilder);
        return stringBuilder.toString();
    }
    public static void capitalizeName(String prefix, String name, CapitalizationTypes capitalization, StringBuilder stringBuilder) {
        int initialLength=stringBuilder.length();
        switch (capitalization) {
            case  ALL_CAPS:
                appendAllCaps(stringBuilder,initialLength,prefix,name);
                break;
            case PascalCase:
                stringBuilder.append(prefix).append(name);
                stringBuilder.setCharAt(initialLength,Character.toUpperCase(stringBuilder.charAt(initialLength)));
                if (prefix.length()>0)
                    stringBuilder.setCharAt(initialLength+prefix.length(),Character.toUpperCase(stringBuilder.charAt(initialLength+prefix.length())));
                break;
            case camelCase:
                stringBuilder.append(prefix).append(name);
                stringBuilder.setCharAt(initialLength,Character.toLowerCase(stringBuilder.charAt(initialLength)));
                if (prefix.length()>0)
                    stringBuilder.setCharAt(initialLength+prefix.length(),Character.toUpperCase(stringBuilder.charAt(initialLength+prefix.length())));
                break;
        }
    }

    private static void appendAllCaps(StringBuilder stringBuilder,int initialCharNum,String... words) {
        initialCharNum=stringBuilder.length();
        for (String word : words) {
            if (word==null || word.length()==0)
                continue;
            for (int i=0; i<word.length(); i++){
                char current=word.charAt(i);
                if ((Character.isUpperCase(current) || i==0) && stringBuilder.length()>initialCharNum)
                    stringBuilder.append("_");
                stringBuilder.append(Character.toUpperCase(current));

            }
        }

    }

    public @Nonnull String[] getMoreImports() {
        return EmptyArray.get(String.class);
    }
}
