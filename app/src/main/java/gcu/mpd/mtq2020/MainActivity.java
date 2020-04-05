package gcu.mpd.mtq2020;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, TrafficURL,
            OnMapReadyCallback, AsyncTaskListener {

    private static final String TAG = "MainActivity";
    private GoogleMap mMap;
    private ArrayList<Event> events;

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
        this.events = events;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear(); // TODO: check if clearing markers here is best place

        for (int i = 0; i < events.size(); i++) {
            createMarker(events.get(i).getLatitude(), events.get(i).getLongitude(),
                    events.get(i).getTitle(), events.get(i).getDescription());
        }

        LatLng UK = new LatLng(56.4907, -4.2026);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UK));
    }

    private Marker createMarker(double latitude, double longitude,
                                String title, String description) {

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(description));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}