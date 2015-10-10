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

import org.ppojo.data.ArtifactData;
import org.ppojo.data.ArtifactMetaData;
import org.ppojo.data.ClassArtifactData;
import org.ppojo.data.TemplateFileData;
import org.ppojo.trace.AllArtifactsCreated;
import org.ppojo.trace.CreatedArtifactFile;
import org.ppojo.trace.ILoggingService;
import org.ppojo.trace.ITraceEvent;
import org.ppojo.utils.Helpers;
import org.ppojo.utils.MapChainValue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * This is the top level object representing the main result of the {@link parsingService} after parsing, validating and resolving the templates.
 * It is a graph of connected objected containing {@link ArtifactFile} files, their child {@link ArtifactBase} objects that are also linked to
 * their dependant artifacts. The graph method {@link ArtifactGraph#produceArtifactFiles()} kicks off the process of outputting the generated source code
 * top down iterating all objects in the graph.
 */
public class ArtifactGraph {
    private final Iterable<ArtifactFile> _artifactFiles;
    private final ILoggingService _loggingService;
    private final ArrayList<ArtifactBase> _debugOptionsArtifacts=new ArrayList<>();
    public ArtifactGraph(Iterable<ArtifactFile> artifactFiles, ILoggingService loggingService) {
        _artifactFiles=artifactFiles;
        _loggingService=loggingService;
    }

    public void produceArtifactFiles() {
        _debugOptionsArtifacts.clear();
        int index=0;
        for (ArtifactFile artifactFile : _artifactFiles) {
            produceArtifactFiles(artifactFile);
            index++;
        }
        addTraceEvent(new AllArtifactsCreated(index));
        logDebugOptions();
    }

    private void logDebugOptions() {
        for (ArtifactBase debugOptionsArtifact : _debugOptionsArtifacts) {
            logDebugOptions(debugOptionsArtifact);
        }
    }

    private void logDebugOptions(ArtifactBase debugOptionsArtifact) {
        appendLineToLog("Options applied to artifact "+debugOptionsArtifact.getName()+" in "+debugOptionsArtifact.getArtifactFileName());
        ArtifactMetaData artifactMetaData=ArtifactMetaData.getArtifactMetaData(debugOptionsArtifact.getType());
        artifactMetaData.getSupportedOptions().forEach(f->{
            MapChainValue mapChainValue=debugOptionsArtifact.getOptions().get(f.toString());
            if (mapChainValue!=null) {
                appendLineToLog(f.toString()+" : "+ f.FormatValue(mapChainValue.getValue())+" : "+mapChainValue.getValueSourceName());
            }
        });
    }

    private void produceArtifactFiles(ArtifactFile artifactFile) {
        String tempArtifactFilename=null;
        RuntimeException caughtException=null;
        try {
            tempArtifactFilename=getTempArtifactFileName(artifactFile);
            //create tempfile directory if not exists
            (new File(tempArtifactFilename)).getParentFile().mkdirs();
            try (FileWriter fileWriter=new FileWriter(tempArtifactFilename)) {
                try (BufferedWriter bufferedWriter=new BufferedWriter(fileWriter)) {
                    writeFileContent(artifactFile, bufferedWriter);
                }
            }
            com.google.common.io.Files.move(new File(tempArtifactFilename),new File(artifactFile.getArtifactFileName()));
            addTraceEvent(new CreatedArtifactFile(artifactFile.getArtifactFileName()));
        }
        catch (IOException ioException) {
            caughtException=new RuntimeException(ioException);
        }
        catch (RuntimeException exception) {
            caughtException=exception;
        }
        if (caughtException!=null) {
            try {
                //best effort cleanup
                File tempFile=new File(tempArtifactFilename);
                if (tempFile.exists())
                    tempFile.delete();
            }
            catch (Exception ignoreException) {}
            throw caughtException;
        }
    }

    private void writeFileContent(ArtifactFile artifactFile, BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(String.format("package %s;%n%n",artifactFile.getPackageName()));
        String[] imports=artifactFile.getOptions().getImports();
        if (imports!=null) {
            for (String anImport : imports)
                bufferedWriter.write(String.format("import %s;%n", anImport));
        }
        int index=0;
        for (ArtifactBase artifactBase : artifactFile.getArtifacts()) {
            if (index==0) {
                String[] moreImports=artifactBase.getMoreImports();
                for (String moreImport : moreImports)
                    bufferedWriter.write(String.format("import %s;%n", moreImport));
                bufferedWriter.write(String.format("%n%n"));
            }
            writeArtifactContent(artifactBase,bufferedWriter);
            index++;
            if (artifactBase.getOptions().isDebugFlag())
                _debugOptionsArtifacts.add(artifactBase);
        }
        bufferedWriter.flush();
    }

    private void writeArtifactContent(ArtifactBase artifactBase, BufferedWriter bufferedWriter) throws IOException {
        artifactBase.writeArtifactDeceleration(bufferedWriter);
        bufferedWriter.write(String.format(" {%n%n"));
        artifactBase.writeArtifactContent(bufferedWriter);
        if (artifactBase.getNestedArtifacts()!=null) {
            for (ArtifactBase nestedArtifactBase : artifactBase.getNestedArtifacts()) {
                writeArtifactContent(artifactBase,bufferedWriter);
                if (nestedArtifactBase.getOptions().isDebugFlag())
                    _debugOptionsArtifacts.add(nestedArtifactBase);
            }
        }
        bufferedWriter.write(String.format("%n}%n"));
    }

    private String getTempArtifactFileName(ArtifactFile artifactFile) {
        Path artifactFilePath= Paths.get(artifactFile.getArtifactFileName());
        Path artifactFolder=artifactFilePath.getParent();
        String baseFileName= Helpers.removeFilenameExtension(artifactFilePath.getFileName().toString());
        int i=0;
        while (i<1000) {
            String tempBaseFileName=String.format(".%s%03d.tmp",baseFileName,i);
            File tempBaseFile=artifactFolder.resolve(tempBaseFileName).toFile();
            if (!tempBaseFile.exists())
                return tempBaseFile.getAbsolutePath();
            i++;
        }
        throw new RuntimeException("Could not find a temp file path for "+artifactFile.getArtifactFileName());
    }

    public List<TemplateFileData> toSimpleTemplateFileData() {
        List<TemplateFileData> templates=new ArrayList<>();
        Map<Schema,List<ArtifactBase>> artifactsBySchema=new HashMap<>();
        for (ArtifactFile artifactFile : _artifactFiles) {
            for (ArtifactBase artifactBase : artifactFile.getArtifacts()) {
                List<ArtifactBase> schemaArtifacts=artifactsBySchema.get(artifactBase.getSchema());
                if (schemaArtifacts==null) {
                    schemaArtifacts=new ArrayList<>();
                    artifactsBySchema.put(artifactBase.getSchema(),schemaArtifacts);
                }
                schemaArtifacts.add(artifactBase);
            }
        }
        artifactsBySchema.forEach((schema,artifacts)-> {
            TemplateFileData templateFileData=new TemplateFileData();
            templateFileData.fields=new HashMap<>();
            for (SchemaField schemaField : schema.getFields()) {
                templateFileData.fields.put(schemaField.getName(),schemaField.getType());
            }
            List<ArtifactData> artifactDatas=new ArrayList<ArtifactData>();
            for (ArtifactBase artifactBase : artifacts) {
                ClassArtifactData artifactData=new ClassArtifactData();
                artifactData.type=artifactBase.getType().toString();
                artifactData.name=artifactBase.getName();
                if (artifactBase.getOptions().hasLocalProperties())
                    artifactData.options=artifactBase.getOptions().cloneLocalProperties();
                switch (artifactBase.getType()) {
                    case Pojo:
                        break;
                    default :
                        throw new RuntimeException("Not implemented");
                }
                artifactDatas.add(artifactData);

            }
            templateFileData.artifacts=artifactDatas.toArray(new ArtifactData[artifactDatas.size()]);
            templates.add(templateFileData);
        });

        return templates;
    }

    public  void appendLineToLog(String message) {
        _loggingService.appendLine(message);
    }
    private void addTraceEvent(ITraceEvent event) {
        _loggingService.addTraceEvent(event);
    }

}
