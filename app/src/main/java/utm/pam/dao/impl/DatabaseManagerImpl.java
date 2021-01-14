package utm.pam.dao.impl;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utm.pam.dao.DatabaseManager;
import utm.pam.model.Database;
import utm.pam.model.Event;

import static java.util.Arrays.asList;

public class DatabaseManagerImpl implements DatabaseManager {
    private final String DEFAULT_URL = "calendar_database.xml";
    private final String sourceFile;
    private final File path;
    private final Serializer serializer;

    public DatabaseManagerImpl(File path) {
        this.path = path;
        this.sourceFile = DEFAULT_URL;
        this.serializer = new Persister();
    }

    public DatabaseManagerImpl(String sourceFile, File path) {
        this.sourceFile = sourceFile;
        this.path = path;
        this.serializer = new Persister();
    }

    @Override
    public List<Event> load() {
        try {
            Database database = serializer.read(Database.class, new File(path, sourceFile));
            return database.getEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Event>();
    }

    @Override
    public void commit(List<Event> events) {
        try {
            final Database database = serializer.read(Database.class, new File(path, sourceFile));
            database.setEvents(events);
            serializer.write(database, new File(path, sourceFile));
        } catch (FileNotFoundException e) {
            final Database database = new Database(events);
            try {
                serializer.write(database, new File(path, sourceFile));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean createFile() {
        File file = new File(path, sourceFile);
        try {
            return file.createNewFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }
    }
}