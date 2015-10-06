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

package org.ppojo.utils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Created by GARY on 9/23/2015.
 */
public class Helpers {

    public static @Nullable <T> T as(Class<T> clazz, Object o){
        if(clazz.isInstance(o)){
            return clazz.cast(o);
        }
        return null;
    }

    public static  boolean IsNullOrEmpty(String str) {
        if (str==null || str.length()==0 || str.trim().length()==0)
            return  true;
        return  false;
    }

    public static String removeFilenameExtension(String fname) {
        return removeLastOccurrenceOf(fname, ".");
    }
    public static String removeLastOccurrenceOf(String str, String substring) {
        int pos = str.lastIndexOf(substring);
        if (pos > 0) {
            str = str.substring(0, pos);
        }
        return str;
    }

    public static <K,V> HashMap<K,V> newHashMap(Iterable<V> items,Function<V,K> keySelector) {
        HashMap<K,V> result=new HashMap<>();
        for (V item : items) {
            result.put(keySelector.apply(item),item);
        }
        return result;
    }

    public static String readResourceTextFile(String fileName) {
        //Get file from resources folder
        ClassLoader classLoader = Helpers.class.getClassLoader();
        return readTextFile(classLoader.getResource(fileName).getFile());
    }
    public static String readTextFile(String path) {
        StringBuilder result = new StringBuilder("");
        File file = new File(path);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append(System.lineSeparator());
            }
            scanner.close();
        } catch (IOException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result.toString();

    }

    public static Path getResourcePath(String resourceName) {
        URL url=Helpers.class.getClassLoader().getResource(resourceName);
        try {
            return Paths.get(url.toURI());
        }
        catch (URISyntaxException exp) {
            throw new RuntimeException(exp);
        }
    }

    public static String EmptyIfNull(String str) {
        return str==null?"":str;
    }
}
