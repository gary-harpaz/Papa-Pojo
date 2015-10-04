package org.ppojo;

import org.ppojo.utils.EmptyArray;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

import static org.ppojo.utils.Helpers.EmptyIfNull;
import static org.ppojo.utils.Helpers.as;

/**
 * Created by GARY on 10/1/2015.
 */
public class ImmutableClassArtifact extends ClassArtifactBase {
    protected ImmutableClassArtifact(@Nonnull IArtifactParent artifactParent, @Nonnull ArtifactParser artifactParser,String extendsClass,
                                     String[] implementsInterfaces, @Nonnull PojoArtifact targetPojoArtifact) {
        super(artifactParent, artifactParser,extendsClass,implementsInterfaces);
        _targetArtifact=targetPojoArtifact;
    }

    private final PojoArtifact _targetArtifact;

    @Override
    public ArtifactTypes getType() {
        return ArtifactTypes.ImmutableClass;
    }

    @Override
    public void writeArtifactDeceleration(BufferedWriter bufferedWriter) throws IOException {
        super.writeArtifactDeceleration(bufferedWriter);
    }

    public  @Nonnull String[] getMoreImports() {
        ArtifactFile artifactFile=as(ArtifactFile.class,_targetArtifact.getArtifactParent());
        if (artifactFile!=null) {
            String possibleImport1 = artifactFile.getPackageName() + "." + _targetArtifact.getName();
            String possibleImport2 = artifactFile.getPackageName() + ".*";
            String[] imports = artifactFile.getOptions().getImports();
            if (imports == null || Arrays.stream(imports).noneMatch(imp -> imp.equals(possibleImport1) || imp.equals(possibleImport2)))
                return new String[] {possibleImport1};
        }
        return EmptyArray.get(String.class);
    }

    @Override
    public void writeArtifactContent(BufferedWriter bufferedWriter) throws IOException {
        String privateFieldPrefix=getOptions().getPrivateFieldPrefix();
        privateFieldPrefix=EmptyIfNull(privateFieldPrefix);
        CapitalizationTypes propertyCapitalization=getOptions().getCapitalization();
        String getterPrefix=getOptions().getGetterPrefix();
        String privateFieldName=getOptions().getPrivateFieldName();

        setCurrentIndent(1);
        bufferedWriter.append(getIndent())
                .append("public ").append(getName()).append("(").append(_targetArtifact.getName()).append(" ").append(privateFieldName)
                .append(") {").append(System.lineSeparator());
        setCurrentIndent(2);
        bufferedWriter.append(getIndent())
                .append("this.").append(privateFieldPrefix).append(privateFieldName).append(" = ").append(privateFieldName).append(";").append(System.lineSeparator());
        setCurrentIndent(1);
        bufferedWriter.append(getIndent())
                .append("}").append(System.lineSeparator());
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.append(getIndent())
                .append("private final ").append(_targetArtifact.getName()).append(" ").append(privateFieldPrefix).append(privateFieldName).append(";").append(System.lineSeparator());
        bufferedWriter.write(System.lineSeparator());
        for (SchemaField schemaField : this.getSchema().getFields()) {
            bufferedWriter.append(getIndent()).append("public ")
                    .append(schemaField.getType()).append(" ")
                    .append(capitalizeName(getterPrefix,schemaField.getName(),propertyCapitalization))
                    .append("() { return ").append(privateFieldPrefix).append(privateFieldName)
                    .append(".").append(_targetArtifact.formatGetValue(schemaField.getName()))
                    .append("; }").append(System.lineSeparator());
        }
    }

}
