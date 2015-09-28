/*
 * Copyright 2015 Laukviks Bedrifter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laukvik.csv.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.laukvik.csv.CSV;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public interface Writeable {

    public void write(CSV csv, OutputStream out, Charset charset) throws IOException;

}
