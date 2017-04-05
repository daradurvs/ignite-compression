package daradurvs.ru.ignite.compression;

import daradurvs.ru.ignite.compression.model.Audit;
import daradurvs.ru.ignite.compression.model.Identifiable;
import daradurvs.ru.ignite.compression.model.ModelFactory;
import daradurvs.ru.ignite.compression.model.Person;
import java.util.ArrayList;
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

public class Benchmark {
    private static final ClassLoader CLASS_LOADER = Benchmark.class.getClassLoader();

    public static void main(String[] args) throws Exception {
        final List<Audit> audits = ModelFactory.createAudits(AUDIT_CSV);
        final List<Audit> audits2 = ModelFactory.createAudits(AUDIT2_CSV);
        final List<Person> people = ModelFactory.createPersons(PERSON_CSV);

        List<String> cfgs = new ArrayList<>();
        cfgs.add("cache-full-comp-off.xml");
        cfgs.add("cache-full-comp-on.xml");
        cfgs.add("cache-full-comp-on-deflater.xml");
        cfgs.add("cache-full-comp-on-snappy.xml");
        cfgs.add("cache-full-comp-on-xz.xml");
        cfgs.add("cache-full-comp-on-lzma.xml");
        cfgs.add("cache-full-comp-on-lz4.xml");

        List<ViewsSuite> suites = new ArrayList<>();
        suites.add(new ViewsSuite("audit_result", audits));
        suites.add(new ViewsSuite("audit2_result", audits2));
        suites.add(new ViewsSuite("person_result", people));

        test(cfgs, suites);

        ResultWriter.write(suites);
    }

    private static List<ViewsSuite> test(List<String> cfgs,
        List<ViewsSuite> suites) throws IgniteCheckedException {
        for (String cfg : cfgs) {

            try (Ignite ignite = Ignition.start(CLASS_LOADER.getResourceAsStream(cfg))) {
                IgniteConfiguration iCfg = ignite.configuration();
                IgniteCache<Long, Identifiable> cache = ignite.getOrCreateCache(cfg);
                Marshaller marsh = iCfg.getMarshaller();

                for (ViewsSuite container : suites) {
                    List<? extends Identifiable> entries = container.getEntries();

                    String name = entries.get(0).getClass().getSimpleName();
                    String compression = getCompressionName(iCfg);

                    View view = new View(name, compression);

                    for (Identifiable entry : entries) {
                        long id = entry.getId();

                        cache.put(id, entry);

                        int len = marsh.marshal(entry).length;

                        view.put(id, len);
                    }

                    for (Identifiable entry : entries)
                        assert entry.equals(cache.get(entry.getId()));

                    container.addView(view);
                }
            }
        }

        return suites;
    }

    private static String getCompressionName(IgniteConfiguration iCfg) {
        return iCfg.isFullCompressionMode() ? iCfg.getCompressor().getClass().getSimpleName() : "NONE";
    }
}
