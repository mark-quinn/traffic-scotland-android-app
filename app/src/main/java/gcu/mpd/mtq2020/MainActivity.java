package gcu.mpd.mtq2020;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback {
    private LatLngBounds SCOTLAND = new LatLngBounds(
            new LatLng(54.0050, 3.0626), new LatLng(57.4778, 4.2247)
    );

    private MapFragment mapFragment;
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = findViewById(R.id.spinnerEvent);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.event_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        new TrafficInformation(this).getCurrentIncidents();
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        // TODO guard clause if choice is already current val
        updateEvents(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateEvents(String event) {
        // TODO refactor better way of handling choice
        if(event.equalsIgnoreCase("Current Incidents")) {
            new TrafficInformation(this).getCurrentIncidents();
            return;
        }
        if(event.equalsIgnoreCase("Ongoing Roadworks")) {
            new TrafficInformation(this).getCurrentRoadworks();
            return;
        }
        if(event.equalsIgnoreCase("Planned Roadworks")) {
            new TrafficInformation(this).getPlannedRoadworks();
            return;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SCOTLAND.getCenter(), 5));
    }

    public void updateMap(List<Event> events) {
        setLatLng(events);
        mapFragment.getMapAsync(this);
    }

    private void setLatLng(List<Event> events) {
        for (Event event: events) {
            System.out.println(event.latitude);
            latlngs.add(new LatLng(event.latitude, event.longitude));
        }
    }
}