package daradurvs.ru.ignite.compression.model;

import org.apache.ignite.internal.binary.compression.BinaryCompression;

public class OneStringAnnotatedFieldClass {
    @BinaryCompression
    private String data;

    public OneStringAnnotatedFieldClass(String data) {
        this.data = data;
    }
}
