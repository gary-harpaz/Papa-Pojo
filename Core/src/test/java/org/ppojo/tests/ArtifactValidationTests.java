package org.ppojo.tests;

import com.google.gson.JsonParseException;
import org.junit.Test;
import org.ppojo.utils.ArrayListBuilder;
import org.ppojo.FolderTemplateFileQuery;
import org.ppojo.ITemplateFileQuery;
import org.ppojo.SchemaGraphParser;
import org.ppojo.exceptions.RequiredPropertyMissing;

/**
 * Created by GARY on 9/29/2015.
 */
public class ArtifactValidationTests {
    @Test(expected = com.google.gson.JsonSyntaxException.class)
    public void InvalidJsonFormatTest() {
        DoValidationTest("Test01");
    }

    @Test(expected = JsonParseException.class)
    public void InvalidArtifactElementType() {
        DoValidationTest("Test02");
    }

    @Test(expected = RequiredPropertyMissing.class)
    public void RequiredArtifactTypeMissing() {
        DoValidationTest("Test03");
    }

    private static void DoValidationTest(String queryRootReltaiveFolder) {
        String rootQueryFolder= SchemaTestResources.getValidationTestQueryRootFolder(queryRootReltaiveFolder);
        FolderTemplateFileQuery query=new FolderTemplateFileQuery(rootQueryFolder);
        SchemaGraphParser.generateArtifacts(
                ArrayListBuilder.newArrayList(SchemaTestResources.MainValidationTestSourcesFolder).create(),
                ArrayListBuilder.<ITemplateFileQuery>newArrayList(query).create(),
                ArtifactConstructionTest.getDefaultOptions());
    }
}
