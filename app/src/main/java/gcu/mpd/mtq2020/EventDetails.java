package gcu.mpd.mtq2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

public class EventDetails extends AppCompatActivity {
    private Event event;
    private TextView tvLocation;
    private TextView tvEventDesc;
    private TextView tvEventGPS;
    private MapsActivity mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra("EVENT");
        tvLocation = findViewById(R.id.eventLocation);
        tvEventDesc = findViewById(R.id.eventDescription);
        tvEventGPS = findViewById(R.id.eventGPS);

        tvLocation.setText(event.getTitle());
        tvEventDesc.setText(event.getDescription());
        tvEventGPS.setText(event.getLocation());

        mMap = new MapsActivity(event);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mMap);
    }
}
