package utm.pam.db;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root
public class Storage {
    @ElementList
    private List<CalendarItem> items;

    public Storage() {
        this.items = new ArrayList<>();
    }

    public List<CalendarItem> getItems() {
        return items;
    }

    public void setItems(List<CalendarItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "items=" + items +
                '}';
    }
}
