package gcu.mpd.mtq2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchResults extends AppCompatActivity implements AsyncTaskListener {
    private static final String TAG = "SearchResults";
    private Date date;
    private ListView listEvents;
    private List<Event> resultEvents;
    private TextView tvw;
    private RoadType road;

    public SearchResults() {
        resultEvents = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        listEvents = findViewById(R.id.eventListView);
        tvw = findViewById(R.id.totalEvents);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        date = (Date)intent.getSerializableExtra("DATE");
        road = (RoadType)intent.getSerializableExtra("ROAD");

        Log.i(TAG, "onCreate: Date passed " + date);
        FetchRSSFeed fetchRSSFeed = new FetchRSSFeed(this, TrafficURL.ongoingRoadworks, EventType.ONGOING_ROADWORK);
        fetchRSSFeed.execute();
    }

    @Override
    public void newEvents(ArrayList<Event> events) {
        Log.d(TAG, "newEvents: New events count" + events.size());
        filterByDay(date, events);
        Log.d(TAG, "newEvents: Filter result events" + resultEvents.size());
        tvw.setText("Found " + resultEvents.size() + " events matching the search criteria");
        FeedAdapter feedAdapter = new FeedAdapter(
                SearchResults.this, R.layout.traffic_event, resultEvents);
        listEvents.setAdapter(feedAdapter);
        listEvents.setOnItemClickListener(onItemClickListener);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), SearchTraffic.class);
        startActivityForResult(intent, 0);
        return true;
    }

    private void filterByDay(Date date, List<Event> events) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyy", Locale.UK);
        String formattedDate = df.format(date);
        for (Event event: events) {
            if (formattedDate.equals(event.getSimpleStartDate())) {
                resultEvents.add(event);
            }
        }
    }

    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TODO: start new event details activity
                }
            };

    private void filterByRange(Date date, List<Event> events) {
        for (Event event: events) {
            Log.d(TAG, "filterByCriteria: Event start date" + event.getStartDate());
            Log.d(TAG, "filterByCriteria: Event end date" + event.getEndDate());
            if (event.getStartDate().before(date) && event.getEndDate().after(date)) {
                resultEvents.add(event);
            }
        }
    }
}
