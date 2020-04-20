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

/**
 * @author Mark Quinn S1510840
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Event> events;
    private Event event = null;

    public MapsActivity(Event event) {
        this.event = event;
    }

    public MapsActivity(ArrayList<Event> events) {
        this.events = events;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        LatLng location = new LatLng(56.4907, -4.2026);

        if(event != null) {
            createMarker(event.getLatitude(), event.getLongitude(),
                    event.getTitle(), event.getDescription());
            location = new LatLng(event.getLatitude(), event.getLongitude());
        } else {
            for (int i = 0; i < events.size(); i++) {
                createMarker(events.get(i).getLatitude(), events.get(i).getLongitude(),
                        events.get(i).getTitle(), events.get(i).getDescription());
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
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
