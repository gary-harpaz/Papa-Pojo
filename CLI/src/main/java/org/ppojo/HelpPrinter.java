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
