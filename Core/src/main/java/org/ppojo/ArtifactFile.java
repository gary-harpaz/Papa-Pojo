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

import org.ppojo.utils.Helpers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;

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
    public @Nonnull Iterable<ArtifactBase> getArtifacts() {
        return _artifacts;
    }
}
