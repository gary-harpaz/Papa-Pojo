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
