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

import org.ppojo.FolderTemplateFileQuery;
import org.ppojo.ITemplateFileQuery;

/**
 * Created by GARY on 10/8/2015.
 */
public class ExecutingTemplateQuery implements ITraceEvent {
    private final ITemplateFileQuery _query;
    public ExecutingTemplateQuery(ITemplateFileQuery query) {
        _query=query;
    }
    @Override
    public String toLogMessage() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("Executing template query");
        if (_query instanceof FolderTemplateFileQuery) {
            FolderTemplateFileQuery folderTemplateFileQuery=(FolderTemplateFileQuery)_query;
            stringBuilder.append(" searching");
            if (folderTemplateFileQuery.isRecursive())
                stringBuilder.append(" recursively");
            else
                stringBuilder.append(" non recursively");
            stringBuilder.append(" filter = ").append(folderTemplateFileQuery.getFileFilter()).append(" ");
            stringBuilder.append(folderTemplateFileQuery.getPath());
        }
        else
            stringBuilder.append(" of type "+_query.getClass().toString());
        return stringBuilder.toString();
    }
    public ITemplateFileQuery getQuery() {
        return _query;
    }

}
