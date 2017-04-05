package daradurvs.ru.ignite.compression;

import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class View {
    private String name;
    private String compression;
    private Map<Long, Integer> map;
    private BigInteger total;

    public View(@NotNull String name, @NotNull String compression) {
        this.name = name;
        this.compression = compression;
        this.map = new LinkedHashMap<>();
        this.total = BigInteger.ZERO;
    }

    public void put(Long id, int size) {
        assert !map.containsKey(id);

        map.put(id, size);

        total = total.add(BigInteger.valueOf(size));
    }

    public String getName() {
        return name;
    }

    public String getCompression() {
        return compression;
    }

    public int getLength(long id) {
        return map.get(id);
    }

    public BigInteger getTotal() {
        return total;
    }

    public int getSize() {
        return map.size();
    }

    public Map<Long, Integer> getMap() {
        return Collections.unmodifiableMap(map);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        View view = (View)o;

        if (!name.equals(view.name))
            return false;
        return compression.equals(view.compression);
    }

    @Override public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + compression.hashCode();
        return result;
    }

    @Override public String toString() {
        return "View{" +
            "name='" + name + '\'' +
            ", compression='" + compression + '\'' +
            '}';
    }
}
