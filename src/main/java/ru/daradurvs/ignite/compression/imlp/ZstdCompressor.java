package ru.daradurvs.ignite.compression.imlp;

import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdDictCompress;
import com.github.luben.zstd.ZstdDirectBufferCompressingStream;
import java.io.IOException;
import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.internal.binary.compression.Compressor;
import org.jetbrains.annotations.NotNull;
import org.xerial.snappy.Snappy;

public class ZstdCompressor implements Compressor {
    @Override public byte[] compress(@NotNull byte[] bytes) throws BinaryObjectException {
        try {
            return Zstd.compress(bytes);
        }
        catch (Exception e) {
            throw new BinaryObjectException("Failed to compress bytes", e);
        }
    }

    @Override public byte[] decompress(@NotNull byte[] bytes) throws BinaryObjectException {
        try {
            return Zstd.decompress(bytes, (int)Zstd.decompressedSize(bytes));
        }
        catch (Exception e) {
            throw new BinaryObjectException("Failed to decompress bytes", e);
        }
    }
}
