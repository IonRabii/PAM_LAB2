package utm.pam.dao.impl;

import java.util.List;
import java.util.stream.Collectors;

import utm.pam.dao.DatabaseManager;
import utm.pam.dao.EventRepository;
import utm.pam.exceptions.NotFoundException;
import utm.pam.model.Event;

import static java.lang.String.format;

public class EventRepositoryImpl implements EventRepository {
    private final DatabaseManager databaseManager;
    private final List<Event> events;

    public EventRepositoryImpl(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.events = databaseManager.load();
    }

    @Override
    public void add(final Event event) {
        events.add(event);
        commit();
    }

    @Override
    public void updateEvent(final String id, final String date, final String title, final String description) {
        final Event event = events.stream().filter(e -> id.equals(e.getId()))
                .findFirst().orElseThrow(() -> new NotFoundException(format("Event with id %s not found", id)));
        event.setDate(date);
        event.setTitle(title);
        event.setDescription(description);
        commit();
    }

    @Override
    public void delete(final String id) {
        List<Event> events = this.events.stream().filter(event -> !id.equals(event.getId())).collect(Collectors.toList());
        this.events.removeAll(events);
        commit();
    }

    @Override
    public List<Event> getAll() {
        return events;
    }

    @Override
    public List<Event> getByDate(final String date) {
        return events.stream().filter(event -> date.equals(event.getDate())).collect(Collectors.toList());
    }

    @Override
    public Event getById(final String id) {
        return events.stream().filter(e -> id.equals(e.getId()))
                .findFirst().orElseThrow(() -> new NotFoundException(format("Event with id %s not found", id)));
    }

    @Override
    public void commit() {
        databaseManager.commit(this.events);
    }
}
