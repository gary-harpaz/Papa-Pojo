package org.ppojo;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;

import static org.ppojo.utils.Helpers.EmptyIfNull;

/**
 * Created by GARY on 9/23/2015.
 */
public class PojoArtifact extends ClassArtifactBase {

    public PojoArtifact(@Nonnull IArtifactParent artifactParent,ArtifactParser artifactParser,String extendsClass, String[] implementsInterfaces) {
        super(artifactParent,artifactParser,extendsClass, implementsInterfaces);
    }

    @Override
    public ArtifactTypes getType() {
        return ArtifactTypes.Pojo;
    }



    @Override
    public void writeArtifactContent(BufferedWriter bufferedWriter) throws IOException {
        boolean encapsulateFields=getOptions().getEncapsulateFields();
        if (!encapsulateFields)
            writeUnencapsulatedField(bufferedWriter);
        else
            writeEncapsulatedFields(bufferedWriter);
    }

    private void writeEncapsulatedFields(BufferedWriter bufferedWriter) throws IOException {
        String privateFieldPrefix=getOptions().getPrivateFieldPrefix();
        privateFieldPrefix=EmptyIfNull(privateFieldPrefix);
        CapitalizationTypes encapsulationCapitalization=getOptions().getCapitalization();
        String getterPrefix=getOptions().getGetterPrefix();
        getterPrefix=EmptyIfNull(getterPrefix);
        String setterPrefix=getOptions().getSetterPrefix();
        setterPrefix=EmptyIfNull(setterPrefix);

        for (SchemaField schemaField : this.getSchema().getFields()) {
            bufferedWriter.append("private ")
                    .append(schemaField.getType())
                    .append(" ").append(privateFieldPrefix).append(schemaField.getName())
                    .append(";").append(System.lineSeparator());
        }
        bufferedWriter.append(System.lineSeparator());

        for (SchemaField schemaField : this.getSchema().getFields()) {
            bufferedWriter.append("public ")
                    .append(schemaField.getType()).append(" ")
                    .append(capitalizeName(getterPrefix, schemaField.getName(), encapsulationCapitalization))
                    .append("() { return ").append(privateFieldPrefix).append(schemaField.getName())
                    .append("; }").append(System.lineSeparator());
            bufferedWriter.append("public void ")
                    .append(capitalizeName(setterPrefix, schemaField.getName(), encapsulationCapitalization))
                    .append("(").append(schemaField.getType()).append(" ").append(schemaField.getName())
                    .append(") { this.").append(privateFieldPrefix).append(schemaField.getName())
                    .append(" = ").append(schemaField.getName()).append("; }").append(System.lineSeparator());
        }
    }

    private void writeUnencapsulatedField(BufferedWriter bufferedWriter) throws IOException {
        CapitalizationTypes capitalization=getOptions().getPublicFieldCapitalization();
        for (SchemaField schemaField : this.getSchema().getFields()) {
            bufferedWriter.append("public ")
                    .append(schemaField.getType())
                    .append(" ").append(capitalizeName("", schemaField.getName(), capitalization))
                    .append(";").append(System.lineSeparator());
        }
    }




}
