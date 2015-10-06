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

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.ppojo.utils.Helpers.getResourcePath;

/**
 * Created by GARY on 9/25/2015.
 */
public class SchemaTestPaths {

    private static Object _locker=new Object();
    private static SchemaTestPaths _instace;
    public static SchemaTestPaths Instance() {
        if (_instace==null) {
            synchronized (_locker) {
                if (_instace==null)
                    _instace=new SchemaTestPaths();
            }
        }
        return _instace;
    }

    private final Set<String> _templateFiles;
    private final Set<String> _artifactFiles;

    SchemaTestPaths() {
        String baseSourceFolder= getResourcePath(SchemaTestResources.MainPackageFolder).toString();
        _templateFiles=new HashSet<>();
        for (String template : SchemaTestResources.getTemplateResources()) {
            _templateFiles.add(Paths.get(baseSourceFolder,template).toString());
        }
        _artifactFiles=new HashSet<>();
        for (String artifact : SchemaTestResources.getExpectedArtifactPaths()) {
            _artifactFiles.add(Paths.get(baseSourceFolder,artifact).toString());
        }
    }
    public Iterable<String> getTemplateResources() {
        return  _templateFiles;
    }
    public int totalTemplateFiles() {
        return _templateFiles.size();
    }
    public Iterable<String> getExpectedArtifactPaths() {
        return _artifactFiles;
    }
    public int totalArtifactFiles() {
        return  _artifactFiles.size();
    }
}
