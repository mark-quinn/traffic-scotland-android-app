package gcu.mpd.mtq2020;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Repository extends AsyncTask<String, Void, String> {
    private static final String TAG = "gcu.mpd.mtq2020.Repository";

    private static Repository instance;
    private MutableLiveData<ArrayList<Event>> events = new MutableLiveData<>();
    private URL url;
    private TrafficEventParser trafficEventParser;
    private EventType type;

    public Repository() {
        setURL(TrafficURL.ongoingRoadworks);
        this.trafficEventParser = new TrafficEventParser();
        this.type = EventType.ONGOING_ROADWORK;
    }

    public static Repository getInstance() {
        if(instance == null) {
            synchronized (Repository.class) {
                if(instance == null) {
                    instance = new Repository();
                }
            }
        }
        return instance;
    }

    public LiveData<ArrayList<Event>> getEvents() {
        return events;
    }


    @Override
    protected String doInBackground(String... strings) {
        String rssFeed = downloadXML();

        if (rssFeed == null) {
            Log.e(TAG, "doInBackground: Error downloading");
        }
        return rssFeed;
    }

    @Override
    protected void onPostExecute(String rawFeed) {
        super.onPostExecute(rawFeed);
        boolean result = trafficEventParser.parse(rawFeed, type);

        if (result) {
            events.postValue(trafficEventParser.getEvents());
        }
    }

    public void getLiveData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String rssFeed = Repository.this.downloadXML();
                    trafficEventParser.parse(rssFeed, type);
                } catch (Exception e) {
                    Log.e(TAG, "run: ", e);
                }

                events.postValue(trafficEventParser.getEvents());
            }
        }).start();
    }

    private String downloadXML() {
        StringBuilder xmlResult = new StringBuilder();

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            int charsRead;
            char[] inputBuffer = new char[500];
            while (true) {
                charsRead = reader.read(inputBuffer);
                if (charsRead < 0) {
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

    private void setURL(String feedURL) {
        try {
            url = new URL(feedURL);
        } catch (MalformedURLException e) {
            Log.e(TAG, "setURL: Error creating URL obj", e);
        }
    }
}
