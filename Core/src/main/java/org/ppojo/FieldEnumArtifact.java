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
