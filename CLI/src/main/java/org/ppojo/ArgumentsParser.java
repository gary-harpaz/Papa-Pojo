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

package org.ppojo;

import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by GARY on 10/6/2015.
 */
public class ArgumentsParser {
    public static Runnable Parse(Options options, String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine line;
        line = parser.parse( options, args );
        if (line.hasOption(OptionNames.HELP))
            return ()->HelpPrinter.print(options);
        if (!line.hasOption(OptionNames.SOURCES))
            throw new ParseException("Missing required option sources");
        ArrayList<String> sourcesFolders=parseSourceFolders(line);
        ArrayList<ITemplateFileQuery> queries=new ArrayList<>();
        parseSearchQueries(line,queries);
        parseTemplateFileArgs(line,queries);
        if (!line.hasOption(OptionNames.LIST))
            return ()->SchemaGraphParser.generateArtifacts(sourcesFolders,queries,ArtifactOptions.getDefaultOptions());
        else
            return ()->SchemaGraphParser.listMatchedTemplates(sourcesFolders, queries, ArtifactOptions.getDefaultOptions());




       // String user_dir=System.getProperty("user.dir");
      //  System.out.println(user_dir);
      //  Path currentRelativePath = Paths.get("");
      //  String rel_path = currentRelativePath.toAbsolutePath().toString();
     //   System.out.println(rel_path);
    }

    private static void parseTemplateFileArgs(CommandLine line, ArrayList<ITemplateFileQuery> queries) {

    }

    private static void parseSearchQueries(CommandLine line, ArrayList<ITemplateFileQuery> queries) {

    }

    private static ArrayList<String> parseSourceFolders(CommandLine line) {
        ArrayList<String> sourcesFolders=new ArrayList<>();
        for (String sourceFolder : line.getOptionValues(OptionNames.SOURCES)) {
            sourcesFolders.add(sourceFolder);
        }
        return sourcesFolders;
    }
}
