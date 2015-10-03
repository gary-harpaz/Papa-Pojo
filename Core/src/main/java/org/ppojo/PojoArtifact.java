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
        _encapsulateFields=getOptions().getEncapsulateFields();
        if (_encapsulateFields)
            _accessor=new PropertyAccessor();
        else
            _accessor=new FieldAccessor();
    }
    private boolean _encapsulateFields;

    private IAccessor _accessor;


    private interface IAccessor {

        String formatFieldReader(String fieldName);

        String formatFieldWriter(String fieldName);
    }

    private class PropertyAccessor implements IAccessor {
        public PropertyAccessor() {
            _propertyCapitalization=getOptions().getCapitalization();
            _getterPrefix=getOptions().getGetterPrefix();
            _setterPrefix=getOptions().getSetterPrefix();
        }
        private CapitalizationTypes _propertyCapitalization;
        private String _getterPrefix;
        private String _setterPrefix;

        @Override
        public String formatFieldReader(String fieldName) {
            return capitalizeName(_getterPrefix, fieldName, _propertyCapitalization);
        }

        @Override
        public String formatFieldWriter(String fieldName) {
            return capitalizeName(_setterPrefix, fieldName, _propertyCapitalization);
        }
    }
    private class FieldAccessor implements IAccessor {
        public FieldAccessor() {
            _publicFieldCapitalizatio=getOptions().getPublicFieldCapitalization();
        }
        private CapitalizationTypes _publicFieldCapitalizatio;

        @Override
        public String formatFieldReader(String fieldName) {
            return capitalizeName("", fieldName, _publicFieldCapitalizatio);
        }

        @Override
        public String formatFieldWriter(String fieldName) {
            return capitalizeName("", fieldName, _publicFieldCapitalizatio);
        }
    }




    @Override
    public ArtifactTypes getType() {
        return ArtifactTypes.Pojo;
    }



    @Override
    public void writeArtifactContent(BufferedWriter bufferedWriter) throws IOException {
        if (!_encapsulateFields)
            writeUnencapsulatedField(bufferedWriter);
        else
            writeEncapsulatedFields(bufferedWriter);
    }

    private void writeEncapsulatedFields(BufferedWriter bufferedWriter) throws IOException {
        String privateFieldPrefix=getOptions().getPrivateFieldPrefix();
        setCurrentIndent(1);

        for (SchemaField schemaField : this.getSchema().getFields()) {
            bufferedWriter.append(getIndent()).append("private ")
                    .append(schemaField.getType())
                    .append(" ").append(privateFieldPrefix).append(schemaField.getName())
                    .append(";").append(System.lineSeparator());
        }
        bufferedWriter.append(System.lineSeparator());


        for (SchemaField schemaField : this.getSchema().getFields()) {
            bufferedWriter.append(getIndent()).append("public ")
                    .append(schemaField.getType()).append(" ")
                    .append(_accessor.formatFieldReader(schemaField.getName()))
                    .append("() { return ").append(privateFieldPrefix).append(schemaField.getName())
                    .append("; }").append(System.lineSeparator());
            bufferedWriter.append(getIndent()).append("public void ")
                      .append(_accessor.formatFieldWriter(schemaField.getName()))
                     .append("(").append(schemaField.getType()).append(" ").append(schemaField.getName())
                    .append(") { this.").append(privateFieldPrefix).append(schemaField.getName())
                    .append(" = ").append(schemaField.getName()).append("; }").append(System.lineSeparator());
        }
    }

    private void writeUnencapsulatedField(BufferedWriter bufferedWriter) throws IOException {
        setCurrentIndent(1);
        for (SchemaField schemaField : this.getSchema().getFields()) {
            bufferedWriter.append(getIndent()).append("public ")
                    .append(schemaField.getType())
                    .append(" ").append(_accessor.formatFieldReader(schemaField.getName()))
                    .append(";").append(System.lineSeparator());
        }
    }

    public String formatFieldReader(String name) {
        return _accessor.formatFieldReader(name)+(_encapsulateFields?"()":"");
    }





}
