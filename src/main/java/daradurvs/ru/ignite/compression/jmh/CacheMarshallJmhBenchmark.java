package daradurvs.ru.ignite.compression.jmh;

import daradurvs.ru.ignite.compression.model.Audit;
import daradurvs.ru.ignite.compression.model.Audit1F;
import java.io.IOException;
import org.apache.ignite.IgniteCheckedException;
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

public class CacheMarshallJmhBenchmark extends AbstractJmhBenchmark {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(CacheMarshallJmhBenchmark.class.getSimpleName())
            .warmupIterations(20)
            .measurementIterations(50)
            .forks(1)
            .jvmArgs("-ea", "-Xms4g", "-Xmx4g")
            .shouldFailOnError(true)
            .build();

        new Runner(opt).run();
    }

    // "put" operation without compression
    @State(Scope.Thread)
    public static class IgniteCacheState {
        IgniteLazyState i;

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
    public Object marshall(IgniteCacheState state) throws IgniteCheckedException {
        return state.i.marshaller.marshal(state.i.entry);
    }

    // "put" operation with deflater compression
    @State(Scope.Thread)
    public static class IgniteCacheDeflaterState {
        IgniteLazyState i;

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
    public Object marshallWithDeflater(IgniteCacheDeflaterState state) throws IgniteCheckedException {
        return state.i.marshaller.marshal(state.i.entry);
    }

    // "put" operation with snappy compression
    @State(Scope.Thread)
    public static class IgniteCacheSnappyState {
        IgniteLazyState i;

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
    public Object marshallWithSnappy(IgniteCacheSnappyState state) throws IgniteCheckedException {
        return state.i.marshaller.marshal(state.i.entry);
    }
}
