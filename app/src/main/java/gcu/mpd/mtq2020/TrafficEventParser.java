package gcu.mpd.mtq2020;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.StringReader;
import java.util.ArrayList;

public class TrafficEventParser {
    private static final String TAG = "TrafficEventParser";
    private ArrayList<Event> events;

    public TrafficEventParser() {
        this.events = new ArrayList<>();
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public boolean parse(String xmlData, EventType type) {
        boolean status = true;
        Event currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if("item".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new Event();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry) {
                            if("item".equalsIgnoreCase(tagName)) {
                                currentRecord.setEventType(type);
                                currentRecord.parseAdditionalFeatures();
                                events.add(currentRecord);
                                inEntry = false;
                            } else if("title".equalsIgnoreCase(tagName)) {
                                currentRecord.setTitle(textValue);
                            } else if("description".equalsIgnoreCase(tagName)) {
                                currentRecord.setDescription(textValue);
                            } else if("point".equalsIgnoreCase(tagName)) {
                                currentRecord.setLocation(textValue);
                            } else if("pubDate".equalsIgnoreCase(tagName)) {
                                currentRecord.setPublishedDate(textValue);
                            }
                        }
                        break;
                }
                eventType = xpp.next();
            }
            Log.d(TAG, "parse: number of events " + events.size());
        } catch(Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }
}
