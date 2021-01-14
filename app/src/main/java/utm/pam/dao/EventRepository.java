package utm.pam.dao;

import java.util.List;

import utm.pam.model.Event;

public interface EventRepository {

    void add(final Event event);

    void updateEvent(final String id, final String date, final String title, String description);

    void delete(final String id);

    void deleteByDate(final String date);

    List<Event> getAll();

    List<Event> getByDate(final String date);

    Event getById(final String id);

    void commit();
}
