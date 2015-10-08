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

/**
 * Created by GARY on 10/7/2015.
 */
public class ValidatedSourceFolderPath implements ITraceEvent {
    private final String _path;
    public ValidatedSourceFolderPath(String path) {
        _path=path;
    }

    public String getPath() {
        return _path;
    }

    @Override
    public String toLogMessage() {
        return "Validated source folder path "+_path;
    }
}