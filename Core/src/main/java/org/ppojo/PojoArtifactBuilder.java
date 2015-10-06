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
