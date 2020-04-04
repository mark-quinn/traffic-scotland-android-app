package gcu.mpd.mtq2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class SearchResults extends AppCompatActivity implements AsyncTaskListener {
    private static final String TAG = "SearchResults";
    private Date date;
    private ListView listEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Intent intent = getIntent();
        date = (Date)intent.getSerializableExtra("DATE");
        Log.i(TAG, "onCreate: Date passed " + date);
        FetchRSSFeed fetchRSSFeed = new FetchRSSFeed(this, TrafficURL.currentIncidents, EventType.CURRENT_INCIDENT);
        fetchRSSFeed.execute();
    }

    @Override
    public void newEvents(ArrayList<Event> events) {
        Log.d(TAG, "newEvents: New events" + events);
    }
}
