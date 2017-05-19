package daradurvs.ru.ignite.compression.imlp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.internal.binary.compression.Compressor;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link Compressor} which use GZIP compression library for compressing data.
 *
 * @see GZIPInputStream
 * @see GZIPOutputStream
 */
public class GZipCompressor implements Compressor {
    /**
     * {@inheritDoc}
     *
     * @see GZIPOutputStream
     */
    @Override public byte[] compress(@NotNull byte[] bytes) throws BinaryObjectException {
        try (
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream out = new GZIPOutputStream(baos);
        ) {
            out.write(bytes);
            out.close();// need it, otherwise EOFException at decompressing
            return baos.toByteArray();
        }
        catch (IOException e) {
            throw new BinaryObjectException("Failed to compress bytes", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see GZIPInputStream
     */
    @Override public byte[] decompress(@NotNull byte[] bytes) throws BinaryObjectException {
        try (
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream in = new GZIPInputStream(new ByteArrayInputStream(bytes));
        ) {
            byte[] buffer = new byte[32];
            int length;
            while ((length = in.read(buffer)) != -1)
                baos.write(buffer, 0, length);
            baos.flush();
            return baos.toByteArray();
        }
        catch (IOException e) {
            throw new BinaryObjectException("Failed to decompress bytes", e);
        }
    }
}
