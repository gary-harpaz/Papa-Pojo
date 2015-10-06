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

import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.ppojo.Main;
import org.ppojo.OptionNames;
import org.ppojo.exceptions.FolderNotFoundException;
import org.ppojo.exceptions.FolderPathNotADirectory;

import java.nio.file.Paths;

/**
 * Created by GARY on 10/6/2015.
 */
public class CLITests {
    private static final String TEST_ROOT_SOURCE_FOLDER1="build/resources/test/test-root-source-folder";
    private static final String TEST_FILE_TXT="test-file.txt";

    @Test(expected = ParseException.class)
    public void sourcesArgumentTest1() throws ParseException {
        Main.runMain(new String[] {});
    }
    @Test(expected = MissingArgumentException.class)
    public void sourcesArgumentTest2() throws ParseException {
        Main.runMain(new String[] {"-"+ OptionNames.SOURCES});
    }
    @Test(expected = FolderNotFoundException.class)
    public void sourcesArgumentTest3() throws ParseException {
        Main.runMain(new String[] {"-"+OptionNames.SOURCES,"xyz"});
    }
    @Test(expected = FolderPathNotADirectory.class)
    public void sourcesArgumentTest4() throws ParseException {
        Main.runMain(new String[] {"-"+OptionNames.SOURCES, Paths.get(TEST_ROOT_SOURCE_FOLDER1,TEST_FILE_TXT).toString()});
    }

    @Test
    public void sourcesArgumentTest5() throws ParseException {
        Main.runMain(new String[] {"-"+OptionNames.SOURCES,TEST_ROOT_SOURCE_FOLDER1});
    }



}
