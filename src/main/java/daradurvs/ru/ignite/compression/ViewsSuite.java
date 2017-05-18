package daradurvs.ru.ignite.compression;

import daradurvs.ru.ignite.compression.model.Identifiable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewsSuite {
    private List<? extends Identifiable> entries;
    private List<View> views;
    private String name;

    public ViewsSuite(String name, List<? extends Identifiable> entries) {
        this.name = name;
        this.entries = entries;
        this.views = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addView(View view) {
        assert !views.contains(view);

        if (!views.isEmpty())
            assert views.get(0).getName().equals(view.getName());

        views.add(view);
    }

    public List<View> getViews() {
        return Collections.unmodifiableList(views);
    }

    public List<? extends Identifiable> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}
