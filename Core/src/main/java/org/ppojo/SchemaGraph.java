package org.ppojo;

import org.ppojo.data.ArtifactData;
import org.ppojo.data.ClassArtifactData;
import org.ppojo.data.TemplateFileData;
import org.ppojo.utils.Helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by GARY on 9/26/2015.
 */
public class SchemaGraph {
    private final Iterable<ArtifactFile> _artifactFiles;
    public SchemaGraph(Iterable<ArtifactFile> artifactFiles) {
        _artifactFiles=artifactFiles;
    }

    public void produceArtifactFiles() {
        for (ArtifactFile artifactFile : _artifactFiles) {
            produceArtifactFiles(artifactFile);
        }

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
}
