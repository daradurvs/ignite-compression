package daradurvs.ru.ignite.compression.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static daradurvs.ru.ignite.compression.model.DataGenerator.RES_MODEL_DIR;

public class ModelFactory {
    public static List<Person> createPersons(String fileName) throws IOException {
        List<Person> list = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(RES_MODEL_DIR + fileName), StandardCharsets.UTF_8);

        for (String line : lines) {
            String[] data = line.split(";");

            assert data.length == 5;

            list.add(
                new Person(
                    Long.valueOf(data[0]),
                    Integer.valueOf(data[1]),
                    Boolean.valueOf(data[2]),
                    data[3],
                    data[4]
                ));
        }

        return list;
    }

    public static List<Audit> createAudits(String fileName) throws IOException {
        List<Audit> list = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(RES_MODEL_DIR + fileName), StandardCharsets.UTF_8);

        for (String line : lines) {
            String[] data = line.split(";");

            assert data.length == 3;

            list.add(
                new Audit(
                    Long.valueOf(data[0]),
                    UUID.fromString(data[1]),
                    data[2]));
        }

        return list;
    }
}
