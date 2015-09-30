package org.ppojo.tests;
import org.junit.Test;

import org.ppojo.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by GARY on 9/24/2015.
 */
public class PojoArtifactTest {
    @Test
    public void testStuff() {
        ArtifactFile parent=ArtifactBaseTest.newParentMock();
        ArtifactParser parser=ArtifactBaseTest.newParserMock();
        PojoArtifact artifact=new PojoArtifact(parent,parser,null,null);
        assertEquals(ArtifactTypes.Pojo,artifact.getType());

    }




}