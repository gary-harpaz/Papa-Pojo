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
import java.io.BufferedWriter;
import java.io.IOException;
import org.ppojo.data.*;

/**
 * Represents a resolved {@link ArtifactTypes#Interface} artifact.
 * @see InterfaceArtifactData
 */
public class InterfaceArtifact extends ArtifactBase{

    protected InterfaceArtifact(@Nonnull IArtifactParent artifactParent, @Nonnull ArtifactParser artifactParser
            , boolean isReadOnly, String extendsInterface) {
        super(artifactParent, artifactParser);
        _isReadOnly=isReadOnly;
        _extendsInterface=extendsInterface;
    }



    private final boolean _isReadOnly;
    private final String _extendsInterface;
    public boolean getIsReadOnly() {
        return _isReadOnly;
    }


    @Override
    public ArtifactTypes getType() {
        return ArtifactTypes.Interface;
    }
    @Override
    public void writeArtifactDeceleration(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("public interface ");
        bufferedWriter.write(this.getName());
        if (!Helpers.IsNullOrEmpty(_extendsInterface)) {
            bufferedWriter.write(" extends ");
            bufferedWriter.write(_extendsInterface);
        }
    }

    @Override
    public void writeArtifactContent(BufferedWriter bufferedWriter) throws IOException {
        CapitalizationTypes encapsulationCapitalization=getOptions().getCapitalization();
        String getterPrefix=getOptions().getGetterPrefix();
        String setterPrefix=getOptions().getSetterPrefix();

        setCurrentIndent(1);

        for (SchemaField schemaField : this.getSchema().getFields()) {
            bufferedWriter.append(getIndent())
                    .append(schemaField.getType()).append(" ")
                    .append(capitalizeName(getterPrefix, schemaField.getName(), encapsulationCapitalization))
                    .append("();").append(System.lineSeparator());
            if (!_isReadOnly) {
                bufferedWriter.append(getIndent()).append("void ")
                        .append(capitalizeName(setterPrefix, schemaField.getName(), encapsulationCapitalization))
                        .append("(").append(schemaField.getType()).append(" ").append(schemaField.getName())
                        .append(");").append(System.lineSeparator());
            }
        }
    }
}
