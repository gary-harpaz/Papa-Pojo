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
import org.junit.Test;
import org.ppojo.*;
import org.ppojo.data.ClassArtifactData;
import org.ppojo.utils.MapChainValue;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by GARY on 9/24/2015.
 */
public class ArtifactBaseTest {
    private static final String TEST_FILENAME="stam";
    private static final String OPTION="opt";
    private static final String OPTION_VALUE="value";
    private static final String PARENT_OPTION="parent_opt";
    private static final String PARENT_OPTION_VALUE="parent_value";
    private static final String TEST_PATH= Paths.get("c:","temp",TEST_FILENAME+".pppj").toString();
    public static ArtifactFile newParentMock() {
        ArtifactFile parent=mock(ArtifactFile.class);
        when(parent.getArtifactFileName()).thenReturn(TEST_PATH);
        Map<String,Object> parentOptions=ArtifactConstructionTest.newDefaultProperties();
        parentOptions.put(PARENT_OPTION, PARENT_OPTION_VALUE);
        ArtifactOptions parentArtifactOptions=new ArtifactOptions("testOptions",parentOptions,null);
        when(parent.getOptions()).thenReturn(parentArtifactOptions);
        return parent;
    }

   @Test(expected = NullPointerException.class)
    public void constructorNullParent() {
        PojoArtifact artifact=new PojoArtifact(null,null,null,null);

    }
//    @Test
    public void constructionTest() {
        ArtifactFile parent=newParentMock();
        ArtifactParser parser=newParserMock();
        PojoArtifact artifact=new PojoArtifact(parent,parser,null,null);

        fileNameIsDefaultArtifactNameTest(parent, artifact);
        localOptionValueTest(artifact);
        parentOptionValueTest(parent, artifact);

    }

    public static ArtifactParser newParserMock() {

        ArtifactFile parent=mock(ArtifactFile.class);
        when(parent.getArtifactFileName()).thenReturn(TEST_PATH);
        Map<String,Object> parentOptions=ArtifactConstructionTest.newDefaultProperties();
        parentOptions.put(PARENT_OPTION, PARENT_OPTION_VALUE);
        parentOptions.put(ArtifactOptions.Fields.encapsulateFields.toString(),true);
        ArtifactOptions parentArtifactOptions=new ArtifactOptions("testOptions",parentOptions,null);
        when(parent.getOptions()).thenReturn(parentArtifactOptions);

        ArtifactParser parser=mock(ArtifactParser.class);
        Schema schema=mock(Schema.class);
        when(parser.getSchema()).thenReturn(schema);
        ClassArtifactData data=new ClassArtifactData();
        when(parser.isValid()).thenReturn(true);

        when(parser.getRawData()).thenReturn(data);
        when(parser.getOptions()).thenReturn(parentArtifactOptions);
        return parser;

    }

    private void parentOptionValueTest(ArtifactFile parent, PojoArtifact artifact) {
        MapChainValue parentValue=artifact.getOptions().get(PARENT_OPTION);
        assertEquals(PARENT_OPTION_VALUE, parentValue.getValue());
        assertEquals(parent.getOptions().getName(), parentValue.getValueSourceName());
        assertFalse(parentValue.isLocalValue());
    }

    private void localOptionValueTest(PojoArtifact artifact) {
        MapChainValue localValue=artifact.getOptions().get(OPTION);
        assertEquals(OPTION_VALUE,localValue.getValue());
        assertEquals(artifact.getOptions().getName(),localValue.getValueSourceName());
        assertTrue(localValue.isLocalValue());
    }

    private void fileNameIsDefaultArtifactNameTest(ArtifactFile parent, PojoArtifact artifact) {
        verify(parent,atLeast(1)).getArtifactFileName();
        assertEquals(TEST_FILENAME, artifact.getName());
    }
}
