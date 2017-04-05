package daradurvs.ru.ignite.compression.imlp;

import java.util.Arrays;
import java.util.Collection;
import org.apache.ignite.internal.binary.compression.Compressor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CompressorImplTest {
    private static final String LINE = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private Compressor compressor;

    public CompressorImplTest(Compressor compressor) {
        this.compressor = compressor;
    }

    @Parameterized.Parameters
    public static Collection<Compressor> instancesToTest() {
        return Arrays.asList(
            new SnappyCompressor(),
            new XZCompressor(),
            new LZMACompressor(),
            new LZ4Compressor()
        );
    }

    @Test
    public void compressDecompressByteArray() throws Exception {
        byte[] bytes = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 10, 11, 12, 13, 14, 15};
        byte[] compressed = compressor.compress(bytes);
        byte[] decompressed = compressor.decompress(compressed);

        assertArrayEquals(bytes, decompressed);
    }

    @Test
    public void compressDecompressString() throws Exception {
        byte[] compressed = compressor.compress(LINE.getBytes());
        byte[] decompressed = compressor.decompress(compressed);

        assertEquals(LINE, new String(decompressed));
    }
}