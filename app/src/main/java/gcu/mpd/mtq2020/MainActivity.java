package gcu.mpd.mtq2020;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public TextView rawDataDisplay;

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
}