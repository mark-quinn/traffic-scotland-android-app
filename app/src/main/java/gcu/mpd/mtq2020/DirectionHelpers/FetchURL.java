package gcu.mpd.mtq2020.DirectionHelpers;

import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Mark Quinn S1510840
 */
public class FetchURL extends AsyncTask<String, Void, String> {
    private static final String TAG = "FetchURL";
    private Fragment fragment;

    public FetchURL(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected String doInBackground(String... strings) {
        String data = "";
        try {
            data = downloadUrl(strings[0]);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        PointsParser parserTask = new PointsParser(fragment);
        parserTask.execute(s);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.e(TAG, "downloadUrl: ", e);
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
