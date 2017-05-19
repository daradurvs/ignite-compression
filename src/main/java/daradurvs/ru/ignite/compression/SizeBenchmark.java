package daradurvs.ru.ignite.compression;

import daradurvs.ru.ignite.compression.model.Audit;
import daradurvs.ru.ignite.compression.model.Audit1F;
import daradurvs.ru.ignite.compression.model.Identifiable;
import daradurvs.ru.ignite.compression.model.ModelFactory;
import daradurvs.ru.ignite.compression.model.Person;
import daradurvs.ru.ignite.compression.model.Person2F;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.marshaller.Marshaller;

import static daradurvs.ru.ignite.compression.model.DataGenerator.AUDIT2_CSV;
import static daradurvs.ru.ignite.compression.model.DataGenerator.AUDIT_CSV;
import static daradurvs.ru.ignite.compression.model.DataGenerator.PERSON_CSV;

public class SizeBenchmark {
    private static final ClassLoader CLASS_LOADER = SizeBenchmark.class.getClassLoader();

    public static void main(String[] args) throws Exception {
        List<String> cfgs = new ArrayList<>();
        cfgs.add("cache-config-deflater.xml");
        cfgs.add("cache-config-gzip.xml");
        cfgs.add("cache-config-lz4.xml");
        cfgs.add("cache-config-snappy.xml");
        cfgs.add("cache-config-xz.xml");
        cfgs.add("cache-config-lzma.xml");

        testAudints(cfgs, AUDIT_CSV, "audits", -1);
        testAudints(cfgs, AUDIT_CSV, "audits_0", 0);
        testAudints(cfgs, AUDIT_CSV, "audits_100", 100);
        testAudints(cfgs, AUDIT_CSV, "audits_500", 500);
        testAudints(cfgs, AUDIT_CSV, "audits_1000", 1000);
        testAudints(cfgs, AUDIT_CSV, "audits_2000", 2000);
        testAudints(cfgs, AUDIT_CSV, "audits_3000", 3000);
        testAudints(cfgs, AUDIT_CSV, "audits_4000", 4000);

        testAudints(cfgs, AUDIT2_CSV, "audits2", -1);

        final List<Identifiable> persons = ModelFactory.create(Person.class, PERSON_CSV);
        final List<Identifiable> persons2F = ModelFactory.create(Person2F.class, PERSON_CSV);
        View personView = test("cache-config-deflater.xml", persons);
        View person2FView = test("cache-config-deflater.xml", persons2F);
        ResultWriter.write("persons", Arrays.asList(personView, person2FView));
    }

    private static void testAudints(List<String> cfgs, String filePath, String resultName, int len) throws Exception {
        final List<Identifiable> audits = ModelFactory.create(Audit.class, filePath, len);
        final List<Identifiable> audits1F = ModelFactory.create(Audit1F.class, filePath, len);

        List<View> views = new ArrayList<>();

        views.add(test("cache-config-deflater.xml", audits));

        for (String cfg : cfgs)
            views.add(test(cfg, audits1F));

        ResultWriter.write(resultName, views);
    }

    private static View test(String cfg, List<? extends Identifiable> entries) throws IgniteCheckedException {
        try (Ignite ignite = Ignition.start(CLASS_LOADER.getResourceAsStream(cfg))) {
            IgniteConfiguration iCfg = ignite.configuration();
            IgniteCache<Long, Identifiable> cache = ignite.getOrCreateCache(cfg);
            Marshaller marsh = iCfg.getMarshaller();

            String name = entries.get(0).getClass().getName();
            String compression = iCfg.getCompressor().getClass().getSimpleName();

            View view = new View(name, compression);

            for (Identifiable entry : entries) {
                long id = entry.getId();

                cache.put(id, entry);

                int len = marsh.marshal(entry).length;

                view.put(id, len);
            }

            for (Identifiable entry : entries)
                assert entry.equals(cache.get(entry.getId()));

            return view;
        }
    }
}
