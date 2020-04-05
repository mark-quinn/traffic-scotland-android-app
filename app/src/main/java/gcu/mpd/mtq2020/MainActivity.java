package gcu.mpd.mtq2020;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, TrafficURL, AsyncTaskListener {

    private static final String TAG = "MainActivity";
    private MapsActivity mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = findViewById(R.id.spinnerEvent);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.event_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        FetchRSSFeed fetchRSSFeed = null;

        if (text.equalsIgnoreCase("current incidents")) {
            fetchRSSFeed = new FetchRSSFeed(this, TrafficURL.currentIncidents, EventType.CURRENT_INCIDENT);
        }
        if (text.equalsIgnoreCase("ongoing roadworks")) {
            fetchRSSFeed = new FetchRSSFeed(this, TrafficURL.ongoingRoadworks, EventType.ONGOING_ROADWORK);
        }
        if (text.equalsIgnoreCase("planned roadworks")) {
            fetchRSSFeed = new FetchRSSFeed(this, TrafficURL.plannedRoadworks, EventType.PLANNED_ROADWORK);
        }
        fetchRSSFeed.execute();
    }

    @Override
    public void newEvents(ArrayList<Event> events) {
        mMap = new MapsActivity(events);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mMap);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
