package org.ppojo;

import javax.annotation.Nonnull;

/**
 * Created by GARY on 9/23/2015.
 */
public class PojoArtifactBuilder {
    private PojoArtifact _artifact;
    private IArtifactParent _parent;
    public PojoArtifactBuilder(@Nonnull IArtifactParent artifactParent,@Nonnull ArtifactParser artifactParser) {
        _parent=artifactParent;
        _artifact=new PojoArtifact(artifactParent,artifactParser,null,null);
    }

    public static @Nonnull PojoArtifactBuilder newBuilder(@Nonnull IArtifactParent artifactParent, @Nonnull ArtifactParser artifactParser) {
        return new PojoArtifactBuilder(artifactParent,artifactParser);
    }

    public @Nonnull PojoArtifact create() {
        _parent.addChildArtifact(_artifact);
        return _artifact;
    }
}
