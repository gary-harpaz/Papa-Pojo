package org.ppojo.tests;

import org.junit.Assert;
import org.junit.Test;
import org.ppojo.*;
import org.ppojo.utils.ArrayListBuilder;
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

    private static ArtifactOptions _defaultOptions;

    static  {
        Map<String,Object> defaultProperties=new HashMap<>();
        defaultProperties.put(ArtifactOptions.Fields.privateFieldPrefix.toString(),"_");
        defaultProperties.put(ArtifactOptions.Fields.encapsulateFields.toString(),true);
        defaultProperties.put(ArtifactOptions.Fields.propertyCapitalization.toString(),CapitalizationTypes.camelCase);
        defaultProperties.put(ArtifactOptions.Fields.getterPrefix.toString(),"get");
        defaultProperties.put(ArtifactOptions.Fields.setterPrefix.toString(),"set");
        _defaultOptions=new ArtifactOptions("Default",defaultProperties,null);
    }
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

        assertArtifact("sample01\\Person1.java");
        assertArtifact("sample02\\Person2.java");
        assertArtifact("sample02\\Person3.java");
        assertArtifact("sample03\\Person6.java");
        assertArtifact("sample03\\S1\\S2\\Person4.java");
        assertArtifact("Person5.java");
        assertArtifact("sample04\\Person7.java");
        assertArtifact("sample05\\Worker.java");
        assertArtifact("sample06\\Article.java");
        assertArtifact("sample06\\IArticleHeader.java");
        assertArtifact("sample06\\IArticleHeaderRO.java");

        
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
