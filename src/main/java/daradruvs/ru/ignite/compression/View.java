package daradruvs.ru.ignite.compression;

import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class View {
    private String name;
    private String compression;
    private Map<Long, Integer> map;
    private BigInteger total;

    public View(String name, String compression) {
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
}
