package daradurvs.ru.ignite.compression.imlp;

import java.io.IOException;
import org.apache.ignite.internal.binary.compression.Compressor;
import org.jetbrains.annotations.NotNull;
import org.xerial.snappy.Snappy;

public class SnappyCompressor implements Compressor {
    @Override public byte[] compress(@NotNull byte[] bytes) throws IOException {
        return Snappy.compress(bytes);
    }

    @Override public byte[] decompress(@NotNull byte[] bytes) throws IOException {
        return Snappy.uncompress(bytes);
    }
}
