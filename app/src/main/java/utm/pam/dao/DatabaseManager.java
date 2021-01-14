package utm.pam.dao;

import java.util.List;

import utm.pam.model.Event;

public interface DatabaseManager {

    List<Event> load();

    void commit(final List<Event> events);
}
