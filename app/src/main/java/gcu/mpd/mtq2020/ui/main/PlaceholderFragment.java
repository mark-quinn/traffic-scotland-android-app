package gcu.mpd.mtq2020.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.Objects;

import gcu.mpd.mtq2020.Event;
import gcu.mpd.mtq2020.FeedAdapter;
import gcu.mpd.mtq2020.R;

/**
 * @author Mark Quinn S1510840
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private ListView listEvents;


    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_results, container, false);
        listEvents = root.findViewById(R.id.eventListView);

        switch (pageViewModel.getIndex()) {
            case 1:
                pageViewModel.getCurrentEvents().observe(getViewLifecycleOwner(), new Observer<ArrayList<Event>>() {
                    @Override
                    public void onChanged(ArrayList<Event> events) {
                        //TODO: filter events
                        FeedAdapter feedAdapter = new FeedAdapter(
                                Objects.requireNonNull(getContext()), R.layout.traffic_event, events);
                        listEvents.setAdapter(feedAdapter);
                    }
                });
                break;
            case 2:
                pageViewModel.getOnGoingRoadworks().observe(getViewLifecycleOwner(), new Observer<ArrayList<Event>>() {
                    @Override
                    public void onChanged(ArrayList<Event> events) {
                        FeedAdapter feedAdapter = new FeedAdapter(
                                Objects.requireNonNull(getContext()), R.layout.traffic_event, events);
                        listEvents.setAdapter(feedAdapter);
                    }
                });
                break;
            case 3:
                pageViewModel.getPlannedRoadworks().observe(getViewLifecycleOwner(), new Observer<ArrayList<Event>>() {
                    @Override
                    public void onChanged(ArrayList<Event> events) {
                        FeedAdapter feedAdapter = new FeedAdapter(
                                Objects.requireNonNull(getContext()), R.layout.traffic_event, events);
                        listEvents.setAdapter(feedAdapter);
                    }
                });
                break;
        }
        return root;
    }
}