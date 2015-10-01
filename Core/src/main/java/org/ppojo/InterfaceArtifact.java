package org.ppojo;

import org.ppojo.utils.Helpers;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;

import static org.ppojo.utils.Helpers.EmptyIfNull;

/**
 * Created by GARY on 9/28/2015.
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
        getterPrefix=EmptyIfNull(getterPrefix);
        String setterPrefix=getOptions().getSetterPrefix();
        setterPrefix=EmptyIfNull(setterPrefix);

        for (SchemaField schemaField : this.getSchema().getFields()) {
            bufferedWriter
                    .append(schemaField.getType()).append(" ")
                    .append(capitalizeName(getterPrefix, schemaField.getName(), encapsulationCapitalization))
                    .append("();").append(System.lineSeparator());
            if (!_isReadOnly) {
                bufferedWriter.append("void ")
                        .append(capitalizeName(setterPrefix, schemaField.getName(), encapsulationCapitalization))
                        .append("(").append(schemaField.getType()).append(" ").append(schemaField.getName())
                        .append(");").append(System.lineSeparator());
            }
        }
    }
}
