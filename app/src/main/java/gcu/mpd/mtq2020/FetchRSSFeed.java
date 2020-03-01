package gcu.mpd.mtq2020;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FetchRSSFeed extends AsyncTask<Void, Void, Boolean> {
    public List<Event> events;
    private URL url;

    public FetchRSSFeed(String feedURL) {
        setURL(feedURL);
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
        }
    }
}
