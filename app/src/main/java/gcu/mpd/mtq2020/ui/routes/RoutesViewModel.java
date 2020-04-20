package gcu.mpd.mtq2020.ui.routes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;

import gcu.mpd.mtq2020.Event;
import gcu.mpd.mtq2020.Repository;

/**
 * @author Mark Quinn S1510840
 */
public class RoutesViewModel extends AndroidViewModel {
    private Repository repo = Repository.getInstance();
    private LiveData<ArrayList<Event>> events;

    public RoutesViewModel(Application application) {
        super(application);
        events = repo.getAllEvents();
    }

    public LiveData<ArrayList<Event>> getEvents() {
        return events;
    }
}