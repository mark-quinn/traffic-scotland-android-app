package gcu.mpd.mtq2020;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, TrafficURL {

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
}