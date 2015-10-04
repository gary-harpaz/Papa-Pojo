package org.ppojo;

import org.ppojo.data.CopyStyleData;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GARY on 10/4/2015.
 */
public abstract class CopyStyleFormatter {
    abstract void writeMethodDeclaration(PojoArtifact pojoArtifact,String methodName, BufferedWriter bufferedWriter) throws IOException;
    abstract void writeMethodContent(PojoArtifact pojoArtifact, BufferedWriter bufferedWriter) throws IOException;
    public abstract void writeMethodCallTo(CopyStyleData mainStyle, PojoArtifact pojoArtifact, BufferedWriter bufferedWriter)  throws IOException;



    private static final Map<CopyStyleTypes,CopyStyleFormatter> _formatters;
    static {
        _formatters=new HashMap<>();
        _formatters.put(CopyStyleTypes.copyConstructor,new CopyConstructorFormatter());
        _formatters.put(CopyStyleTypes.staticFactory,new StaticFactoryFormatter());
        _formatters.put(CopyStyleTypes.memberFactory,new MemberFactoryFormatter());
        _formatters.put(CopyStyleTypes.staticMethod,new StaticMethodFormatter());
        _formatters.put(CopyStyleTypes.memberCopyFrom,new MemberCopyFromFormatter());
        _formatters.put(CopyStyleTypes.memberCopyTo,new MemberCopyToFormatter());

    }


    public static CopyStyleFormatter getFormatter(CopyStyleTypes copyStyleTypes) {
        CopyStyleFormatter formatter=_formatters.get(copyStyleTypes);
        if (formatter==null)
            throw new RuntimeException("Unrecognized formatter "+copyStyleTypes);
        return formatter;
    }

    private static void FormatFieldAllAssignments(String sourceVarName,String targetVarName,PojoArtifact p, BufferedWriter bufferedWriter) throws IOException {
        FormatFieldAllAssignments(sourceVarName,targetVarName,p,bufferedWriter,true);
    }

    private static void FormatFieldAllAssignments(String sourceVarName,String targetVarName,PojoArtifact p, BufferedWriter bufferedWriter,boolean returnTarget) throws IOException {
        for (SchemaField schemaField : p.getSchema().getFields()) {
            bufferedWriter.append(p.getIndent()).append(targetVarName)
                    .append(".").append(p.formatFieldMember(schemaField.getName()))
                    .append(" = ").append(sourceVarName).append(".")
                    .append(p.formatGetValue(schemaField.getName())).append(";").append(System.lineSeparator());
        }
        if (returnTarget)
            FormatReturnTarget(targetVarName,p,bufferedWriter);
    }
    private static void FormatReturnTarget(String targetVarName,PojoArtifact p, BufferedWriter bufferedWriter)  throws IOException {
        bufferedWriter.append(p.getIndent()).append("return ").append(targetVarName).append(";").append(System.lineSeparator());
    }

    private static void FormatNewLocalTargerVar(String targetVarName,PojoArtifact p, BufferedWriter bufferedWriter)  throws IOException {
        bufferedWriter.append(p.getIndent()).append(p.getName()).append(" ").append(targetVarName).append(" = new ")
                .append(p.getName()).append("();").append(System.lineSeparator());
    }





    private static class CopyConstructorFormatter extends CopyStyleFormatter {

        @Override
        public void writeMethodDeclaration(PojoArtifact p,String methodName, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(1);
            bufferedWriter.append(p.getIndent()).append("public ").append(p.getName())
                .append("(").append(p.getName()).append(" source) {").append(System.lineSeparator());
        }

        @Override
        void writeMethodContent(PojoArtifact p, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(2);
            FormatFieldAllAssignments("source","this",p,bufferedWriter,false);
        }

        @Override
        public void writeMethodCallTo(CopyStyleData mainStyle, PojoArtifact p, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(2);
            switch (mainStyle.style) {
                case copyConstructor:
                case memberFactory:
                case staticFactory:
                    throw new RuntimeException("Unsupported call to "+mainStyle.style);
                case memberCopyTo:
                    bufferedWriter.append(p.getIndent()).append("source.").append(mainStyle.methodName).append("(this);").append(System.lineSeparator());
                    break;
                case staticMethod:
                    bufferedWriter.append(p.getIndent()).append(mainStyle.methodName).append("(source,this);").append(System.lineSeparator());
                    break;
                case memberCopyFrom:
                    bufferedWriter.append(p.getIndent()).append(mainStyle.methodName).append("(source);").append(System.lineSeparator());
                    break;
            }

        }
    }

