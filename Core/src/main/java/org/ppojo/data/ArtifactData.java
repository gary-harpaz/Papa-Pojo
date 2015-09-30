package org.ppojo.data;

import org.ppojo.CapitalizationTypes;

import java.util.Map;

/**
 * Created by GARY on 9/23/2015.
 */
public abstract class ArtifactData {
    public String type;
    public String name;
    public String path;
    public String nestInArtifact;
    public transient Map<String,Object> options;



}
