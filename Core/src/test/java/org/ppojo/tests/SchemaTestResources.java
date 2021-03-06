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

import org.ppojo.utils.Helpers;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by GARY on 9/24/2015.
 */
public class SchemaTestResources {
    public static final String SimplePojoSchema="SimplePojoSchema.json";
    public static final String SampleJavaApp="SampleJavaApp";
    public static final String srcFolder="src";
    public static final String rootPackagePath="com\\company";
    public static final String secondaryPackagePath="other\\tests";
    public static final String testResultsFolder="TestResults";
    public static final String MainSourcesFolder= Paths.get(SampleJavaApp, srcFolder).toString();
    public static final String MainPackageFolder= Paths.get(MainSourcesFolder, rootPackagePath).toString();
    public static final String TestResultsMainPackageFolder=Paths.get(testResultsFolder,srcFolder,rootPackagePath).toString();
    public static final String SecondaryPackageFolder= Paths.get(MainSourcesFolder, secondaryPackagePath).toString();
    public static final String TestResultsSecondaryPackageFolder=Paths.get(testResultsFolder,srcFolder,secondaryPackagePath).toString();

    public static final String BadTemplateRootFolder=SampleJavaApp+"\\srcBadTemplates\\com\\company2";
    private static final Set<String> _templateFiles;
    private static final Set<String> _artifactFiles;
    static  {
        _templateFiles=new HashSet<>();
        _templateFiles.add("SimplePojo.pppj");
        _templateFiles.add("ConvetntionOptions.pppj");
        _templateFiles.add("sample03\\RelativePath.pppj");
        _templateFiles.add("sample04\\LibsAndImports.pppj");
        _templateFiles.add("sample05\\ExtendsImplemets.pppj");

        _artifactFiles=new HashSet<>();
        _artifactFiles.add("Person1.java");
        _artifactFiles.add("Person2.java");
        _artifactFiles.add("Person3.java");
    }
    public static Iterable<String> getTemplateResources() {
        return  _templateFiles;
    }
    public static int totalTemplateFiles() {
        return _templateFiles.size();
    }
    public static Iterable<String> getExpectedArtifactPaths() {
        return _artifactFiles;
    }
    public static int totalArtifactFiles() {
        return  _artifactFiles.size();
    }

    public static final String testResultWarning="/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */";

    private static final String validationSrcFolder="srcInvalidTemplates";
    public static final String MainValidationTestSourcesFolder= Paths.get(SampleJavaApp, validationSrcFolder).toString();
    public static String getValidationTestQueryRootFolder(String subpath) {
        return Helpers.getResourcePath(Paths.get(MainValidationTestSourcesFolder,"com\\company2",subpath).toString()).toString();
    }
}
