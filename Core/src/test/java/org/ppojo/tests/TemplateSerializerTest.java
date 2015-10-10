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

package org.ppojo.tests;

import org.junit.Before;
import org.junit.Test;
import org.ppojo.*;
import org.ppojo.data.*;
import org.ppojo.trace.LoggingService;
import org.ppojo.utils.ArrayListBuilder;

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

    private static ArtifactGraph newSimplePojoSchema() {

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

        ArtifactGraph artifactGraph =new ArtifactGraph(ArrayListBuilder.newArrayList(file).create(),new LoggingService());

        PojoArtifact pojoArtifact=new PojoArtifact(file,artifactParser,null,null);
        file.addChildArtifact(pojoArtifact);
        return artifactGraph;


    }



}







