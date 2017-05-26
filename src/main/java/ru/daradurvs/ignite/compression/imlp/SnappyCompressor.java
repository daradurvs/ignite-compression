package ru.daradurvs.ignite.compression.imlp;

import java.io.IOException;
import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.internal.binary.compression.Compressor;
import org.jetbrains.annotations.NotNull;
import org.xerial.snappy.Snappy;

public class SnappyCompressor implements Compressor {
    @Override public byte[] compress(@NotNull byte[] bytes) throws BinaryObjectException {
        try {
            return Snappy.compress(bytes);
        }
        catch (IOException e) {
            throw new BinaryObjectException("Failed to compress bytes", e);
        }
    }

    @Override public byte[] decompress(@NotNull byte[] bytes) throws BinaryObjectException {
        try {
            return Snappy.uncompress(bytes);
        }
        catch (IOException e) {
            throw new BinaryObjectException("Failed to decompress bytes", e);
        }
    }
}
