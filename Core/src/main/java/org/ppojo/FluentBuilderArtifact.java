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
import java.io.BufferedWriter;
import java.io.IOException;
import org.ppojo.data.*;
import static org.ppojo.utils.Helpers.EmptyIfNull;

/**
 * Represents a resolved {@link ArtifactTypes#FluentBuilder} artifact.
 * @see FluentBuilderData
 */
public class FluentBuilderArtifact extends ClassArtifactBase {
    protected FluentBuilderArtifact(@Nonnull IArtifactParent artifactParent, ArtifactParser artifactParser, String extendsClass, String[] implementsInterfaces
        , @Nonnull PojoArtifact targetPojoArtifact) {
        super(artifactParent, artifactParser, extendsClass, implementsInterfaces);
        _targetArtifact=targetPojoArtifact;
    }
    private final PojoArtifact _targetArtifact;

    @Override
    public ArtifactTypes getType() {
        return ArtifactTypes.FluentBuilder;
    }

    public  @Nonnull String[] getMoreImports() {
        return getRequiredArtifactImports(_targetArtifact);
    }

    @Override
    public void writeArtifactContent(BufferedWriter bufferedWriter) throws IOException {
        String privateFieldPrefix=getOptions().getPrivateFieldPrefix();
        privateFieldPrefix=EmptyIfNull(privateFieldPrefix);
        CapitalizationTypes propertyCapitalization=getOptions().getCapitalization();
        String setterPrefix=getOptions().getSetterPrefix();
        String privateFieldName=getOptions().getPrivateFieldName();
        String resetBuilderName=getOptions().getFluentResetBuilderName();
        String newBuilderName=getOptions().getFluentNewBuilderName();
        String getDataName=getOptions().getFluentGetDataName();

        writeConstructor(bufferedWriter, privateFieldPrefix, privateFieldName,resetBuilderName);
        writeNewBuilderFactory(bufferedWriter,newBuilderName);
        writeResetBuilder(bufferedWriter, privateFieldPrefix, privateFieldName,resetBuilderName);
        writeGetDataMember(bufferedWriter,privateFieldPrefix,privateFieldName,getDataName);
        writeSetters(bufferedWriter, privateFieldPrefix, propertyCapitalization, setterPrefix, privateFieldName);

    }

    private void writeGetDataMember(BufferedWriter bufferedWriter, String privateFieldPrefix, String privateFieldName, String getDataName) throws IOException {
        bufferedWriter.append(System.lineSeparator());
        setCurrentIndent(1);
        bufferedWriter.append(getIndent()).append("public ").append(_targetArtifact.getName()).append(" ").append(getDataName).append("() {").append(System.lineSeparator());
        setCurrentIndent(2);
        bufferedWriter.append(getIndent()).append("return ").append(privateFieldPrefix).append(privateFieldName).append(";").append(System.lineSeparator());
        setCurrentIndent(1);
        bufferedWriter.append(getIndent()).append("}").append(System.lineSeparator());
    }

    private void writeNewBuilderFactory(BufferedWriter bufferedWriter, String newBuilderName) throws IOException {
        bufferedWriter.append(System.lineSeparator());
        setCurrentIndent(1);
        bufferedWriter.append(getIndent()).append("public static ").append(getName()).append(" ").append(newBuilderName)
                .append("() {").append(System.lineSeparator());
        setCurrentIndent(2);
        bufferedWriter.append(getIndent()).append("return new ").append(getName()).append("();").append(System.lineSeparator());
        setCurrentIndent(1);
        bufferedWriter.append(getIndent()).append("}").append(System.lineSeparator());
    }

    private void writeResetBuilder(BufferedWriter bufferedWriter, String privateFieldPrefix, String privateFieldName, String resetBuilderName) throws IOException {
        bufferedWriter.append(System.lineSeparator());
        setCurrentIndent(1);
        bufferedWriter.append(getIndent()).append("public ").append(_targetArtifact.getName()).append(" ").append(resetBuilderName).append("() {").append(System.lineSeparator());
        setCurrentIndent(2);
        bufferedWriter.append(getIndent()).append(_targetArtifact.getName()).append(" result = ").append(privateFieldPrefix)
                .append(privateFieldName).append(";").append(System.lineSeparator());
        bufferedWriter.append(getIndent()).append(privateFieldPrefix).append(privateFieldName).append(" = new ").
                append(_targetArtifact.getName()).append("();").append(System.lineSeparator());
        bufferedWriter.append(getIndent()).append("return result;").append(System.lineSeparator());
        setCurrentIndent(1);
        bufferedWriter.append(getIndent()).append("}").append(System.lineSeparator());
    }

    private void writeSetters(BufferedWriter bufferedWriter, String privateFieldPrefix, CapitalizationTypes propertyCapitalization, String setterPrefix, String privateFieldName) throws IOException {
        for (SchemaField schemaField : this.getSchema().getFields()) {
            bufferedWriter.append(getIndent()).append("public ")
                    .append(getName()).append(" ")
                    .append(capitalizeName(setterPrefix,schemaField.getName(),propertyCapitalization))
                    .append("(").append(schemaField.getType()).append(" ").append(schemaField.getName()).append(") {").append(System.lineSeparator());
            setCurrentIndent(2);
            bufferedWriter.append(getIndent()).append(privateFieldPrefix).append(privateFieldName).append(".")
                    .append(_targetArtifact.formatSetValue(schemaField.getName(),schemaField.getName())).append(";").append(System.lineSeparator());
            bufferedWriter.append(getIndent()).append("return this;").append(System.lineSeparator());
            setCurrentIndent(1);
            bufferedWriter.append(getIndent()).append("}").append(System.lineSeparator());
        }
    }

    private void writeConstructor(BufferedWriter bufferedWriter, String privateFieldPrefix, String privateFieldName,String resetDataName) throws IOException {
        setCurrentIndent(1);
        bufferedWriter.append(getIndent())
                .append("public ").append(getName()).append("() {").append(System.lineSeparator());
        setCurrentIndent(2);
        bufferedWriter.append(getIndent()).append(resetDataName).append("();").append(System.lineSeparator());
        setCurrentIndent(1);
        bufferedWriter.append(getIndent())
                .append("}").append(System.lineSeparator());
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.append(getIndent())
                .append("private ").append(_targetArtifact.getName()).append(" ").append(privateFieldPrefix).append(privateFieldName).append(";").append(System.lineSeparator());
        bufferedWriter.write(System.lineSeparator());
    }
}
