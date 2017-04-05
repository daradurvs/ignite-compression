package daradurvs.ru.ignite.compression.imlp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import org.apache.ignite.internal.binary.compression.Compressor;
import org.jetbrains.annotations.NotNull;

public class LZ4Compressor implements Compressor {
    private static final int BUFFER_SIZE = 100;

    @Override public byte[] compress(@NotNull byte[] bytes) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             LZ4BlockOutputStream lz4Out = new LZ4BlockOutputStream(baos)) {

            final byte[] buffer = new byte[BUFFER_SIZE];
            int n;

            while ((n = bais.read(buffer)) != -1)
                lz4Out.write(buffer, 0, n);

            lz4Out.close(); // Need it, otherwise EOFException at decompressing

            return baos.toByteArray();
        }
    }

    @Override public byte[] decompress(@NotNull byte[] bytes) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             LZ4BlockInputStream lz4In = new LZ4BlockInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            final byte[] buffer = new byte[BUFFER_SIZE];
            int n;

            while ((n = lz4In.read(buffer)) != -1)
                baos.write(buffer, 0, n);

            return baos.toByteArray();
        }
    }
}
