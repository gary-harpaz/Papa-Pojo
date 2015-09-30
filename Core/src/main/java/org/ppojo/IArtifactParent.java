package org.ppojo;

/**
 * Created by GARY on 9/26/2015.
 */
public interface IArtifactParent {
    ArtifactOptions getOptions();

    void addChildArtifact(ArtifactBase artifactBase);
}
