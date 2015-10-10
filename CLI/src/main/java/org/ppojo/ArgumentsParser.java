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
import org.ppojo.exceptions.FolderNotFoundException;
import org.ppojo.exceptions.InvalidFolderPathException;
import org.ppojo.trace.ILoggingService;
import org.ppojo.utils.Helpers;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the user command line arguments, and validates them. Returns a command to be executen in the {@link Main} class.
 */
public class ArgumentsParser {
    public static Runnable Parse(Options options, String[] args,ILoggingService loggingService) throws ParseException {

        CommandLineParser parser = new DefaultParser();
        CommandLine line;
        line = parser.parse( options, args );
        if (line.hasOption(OptionNames.HELP))
            return ()->HelpPrinter.print(options);
        if (!line.hasOption(OptionNames.SOURCES))
            throw new ParseException("Missing required option sources");
        parsingService parsingService = getSchemaGraphParser(loggingService, line);
        boolean listOption=line.hasOption(OptionNames.LIST);
        if (!line.hasOption(OptionNames.CLEAN))
            if (!listOption)
                return ()-> parsingService.generateArtifacts();
            else
                return ()-> parsingService.listMatchedTemplates();
        else
            return ()-> parsingService.cleanArtifacts(listOption);
    }

    public static parsingService getSchemaGraphParser(ILoggingService loggingService, CommandLine line) throws ParseException {
        ArrayList<String> sourcesFolders=parseSourceFolders(line);
        ArrayList<ITemplateFileQuery> queries=new ArrayList<>();
        if (!definesQueries(line)) {
            for (String sourcesFolder : sourcesFolders) {
                FolderTemplateFileQuery query=new FolderTemplateFileQuery(sourcesFolder,FolderTemplateFileQuery.getDefaultFileFilter(),true);
                queries.add(query);
            }
        }
        parseSearchQueries(line,queries);
        parseTemplateFileArgs(line,queries);
        return new parsingService(sourcesFolders,queries, ArtifactOptions.getDefaultOptions(),loggingService);
    }

    private static boolean definesQueries(CommandLine line) {
        return line.hasOption(OptionNames.SEARCH) || line.hasOption(OptionNames.TEMPLATE);
    }

    private static void parseTemplateFileArgs(CommandLine line, ArrayList<ITemplateFileQuery> queries) throws ParseException {
        if (line.hasOption(OptionNames.SEARCH)) {
            Option[] options=line.getOptions();
            for (Option option : options) {
                if (option.getOpt()==OptionNames.SEARCH) {
                    List<String> values=option.getValuesList();
                    if (values==null || values.size()!=2)
                        throw new ParseException("Invalid values for search option. Expected two arguments for option.");
                    boolean isRecursive=parserIsRecursive(values.get(0));

                    String fileFilter=FolderTemplateFileQuery.getDefaultFileFilter();
                    String folderPath=null;
                    File file=(new File(values.get(1))).getAbsoluteFile();
                    boolean exists=file.exists();
                    boolean isDirectory=file.isDirectory();
                    if (exists) {
                        if (isDirectory) //folder path specification with omitted pattern
                            folderPath=file.getAbsolutePath();
                        else
                            if (file.isFile()) { //the pattern resolves to a specific file
                                TemplateFileQuery templateFileQuery=new TemplateFileQuery(file.getAbsolutePath(),true);
                                queries.add(templateFileQuery);
                            }
                            else
                                throw new ParseException("Invalid value for search option. Existing path value is neither a file or directory");

                    }
                    else {
                        File parentFile=file.getParentFile();
                        if (!parentFile.exists())
                            throw new FolderNotFoundException("Invalid value for search option. The folder path "+parentFile.getAbsolutePath()+" does not exist");
                        if (!parentFile.isDirectory())
                            throw new InvalidFolderPathException("Invalid value for search option. The folder path "+parentFile.getAbsolutePath()+" is not a folder");
                        String replaceStr=parentFile.toString();
                        if (!replaceStr.endsWith(File.separator))
                            replaceStr+= File.separator;
                        fileFilter=file.toString().replace(replaceStr, "");
                        folderPath=parentFile.getAbsolutePath();
                    }
                    if (!Helpers.IsNullOrEmpty(folderPath)){
                        folderPath= Paths.get(folderPath).normalize().toString();
                        FolderTemplateFileQuery query=new FolderTemplateFileQuery(folderPath,fileFilter,isRecursive);
                        queries.add(query);
                    }
                }
            }

        }

    }
    private static boolean parserIsRecursive(String arg) throws ParseException {
        boolean isRecursive=true;
        switch (arg) {
            case "r":
                isRecursive=true;
                break;
            case "nr":
                isRecursive=false;
                break;
            default:
                throw new ParseException("Invalid value for search option. Expected either r or nr got "+arg);
        }
        return isRecursive;
    }

    private static void parseSearchQueries(CommandLine line, ArrayList<ITemplateFileQuery> queries) {
        if (line.hasOption(OptionNames.TEMPLATE)) {
            String[] values=line.getOptionValues(OptionNames.TEMPLATE);
            if (values!=null) {
                for (String value : values) {
                    TemplateFileQuery query=new TemplateFileQuery(value,false);
                    queries.add(query);
                }
            }
        }

    }

    private static ArrayList<String> parseSourceFolders(CommandLine line) {
        ArrayList<String> sourcesFolders=new ArrayList<>();
        for (String sourceFolder : line.getOptionValues(OptionNames.SOURCES)) {
            sourcesFolders.add(sourceFolder);
        }
        return sourcesFolders;
    }
}
