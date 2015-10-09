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

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.File;

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
        Option list=new Option(OptionNames.LIST,"Lists all template files matched but don't process them");
        Option clean=new Option(OptionNames.CLEAN,"Deletes all artifact files defined in matched templates");

        Option search=Option.builder(OptionNames.SEARCH).numberOfArgs(2).argName("r|nr folder[" + File.separator + "pattern]")
                .desc("Searches a folder for  template files. The first argument determines whether the search is recursive (r) or non recursive (nr)." +
                        " The second argument is a path to the folder optionally ending with a pattern for matching template files. When omitted a default pattern value of " + FolderTemplateFileQuery.getDefaultFileFilter() + " is used." +
                        " Folder path can be a relative path and multiple searches can be specified.").build();



        Options options=new Options();
        options.addOption(help);
        options.addOption(sources);
        options.addOption(template);
        options.addOption(list);
        options.addOption(search);
        options.addOption(clean);

        _options=options;
        return options;
    }
}
