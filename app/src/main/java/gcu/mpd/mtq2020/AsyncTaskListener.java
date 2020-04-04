package gcu.mpd.mtq2020;

import java.util.ArrayList;

public interface AsyncTaskListener {

    public void newEvents(ArrayList<Event> events);
}
