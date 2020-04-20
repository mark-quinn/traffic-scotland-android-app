package gcu.mpd.mtq2020;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Repository {
    private static final String TAG = "gcu.mpd.mtq2020.Repository";

    private static Repository instance;
    private MutableLiveData<ArrayList<Event>> events = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Event>> onGoingRoadworks = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Event>> plannedRoadworks = new MutableLiveData<>();


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

    public LiveData<ArrayList<Event>> getAllEvents() {
        getCurrentEvents();
        getOnGoingRoadworks();
        getPlannedRoadworks();

        final MediatorLiveData liveData = new MediatorLiveData();
        liveData.addSource(events, new Observer() {
            @Override
            public void onChanged(Object value) {
                liveData.setValue(value);
            }
        });
        liveData.addSource(onGoingRoadworks, new Observer() {
            @Override
            public void onChanged(Object value) {
                liveData.setValue(value);
            }
        });
        liveData.addSource(plannedRoadworks, new Observer() {
            @Override
            public void onChanged(Object value) {
                liveData.setValue(value);
            }
        });

        return liveData;
    }

    public LiveData<ArrayList<Event>> getCurrentEvents() {
        GetData data = new GetData(TrafficURL.currentIncidents, EventType.CURRENT_INCIDENT);
        data.execute();
        return events;
    }

    public LiveData<ArrayList<Event>> getOnGoingRoadworks() {
        GetData data = new GetData(TrafficURL.ongoingRoadworks, EventType.ONGOING_ROADWORK);
        data.execute();
        return onGoingRoadworks;
    }

    public LiveData<ArrayList<Event>> getPlannedRoadworks() {
        GetData data = new GetData(TrafficURL.plannedRoadworks, EventType.PLANNED_ROADWORK);
        data.execute();
        return plannedRoadworks;
    }

    private class GetData extends AsyncTask<String, Void, String> {
        private static final String TAG = "GetData";
        private URL url;
        private TrafficEventParser trafficEventParser;
        private EventType type;

        public GetData(String trafficURL, EventType type) {
            setURL(trafficURL);
            this.trafficEventParser = new TrafficEventParser();
            this.type = type;
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
                switch (type) {
                    case CURRENT_INCIDENT:
                        events.postValue(trafficEventParser.getEvents());
                        break;
                    case ONGOING_ROADWORK:
                        onGoingRoadworks.postValue(trafficEventParser.getEvents());
                        break;
                    case PLANNED_ROADWORK:
                        plannedRoadworks.postValue(trafficEventParser.getEvents());
                        break;
                }
            }
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
}
