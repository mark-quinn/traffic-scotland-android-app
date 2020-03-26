package gcu.mpd.mtq2020;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, TrafficURL {

    private static final String TAG = "MainActivity";

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
            fetchRSSFeed = new FetchRSSFeed(TrafficURL.currentIncidents);
        }
        if (text.equalsIgnoreCase("ongoing roadworks")) {
            fetchRSSFeed = new FetchRSSFeed(TrafficURL.ongoingRoadworks);
        }
        if (text.equalsIgnoreCase("planned roadworks")) {
            fetchRSSFeed = new FetchRSSFeed(TrafficURL.plannedRoadworks);
        }
        fetchRSSFeed.execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public class FetchRSSFeed extends AsyncTask<String, Void, String>
            implements OnMapReadyCallback {

        private static final String TAG = "FetchRSSFeed";
        private URL url;
        private GoogleMap mMap;
        private TrafficEventParser trafficEventParser;

        public FetchRSSFeed(String feedURL) {
            setURL(feedURL);
            this.trafficEventParser = new TrafficEventParser();
        }

        private void setURL(String feedURL) {
            try {
                url = new URL(feedURL);
            } catch (MalformedURLException e) {
                Log.e(TAG, "setURL: Error creating URL obj", e);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String rssFeed = downloadXML();

            if(rssFeed == null) {
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return rssFeed;
        }

        @Override
        protected void onPostExecute(String rawFeed) {
            super.onPostExecute(rawFeed);
            boolean result = trafficEventParser.parse(rawFeed);

            if(result) {
                // TODO: display events on map
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            ArrayList<Event> events = trafficEventParser.getEvents();

            for(int i = 0 ; i < events.size() ; i++) {

                createMarker(events.get(i).getLatitude(), events.get(i).getLongitude(), events.get(i).getTitle(), events.get(i).getDescription());
            }

            LatLng UK = new LatLng(-55.3781, 3.4360);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(UK));
        }

        private String downloadXML() {
            StringBuilder xmlResult = new StringBuilder();

            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: Response code " + response);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer = new char[500];
                while(true) {
                    charsRead = reader.read(inputBuffer);
                    if(charsRead < 0) {
                        break;
                    }
                    if (charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();

                return xmlResult.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private Marker createMarker(double latitude, double longitude, String title, String description) {

            return mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .anchor(0.5f, 0.5f)
                    .title(title)
                    .snippet(description));
        }

    }
}