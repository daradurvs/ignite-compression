package daradruvs.ru.ignite.compression;

import daradruvs.ru.ignite.compression.model.Audit;
import daradruvs.ru.ignite.compression.model.Identifiable;
import daradruvs.ru.ignite.compression.model.ModelFactory;
import daradruvs.ru.ignite.compression.model.Person;
import java.util.ArrayList;
import java.util.List;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.marshaller.Marshaller;

import static daradruvs.ru.ignite.compression.model.DataGenerator.AUDIT2_CSV;
import static daradruvs.ru.ignite.compression.model.DataGenerator.AUDIT_CSV;
import static daradruvs.ru.ignite.compression.model.DataGenerator.PERSON_CSV;

public class Benchmark {
    private static final ClassLoader CLASS_LOADER = Benchmark.class.getClassLoader();

    public static void main(String[] args) throws Exception {
        final List<Audit> audits = ModelFactory.createAudits(AUDIT_CSV);
        final List<Audit> audits2 = ModelFactory.createAudits(AUDIT2_CSV);
        final List<Person> people = ModelFactory.createPersons(PERSON_CSV);

        List<String> cfgNames = new ArrayList<>();
        cfgNames.add("cache-full-comp-off.xml");
        cfgNames.add("cache-full-comp-on.xml");
        cfgNames.add("cache-full-comp-on-deflater.xml");

        test(cfgNames, audits, "audit_result");
        test(cfgNames, audits2, "audit2_result");
        test(cfgNames, people, "person_result");
    }

    private static void test(List<String> cfgs, List<? extends Identifiable> enties,
        String resultName) throws Exception {
        List<View> views = new ArrayList<>();

        for (String cfg : cfgs)
            views.add(test(cfg, enties));

        ResultWriter.write(views, resultName);
    }

    private static View test(String cfgName, List<? extends Identifiable> entries) throws Exception {
        try (Ignite ignite = Ignition.start(CLASS_LOADER.getResourceAsStream(cfgName))) {
            IgniteConfiguration iCfg = ignite.configuration();
            IgniteCache<Long, Identifiable> cache = ignite.getOrCreateCache(cfgName);
            Marshaller marsh = iCfg.getMarshaller();

            String name = entries.get(0).getClass().getSimpleName();
            String compression = iCfg.isFullCompressionMode() ? iCfg.getCompressor().getClass().getSimpleName() : "NONE";

            View view = new View(name, compression);

            for (Identifiable entry : entries) {
                long id = entry.getId();

                cache.put(id, entry);

                int len = marsh.marshal(entry).length;

                view.put(id, len);
            }

            for (Identifiable o : entries)
                assert o.equals(cache.get(o.getId()));

            return view;
        }
    }
}
