package org.ppojo.tests;

import org.junit.Assert;
import org.junit.Test;
import org.ppojo.*;
import org.ppojo.utils.ArrayListBuilder;
import org.ppojo.utils.EmptyArray;
import org.ppojo.utils.Helpers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.ppojo.utils.Helpers.getResourcePath;

/**
 * Created by GARY on 9/25/2015.
 * Important to note about this test that default options supplied here belong to this specific test and should not be altered.
 * Production may very well use different set of default options.
 */
public class ArtifactConstructionTest {

    public static Map<String,Object> newDefaultProperties() {
        Map<String,Object> defaultProperties=new HashMap<>();
        defaultProperties.put(ArtifactOptions.Fields.privateFieldPrefix.toString(),"_");
        defaultProperties.put(ArtifactOptions.Fields.encapsulateFields.toString(),true);
        defaultProperties.put(ArtifactOptions.Fields.propertyCapitalization.toString(),CapitalizationTypes.camelCase);
        defaultProperties.put(ArtifactOptions.Fields.publicFieldCapitalization.toString(),CapitalizationTypes.camelCase);
        defaultProperties.put(ArtifactOptions.Fields.getterPrefix.toString(),"get");
        defaultProperties.put(ArtifactOptions.Fields.setterPrefix.toString(),"set");
        defaultProperties.put(ArtifactOptions.Fields.privateFieldName.toString(),"data");
        defaultProperties.put(ArtifactOptions.Fields.imports.toString(), EmptyArray.get(String.class));
        defaultProperties.put(ArtifactOptions.Fields.enumCapitalization.toString(),CapitalizationTypes.camelCase);
        defaultProperties.put(ArtifactOptions.Fields.undefinedMember.toString(),"");
        defaultProperties.put(ArtifactOptions.Fields.constantMemberCapitalization.toString(),CapitalizationTypes.ALL_CAPS);
        defaultProperties.put(ArtifactOptions.Fields.constantValueCapitalization.toString(),CapitalizationTypes.camelCase);
        defaultProperties.put(ArtifactOptions.Fields.indentString.toString(),"    ");
        return defaultProperties;
    }
    private static ArtifactOptions _defaultOptions=new ArtifactOptions("Default",newDefaultProperties(),null);;


    public static ArtifactOptions getDefaultOptions() {
        return _defaultOptions;
    }

    @Test
    public void constructArtifacts() {
        cleanupPreviousArtifacts();
        String baseSourceFolder= getResourcePath(SchemaTestResources.MainSourcesFolder).toString();
        FolderTemplateFileQuery query=new FolderTemplateFileQuery(baseSourceFolder);
        SchemaGraphParser.generateArtifacts(
                ArrayListBuilder.newArrayList(baseSourceFolder).create(),
                ArrayListBuilder.<ITemplateFileQuery>newArrayList(query).create(),
                _defaultOptions);

        assertArtifact("sampleSimplePojo\\Person.java");
        assertArtifact("sampleNamingOptions\\Person1.java");
        assertArtifact("sampleNamingOptions\\Person2.java");
        assertArtifact("sampleNamingOptions\\Person3.java");
        assertArtifact("sampleRelativeOutputPath\\Person3.java");
        assertArtifact("sampleRelativeOutputPath\\S1\\S2\\Person1.java");
        assertArtifact("Person2.java");
        assertArtifact("sampleLibsAndImports\\Person.java");
        assertArtifact("sampleExtendsImplements\\Worker.java");
        assertArtifact("sampleInterface\\Article.java");
        assertArtifact("sampleInterface\\IArticleHeader.java");
        assertArtifact("sampleInterface\\IArticleHeaderRO.java");

        
    }

    private void assertArtifact(String path) {
        File artifactFile= getResourcePath(SchemaTestResources.MainPackageFolder).resolve(path).toFile();
        Assert.assertTrue("Artifact output file not found in "+artifactFile.getAbsolutePath(),artifactFile.exists());
        File resultsFile=getResourcePath(SchemaTestResources.TestResultsMainPackageFolder).resolve(path).toFile();
        Assert.assertTrue("Artifact results file not found in "+resultsFile.getAbsolutePath(),artifactFile.exists());
        String outputText= Helpers.readTextFile(artifactFile.getAbsolutePath());
        outputText=outputText.replaceAll("\\s+","");
        String resultText=Helpers.readTextFile(resultsFile.getAbsolutePath());
        resultText=resultText.replace(SchemaTestResources.testResultWarning,"");
        resultText=resultText.replaceAll("\\s+","");
        Assert.assertTrue("Output file and result file don't match. output has changed in "+path,outputText.equals(resultText));

    }

    private void cleanupPreviousArtifacts() {
        for (String artifact : SchemaTestPaths.Instance().getExpectedArtifactPaths()) {
            File file=new File(artifact);
            if (file.exists())
                if (!file.delete())
                    throw new RuntimeException("Failed to cleanup artifact file "+artifact);
        }
    }
}
