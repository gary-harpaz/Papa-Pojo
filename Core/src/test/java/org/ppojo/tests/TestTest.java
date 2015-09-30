package org.ppojo.tests;

import org.junit.Test;
import org.ppojo.ArtifactTypes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by GARY on 9/26/2015.
 */
public class TestTest {

    public void DoComparable() {
        ArrayList<MyTestClass> arr=new ArrayList<>();
        arr.add(new MyTestClass(3));
        arr.add(new MyTestClass(2));
        arr.add(new MyTestClass(4));
        arr.add(new MyTestClass(5));
        arr.sort((x,y)-> {
            if (x.AField!=y.AField)
                if (x.AField>y.AField)
                    return 1;
                else
                    return -1;
            else
                return 0;
        });




    }
    public void ValueOfTest() {
//       ArtifactTypes ss2=ArtifactTypes.valueOf("Psojo");
//        ArtifactTypes ss2=Enum.valueOf(ArtifactTypes.class, "Psojo");

//        ArtifactTypes[] sds=ArtifactTypes.values();
//        Object aaa=sds[0];

//        ArtifactTypes bbb=(ArtifactTypes)aaa;



//        Converter<String,ArtifactTypes> conv=Enums.stringConverter(ArtifactTypes.class);

//        ArtifactTypes type=conv.convert("Pojo");
  //      type=conv.convert("Psoso");

/*        Class<ArtifactTypes> type=ArtifactTypes.class;
        ArtifactTypes[] arr=type.getEnumConstants();
        Field[] aaa=type.getFields();*/
        ArtifactTypes aaa= ArtifactTypes.Parse("Pojo");
        if (aaa==ArtifactTypes.Unknown)
            System.out.println("None");
        aaa= ArtifactTypes.Parse("asd");
        if (aaa==ArtifactTypes.Unknown)
            System.out.println("null");
        switch (aaa) {
            default:
                System.out.println("null");

        }




    }
    public void DoTempFile() {

        Path path= Paths.get("abc");
        Path path2=path.resolve("efg");
        String result=path2.toString();

        //File tempDir=com.google.common.io.Files.createTempDir();
        StringBuilder as=new StringBuilder();
        as.append("f");
        String dsf="";
        as.append(dsf);


    }

    @Test
    public void TestGenericTypes() {
        ArrayList<String> list1=new ArrayList<String>() {};
        ArrayList<Path> list2=new ArrayList<Path>() {};
        Class class1=list1.getClass() ;
        Class class2=list2.getClass();
        boolean test=class1.equals(class2);
        

        String[] arr1=new String[0];
        Integer[] arr2=new Integer[0];
        class1=arr1.getClass();
        class2=arr2.getClass();
        test=class1.equals(class2);




    }


    private class MyTestClass {
        MyTestClass(int AField) {
            this.AField=AField;
        }
        public Integer AField;

        @Override
        public String toString() {
            return this.AField.toString();
        }
    }
}
