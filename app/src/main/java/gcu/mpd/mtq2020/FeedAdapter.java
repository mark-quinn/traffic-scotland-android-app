package gcu.mpd.mtq2020;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

/**
 * @author Mark Quinn S1510840
 */
public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<Event> events;
    private Context context;

    public FeedAdapter(@NonNull Context context, int resource, List<Event> events) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Event currentApp = events.get(position);

        viewHolder.tvTitle.setText(currentApp.getTitle());
        viewHolder.tvPubDate.setText(currentApp.getPublishedDate());

        if (currentApp.getEventLength() == EventLength.SHORT) {
            viewHolder.tvLengthMessage.setText("This event should last less than a week.");
            viewHolder.tvLengthMessage.setTextColor(Color.GREEN);
        } else if (currentApp.getEventLength() == EventLength.INTERMEDIATE) {
            viewHolder.tvLengthMessage.setText("This event should last less than a month.");
            viewHolder.tvLengthMessage.setTextColor(ContextCompat.getColor(context, R.color.amber));
        } else {
            viewHolder.tvLengthMessage.setText("This event could last more than several months.");
            viewHolder.tvLengthMessage.setTextColor(Color.RED);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetails.class);
                intent.putExtra("EVENT", currentApp);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        final TextView tvTitle;
        final TextView tvPubDate;
        final TextView tvLengthMessage;

        ViewHolder(View v) {
            this.tvTitle = v.findViewById(R.id.tvTitle);
            this.tvPubDate = v.findViewById(R.id.tvPubDate);
            this.tvLengthMessage = v.findViewById(R.id.lengthMessage);
        }
    }
}
