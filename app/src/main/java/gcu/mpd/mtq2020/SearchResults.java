package gcu.mpd.mtq2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;

public class SearchResults extends AppCompatActivity {
    private static final String TAG = "SearchResults";
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Intent intent = getIntent();
        Date date = (Date)intent.getSerializableExtra("DATE");
        Log.i(TAG, "onCreate: Date passed " + date);
    }
}
