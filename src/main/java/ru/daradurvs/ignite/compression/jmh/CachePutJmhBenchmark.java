package ru.daradurvs.ignite.compression.jmh;

import ru.daradurvs.ignite.compression.model.Audit;
import ru.daradurvs.ignite.compression.model.Audit1F;
import java.io.IOException;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;

import static ru.daradurvs.ignite.compression.model.DataGenerator.AUDIT_CSV;

public class CachePutJmhBenchmark extends AbstractJmhBenchmark {
    public static void main(String[] args) throws RunnerException {
        Options opt = options("put.csv");

        new Runner(opt).run();
    }

    // "put" operation without compression
    @State(Scope.Thread)
    public static class IgniteCacheState {
        IgniteLazyState i;

        @Setup(Level.Iteration)
        public void clear() {
            i.cache.clear();

            assert i.cache.size() == 0;
        }

        @Setup(Level.Trial)
        public void setUp() throws IOException {
            i = new IgniteLazyState(Audit.class, "cache-config-deflater.xml", AUDIT_CSV);
            i.init();
        }

        @TearDown(Level.Trial)
        public void teatDown() {
            i.destroy();
        }
    }

    @Benchmark
    public void put(IgniteCacheState state) {
        state.i.cache.put(state.i.id, state.i.entry);
    }

    // "put" operation with compression
    @State(Scope.Thread)
    public static class IgniteCacheCompressionState {
        @Param({
            "cache-config-apache-deflater.xml",
            "cache-config-gzip.xml",
            "cache-config-lz4.xml",
            "cache-config-snappy.xml",
            "cache-config-xz.xml",
            "cache-config-lzma.xml"})
        public String config;

        IgniteLazyState i;

        @Setup(Level.Iteration)
        public void clear() {
            i.cache.clear();

            assert i.cache.size() == 0;
        }

        @Setup(Level.Trial)
        public void setUp() throws IOException {
            i = new IgniteLazyState(Audit1F.class, config, AUDIT_CSV);
            i.init();
        }

        @TearDown(Level.Trial)
        public void teatDown() {
            i.destroy();
        }
    }

    @Benchmark
    public void putWithCompression(IgniteCacheCompressionState state) {
        state.i.cache.put(state.i.id, state.i.entry);
    }
}
