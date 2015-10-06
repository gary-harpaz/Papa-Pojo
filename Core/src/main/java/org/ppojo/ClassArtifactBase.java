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

import org.ppojo.utils.EmptyArray;
import org.ppojo.utils.Helpers;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

import static org.ppojo.utils.Helpers.as;

/**
 * Created by GARY on 9/26/2015.
 */
public abstract class ClassArtifactBase extends ArtifactBase {

    private final String _extendsClass;
    private final String[] _implementsInterfaces;
    public String[] getImplementsInterfaces() {
        return _implementsInterfaces;
    }
    public String getExtendsClass() {
        return _extendsClass;
    }

    protected ClassArtifactBase(@Nonnull IArtifactParent artifactParent, ArtifactParser artifactParser,
                                String extendsClass,String[] implementsInterfaces) {

        super(artifactParent,artifactParser);
        _extendsClass=extendsClass;
        _implementsInterfaces=implementsInterfaces;
    }

    @Override
    public void writeArtifactDeceleration(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("public class ");
        bufferedWriter.write(this.getName());
        if (!Helpers.IsNullOrEmpty(_extendsClass)) {
            bufferedWriter.write(" extends ");
            bufferedWriter.write(_extendsClass);
        }
        if (_implementsInterfaces!=null && _implementsInterfaces.length>0) {
            bufferedWriter.write(" implements ");
            for (int i = 0; i < _implementsInterfaces.length; i++) {
                if (i>0)
                    bufferedWriter.write((", "));
                bufferedWriter.write(_implementsInterfaces[i]);
            }
        }
    }

    public  @Nonnull String[] getRequiredArtifactImports(ArtifactBase artifact) {
        ArtifactFile artifactFile=as(ArtifactFile.class,artifact.getArtifactParent());
        if (artifactFile!=null) {
            String possibleImport1 = artifactFile.getPackageName() + "." + artifact.getName();
            String possibleImport2 = artifactFile.getPackageName() + ".*";
            String[] imports = artifactFile.getOptions().getImports();
            if (imports == null || Arrays.stream(imports).noneMatch(imp -> imp.equals(possibleImport1) || imp.equals(possibleImport2)))
                return new String[] {possibleImport1};
        }
        return EmptyArray.get(String.class);
    }
}
