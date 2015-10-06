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

import org.junit.Assert;
import org.junit.Test;
import org.ppojo.ArtifactBase;
import org.ppojo.CapitalizationTypes;

/**
 * Created by GARY on 9/30/2015.
 */
public class CapitalizationTests {
    private static final  String INITIAL_STRINGBUILDER_TEXT="Existing text to add to ";
    private static StringBuilder newStringBuilderWithText() {
        return new StringBuilder(INITIAL_STRINGBUILDER_TEXT);
    }

    @Test
    public void AllCapsTests() {
        String prefix;
        String fieldName;
        CapitalizationTypes capitalizationTypes=CapitalizationTypes.ALL_CAPS;
        prefix="prefPref";
        fieldName="name";
        Assert.assertEquals("PREF_PREF_NAME", ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes));
        StringBuilder stringBuilder=newStringBuilderWithText();
        ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes, stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"PREF_PREF_NAME", stringBuilder.toString());

        prefix="startWord";
        fieldName="employeeNumber";
        Assert.assertEquals("START_WORD_EMPLOYEE_NUMBER", ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes));
        stringBuilder=newStringBuilderWithText();
        ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes, stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"START_WORD_EMPLOYEE_NUMBER", stringBuilder.toString());
        prefix="";
        Assert.assertEquals("EMPLOYEE_NUMBER", ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes));
        stringBuilder=newStringBuilderWithText();
        ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes, stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"EMPLOYEE_NUMBER",stringBuilder.toString());

        fieldName="Age";
        Assert.assertEquals("AGE", ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes));
    }

    @Test
    public void camelCaseTest() {
        String prefix;
        String fieldName;
        CapitalizationTypes capitalizationTypes=CapitalizationTypes.camelCase;
        prefix="prefPref";
        fieldName="name";
        Assert.assertEquals("prefPrefName", ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes));
        StringBuilder stringBuilder=newStringBuilderWithText();
        ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes, stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"prefPrefName", stringBuilder.toString());

        prefix="startWord";
        fieldName="employeeNumber";
        Assert.assertEquals("startWordEmployeeNumber", ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes));
        stringBuilder=newStringBuilderWithText();
        ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes, stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"startWordEmployeeNumber", stringBuilder.toString());
        prefix="";
        Assert.assertEquals("employeeNumber", ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes));
        stringBuilder=newStringBuilderWithText();
        ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes, stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"employeeNumber",stringBuilder.toString());
    }


    @Test
    public void PascalCaseTest() {
        String prefix;
        String fieldName;
        CapitalizationTypes capitalizationTypes=CapitalizationTypes.PascalCase;
        prefix="prefPref";
        fieldName="name";
        Assert.assertEquals("PrefPrefName", ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes));
        StringBuilder stringBuilder=newStringBuilderWithText();
        ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes, stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"PrefPrefName", stringBuilder.toString());

        prefix="startWord";
        fieldName="employeeNumber";
        Assert.assertEquals("StartWordEmployeeNumber", ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes));
        stringBuilder=newStringBuilderWithText();
        ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes, stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"StartWordEmployeeNumber", stringBuilder.toString());
        prefix="";
        Assert.assertEquals("EmployeeNumber", ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes));
        stringBuilder=newStringBuilderWithText();
        ArtifactBase.capitalizeName(prefix, fieldName, capitalizationTypes, stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"EmployeeNumber",stringBuilder.toString());
    }
}
