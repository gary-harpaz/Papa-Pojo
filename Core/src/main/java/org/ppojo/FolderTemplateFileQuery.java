package org.ppojo;
import org.apache.commons.io.filefilter.WildcardFileFilter;
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
 * Created by GARY on 9/24/2015.
 */
public class FolderTemplateFileQuery implements ITemplateFileQuery {
    private final String _path;
    private final String _fileFilter;
    private final boolean _isRecursive;
    public FolderTemplateFileQuery(String path) {
        this(path,null,true);
    }

    public FolderTemplateFileQuery(String path,String fileFilter,boolean isRecursive) {
        _path=path;
        if (IsNullOrEmpty(fileFilter))
            fileFilter="*.pppj";
        _fileFilter=fileFilter;
        _isRecursive=isRecursive;
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
