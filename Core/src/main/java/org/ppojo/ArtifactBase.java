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

import org.ppojo.data.ArtifactMetaData;
import org.ppojo.utils.EmptyArray;
import org.ppojo.utils.MapChainValue;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.ppojo.utils.Helpers.*;

/**
 * Base class for representing an artifact after being parsed ,validated and resolved. Once fully resolved an
 * artifact will contain a schema, options and linked to its parent entity, usually the {@link ArtifactFile} in which it will be generated.
 * The abstract methods in this class define the base interface methods required to write artifact code output to an input StreamWriter,
 * thus derived classes corresponding to various artifact types can implement their specific logic for the output.
 * @see ArtifactParser
 * @see ArtifactBase
 * @see ArtifactFile
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

    @Override
    public String getArtifactFileName() {
        ArtifactFile artifactFile=as(ArtifactFile.class,_artifactParent);
        if (artifactFile!=null)
            return artifactFile.getArtifactFileName();
        return _artifactParent.getArtifactFileName();
    }
}
