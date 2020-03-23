package gcu.mpd.mtq2020;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchRSSFeed extends AsyncTask<String, Void, String> {
    private static final String TAG = "FetchRSSFeed";
    private URL url;

    public FetchRSSFeed(String feedURL) {
        setURL(feedURL);
    }

    private void setURL(String feedURL) {
        try {
            url = new URL(feedURL);
        } catch (MalformedURLException e) {
            Log.e("setURL", "Error creating URL obj", e);
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String rssFeed = downloadXML();

        if(rssFeed == null) {
            Log.e(TAG, "doInBackground: Error downloading");
        }
        return rssFeed;
    }

    @Override
    protected void onPostExecute(String rawFeed) {
        super.onPostExecute(rawFeed);

    }

    private String downloadXML() {
        StringBuilder xmlResult = new StringBuilder();

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int response = connection.getResponseCode();
            Log.d(TAG, "downloadXML: Response code " + response);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            int charsRead;
            char[] inputBuffer = new char[500];
            while(true) {
                charsRead = reader.read(inputBuffer);
                if(charsRead < 0) {
                    break;
                }
                if (charsRead > 0) {
                    xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                }
            }
            reader.close();

            return xmlResult.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
