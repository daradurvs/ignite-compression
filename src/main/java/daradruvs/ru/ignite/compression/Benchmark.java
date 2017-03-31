package daradruvs.ru.ignite.compression;

import daradruvs.ru.ignite.compression.model.Audit;
import daradruvs.ru.ignite.compression.model.Identifiable;
import daradruvs.ru.ignite.compression.model.ModelFactory;
import daradruvs.ru.ignite.compression.model.Person;
import java.io.BufferedWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

            String cName = cIgnite.configuration().getCompressor().getClass().getSimpleName();

            test(audits, marsh, cMarsh, "audit_result.md", cName);
            test(audits2, marsh, cMarsh, "audit2_result.md", cName);
            test(people, marsh, cMarsh, "person_result.md", cName);
        }
    }

    private static void test(List<? extends Identifiable> data, Marshaller marsh, Marshaller cMarsh,
        String fileName, String cName) throws Exception {

        BigInteger totalSize = BigInteger.ZERO;
        BigInteger cTotalSize = BigInteger.ZERO;

        List<String> lines = new ArrayList<>();

        for (Identifiable o : data) {
            int len = marsh.marshal(o).length;
            totalSize = totalSize.add(BigInteger.valueOf(len));

            int cLen = cMarsh.marshal(o).length;
            cTotalSize = cTotalSize.add(BigInteger.valueOf(cLen));

            lines.add(String.format("| %s_id=%s | %d | %d | %f |", o.getClass().getSimpleName(), o.getId(), len, cLen, ((double)len / cLen)));
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(RES_RESULTS_DIR + fileName), StandardCharsets.UTF_8)) {

            writer.write("| *compressor* | *num* | *total bytes* | *total compressed bytes* | *avg compression ratio* |");
            writer.newLine();
            writer.write("--- | :---: | :---: | :---: | :---:");
            writer.newLine();
            writer.write(String.format("| %s | %d | %d | %d | %f |", cName, data.size(), totalSize, cTotalSize, (totalSize.doubleValue() / cTotalSize.doubleValue())));
            writer.newLine();

            writer.newLine(); // new table

            writer.write("| *id* | *bytes length* | *compressed bytes length* | *compression ratio* |");
            writer.newLine();
            writer.write("--- | :---: | :---: | :---:");
            writer.newLine();

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
