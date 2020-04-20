package gcu.mpd.mtq2020.ui.search;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Calendar;
import java.util.Date;

import gcu.mpd.mtq2020.R;
import gcu.mpd.mtq2020.Results;
import gcu.mpd.mtq2020.RoadType;

/**
 * @author Mark Quinn S1510840
 */
public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private DatePickerDialog picker;
    private EditText eText;
    private Button btnSearch;
    private Date date;
    private Calendar cal;
    private RadioGroup rGrp;
    private String selectedRoad;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_search, container, false);

        this.cal = Calendar.getInstance();
        eText = root.findViewById(R.id.eventDate);
        rGrp = root.findViewById(R.id.roadSelection);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                picker = new DatePickerDialog(root.getContext(),
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

        final Spinner spinner = root.findViewById(R.id.roadOptions);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.motorways, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);

        rGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioMotorway:
                        ArrayAdapter<CharSequence> motorwayAdapter = ArrayAdapter.createFromResource(root.getContext(), R.array.motorways, android.R.layout.simple_spinner_item);
                        spinner.setAdapter(motorwayAdapter);
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.radioRoad:
                        ArrayAdapter<CharSequence> aRoadAdapter = ArrayAdapter.createFromResource(root.getContext(), R.array.a_roads, android.R.layout.simple_spinner_item);
                        spinner.setAdapter(aRoadAdapter);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });

        btnSearch = root.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RadioButton checkedButton = root.findViewById(rGrp.getCheckedRadioButtonId());
                String text = (String) checkedButton.getText();
                RoadType roadType = getRoadType(text);

                Intent intent = new Intent(root.getContext(), Results.class);
                intent.putExtra("DATE", date);
                intent.putExtra("ROAD_TYPE", roadType);
                intent.putExtra("ROAD", selectedRoad);
                startActivity(intent);
            }
        });
        return root;
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