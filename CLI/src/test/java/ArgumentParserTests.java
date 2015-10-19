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

import com.google.common.collect.Lists;
import org.apache.commons.cli.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ppojo.*;
import org.ppojo.trace.LoggingService;

import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by GARY on 10/8/2015.
 */
public class ArgumentParserTests {
    private static Options _options;
    private static LoggingService _loggingService;
    private static CommandLineParser  _parser = new DefaultParser();
    CommandLine _line;

    @BeforeClass
    public static void ClassInit() {
        _options=OptionsProvider.Provide();
        _loggingService=new LoggingService();
        _parser = new DefaultParser();
    }

    @AfterClass
    public static void classCleanup() {
        _options=null;
        _loggingService=null;
    }

    @Test
    public void TestDefaultSearchQuery() throws ParseException {
        setNewCommandLine(new String[]{"-" + OptionNames.SOURCES, CLITests.TEST_ROOT_SOURCE_FOLDER1});
        parsingService parsingService = ArgumentsParser.getSchemaGraphParser(_loggingService, _line);
        ArrayList<ITemplateFileQuery> queries= Lists.newArrayList(parsingService.getTemplateQueries());
        assertTrue("Expected a single default FolderTemplateFileQuery", queries != null && queries.size() == 1);
        assertTrue("Expected a query of type FolderTemplateFileQuery",queries.get(0) instanceof FolderTemplateFileQuery);
        FolderTemplateFileQuery query=(FolderTemplateFileQuery)queries.get(0);
        assertTrue("Expected a recursive query", query.isRecursive());
        assertTrue("Expected the folder query to contain the TEST_ROOT_SOURCE_FOLDER1 path",query.getPath().contains(Paths.get(CLITests.TEST_ROOT_SOURCE_FOLDER1).toString()));


    }
    @Test
    public void TestFolderTemplateFileQueryArgs() throws ParseException {
        setNewCommandLine(new String[]{"-" + OptionNames.SOURCES, CLITests.TEST_ROOT_SOURCE_FOLDER1
        ,"-"+OptionNames.SEARCH,"r", CLITests.TEST_ROOT_SOURCE_FOLDER1,"-"+OptionNames.SEARCH,"nr",CLITests.TEST_ROOT_SOURCE_FOLDER2});
        parsingService parsingService = ArgumentsParser.getSchemaGraphParser(_loggingService, _line);
        ArrayList<ITemplateFileQuery> queries= Lists.newArrayList(parsingService.getTemplateQueries());

    }

    private void setNewCommandLine(String[] args) {
        try {
            _line = _parser.parse( _options, args );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
