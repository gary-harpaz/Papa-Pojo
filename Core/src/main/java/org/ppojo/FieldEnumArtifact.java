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

/**
 * Created by GARY on 9/28/2015.
 */
public class FieldEnumArtifact extends ArtifactBase {
    public FieldEnumArtifact(@Nonnull IArtifactParent artifactParent,@Nonnull ArtifactParser artifactParser) {
        super(artifactParent, artifactParser);
    }


    @Override
    public ArtifactTypes getType() {
        return ArtifactTypes.FieldEnum;
    }

    @Override
    public void writeArtifactDeceleration(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("public enum ");
        bufferedWriter.write(this.getName());
    }

    @Override
    public void writeArtifactContent(BufferedWriter bufferedWriter) throws IOException {
        CapitalizationTypes capitalization=getOptions().getEnumCapitalization();
        String undefinedMember=getOptions().getUndefinedMember();
        setCurrentIndent(1);
        int idx=0;
        if (!Helpers.IsNullOrEmpty(undefinedMember)) {
            bufferedWriter.append(getIndent())
                    .append(capitalizeName("", undefinedMember, capitalization));
            idx++;
        }
        for (SchemaField schemaField : this.getSchema().getFields()) {
            if (idx>0)
                bufferedWriter.append(",").append(System.lineSeparator());
            bufferedWriter.append(getIndent())
                    .append(capitalizeName("", schemaField.getName(), capitalization));
            idx++;
        }
        bufferedWriter.append(System.lineSeparator());
    }
}
