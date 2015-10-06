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

package org.ppojo.tests;

import org.junit.Assert;
import org.junit.Test;
import org.ppojo.*;
import org.ppojo.data.CopyStyleData;
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
        defaultProperties.put(ArtifactOptions.Fields.pojoCopyStyles.toString(),EmptyArray.get(CopyStyleData.class));
        defaultProperties.put(ArtifactOptions.Fields.immutableCopyDataMember .toString(),"");
        defaultProperties.put(ArtifactOptions.Fields.immutableDefensiveCopy.toString(),false);
        defaultProperties.put(ArtifactOptions.Fields.fluentNewBuilderName.toString(),"newBuilder");
        defaultProperties.put(ArtifactOptions.Fields.fluentGetDataName.toString(),"create");
        defaultProperties.put(ArtifactOptions.Fields.fluentResetBuilderName.toString(),"reset");
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

        assetMainArtifactFile("sampleSimplePojo\\Person.java");
        assetMainArtifactFile("sampleNamingOptions\\Person1.java");
        assetMainArtifactFile("sampleNamingOptions\\Person2.java");
        assetMainArtifactFile("sampleNamingOptions\\Person3.java");
        assetMainArtifactFile("sampleRelativeOutputPath\\Person3.java");
        assetMainArtifactFile("sampleRelativeOutputPath\\S1\\S2\\Person1.java");
        assetMainArtifactFile("Person2.java");
        assetMainArtifactFile("sampleLibsAndImports\\Person.java");
        assetMainArtifactFile("sampleExtendsImplements\\Worker.java");
        assetMainArtifactFile("sampleInterface\\Article.java");
        assetMainArtifactFile("sampleInterface\\IArticleHeader.java");
        assetMainArtifactFile("sampleInterface\\IArticleHeaderRO.java");
        assetMainArtifactFile("sampleExtendsImplements\\Worker.java");
        assetMainArtifactFile("sampleEnum\\Product.java");
        assetMainArtifactFile("sampleEnum\\ProductFields.java");
        assetMainArtifactFile("sampleFluentBuilder\\Article.java");
        assetMainArtifactFile("sampleFluentBuilder\\ArticleBuilder.java");

        assertSecondaryArtifactFile("copy\\Person1.java");
        assertSecondaryArtifactFile("copy\\Person2.java");
        assertSecondaryArtifactFile("copy\\Person3.java");
        assertSecondaryArtifactFile("copy\\Person4.java");
        assertSecondaryArtifactFile("copy\\Person5.java");
        assertSecondaryArtifactFile("copy\\Person6.java");
        assertSecondaryArtifactFile("copy\\Person7.java");
        assertSecondaryArtifactFile("copy\\Person8.java");
        assertSecondaryArtifactFile("copy\\Person9.java");
        assertSecondaryArtifactFile("copy\\Person10.java");
        assertSecondaryArtifactFile("copy\\Person11.java");
        assertSecondaryArtifactFile("copy\\Person12.java");

        assertSecondaryArtifactFile("immutable\\Article1.java");
        assertSecondaryArtifactFile("immutable\\Article2.java");
        assertSecondaryArtifactFile("immutable\\Article3.java");
        assertSecondaryArtifactFile("immutable\\Article4.java");
        assertSecondaryArtifactFile("immutable\\Article5.java");
        assertSecondaryArtifactFile("immutable\\Article6.java");
        assertSecondaryArtifactFile("immutable\\ROArticle1.java");
        assertSecondaryArtifactFile("immutable\\ROArticle2.java");
        assertSecondaryArtifactFile("immutable\\ROArticle3.java");
        assertSecondaryArtifactFile("immutable\\ROArticle4.java");
        assertSecondaryArtifactFile("immutable\\ROArticle5.java");
        assertSecondaryArtifactFile("immutable\\ROArticle6.java");


        
    }

    private void assetMainArtifactFile(String path) {
        File artifactFile= getResourcePath(SchemaTestResources.MainPackageFolder).resolve(path).toFile();
        Assert.assertTrue("Artifact output file not found in "+artifactFile.getAbsolutePath(),artifactFile.exists());
        File resultsFile=getResourcePath(SchemaTestResources.TestResultsMainPackageFolder).resolve(path).toFile();
        Assert.assertTrue("Artifact results file not found in "+resultsFile.getAbsolutePath(),artifactFile.exists());
        assertArtifactFile(path, artifactFile, resultsFile);
    }

    private void assertSecondaryArtifactFile(String path) {
        File artifactFile= getResourcePath(SchemaTestResources.SecondaryPackageFolder).resolve(path).toFile();
        Assert.assertTrue("Artifact output file not found in "+artifactFile.getAbsolutePath(),artifactFile.exists());
        File resultsFile=getResourcePath(SchemaTestResources.TestResultsSecondaryPackageFolder).resolve(path).toFile();
        Assert.assertTrue("Artifact results file not found in "+resultsFile.getAbsolutePath(),artifactFile.exists());
        assertArtifactFile(path, artifactFile, resultsFile);
    }

    private void assertArtifactFile(String path, File artifactFile, File resultsFile) {
        String outputText= Helpers.readTextFile(artifactFile.getAbsolutePath());
        outputText=outputText.replaceAll("\\s+","");
        String resultText=Helpers.readTextFile(resultsFile.getAbsolutePath());
        resultText=resultText.replace(SchemaTestResources.testResultWarning,"");
        resultText=resultText.replaceAll("\\s+","");
        Assert.assertTrue("Output file and result file don't match. output has changed in " + path, outputText.equals(resultText));
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
