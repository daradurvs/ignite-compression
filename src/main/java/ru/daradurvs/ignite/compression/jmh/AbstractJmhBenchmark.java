package ru.daradurvs.ignite.compression.jmh;

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
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import ru.daradurvs.ignite.compression.model.Identifiable;
import ru.daradurvs.ignite.compression.model.ModelFactory;

@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
abstract class AbstractJmhBenchmark {
    static final ClassLoader CLASS_LOADER = AbstractJmhBenchmark.class.getClassLoader();
    static final String RESULT_DIR = "src/main/resources/result/jmh/";

//    @Benchmark
//    public void baseline() {
//    }

    static Options options(String result) {
        return new OptionsBuilder()
            .include(CachePutJmhBenchmark.class.getSimpleName())
            .warmupIterations(20)
            .measurementIterations(50)
            .forks(1)
            .jvmArgs("-ea", "-Xms4g", "-Xmx4g")
            .shouldFailOnError(true)
            .result(RESULT_DIR + result)
            .param("len", "100", "500", "1000", "2000", "3000", "4000", "5000", "6000")
            .param("config",
                "cache-config-apache-deflater.xml",
                "cache-config-gzip.xml",
                "cache-config-lz4.xml",
                "cache-config-snappy.xml",
                "cache-config-xz.xml",
                "cache-config-lzma.xml"
            )
            .build();
    }

    static Ignite startIgnite(String cfg) {
        return Ignition.start(CLASS_LOADER.getResourceAsStream(cfg));
    }

    static class IgniteLazyState {
        private Class cls;
        private String cfg;
        private String resource;
        private int len;

        public ClassLoader ldr = CLASS_LOADER;
        public Ignite ignite;
        public Marshaller marshaller;
        public IgniteCache<Long, Identifiable> cache;
        public Identifiable entry;
        public List<Identifiable> entries;
        public long id;

        public IgniteLazyState(Class cls, String cfg, String resource, int len) {
            this.cls = cls;
            this.cfg = cfg;
            this.resource = resource;
            this.len = len;
        }

        public IgniteLazyState(Class cls, String cfg, String resource) {
            this(cls, cfg, resource, -1);
        }

        public void init() throws IOException {
            ignite = startIgnite(cfg);
            marshaller = ignite.configuration().getMarshaller();
            cache = ignite.createCache(Long.toString(System.currentTimeMillis()));
            entries = ModelFactory.create(cls, resource, len);
            entry = entries.get(0);
            id = entry.getId();
        }

        public void destroy() {
            cache.destroy();
            ignite.close();
        }
    }
}
