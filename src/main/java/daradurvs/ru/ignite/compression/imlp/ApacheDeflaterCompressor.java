package daradurvs.ru.ignite.compression.imlp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.internal.binary.compression.Compressor;
import org.jetbrains.annotations.NotNull;

public class ApacheDeflaterCompressor implements Compressor {
    private static final int BUFFER_SIZE = 256;

    @Override public byte[] compress(@NotNull byte[] bytes) throws BinaryObjectException {

        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DeflateCompressorOutputStream defOut = new DeflateCompressorOutputStream(baos)) {

            final byte[] buffer = new byte[BUFFER_SIZE];
            int n;

            while ((n = bais.read(buffer)) != -1)
                defOut.write(buffer, 0, n);

            defOut.close(); // Need it, otherwise EOFException at decompressing

            return baos.toByteArray();
        }
        catch (IOException e) {
            throw new BinaryObjectException("Failed to compress bytes", e);
        }
    }

    @Override public byte[] decompress(@NotNull byte[] bytes) throws BinaryObjectException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             DeflateCompressorInputStream defIn = new DeflateCompressorInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            final byte[] buffer = new byte[BUFFER_SIZE];
            int n;

            while ((n = defIn.read(buffer)) != -1)
                baos.write(buffer, 0, n);

            return baos.toByteArray();
        }
        catch (IOException e) {
            throw new BinaryObjectException("Failed to decompress bytes", e);
        }
    }
}
