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

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by GARY on 10/6/2015.
 */
public class HelpPrinter {
    public static void print(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("");
        formatter.setOptionComparator(Comparator.instance);
        formatter.setWidth(140);
        formatter.printHelp("usage: papa-pojo -sources <sources folder> [options]", options);
    }

    private static class Comparator implements java.util.Comparator<Option> {
        public static final Comparator instance=new Comparator();

        private ArrayList<String> _optionOrder=new ArrayList<>();
        private Comparator() {
            _optionOrder=new ArrayList<>();
            _optionOrder.add(OptionNames.SOURCES);
            _optionOrder.add(OptionNames.SEARCH);
            _optionOrder.add(OptionNames.TEMPLATE);
            _optionOrder.add(OptionNames.LIST);
            _optionOrder.add(OptionNames.CLEAN);
            _optionOrder.add(OptionNames.HELP);
        }

        @Override
        public int compare(Option o1, Option o2) {
            Integer order1=getOrder(o1);
            Integer order2=getOrder(o2);
            return order1.compareTo(order2);
        }

        private Integer getOrder(Option option) {
            int index=_optionOrder.indexOf(option.getOpt());
            if (index>=0)
                return index;
            return 9999;
        }
    }
}
