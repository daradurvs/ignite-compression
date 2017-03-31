package daradruvs.ru.ignite.compression;

import daradruvs.ru.ignite.compression.model.Audit;
import daradruvs.ru.ignite.compression.model.Identifiable;
import daradruvs.ru.ignite.compression.model.ModelFactory;
import daradruvs.ru.ignite.compression.model.Person;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.marshaller.Marshaller;

import static daradruvs.ru.ignite.compression.model.DataGenerator.AUDIT2_CSV;
import static daradruvs.ru.ignite.compression.model.DataGenerator.AUDIT_CSV;
import static daradruvs.ru.ignite.compression.model.DataGenerator.PERSON_CSV;

public class Benchmark {
    private static final ClassLoader CLASS_LOADER = Benchmark.class.getClassLoader();
    private static final String RES_RESULTS_DIR = "src/main/resources/result/";

    public static void main(String[] args) throws Exception {
        final List<Audit> audits = ModelFactory.createAudits(AUDIT_CSV);
        final List<Audit> audits2 = ModelFactory.createAudits(AUDIT2_CSV);
        final List<Person> people = ModelFactory.createPersons(PERSON_CSV);

        try (Ignite ignite = Ignition.start(CLASS_LOADER.getResourceAsStream("cache-full-comp-off.xml"));
             Ignite cIgnite = Ignition.start(CLASS_LOADER.getResourceAsStream("cache-full-comp-on.xml"));) {

            Marshaller marsh = ignite.configuration().getMarshaller();

            Marshaller cMarsh = cIgnite.configuration().getMarshaller();

            test(audits, marsh, cMarsh, "audit_result.md");
            test(audits2, marsh, cMarsh, "audit2_result.md");
            test(people, marsh, cMarsh, "person_result.md");
        }
    }

    private static void test(List<? extends Identifiable> data, Marshaller marsh, Marshaller cMarsh,
        String fileName) throws Exception {

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(RES_RESULTS_DIR + fileName), StandardCharsets.UTF_8)) {

            writer.write("| *id* | *bytes length* | *compressed bytes length* | *compression ratio* |");
            writer.newLine();
            writer.write("--- | :---: | :---: | :---:");
            writer.newLine();

            for (Identifiable o : data) {
                int len = marsh.marshal(o).length;

                int cLen = cMarsh.marshal(o).length;

                writer.write(String.format("| %s_id=%s | %d | %d | %f |", o.getClass().getSimpleName(), o.getId(), len, cLen, ((double)len / cLen)));
                writer.newLine();
            }
        }
    }

}