    private static class StaticFactoryFormatter extends CopyStyleFormatter {


        @Override
        void writeMethodDeclaration(PojoArtifact p, String methodName, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(1);
            bufferedWriter.append(p.getIndent()).append("public static ").append(p.getName()).append(" ")
                    .append(methodName)
                    .append("(").append(p.getName()).append(" source) {").append(System.lineSeparator());
        }

        @Override
        void writeMethodContent(PojoArtifact p, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(2);
            FormatNewLocalTargerVar("target",p,bufferedWriter);
            FormatFieldAllAssignments("source","target",p,bufferedWriter);

        }

        @Override
        public void writeMethodCallTo(CopyStyleData mainStyle, PojoArtifact p, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(2);
            switch (mainStyle.style) {
                case staticFactory:
                    throw new RuntimeException("Unsupported call to "+mainStyle.style);
                case memberCopyTo:
                    FormatNewLocalTargerVar("target",p,bufferedWriter);
                    bufferedWriter.append(p.getIndent()).append("source.").append(mainStyle.methodName).append("(target);").append(System.lineSeparator());
                    FormatReturnTarget("target",p,bufferedWriter);
                    break;
                case memberFactory:
                    bufferedWriter.append(p.getIndent()).append("return source.").append(mainStyle.methodName).append("();").append(System.lineSeparator());
                    break;
                case copyConstructor:
                    bufferedWriter.append(p.getIndent()).append("return new ").append(p.getName()).append("(source);").append(System.lineSeparator());
                    break;
                case staticMethod:
                    FormatNewLocalTargerVar("target",p,bufferedWriter);
                    bufferedWriter.append(p.getIndent()).append(mainStyle.methodName).append("(source,target);").append(System.lineSeparator());
                    FormatReturnTarget("target",p,bufferedWriter);
                    break;
                case memberCopyFrom:
                    FormatNewLocalTargerVar("target",p,bufferedWriter);
                    bufferedWriter.append(p.getIndent()).append("target.").append(mainStyle.methodName).append("(source);").append(System.lineSeparator());
                    FormatReturnTarget("target",p,bufferedWriter);
                    break;

            }
        }



    }

    private static class MemberFactoryFormatter extends CopyStyleFormatter {


        @Override
        void writeMethodDeclaration(PojoArtifact p, String methodName, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(1);
            bufferedWriter.append(p.getIndent()).append("public ").append(p.getName()).append(" ")
                    .append(methodName)
                    .append("() {").append(System.lineSeparator());
        }

        @Override
        void writeMethodContent(PojoArtifact p, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(2);
            FormatNewLocalTargerVar("target",p,bufferedWriter);
            FormatFieldAllAssignments("this","target",p,bufferedWriter);

        }

        @Override
        public void writeMethodCallTo(CopyStyleData mainStyle, PojoArtifact p, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(2);
            switch (mainStyle.style) {
                case memberFactory:
                    throw new RuntimeException("Unsupported call to "+mainStyle.style);
                case memberCopyTo:
                    FormatNewLocalTargerVar("target",p,bufferedWriter);
                    bufferedWriter.append(p.getIndent()).append("this.").append(mainStyle.methodName).append("(target);").append(System.lineSeparator());
                    FormatReturnTarget("target",p,bufferedWriter);
                    break;
                case staticFactory:
                    bufferedWriter.append(p.getIndent()).append("return ").append(mainStyle.methodName).append("(this);").append(System.lineSeparator());
                    break;
                case copyConstructor:
                    bufferedWriter.append(p.getIndent()).append("return new ").append(p.getName()).append("(this);").append(System.lineSeparator());
                    break;
                case staticMethod:
                    FormatNewLocalTargerVar("target",p,bufferedWriter);
                    bufferedWriter.append(p.getIndent()).append(mainStyle.methodName).append("(this,target);").append(System.lineSeparator());
                    FormatReturnTarget("target",p,bufferedWriter);
                    break;
                case memberCopyFrom:
                    FormatNewLocalTargerVar("target",p,bufferedWriter);
                    bufferedWriter.append(p.getIndent()).append("target.").append(mainStyle.methodName).append("(this);").append(System.lineSeparator());
                    FormatReturnTarget("target",p,bufferedWriter);
                    break;
            }
        }
    }

