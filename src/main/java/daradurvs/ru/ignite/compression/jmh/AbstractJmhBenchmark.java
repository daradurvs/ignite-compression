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

package daradurvs.ru.ignite.compression.jmh;

import daradurvs.ru.ignite.compression.model.Identifiable;
import daradurvs.ru.ignite.compression.model.ModelFactory;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.marshaller.Marshaller;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
abstract class AbstractJmhBenchmark {
    static final ClassLoader CLASS_LOADER = AbstractJmhBenchmark.class.getClassLoader();

//    @Benchmark
//    public void baseline() {
//    }

    static Ignite startIgnite(String cfg) {
        return Ignition.start(CLASS_LOADER.getResourceAsStream(cfg));
    }

    static class IgniteLazyState {
        private Class cls;
        private String cfg;
        private String resource;

        public ClassLoader ldr = CLASS_LOADER;
        public Ignite ignite;
        public Marshaller marshaller;
        public IgniteCache<Long, Identifiable> cache;
        public Identifiable entry;
        public List<Identifiable> entries;
        public long id;

        public IgniteLazyState(Class cls, String cfg, String resource) {
            this.cls = cls;
            this.cfg = cfg;
            this.resource = resource;
        }

        public void init() throws IOException {
            ignite = startIgnite(cfg);
            marshaller = ignite.configuration().getMarshaller();
            cache = ignite.createCache(Long.toString(System.currentTimeMillis()));
            entries = ModelFactory.create(cls, resource);
            entry = entries.get(0);
            id = entry.getId();
        }

        public void destroy() {
            cache.destroy();
            ignite.close();
        }
    }
}
