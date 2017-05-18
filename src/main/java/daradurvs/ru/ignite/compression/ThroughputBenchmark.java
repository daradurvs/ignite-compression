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

package daradurvs.ru.ignite.compression;

import daradurvs.ru.ignite.compression.model.Audit;
import daradurvs.ru.ignite.compression.model.Audit1F;
import daradurvs.ru.ignite.compression.model.Identifiable;
import daradurvs.ru.ignite.compression.model.ModelFactory;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.Ignition;
import org.apache.ignite.marshaller.Marshaller;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import static daradurvs.ru.ignite.compression.model.DataGenerator.AUDIT_CSV;

@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ThroughputBenchmark {
    private static final ClassLoader CLASS_LOADER = ThroughputBenchmark.class.getClassLoader();

    @State(Scope.Thread)
    public static class IgniteState {
        public Ignite ignite;
        public IgniteCache cache;
        public Marshaller marshaller;
        public List<Identifiable> audits;
        public List<Identifiable> audits1F;

        @Setup(Level.Trial)
        public void setUp() throws IOException {
            ignite = Ignition.start(CLASS_LOADER.getResourceAsStream("cache-config-snappy.xml"));
            cache = ignite.createCache(Long.toString(System.currentTimeMillis()));
            marshaller = ignite.configuration().getMarshaller();
            audits = ModelFactory.create(Audit.class, AUDIT_CSV);
            audits1F = ModelFactory.create(Audit1F.class, AUDIT_CSV);
        }

        @TearDown(Level.Trial)
        public void teatDown() {
            cache.destroy();
            ignite.close();
        }
    }

    @Benchmark
    @Measurement(iterations = 20)
    @Warmup(iterations = 20)
    @Fork(1)
    public byte[] marshal(IgniteState state) throws IgniteCheckedException {
        // filled cache
        // ids of objects
        return state.marshaller.marshal(state.audits.get(0));
    }

    @Benchmark
    @Measurement(iterations = 20)
    @Warmup(iterations = 20)
    @Fork(1)
    public byte[] marshal1F(IgniteState state) throws IgniteCheckedException {
        // filled cache
        // ids of objects
        return state.marshaller.marshal(state.audits1F.get(0));
    }
/*
    @Benchmark
    public Object get(IgniteState state) throws IgniteCheckedException {
        // filled cache
        // ids of objects
        return state.marshaller.marshal(state.audits.get(0));
    }

    @Benchmark
    public Object get1F(IgniteState state) throws IgniteCheckedException {
        // filled cache
        // ids of objects
        return state.marshaller.marshal(state.audits1F.get(0));
    }
/*
    @Benchmark
    public void put() {
        // objects
        // empty cache
    }

    @Benchmark
    public void put1F() {
        // objects
        // empty cache
    }*/
}
