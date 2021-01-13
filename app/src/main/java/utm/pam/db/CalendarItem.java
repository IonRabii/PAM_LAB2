package utm.pam.db;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Default
public class CalendarItem {
    //    @Element
    private String date;
    //    @Element
    private String title;
    //    @Element
    private String description;

    public CalendarItem() {
    }

    public CalendarItem(String date, String title, String description) {
        this.date = date;
        this.title = title;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CalendarItem{" +
                "date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
