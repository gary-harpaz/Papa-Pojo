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
