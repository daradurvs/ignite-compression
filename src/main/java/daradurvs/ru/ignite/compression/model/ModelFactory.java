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
    public static List<Identifiable> create(Class cls, String fileName) throws IOException {
        return create(cls, fileName, -1);
    }

    public static List<Identifiable> create(Class cls, String fileName, int len) throws IOException {
        List<Identifiable> list = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(RES_MODEL_DIR + fileName), StandardCharsets.UTF_8);

        for (String line : lines) {
            String[] data = line.split(";");

            if (cls == Audit.class)
                list.add(
                    new Audit(
                        Long.valueOf(data[0]),
                        UUID.fromString(data[1]),
                        cut(data[2], len)
                    ));
            else if (cls == Audit1F.class)
                list.add(
                    new Audit1F(
                        Long.valueOf(data[0]),
                        UUID.fromString(data[1]),
                        cut(data[2], len)
                    ));
            else if (cls == Person.class)
                list.add(
                    new Person(
                        Long.valueOf(data[0]),
                        Integer.valueOf(data[1]),
                        Boolean.valueOf(data[2]),
                        cut(data[3], len),
                        cut(data[4], len)
                    ));
            else if (cls == Person2F.class)
                list.add(
                    new Person2F(
                        Long.valueOf(data[0]),
                        Integer.valueOf(data[1]),
                        Boolean.valueOf(data[2]),
                        cut(data[3], len),
                        cut(data[4], len)
                    ));
            else
                throw new IllegalArgumentException("Unsupported class: " + cls);
        }

        return list;
    }

    private static String cut(String line, int len) {
        return (len < 0 || len > line.length()) ? line : line.substring(0, len);
    }
}
