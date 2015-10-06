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

import org.ppojo.data.CopyStyleData;
import org.ppojo.utils.EmptyArray;

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
        _forceCopyMember=artifactParser.isForceCopyMember();
        initCopyStyles();
    }
    private final boolean _encapsulateFields;
    private final boolean _forceCopyMember;
    private final IAccessor _accessor;
    private CopyStyleData[] _copyStyleDatas;
    private CopyStyleData _mainCopyStyle;


    public boolean hasCopyMember() {
        return _copyStyleDatas.length>0;
    }

    public CopyStyleTypes getMainCopyStyle() {
        return  _mainCopyStyle.style;
    }
    public String getMainCopyStyleMethod() {
        return  _mainCopyStyle.methodName;
    }



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

    private void initCopyStyles() {
        CopyStyleData[] copyStyles=getOptions().getPojoCopyStyles();
        if (copyStyles.length==0) {
            if (!_forceCopyMember) {
                _copyStyleDatas= EmptyArray.get(CopyStyleData.class);
                return;
            }
            else {
                copyStyles = new CopyStyleData[]{new CopyStyleData()};
                copyStyles[0].style=CopyStyleTypes.copyConstructor;
                copyStyles[0].methodName=CopyStyleTypes.copyConstructor.getDefaultMethodName();
            }
        }

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
        _copyStyleDatas=copyStyles;
        _mainCopyStyle=mainStyle;
    }

    private void writePojoCopyingMembers(BufferedWriter bufferedWriter) throws IOException {

        for (CopyStyleData copyStyle : _copyStyleDatas) {
            if (copyStyle.style==CopyStyleTypes.copyConstructor) {
                writeEmptyConstructor(bufferedWriter);
            }
            CopyStyleFormatter formatter=CopyStyleFormatter.getFormatter(copyStyle.style);
            bufferedWriter.append(System.lineSeparator());
            formatter.writeMethodDeclaration(this,copyStyle.methodName,bufferedWriter);
            if (copyStyle.equals(_mainCopyStyle))
                formatter.writeMethodContent(this, bufferedWriter);
            else
                formatter.writeMethodCallTo(_mainCopyStyle,this,bufferedWriter);
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

    public String formatSetValue(String fieldName,String varName) {
        if (_encapsulateFields)
            return _accessor.formatSetValueMember(fieldName)+"("+varName+")";
        return _accessor.formatFieldValueMember(fieldName)+" = "+varName;
    }

}
