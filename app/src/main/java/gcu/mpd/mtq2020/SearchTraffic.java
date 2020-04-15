package gcu.mpd.mtq2020;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class SearchTraffic extends AppCompatActivity {
    private static final String TAG = "SearchTraffic";
    private DatePickerDialog picker;
    private EditText eText;
    private Button btnSearch;
    private Date date;
    private Calendar cal;
    private RadioGroup rGrp;
    private String selectedRoad;

    public SearchTraffic() {
        this.cal = Calendar.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_traffic);
        eText = findViewById(R.id.eventDate);
        rGrp = findViewById(R.id.roadSelection);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                picker = new DatePickerDialog(SearchTraffic.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                cal.set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());
                                cal.set(Calendar.MONTH, view.getMonth());
                                cal.set(Calendar.YEAR, view.getYear());
                                date = new Date(cal.getTimeInMillis());
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        final Spinner spinner = findViewById(R.id.roadOptions);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.motorways, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);

        rGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioMotorway:
                        ArrayAdapter<CharSequence> motorwayAdapter = ArrayAdapter.createFromResource(SearchTraffic.this, R.array.motorways, android.R.layout.simple_spinner_item);
                        spinner.setAdapter(motorwayAdapter);
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.radioRoad:
                        ArrayAdapter<CharSequence> aRoadAdapter = ArrayAdapter.createFromResource(SearchTraffic.this, R.array.a_roads, android.R.layout.simple_spinner_item);
                        spinner.setAdapter(aRoadAdapter);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });

        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RadioButton checkedButton = findViewById(rGrp.getCheckedRadioButtonId());
                String text = (String) checkedButton.getText();
                RoadType roadType = getRoadType(text);

                Intent intent = new Intent(SearchTraffic.this, SearchResults.class);
                intent.putExtra("DATE", date);
                intent.putExtra("ROAD_TYPE", roadType);
                intent.putExtra("ROAD", selectedRoad);
                startActivity(intent);
            }
        });
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String text = parent.getItemAtPosition(position).toString();
                    selectedRoad = text;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    private RoadType getRoadType(String road) {
        RoadType rt;
        if (road.equalsIgnoreCase("motorway")) {
            rt = RoadType.MOTORWAY;
        } else {
            rt = RoadType.A_ROAD;
        }
        return rt;
    }
}
