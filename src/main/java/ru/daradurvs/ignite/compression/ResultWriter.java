package ru.daradurvs.ignite.compression;

import java.io.BufferedWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultWriter {
    private static final String RES_RESULTS_DIR = "src/main/resources/result/";

    public static void write(List<ViewsSuite> suites) throws Exception {
        for (ViewsSuite suite : suites)
            write(suite.getName(), suite.getViews());
    }

    public static void write(String fileName, List<View> views) throws Exception {
        List<String> lines = prepare(views);

        writeLines(lines, fileName + ".MD");
    }

    private static List<String> prepare(List<View> views) {
        for (int i = 0; i < views.size() - 1; i++) {
            assert views.get(i).getSize() == views.get(i + 1).getSize();

            assert views.get(i).getName().equals(views.get(i + 1).getName());
        }

        View raw = views.get(0);
        String name = detachLastWord(raw.getName(), ".");
        BigInteger total = raw.getTotal();

        List<String> lines = new ArrayList<>();

        lines.add(String.format("| *Entries(n=%d)* | *%s* |", raw.getSize(), name));
        lines.add("| --- | :---: | ");
        lines.add(String.format("| =>> Compressor impl =>> | %s | ", raw.getCompression()));
        lines.add(String.format("| =>> total, byte(avg c.r.)=>> | %d | ", total));
        lines.add("| **id** | **length, byte** |");

        boolean updHeader = true;

        for (Map.Entry<Long, Integer> entry : raw.getMap().entrySet()) {
            long id = entry.getKey();
            int len = entry.getValue();

            String line = String.format("| %s_id=%d | %d |", name, id, len);

            for (int i = 1; i < views.size(); i++) {
                View view = views.get(i);
                BigInteger vTotal = view.getTotal();

                if (updHeader) {
                    lines.set(0, lines.get(0) + " *" + detachLastWord(view.getName(), ".") + "* |");
                    lines.set(1, lines.get(1) + " :---: |");
                    lines.set(2, String.format("%s %s |", lines.get(2), view.getCompression()));
                    lines.set(3, String.format("%s %d(%.3f) |", lines.get(3), vTotal, total.doubleValue() / vTotal.doubleValue()));
                    lines.set(4, lines.get(4) + " **length, byte(c.r.)** |");
                }

                long vLen = view.getLength(id);

                line += String.format(" %d(%.3f) |", vLen, (double)len / vLen);
            }

            updHeader = false;

            lines.add(line);
        }

        return lines;
    }

    private static String detachLastWord(String line, String delimiter) {
        return line.substring(line.lastIndexOf(delimiter) + 1);
    }

    private static void writeLines(List<String> lines, String fileName) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(RES_RESULTS_DIR + fileName), StandardCharsets.UTF_8)) {

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
