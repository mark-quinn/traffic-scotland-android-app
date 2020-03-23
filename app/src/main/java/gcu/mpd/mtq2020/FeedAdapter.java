package gcu.mpd.mtq2020;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<Event> events;

    public FeedAdapter(@NonNull Context context, int resource, List<Event> events) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Event currentApp = events.get(position);

        viewHolder.tvTitle.setText(currentApp.getTitle());
        viewHolder.tvDescription.setText(currentApp.getDescription());
        viewHolder.tvPubDate.setText(currentApp.getPublishedDate());
        viewHolder.tvPoint.setText(currentApp.getLocation());

        return convertView;
    }

    private class ViewHolder {
        final TextView tvTitle;
        final TextView tvDescription;
        final TextView tvPubDate;
        final TextView tvPoint;

        ViewHolder(View v) {
            this.tvTitle = v.findViewById(R.id.tvTitle);
            this.tvDescription = v.findViewById(R.id.tvDescription);
            this.tvPubDate = v.findViewById(R.id.tvPubDate);
            this.tvPoint = v.findViewById(R.id.tvPoint);
        }
    }
}
