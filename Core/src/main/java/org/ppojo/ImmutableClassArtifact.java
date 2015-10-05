package org.ppojo;

import org.ppojo.utils.Helpers;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;

import static org.ppojo.utils.Helpers.EmptyIfNull;

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

    public  @Nonnull String[] getMoreImports() {
        return getRequiredArtifactImports(_targetArtifact);
    }

    @Override
    public void writeArtifactContent(BufferedWriter bufferedWriter) throws IOException {
        String privateFieldPrefix=getOptions().getPrivateFieldPrefix();
        privateFieldPrefix=EmptyIfNull(privateFieldPrefix);
        CapitalizationTypes propertyCapitalization=getOptions().getCapitalization();
        String getterPrefix=getOptions().getGetterPrefix();
        String privateFieldName=getOptions().getPrivateFieldName();

        writeConstructor(bufferedWriter, privateFieldPrefix, privateFieldName);
        writeGetters(bufferedWriter, privateFieldPrefix, propertyCapitalization, getterPrefix, privateFieldName);
        writeCopyData(bufferedWriter,privateFieldPrefix,privateFieldName);
    }

    private void writeCopyData(BufferedWriter bufferedWriter,String privateFieldPrefix, String privateFieldName) throws IOException {
        bufferedWriter.write(System.lineSeparator());
        String copyDataMemberName=getOptions().getImmutableCopyDataMember();
        if (!Helpers.IsNullOrEmpty(copyDataMemberName)) {
            assetTargetHasCopyMember();
            CopyStyleTypes styleType=_targetArtifact.getMainCopyStyle();
            String pojoCopyMethodName=_targetArtifact.getMainCopyStyleMethod();
            setCurrentIndent(1);
            bufferedWriter.append(getIndent()).append("public ").append(_targetArtifact.getName()).append(" ")
                    .append(copyDataMemberName).append("() {").append(System.lineSeparator());
            setCurrentIndent(2);
            writeInvokeCopy(bufferedWriter, privateFieldPrefix+privateFieldName,"return", styleType, pojoCopyMethodName);
            setCurrentIndent(1);
            bufferedWriter.append(getIndent()).append("}").append(System.lineSeparator());
        }
    }

    private void assetTargetHasCopyMember() {
        if (!_targetArtifact.hasCopyMember())
            throw new RuntimeException("Can not create ImmutableClassArtifact, target Pojo must have at least one copy member");
    }

    private void writeInvokeCopy(BufferedWriter bufferedWriter, String copyArgument,String returnValue,
                                 CopyStyleTypes styleType, String pojoCopyMethodName) throws IOException {
        switch (styleType) {

            case staticMethod:
                bufferedWriter.append(getIndent()).append(returnValue).append(" ").append(_targetArtifact.getName()).append(".").append(pojoCopyMethodName).append("(")
                        .append(copyArgument).append(",new ").append(_targetArtifact.getName()).append("());").append(System.lineSeparator());
                break;
            case staticFactory:
                bufferedWriter.append(getIndent()).append(returnValue).append(" ").append(_targetArtifact.getName()).append(".").append(pojoCopyMethodName).append("(")
                        .append(copyArgument).append(");").append(System.lineSeparator());
                break;
            case copyConstructor:
                bufferedWriter.append(getIndent()).append(returnValue).append(" new ").append(_targetArtifact.getName()).append("(")
                        .append(copyArgument).append(");").append(System.lineSeparator());
                break;
            case memberFactory:
                bufferedWriter.append(getIndent()).append(returnValue).append(" ").append(copyArgument).append(".").append(pojoCopyMethodName).append("();")
                        .append(System.lineSeparator());
                break;
            case memberCopyTo:
                bufferedWriter.append(getIndent()).append(returnValue).append(" ").append(copyArgument)
                        .append(".").append(pojoCopyMethodName).append("(new ").append(_targetArtifact.getName()).append("());").append(System.lineSeparator());
                break;
            case memberCopyFrom:
                bufferedWriter.append(getIndent()).append(_targetArtifact.getName()).
                        append(" copy = new ").append(_targetArtifact.getName()).append("();").append(System.lineSeparator());
                bufferedWriter.append(getIndent()).append(returnValue).append(" copy.").append(pojoCopyMethodName).append("(")
                        .append(copyArgument).append(");").append(System.lineSeparator());
                break;
            default:
                throw new RuntimeException("Unsupported pojo copy style when writing immutable class");
        }
    }

    private void writeGetters(BufferedWriter bufferedWriter, String privateFieldPrefix, CapitalizationTypes propertyCapitalization, String getterPrefix, String privateFieldName) throws IOException {
        for (SchemaField schemaField : this.getSchema().getFields()) {
            bufferedWriter.append(getIndent()).append("public ")
                    .append(schemaField.getType()).append(" ")
                    .append(capitalizeName(getterPrefix,schemaField.getName(),propertyCapitalization))
                    .append("() { return ").append(privateFieldPrefix).append(privateFieldName)
                    .append(".").append(_targetArtifact.formatGetValue(schemaField.getName()))
                    .append("; }").append(System.lineSeparator());
        }
    }

    private void writeConstructor(BufferedWriter bufferedWriter, String privateFieldPrefix, String privateFieldName) throws IOException {
        String localVarName=privateFieldName;
        setCurrentIndent(1);
        bufferedWriter.append(getIndent())
                .append("public ").append(getName()).append("(").append(_targetArtifact.getName()).append(" ").append(localVarName)
                .append(") {").append(System.lineSeparator());
        setCurrentIndent(2);
        if (getOptions().getIsDefensiveCopy()) {
            assetTargetHasCopyMember();
            CopyStyleTypes styleType=_targetArtifact.getMainCopyStyle();
            String pojoCopyMethodName=_targetArtifact.getMainCopyStyleMethod();
            writeInvokeCopy(bufferedWriter,localVarName,localVarName+" = ",styleType,pojoCopyMethodName);
        }
        bufferedWriter.append(getIndent())
                .append("this.").append(privateFieldPrefix).append(privateFieldName).append(" = ").append(localVarName).append(";").append(System.lineSeparator());
        setCurrentIndent(1);
        bufferedWriter.append(getIndent())
                .append("}").append(System.lineSeparator());
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.append(getIndent())
                .append("private final ").append(_targetArtifact.getName()).append(" ").append(privateFieldPrefix).append(privateFieldName).append(";").append(System.lineSeparator());
        bufferedWriter.write(System.lineSeparator());
    }

}
