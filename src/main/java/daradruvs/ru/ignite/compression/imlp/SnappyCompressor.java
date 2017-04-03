package daradruvs.ru.ignite.compression.imlp;

import java.io.IOException;
import org.apache.ignite.internal.binary.compression.Compressor;
import org.xerial.snappy.Snappy;

public class SnappyCompressor implements Compressor {
    @Override public byte[] compress(byte[] bytes) throws IOException {
        return Snappy.compress(bytes);
    }

    @Override public byte[] decompress(byte[] bytes) throws IOException {
        return Snappy.uncompress(bytes);
    }
}
