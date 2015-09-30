package org.ppojo;

import org.ppojo.utils.Helpers;

import javax.annotation.Nullable;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GARY on 9/26/2015.
 */
public class ArtifactFile implements IArtifactParent {
    private final String _artifactFileName;
    private final String _packageName;
    private final ArtifactOptions _options;
    private List<ArtifactBase> _artifacts;

    public String getArtifactFileName() {
        return _artifactFileName;
    }
    public String getPackageName() {
        return _packageName;
    }




public ArtifactFile(String artifactFileName, String parentSourceRootFolder, @Nullable ArtifactOptions templateOptions) {
        _artifacts=new ArrayList<>();
        _artifactFileName =artifactFileName;
        URI artifactFolderURI= Paths.get(artifactFileName).getParent().toUri();
        URI parentSourceURI= Paths.get(parentSourceRootFolder).toUri();
        String relativeUti=parentSourceURI.relativize(artifactFolderURI).toString();
        if (relativeUti.endsWith("/"))
            relativeUti= Helpers.removeLastOccurrenceOf(relativeUti, "/");
        _packageName= relativeUti.replace("/",".");
       _options=templateOptions;
    }

    @Override
    public ArtifactOptions getOptions() {
        return _options;
    }

    @Override
    public void addChildArtifact(ArtifactBase artifactBase) {
        _artifacts.add(artifactBase);
    }
    public Iterable<ArtifactBase> getArtifacts() {
        return _artifacts;
    }
}
