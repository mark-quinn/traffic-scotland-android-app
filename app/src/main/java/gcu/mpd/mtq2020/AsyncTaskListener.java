package gcu.mpd.mtq2020;

import java.util.ArrayList;

public interface AsyncTaskListener {
    void newEvents(ArrayList<Event> events);
}
