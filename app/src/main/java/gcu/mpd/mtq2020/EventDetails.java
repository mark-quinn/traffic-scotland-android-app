package gcu.mpd.mtq2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * @author Mark Quinn S1510840
 */
public class EventDetails extends AppCompatActivity {
    private Event event;
    private TextView tvTitle;
    private TextView tvLocation;
    private TextView tvEventDesc;
    private TextView tvEventGPS;
    private TextView tvTrafficManagement;
    private TextView tvWorksInfo;
    private MapsActivity mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra("EVENT");
        tvTitle = findViewById(R.id.eventTitle);
        tvLocation = findViewById(R.id.eventLocation);
        tvTrafficManagement = findViewById(R.id.trafficManagement);
        tvWorksInfo = findViewById(R.id.worksInfo);
        tvEventDesc = findViewById(R.id.eventDescription);
        tvEventGPS = findViewById(R.id.eventGPS);

        tvTitle.setText(event.getTitle());
        tvLocation.setText(event.getTitle());
        tvEventDesc.setText(event.getDescription());
        tvEventGPS.setText(event.getLocation());
        tvTrafficManagement.setText(event.getTrafficManagement());
        tvWorksInfo.setText(event.getWorkDetails());

        mMap = new MapsActivity(event);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mMap);
    }
}
