package org.ppojo.tests;

import org.junit.Before;
import org.junit.Test;
import org.ppojo.*;
import org.ppojo.data.*;
import org.ppojo.utils.ArrayListBuilder;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.ppojo.utils.Helpers.readResourceTextFile;

/**
 * Created by GARY on 9/23/2015.
 */
public class TemplateSerializerTest {
    private Serializer _serializer;
    private static final String TEMPLATE_FILENAME = "stam";


    @Before
    public void InitSerializer() {
        _serializer = new Serializer();
    }

    @Test
    public void SerializeSimplePojoSchema() {
        TemplateFileData template = newSimplePojoSchema().toSimpleTemplateFileData().get(0);

        String result = _serializer.Serialize(template);
        _serializer.setIsPretty(true);
        String prettyResult = _serializer.Serialize(template);

    }

    @Test
    public void DeserializeSimplePojoSchema() {
        String fileName=SchemaTestResources.SimplePojoSchema;
        String json = readResourceTextFile(fileName);
        TemplateFileData template = _serializer.deserialize(json,fileName);


    }

    private static SchemaGraph newSimplePojoSchema() {

        Schema schema = SchemaBuilder.newBuilder()
                .addField(new SchemaField("FirstName", "String"))
                .addField(new SchemaField("LastName", "String"))
                .addField(new SchemaField("Age", "int"))
                .create();
        ArtifactFile file = new ArtifactFile("c:\\temp\\src\\person.java","c:\\temp\\src\\",null);
        ArtifactParser artifactParser= mock(ArtifactParser.class);
        when(artifactParser.getSchema()).thenReturn(schema);
        ClassArtifactData classArtifactData=new ClassArtifactData();
        when(artifactParser.getRawData()).thenReturn(classArtifactData);
        when(artifactParser.isValid()).thenReturn(true);
        Map<String,Object> options=ArtifactConstructionTest.newDefaultProperties();
        options.put(ArtifactOptions.Fields.encapsulateFields.toString(),true);

        ArtifactOptions artifactOptions=new ArtifactOptions("test options newSimplePojoSchema",options,null);
        when(artifactParser.getOptions()).thenReturn(artifactOptions);

        SchemaGraph schemaGraph=new SchemaGraph(ArrayListBuilder.newArrayList(file).create());

        PojoArtifactBuilder.newBuilder(file,artifactParser).create();
        return schemaGraph;


    }



}







