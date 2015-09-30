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
        Assert.assertEquals("PREF_PREF_NAME", ArtifactBase.formatProperty(prefix, fieldName, capitalizationTypes));
        StringBuilder stringBuilder=newStringBuilderWithText();
        ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes,stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"PREF_PREF_NAME", stringBuilder.toString());

        prefix="startWord";
        fieldName="employeeNumber";
        Assert.assertEquals("START_WORD_EMPLOYEE_NUMBER", ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes));
        stringBuilder=newStringBuilderWithText();
        ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes,stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"START_WORD_EMPLOYEE_NUMBER", stringBuilder.toString());
        prefix="";
        Assert.assertEquals("EMPLOYEE_NUMBER", ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes));
        stringBuilder=newStringBuilderWithText();
        ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes,stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"EMPLOYEE_NUMBER",stringBuilder.toString());
    }

    @Test
    public void camelCaseTest() {
        String prefix;
        String fieldName;
        CapitalizationTypes capitalizationTypes=CapitalizationTypes.camelCase;
        prefix="prefPref";
        fieldName="name";
        Assert.assertEquals("prefPrefName", ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes));
        StringBuilder stringBuilder=newStringBuilderWithText();
        ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes,stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"prefPrefName", stringBuilder.toString());

        prefix="startWord";
        fieldName="employeeNumber";
        Assert.assertEquals("startWordEmployeeNumber", ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes));
        stringBuilder=newStringBuilderWithText();
        ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes,stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"startWordEmployeeNumber", stringBuilder.toString());
        prefix="";
        Assert.assertEquals("employeeNumber", ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes));
        stringBuilder=newStringBuilderWithText();
        ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes,stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"employeeNumber",stringBuilder.toString());
    }


    @Test
    public void PascalCaseTest() {
        String prefix;
        String fieldName;
        CapitalizationTypes capitalizationTypes=CapitalizationTypes.PascalCase;
        prefix="prefPref";
        fieldName="name";
        Assert.assertEquals("PrefPrefName", ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes));
        StringBuilder stringBuilder=newStringBuilderWithText();
        ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes,stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"PrefPrefName", stringBuilder.toString());

        prefix="startWord";
        fieldName="employeeNumber";
        Assert.assertEquals("StartWordEmployeeNumber", ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes));
        stringBuilder=newStringBuilderWithText();
        ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes,stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"StartWordEmployeeNumber", stringBuilder.toString());
        prefix="";
        Assert.assertEquals("EmployeeNumber", ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes));
        stringBuilder=newStringBuilderWithText();
        ArtifactBase.formatProperty(prefix,fieldName,capitalizationTypes,stringBuilder);
        Assert.assertEquals(INITIAL_STRINGBUILDER_TEXT+"EmployeeNumber",stringBuilder.toString());
    }
}
