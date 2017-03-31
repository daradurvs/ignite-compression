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

package daradruvs.ru.ignite.compression.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static daradruvs.ru.ignite.compression.model.DataGenerator.RES_MODEL_DIR;

public class ModelFactory {
    public static List<Person> createPersons(String fileName) throws IOException {
        List<Person> list = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(RES_MODEL_DIR + fileName), StandardCharsets.UTF_8);

        for (String line : lines) {
            String[] data = line.split(";");

            assert data.length == 5;

            list.add(
                new Person(
                    Long.valueOf(data[0]),
                    Integer.valueOf(data[1]),
                    Boolean.valueOf(data[2]),
                    data[3],
                    data[4]
                ));
        }

        return list;
    }

    public static List<Audit> createAudits(String fileName) throws IOException {
        List<Audit> list = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(RES_MODEL_DIR + fileName), StandardCharsets.UTF_8);

        for (String line : lines) {
            String[] data = line.split(";");

            assert data.length == 3;

            list.add(
                new Audit(
                    Long.valueOf(data[0]),
                    UUID.fromString(data[1]),
                    data[2]));
        }

        return list;
    }
}
