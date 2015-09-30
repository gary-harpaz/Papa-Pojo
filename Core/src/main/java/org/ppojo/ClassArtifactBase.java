package org.ppojo;

import org.ppojo.utils.Helpers;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by GARY on 9/26/2015.
 */
public abstract class ClassArtifactBase extends ArtifactBase {

    private final String _extendsClass;
    private final String[] _implementsInterfaces;
    public String[] getImplementsInterfaces() {
        return _implementsInterfaces;
    }
    public String getExtendsClass() {
        return _extendsClass;
    }

    protected ClassArtifactBase(@Nonnull IArtifactParent artifactParent, ArtifactParser artifactParser,
                                String extendsClass,String[] implementsInterfaces) {

        super(artifactParent,artifactParser);
        _extendsClass=extendsClass;
        _implementsInterfaces=implementsInterfaces;
    }

    @Override
    public void writeArtifactDeceleration(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("public class ");
        bufferedWriter.write(this.getName());
        if (!Helpers.IsNullOrEmpty(_extendsClass)) {
            bufferedWriter.write(" extends ");
            bufferedWriter.write(_extendsClass);
        }
        if (_implementsInterfaces!=null && _implementsInterfaces.length>0) {
            bufferedWriter.write(" implements ");
            for (int i = 0; i < _implementsInterfaces.length; i++) {
                if (i>0)
                    bufferedWriter.write((", "));
                bufferedWriter.write(_implementsInterfaces[i]);
            }
        }
    }
}
