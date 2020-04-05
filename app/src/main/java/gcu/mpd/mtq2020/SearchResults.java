package gcu.mpd.mtq2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchResults extends AppCompatActivity implements AsyncTaskListener {
    private static final String TAG = "SearchResults";
    private Date date;
    private ListView listEvents;
    private List<Event> resultEvents;

    public SearchResults() {
        resultEvents = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Intent intent = getIntent();
        date = (Date)intent.getSerializableExtra("DATE");
        Log.i(TAG, "onCreate: Date passed " + date);
        FetchRSSFeed fetchRSSFeed = new FetchRSSFeed(this, TrafficURL.ongoingRoadworks, EventType.ONGOING_ROADWORK);
        fetchRSSFeed.execute();
    }

    @Override
    public void newEvents(ArrayList<Event> events) {
        Log.d(TAG, "newEvents: New events" + events);
        filterByCriteria(date, events);
        Log.d(TAG, "newEvents: Result events" + resultEvents);
    }

    private void filterByCriteria(Date date, List<Event> events) {
        for (Event event: events) {
            Log.d(TAG, "filterByCriteria: Event start date" + event.getStartDate());
            Log.d(TAG, "filterByCriteria: Event end date" + event.getEndDate());
            if (event.getStartDate().before(date) && event.getEndDate().after(date)) {
                resultEvents.add(event);
            }
        }
    }
}
