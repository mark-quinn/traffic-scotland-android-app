package gcu.mpd.mtq2020;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Event> events;

    public MapsActivity(Event event) {
        // TODO: overload constructor so it accepts single event too
    }

    public MapsActivity(ArrayList<Event> events) {
        this.events = events;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();

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
}
