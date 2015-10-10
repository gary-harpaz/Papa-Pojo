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

import org.ppojo.exceptions.FileNotFound;
import org.ppojo.exceptions.InvalidFilePath;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Implements a query which always results in a single file result, if the file exists.
 */
public class TemplateFileQuery implements ITemplateFileQuery {
    public TemplateFileQuery(String path,boolean isValidated) {
       _path=path;
        _isValidated=isValidated;
    }

    public String getPath() {
        return _path;
    }

    private final String _path;
    private boolean _isValidated;

    @Override
    public Iterable<String> getTemplateFiles() {
        CheckFileExists();
        Set<String> result=new HashSet<>();;
        result.add(_path);
        return result;
    }

    private void CheckFileExists() {
        if (_isValidated)
            return;;
        File file=(new File(_path)).getAbsoluteFile();
        if (!file.exists())
            throw new FileNotFound("Template file "+file.getAbsolutePath()+" does not exist");
        if (!file.isFile())
            throw new InvalidFilePath("Template file "+file.getAbsolutePath()+" is not a file");
        _isValidated=true;
    }
}
