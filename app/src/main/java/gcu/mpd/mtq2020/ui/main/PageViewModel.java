package gcu.mpd.mtq2020.ui.main;

import android.app.Application;

import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import gcu.mpd.mtq2020.Event;
import gcu.mpd.mtq2020.Repository;

public class PageViewModel extends AndroidViewModel {
    private Repository repo = Repository.getInstance();
    private LiveData<ArrayList<Event>> events;

    public PageViewModel(Application application) {
        super(application);
        events = repo.getEvents();
    }

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<ArrayList<Event>> getEvents() {
        return events;
    }
}