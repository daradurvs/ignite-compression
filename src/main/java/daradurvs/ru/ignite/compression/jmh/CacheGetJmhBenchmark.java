package daradurvs.ru.ignite.compression.jmh;

import daradurvs.ru.ignite.compression.model.Audit;
import daradurvs.ru.ignite.compression.model.Audit1F;
import java.io.IOException;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import static daradurvs.ru.ignite.compression.model.DataGenerator.AUDIT_CSV;

public class CacheGetJmhBenchmark extends AbstractJmhBenchmark {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(CacheGetJmhBenchmark.class.getSimpleName())
            .warmupIterations(20)
            .measurementIterations(50)
            .forks(1)
            .jvmArgs("-ea", "-Xms4g", "-Xmx4g")
            .shouldFailOnError(true)
            .build();

        new Runner(opt).run();
    }

    // "get" operation without compression
    @State(Scope.Thread)
    public static class IgniteCacheState {
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
            i = new IgniteLazyState(Audit.class, "cache-config-deflater.xml", AUDIT_CSV);
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

    // "get" operation with deflater compression
    @State(Scope.Thread)
    public static class IgniteCacheDeflaterState {
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
            i = new IgniteLazyState(Audit1F.class, "cache-config-deflater.xml", AUDIT_CSV);
            i.init();
        }

        @TearDown(Level.Trial)
        public void teatDown() {
            i.destroy();
        }
    }

    @Benchmark
    public Object getWithDeflater(IgniteCacheDeflaterState state) {
        return state.i.cache.get(state.i.id);
    }

    // "get" operation with snappy compression
    @State(Scope.Thread)
    public static class IgniteCacheSnappyState {
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
            i = new IgniteLazyState(Audit1F.class, "cache-config-snappy.xml", AUDIT_CSV);
            i.init();
        }

        @TearDown(Level.Trial)
        public void teatDown() {
            i.destroy();
        }
    }

    @Benchmark
    public Object getWithSnappy(IgniteCacheSnappyState state) {
        return state.i.cache.get(state.i.id);
    }

    // "get" operation with gzip compression
    @State(Scope.Thread)
    public static class IgniteCacheGZipState {
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
            i = new IgniteLazyState(Audit1F.class, "cache-config-gzip.xml", AUDIT_CSV);
            i.init();
        }

        @TearDown(Level.Trial)
        public void teatDown() {
            i.destroy();
        }
    }

    @Benchmark
    public Object getWithGZip(IgniteCacheGZipState state) {
        return state.i.cache.get(state.i.id);
    }
}