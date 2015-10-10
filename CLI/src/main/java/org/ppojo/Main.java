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
import org.ppojo.utils.Helpers;
import org.ppojo.trace.ILoggingService;
import org.ppojo.trace.LoggingService;

/**
 * Main CLI entry point
 */
public class Main {

    private static ILoggingService _loggingService =new LoggingService();
    public static void main(String[] args) {
        boolean success=false;
        try {
            runMain(args, _loggingService);
            success=true;
        }
        catch( ParseException exp ) {
            System.err.println(exp.getMessage());
            HelpPrinter.print(OptionsProvider.Provide());
        }
        catch (Exception exp2) {
            System.err.println(exp2.toString());
        }
        tryPrintLog();
        if (success)
            System.exit(0);
        else
            System.exit(1);
    }

    private static void tryPrintLog() {
        String log=_loggingService.getLog();
        if (!Helpers.IsNullOrEmpty(log))
            System.out.println(log);
    }

    public static void runMain(String[] args,ILoggingService loggingService) throws ParseException {
        if (loggingService==null)
            throw new NullPointerException("loggingService");
        Options options = OptionsProvider.Provide();
        Runnable command=ArgumentsParser.Parse(options,args,loggingService);
        command.run();

    }

}
