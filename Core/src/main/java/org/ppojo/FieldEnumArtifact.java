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

        StringBuilder stringBuilder=new StringBuilder();
        int idx=0;
        if (!Helpers.IsNullOrEmpty(undefinedMember)) {
            appendEnumMember(undefinedMember,capitalization,stringBuilder);
            idx++;
        }
        for (SchemaField schemaField : this.getSchema().getFields()) {
            if (idx>0)
                stringBuilder.append(",").append(System.lineSeparator());
            appendEnumMember(schemaField.getName(),capitalization,stringBuilder);
            idx++;
        }
        stringBuilder.append(System.lineSeparator());
        bufferedWriter.write(stringBuilder.toString());
    }

    private void appendEnumMember(String name, CapitalizationTypes encapsulationCapitalization, StringBuilder stringBuilder) {
        capitalizeName("", name, encapsulationCapitalization, stringBuilder);
    }
}
