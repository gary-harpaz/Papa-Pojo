package org.ppojo;

import org.ppojo.data.CopyStyleData;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

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

        String formatGetValueMember(String fieldName);
        String formatSetValueMember(String fieldName);
        String formatFieldValueMember(String fieldName);
    }

    private class PropertyAccessor implements IAccessor {
        public PropertyAccessor() {
            _propertyCapitalization=getOptions().getCapitalization();
            _getterPrefix=getOptions().getGetterPrefix();
            _setterPrefix=getOptions().getSetterPrefix();
            _privateFieldPrefix=getOptions().getPrivateFieldPrefix();
        }
        private final CapitalizationTypes _propertyCapitalization;
        private final String _getterPrefix;
        private final String _setterPrefix;
        private final String _privateFieldPrefix;

        @Override
        public String formatGetValueMember(String fieldName) {
            return capitalizeName(_getterPrefix, fieldName, _propertyCapitalization);
        }

        @Override
        public String formatSetValueMember(String fieldName) {
            return capitalizeName(_setterPrefix, fieldName, _propertyCapitalization);
        }

        @Override
        public String formatFieldValueMember(String fieldName) {
            return _privateFieldPrefix+fieldName;
        }
    }
    private class FieldAccessor implements IAccessor {
        public FieldAccessor() {
            _publicFieldCapitalization =getOptions().getPublicFieldCapitalization();
        }
        private CapitalizationTypes _publicFieldCapitalization;

        @Override
        public String formatGetValueMember(String fieldName) {
            return capitalizeName("", fieldName, _publicFieldCapitalization);
        }

        @Override
        public String formatSetValueMember(String fieldName) {
            return capitalizeName("", fieldName, _publicFieldCapitalization);
        }

        @Override
        public String formatFieldValueMember(String fieldName) {
            return capitalizeName("", fieldName, _publicFieldCapitalization);
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
        writePojoCopyingMembers(bufferedWriter);
    }

    private void writePojoCopyingMembers(BufferedWriter bufferedWriter) throws IOException {
        CopyStyleData[] copyStyles=getOptions().getPojoCopyStyles();
        if (copyStyles.length==0)
            return;
        CopyStyleData mainStyle=copyStyles[0];
        int i=1;
        while (i<copyStyles.length && (mainStyle.style.isFactory())) {
            CopyStyleData copyStyle = copyStyles[i];
            if (!copyStyle.style.isFactory())
                mainStyle=copyStyle;
            else
                if (mainStyle.style!=CopyStyleTypes.copyConstructor && copyStyle.style==CopyStyleTypes.copyConstructor)
                    mainStyle=copyStyle;
            i++;
        }
        Arrays.sort(copyStyles,(x,y)-> {
            CopyStyleData cx=x;
            CopyStyleData cy=y;
            if (cx.style==CopyStyleTypes.copyConstructor || cy.style==CopyStyleTypes.copyConstructor) {
                if (cy.style==CopyStyleTypes.copyConstructor)
                    return 1;
                return -1;
            }
            return 0;
        });

        for (CopyStyleData copyStyle : copyStyles) {
            if (copyStyle.style==CopyStyleTypes.copyConstructor) {
                writeEmptyConstructor(bufferedWriter);
            }
            CopyStyleFormatter formatter=CopyStyleFormatter.getFormatter(copyStyle.style);
            bufferedWriter.append(System.lineSeparator());
            formatter.writeMethodDeclaration(this,copyStyle.methodName,bufferedWriter);
            if (copyStyle.equals(mainStyle))
                formatter.writeMethodContent(this, bufferedWriter);
            else
                formatter.writeMethodCallTo(mainStyle,this,bufferedWriter);
            setCurrentIndent(1);
            bufferedWriter.append(this.getIndent()).append("}").append(System.lineSeparator());
        }
    }

    private void writeEmptyConstructor(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.append(System.lineSeparator());
        this.setCurrentIndent(1);
        bufferedWriter.append(this.getIndent()).append("public ")
                .append(this.getName()).append("() {").append(System.lineSeparator());
        bufferedWriter.append(this.getIndent()).append("}").append(System.lineSeparator());
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
                    .append(_accessor.formatGetValueMember(schemaField.getName()))
                    .append("() { return ").append(privateFieldPrefix).append(schemaField.getName())
                    .append("; }").append(System.lineSeparator());
            bufferedWriter.append(getIndent()).append("public void ")
                      .append(_accessor.formatSetValueMember(schemaField.getName()))
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
                    .append(" ").append(_accessor.formatGetValueMember(schemaField.getName()))
                    .append(";").append(System.lineSeparator());
        }
    }

    public String formatFieldMember(String fieldName) {
        return _accessor.formatFieldValueMember(fieldName);
    }
    public String formatGetValue(String fieldName) {
        if (_encapsulateFields)
            return _accessor.formatGetValueMember(fieldName)+"()";
        return _accessor.formatGetValueMember(fieldName);
    }

}
