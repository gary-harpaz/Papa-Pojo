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
import org.ppojo.exceptions.FolderNotFoundException;
import org.ppojo.exceptions.InvalidFolderPathException;

import java.util.Set;

import static org.ppojo.utils.Helpers.getResourcePath;

/**
 * Created by GARY on 9/24/2015.
 */
public class FolderTemplateFileQueryTest {

    @Test(expected = FolderNotFoundException.class)
    public void FolderNotFoundTest() {
        String path= getResourcePath(SchemaTestResources.SampleJavaApp).resolve("NotExists").toString();
        FolderTemplateFileQuery query=new FolderTemplateFileQuery(path);
        query.getTemplateFiles();
    }
    @Test(expected = InvalidFolderPathException.class)
    public void FolderInvalidPathTest() {
        String path= getResourcePath(SchemaTestResources.MainPackageFolder+ "\\Main.java").toString();
        FolderTemplateFileQuery query=new FolderTemplateFileQuery(path);
        query.getTemplateFiles();
    }
    //TODO restore this test when sample code has setteled
    public void TemplateQueryTest (){
        String baseSourceFolder= getResourcePath(SchemaTestResources.MainSourcesFolder).toString();
        FolderTemplateFileQuery query=new FolderTemplateFileQuery(baseSourceFolder);
        Set<String> templates=query.getTemplateFiles();
        for (String templateFile : SchemaTestPaths.Instance().getTemplateResources()) {
            Assert.assertTrue("Expected to find template "+templateFile,templates.contains(templateFile));
        }
        Assert.assertTrue("Expected total number of templates "+SchemaTestResources.totalTemplateFiles(),templates.size()==SchemaTestResources.totalTemplateFiles());
    }
}
