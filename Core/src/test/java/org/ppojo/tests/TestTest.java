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
import org.ppojo.ArtifactTypes;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
    public  void TestPathWithWildcard() {
        File file=new File("MyFolder\\*some?.txt");
        File parent=file.getParentFile();
        String parent_path=parent.getAbsolutePath();


    }


    public void TestGenericTypes() {

        ArrayList<String> arr=new ArrayList<>();
        arr.add("abc");
        Path path=Paths.get("zxc");
        _stam=arr;
        ArrayList<Path> paths=getSomething();
        paths.add(path);
    }

    private static Object _stam;

    private static <T> ArrayList<T> getSomething() {
        return (ArrayList<T>)_stam;

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
