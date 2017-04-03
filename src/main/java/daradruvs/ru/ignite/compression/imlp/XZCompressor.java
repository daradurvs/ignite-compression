/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package daradruvs.ru.ignite.compression.imlp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.ignite.internal.binary.compression.Compressor;

public class XZCompressor implements Compressor {
    private static final int BUFFER_SIZE = 100;

    @Override public byte[] compress(byte[] bytes) throws IOException {

        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             XZCompressorOutputStream xzOut = new XZCompressorOutputStream(baos)) {

            final byte[] buffer = new byte[BUFFER_SIZE];
            int n;

            while ((n = bais.read(buffer)) != -1)
                xzOut.write(buffer, 0, n);

            xzOut.close(); // Need it, otherwise EOFException at decompressing

            return baos.toByteArray();
        }
    }

    @Override public byte[] decompress(byte[] bytes) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             XZCompressorInputStream xzIn = new XZCompressorInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            final byte[] buffer = new byte[BUFFER_SIZE];
            int n;

            while ((n = xzIn.read(buffer)) != -1)
                baos.write(buffer, 0, n);

            return baos.toByteArray();
        }
    }
}
