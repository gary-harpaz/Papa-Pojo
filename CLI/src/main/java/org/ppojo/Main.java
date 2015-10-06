package org.ppojo;

import org.apache.commons.cli.*;

/**
 * Created by GARY on 10/6/2015.
 */
public class Main {
    public static void main(String[] args) {
        try {
            runMain(args);
        }
        catch( ParseException exp ) {
            System.err.println(exp.getMessage());
            HelpPrinter.print(OptionsProvider.Provide());
        }
        catch (Exception exp2) {
            System.err.println(exp2.toString());
        }
        System.exit(1);
    }
    public static void runMain(String[] args) throws ParseException {
        Options options = OptionsProvider.Provide();
        Runnable command=ArgumentsParser.Parse(options,args);
        command.run();
    }
}
