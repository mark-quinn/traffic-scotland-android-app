package gcu.mpd.mtq2020.DirectionHelpers;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author Mark Quinn S1510840
 */
public class PointsParser extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    private static final String TAG = "PointsParser";
    private TaskLoadedCallback taskCallback;

    public PointsParser(Fragment fragment) {
        this.taskCallback = (TaskLoadedCallback) fragment;
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject response;
        List<List<HashMap<String, String>>> routes = null;

        try {
            response = new JSONObject(jsonData[0]);
            DataParser parser = new DataParser();
            routes = parser.parse(response);
            Log.d(TAG, routes.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();
            List<HashMap<String, String>> path = result.get(i);
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);
                double lat = Double.parseDouble(Objects.requireNonNull(point.get("lat")));
                double lng = Double.parseDouble(Objects.requireNonNull(point.get("lng")));
                LatLng position = new LatLng(lat, lng);
                points.add(position);
            }
            lineOptions.addAll(points);
            lineOptions.width(15);
            lineOptions.color(Color.RED);
        }

        if (lineOptions != null) {
            taskCallback.onTaskDone(lineOptions);
        } else {
            Log.e(TAG, "onPostExecute: Unable to draw Polylines");
        }
    }
}
