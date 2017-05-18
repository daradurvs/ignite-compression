package daradurvs.ru.ignite.compression.imlp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorOutputStream;
import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.internal.binary.compression.Compressor;
import org.jetbrains.annotations.NotNull;

public class LZMACompressor implements Compressor {
    private static final int BUFFER_SIZE = 100;

    @Override public byte[] compress(@NotNull byte[] bytes) throws BinaryObjectException {

        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             LZMACompressorOutputStream lzmaOut = new LZMACompressorOutputStream(baos)) {

            final byte[] buffer = new byte[BUFFER_SIZE];
            int n;

            while ((n = bais.read(buffer)) != -1)
                lzmaOut.write(buffer, 0, n);

            lzmaOut.close(); // Need it, otherwise EOFException at decompressing

            return baos.toByteArray();
        }
        catch (IOException e) {
            throw new BinaryObjectException("Failed to compress bytes", e);
        }
    }

    @Override public byte[] decompress(@NotNull byte[] bytes) throws BinaryObjectException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             LZMACompressorInputStream lzmaIn = new LZMACompressorInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            final byte[] buffer = new byte[BUFFER_SIZE];
            int n;

            while ((n = lzmaIn.read(buffer)) != -1)
                baos.write(buffer, 0, n);

            return baos.toByteArray();
        }
        catch (IOException e) {
            throw new BinaryObjectException("Failed to decompress bytes", e);
        }
    }
}