    private static class StaticMethodFormatter extends CopyStyleFormatter {

        @Override
        void writeMethodDeclaration(PojoArtifact p, String methodName, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(1);
            bufferedWriter.append(p.getIndent()).append("public static ").append(p.getName()).append(" ")
                    .append(methodName)
                    .append("(").append(p.getName()).append(" source,").append(p.getName()).append(" target) {").append(System.lineSeparator());
        }

        @Override
        void writeMethodContent(PojoArtifact p, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(2);
            FormatFieldAllAssignments("source","target",p,bufferedWriter);
        }

        @Override
        public void writeMethodCallTo(CopyStyleData mainStyle, PojoArtifact p, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(2);
            switch (mainStyle.style) {
                case memberFactory:
                case staticMethod:
                case staticFactory:
                case copyConstructor:
                    throw new RuntimeException("Unsupported call to "+mainStyle.style);
                case memberCopyTo:
                    bufferedWriter.append(p.getIndent()).append("source.").append(mainStyle.methodName).append("(target);").append(System.lineSeparator());
                    FormatReturnTarget("target",p,bufferedWriter);
                    break;
                case memberCopyFrom:
                    bufferedWriter.append(p.getIndent()).append("target.").append(mainStyle.methodName).append("(source);").append(System.lineSeparator());
                    FormatReturnTarget("target",p,bufferedWriter);
                    break;

            }
        }
    }

    private static class MemberCopyFromFormatter extends CopyStyleFormatter {

        @Override
        void writeMethodDeclaration(PojoArtifact p, String methodName, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(1);
            bufferedWriter.append(p.getIndent()).append("public ").append(p.getName()).append(" ")
                    .append(methodName)
                    .append("(").append(p.getName()).append(" source) {").append(System.lineSeparator());
        }

        @Override
        void writeMethodContent(PojoArtifact p, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(2);
            FormatFieldAllAssignments("source","this",p,bufferedWriter);
        }

        @Override
        public void writeMethodCallTo(CopyStyleData mainStyle, PojoArtifact p, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(2);
            switch (mainStyle.style) {
                case memberFactory:
                case memberCopyFrom:
                case staticFactory:
                case copyConstructor:
                    throw new RuntimeException("Unsupported call to "+mainStyle.style);
                case memberCopyTo:
                    bufferedWriter.append(p.getIndent()).append("source.").append(mainStyle.methodName).append("(this);").append(System.lineSeparator());
                    FormatReturnTarget("this",p,bufferedWriter);
                    break;
                case staticMethod:
                    bufferedWriter.append(p.getIndent()).append(mainStyle.methodName).append("(source,this);").append(System.lineSeparator());
                    FormatReturnTarget("this",p,bufferedWriter);
                    break;
            }
        }
    }


    private static class MemberCopyToFormatter extends CopyStyleFormatter {

        @Override
        void writeMethodDeclaration(PojoArtifact p, String methodName, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(1);
            bufferedWriter.append(p.getIndent()).append("public ").append(p.getName()).append(" ")
                    .append(methodName)
                    .append("(").append(p.getName()).append(" target) {").append(System.lineSeparator());
        }

        @Override
        void writeMethodContent(PojoArtifact p, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(2);
            FormatFieldAllAssignments("this","target",p,bufferedWriter);
        }

        @Override
        public void writeMethodCallTo(CopyStyleData mainStyle, PojoArtifact p, BufferedWriter bufferedWriter) throws IOException {
            p.setCurrentIndent(2);
            switch (mainStyle.style) {
                case memberFactory:
                case staticFactory:
                case copyConstructor:
                case memberCopyTo:
                    throw new RuntimeException("Unsupported call to "+mainStyle.style);
                case memberCopyFrom:
                    bufferedWriter.append(p.getIndent()).append("target.").append(mainStyle.methodName).append("(this);").append(System.lineSeparator());
                    FormatReturnTarget("target",p,bufferedWriter);
                    break;
                case staticMethod:
                    bufferedWriter.append(p.getIndent()).append(mainStyle.methodName).append("(this,target);").append(System.lineSeparator());
                    FormatReturnTarget("target",p,bufferedWriter);
                    break;
            }
        }
    }
}
