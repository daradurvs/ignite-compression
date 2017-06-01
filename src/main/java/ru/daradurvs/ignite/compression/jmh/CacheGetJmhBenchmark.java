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

public class CacheGetJmhBenchmark extends AbstractJmhBenchmark {
    public static void main(String[] args) throws RunnerException {
        Options opt = options("get.csv");

        new Runner(opt).run();
    }

    // "get" operation without compression
    @State(Scope.Thread)
    public static class IgniteCacheState {
        @Param({"0"})
        public int len;

        IgniteLazyState i;

        @Setup(Level.Iteration)
        public void prepare() {
            i.cache.clear();
            assert i.cache.size() == 0;

            i.cache.put(i.id, i.entry);
            assert i.cache.size() == 1;
        }

        @Setup(Level.Trial)
        public void setUp() throws IOException {
            i = new IgniteLazyState(Audit.class, "cache-config-deflater.xml", AUDIT_CSV, len);
            i.init();
        }

        @TearDown(Level.Trial)
        public void teatDown() {
            i.destroy();
        }
    }

    @Benchmark
    public Object get(IgniteCacheState state) {
        return state.i.cache.get(state.i.id);
    }

    // "get" operation with compression
    @State(Scope.Thread)
    public static class IgniteCacheCompressionState {
        @Param({"0"})
        public String config = "";
        @Param({"0"})
        public int len;

        IgniteLazyState i;

        @Setup(Level.Iteration)
        public void prepare() {
            i.cache.clear();
            assert i.cache.size() == 0;

            i.cache.put(i.id, i.entry);
            assert i.cache.size() == 1;
        }

        @Setup(Level.Trial)
        public void setUp() throws IOException {
            i = new IgniteLazyState(Audit1F.class, config, AUDIT_CSV, len);
            i.init();
        }

        @TearDown(Level.Trial)
        public void teatDown() {
            i.destroy();
        }
    }

    @Benchmark
    public Object getWithCompression(IgniteCacheCompressionState state) {
        return state.i.cache.get(state.i.id);
    }
}
