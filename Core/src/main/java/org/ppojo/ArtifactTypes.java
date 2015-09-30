package org.ppojo;

import org.ppojo.utils.EnumParser;

import javax.annotation.Nonnull;

/**
 * Created by GARY on 9/23/2015.
 */
public enum ArtifactTypes {
    Unknown,
    Pojo,
    Interface,
    ROPojo,
    FieldEnum,
    FluentBuilder;

    private static final EnumParser<ArtifactTypes> _parser =new EnumParser<>(org.ppojo.ArtifactTypes.class,ArtifactTypes.Unknown);

    public static @Nonnull ArtifactTypes Parse(String artifactType) {
        return _parser.Parse(artifactType);
    }
}
