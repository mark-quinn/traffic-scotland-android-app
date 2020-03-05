package gcu.mpd.mtq2020;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FetchRSSFeed extends AsyncTask<Void, Void, Boolean> {
    private final Activity mActivity;
    public List<Event> events;
    private URL url;

    public FetchRSSFeed(String feedURL, Activity activity) {
        setURL(feedURL);
        mActivity = activity;
    }

    private void setURL(String feedURL) {
        try {
            url = new URL(feedURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("URL", "Error creating URL obj", e);
        }
    }

    @Override
    protected void onPreExecute() {
        /* TODO show spinner
           TODO get users choice from dropdown */
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            InputStream inputStream = url.openConnection().getInputStream();
            events = new TrafficEventParser(inputStream).parseFeed();
            return true;
        } catch (IOException e) {
            Log.e("IO", "Error connecting to URL");
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        // TODO disable refreshing state

        if (success) {
            // TODO update view
            TextView result = mActivity.findViewById(R.id.rawDataDisplay);
            result.setText(Integer.toString(events.size()));
        }
    }
}
