package org.ppojo;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Created by GARY on 10/6/2015.
 */
public class OptionsProvider {

    private static Options _options;
    public static Options Provide() {
        if (_options!=null)
            return _options;
        Option help = new Option( OptionNames.HELP, "Print this message." );

        Option sources= Option.builder(OptionNames.SOURCES).argName("sources folder")
                .hasArg()
                .desc("A source files folder under which java files can be created. It should correspond to the root package name folder. Folder can be a relative path and multiple sources options can be specified.")
                .build();
        Option template=Option.builder(OptionNames.TEMPLATE).argName("template file")
                .hasArg()
                .desc("Path to template file for generating source files. File path can be a relative path and multiple template options can be specified.")
                .build();
        Option list=new Option(OptionNames.LIST,"lists all template files matched but don't process them");
        Option search=Option.builder(OptionNames.SEARCH).numberOfArgs(2).hasArgs().argName("r|nr folder[\\pattern]")
                .desc("Searches a folder for  template files. The first argument determines whether the search is recursive (r) or non recursive (nr)." +
                        " The second argument is a path to the folder optionally ending with a pattern for matching template files. When omitted a default pattern value of *.pppj is used." +
                        " Folder path can be a relative path and multiple searches can be specified.").build();



        Options options=new Options();
        options.addOption(help);
        options.addOption(sources);
        options.addOption(template);
        options.addOption(list);
        options.addOption(search);

        _options=options;
        return options;
    }
}
