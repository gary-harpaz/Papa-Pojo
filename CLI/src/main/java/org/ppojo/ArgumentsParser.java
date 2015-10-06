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
            return ()->SchemaGraphParser.listMatchedTemplates(sourcesFolders,queries,ArtifactOptions.getDefaultOptions());




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
