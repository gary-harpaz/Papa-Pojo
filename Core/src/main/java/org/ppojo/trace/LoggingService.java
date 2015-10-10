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

package org.ppojo.trace;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements logging during artifact generation using a StringBuilder
 * @see ILoggingService
 */
public class LoggingService implements ILoggingService {

    private StringBuilder _stringBuilder;
    private ArrayList<ITraceEvent> _traceEvents;
    public LoggingService() {
        _stringBuilder=new StringBuilder();
        _traceEvents =new ArrayList<>();

    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    @Override
    public void appendLine(String message) {
        LocalDateTime dateTime=ZonedDateTime.now().toLocalDateTime();
        _stringBuilder.append(dateTime.format(formatter)).append("\t").append(message).append(System.lineSeparator());
    }

    @Override
    public String getLog() {
        return  _stringBuilder.toString();
    }

    @Override
    public void addTraceEvent(ITraceEvent event) {
        _traceEvents.add(event);
        appendLine(event.toLogMessage());
    }

}
