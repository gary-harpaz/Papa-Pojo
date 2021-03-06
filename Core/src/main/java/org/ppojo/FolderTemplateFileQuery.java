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
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.ppojo.data.TemplateFileData;
import org.ppojo.exceptions.FolderNotFoundException;
import org.ppojo.exceptions.InvalidFolderPathException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import static org.ppojo.utils.Helpers.IsNullOrEmpty;


/**
 * Implements a query for matching template files in an input folder using a pattern that can contain wildcards. The query can
 * optionaly be recursive or non recursive.
 */
public class FolderTemplateFileQuery implements ITemplateFileQuery {
    public String getPath() {
        return _path;
    }

    private final String _path;

    public String getFileFilter() {
        return _fileFilter;
    }

    private final String _fileFilter;

    public boolean isRecursive() {
        return _isRecursive;
    }

    private final boolean _isRecursive;
    public FolderTemplateFileQuery(String path) {
        this(path,null,true);
    }

    public FolderTemplateFileQuery(String path,String fileFilter,boolean isRecursive) {
        _path=Paths.get(path).toAbsolutePath().normalize().toString();
        if (IsNullOrEmpty(fileFilter))
            fileFilter=getDefaultFileFilter();
        _fileFilter=fileFilter;
        _isRecursive=isRecursive;
    }

    public static String getDefaultFileFilter() {
        return "*."+ TemplateFileData.FILE_EXTENSION;
    }



    @Override
    public Set<String> getTemplateFiles() {
        CheckFolderExists();
        Set<String> result;
        FileFilter fileFilter=new WildcardFileFilter(_fileFilter);
        try {
            result=Files.walk(Paths.get(_path),_isRecursive?Integer.MAX_VALUE:1)
            .map(p->new File(p.toUri()))
            .filter(f->fileFilter.accept(f))
            .map(f->f.getAbsolutePath())
            .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private void CheckFolderExists() {
        File f = new File(_path);
        if (!f.exists())
            throw new FolderNotFoundException(_path);
        if (!f.isDirectory())
            throw new InvalidFolderPathException("Path parameter does no point to a folder "+_path);

    }
}
