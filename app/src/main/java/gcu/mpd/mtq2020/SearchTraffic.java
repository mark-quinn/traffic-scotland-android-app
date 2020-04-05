package gcu.mpd.mtq2020;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RadioButton checkedButton = findViewById(rGrp.getCheckedRadioButtonId());
                String text = (String) checkedButton.getText();
                RoadType road = getRoadType(text);

                Intent intent = new Intent(SearchTraffic.this, SearchResults.class);
                intent.putExtra("DATE", date);
                intent.putExtra("ROAD", road);
                startActivity(intent);
            }
        });
    }

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
