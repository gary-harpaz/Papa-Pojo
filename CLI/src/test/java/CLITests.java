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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppojo.FolderTemplateFileQuery;
import org.ppojo.Main;
import org.ppojo.OptionNames;
import org.ppojo.exceptions.FolderNotFoundException;
import org.ppojo.exceptions.FolderPathNotADirectory;
import org.ppojo.trace.ExecutingTemplateQuery;
import org.ppojo.trace.ILoggingService;
import org.ppojo.trace.ITraceEvent;
import org.ppojo.trace.LoggingService;

import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by GARY on 10/6/2015.
 */
public class CLITests {
    public static final String TEST_ROOT_SOURCE_FOLDER1="build/resources/test/test-root-source-folder";
    public static final String TEST_ROOT_SOURCE_FOLDER2="build/resources/test/test-root-source-folder2";
    public static final String TEST_FILE_TXT="test-file.txt";

    private static LoggingService _loggingService;

    @Before
    public void InitTest() {
        _loggingService=new LoggingService();
    }

    @After
    public void CleanupTest() {
        _loggingService=null;
    }


    @Test(expected = ParseException.class)
    public void sourcesArgumentTest1() throws Throwable {
        Main.runMain(new String[]{}, _loggingService);
    }
    @Test(expected = MissingArgumentException.class)
    public void sourcesArgumentTest2() throws Throwable {
        Main.runMain(new String[]{"-" + OptionNames.SOURCES},_loggingService);
    }
    @Test(expected = FolderNotFoundException.class)
    public void sourcesArgumentTest3() throws Throwable {
        Main.runMain(new String[]{"-" + OptionNames.SOURCES, "xyz"},_loggingService);
    }
    @Test(expected = FolderPathNotADirectory.class)
    public void sourcesArgumentTest4() throws Throwable {
        Main.runMain(new String[]{"-" + OptionNames.SOURCES, Paths.get(TEST_ROOT_SOURCE_FOLDER1, TEST_FILE_TXT).toString()},_loggingService);
    }

    @Test(expected = ParseException.class)
    public void searchArgumentsTest1() throws ParseException {
        Main.runMain(new String[]{"-"+OptionNames.LIST,
                "-" + OptionNames.SOURCES, Paths.get(TEST_ROOT_SOURCE_FOLDER1).toString(),
                "-"+OptionNames.SEARCH,"a","abc"},
                _loggingService);
    }
    @Test()
    public void searchArgumentsTest2() throws ParseException {
        try {
            Main.runMain(new String[]{"-" + OptionNames.LIST,
                            "-" + OptionNames.SOURCES, Paths.get(TEST_ROOT_SOURCE_FOLDER1).toString(),
                            "-" + OptionNames.SEARCH, "nr", "../",
                            "-" + OptionNames.SEARCH, "nr", "abc",
                            "-" + OptionNames.TEMPLATE,"CLI.iml",
                            "-"+OptionNames.SOURCES,Paths.get(TEST_ROOT_SOURCE_FOLDER2).toString()
                    },
                    _loggingService);
        }
        catch (Exception ex) {
           throw ex;


        }
       // System.out.println(_loggingService.getLog());

    }


}
