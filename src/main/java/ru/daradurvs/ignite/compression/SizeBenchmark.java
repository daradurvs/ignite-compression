package ru.daradurvs.ignite.compression;

import ru.daradurvs.ignite.compression.model.Audit;
import ru.daradurvs.ignite.compression.model.Audit1F;
import ru.daradurvs.ignite.compression.model.Identifiable;
import ru.daradurvs.ignite.compression.model.ModelFactory;
import ru.daradurvs.ignite.compression.model.Person;
import ru.daradurvs.ignite.compression.model.Person2F;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.marshaller.Marshaller;

import static ru.daradurvs.ignite.compression.model.DataGenerator.AUDIT2_CSV;
import static ru.daradurvs.ignite.compression.model.DataGenerator.AUDIT_CSV;
import static ru.daradurvs.ignite.compression.model.DataGenerator.PERSON_CSV;

public class SizeBenchmark {
    private static final ClassLoader CLASS_LOADER = SizeBenchmark.class.getClassLoader();

    public static void main(String[] args) throws Exception {
        List<String> cfgs = new ArrayList<>();
//        cfgs.add("cache-config-deflater.xml");
        cfgs.add("cache-config-apache-deflater.xml");
        cfgs.add("cache-config-gzip.xml");
        cfgs.add("cache-config-lz4.xml");
        cfgs.add("cache-config-snappy.xml");
        cfgs.add("cache-config-xz.xml");
        cfgs.add("cache-config-lzma.xml");

        testAudits(cfgs, AUDIT_CSV, "audits_symbols_100", 100);
        testAudits(cfgs, AUDIT_CSV, "audits_symbols_500", 500);
        testAudits(cfgs, AUDIT_CSV, "audits_symbols_1000", 1000);
        testAudits(cfgs, AUDIT_CSV, "audits_symbols_2000", 2000);
        testAudits(cfgs, AUDIT_CSV, "audits_symbols_3000", 3000);
        testAudits(cfgs, AUDIT_CSV, "audits_symbols_4000", 4000);
        testAudits(cfgs, AUDIT_CSV, "audits_symbols_5000", 5000);
        testAudits(cfgs, AUDIT_CSV, "audits_symbols_6000", 6000);

        testAudits(cfgs, AUDIT2_CSV, "audits_text_100", 100);
        testAudits(cfgs, AUDIT2_CSV, "audits_text_500", 500);
        testAudits(cfgs, AUDIT2_CSV, "audits_text_1000", 1000);
        testAudits(cfgs, AUDIT2_CSV, "audits_text_2000", 2000);
        testAudits(cfgs, AUDIT2_CSV, "audits_text_3000", 3000);
        testAudits(cfgs, AUDIT2_CSV, "audits_text_4000", 4000);
        testAudits(cfgs, AUDIT2_CSV, "audits_text_5000", 5000);
        testAudits(cfgs, AUDIT2_CSV, "audits_text_6000", 6000);

        final List<Identifiable> persons = ModelFactory.create(Person.class, PERSON_CSV);
        final List<Identifiable> persons2F = ModelFactory.create(Person2F.class, PERSON_CSV);
        View personView = test("cache-config-deflater.xml", persons);
        View person2FView = test("cache-config-deflater.xml", persons2F);
        ResultWriter.write("persons", Arrays.asList(personView, person2FView));
    }

    private static void testAudits(List<String> cfgs, String filePath, String resultName, int len) throws Exception {
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
