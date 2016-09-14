package com.searchlocations.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.searchlocations.R;
import com.searchlocations.models.Venue;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VenueAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Venue> mDataSource;

    private String IMG_URL = "https://cdn4.iconfinder.com/data/icons/office-and-business-conceptual-flat/178/13-128.png";

    public VenueAdapter(Context context, List<Venue> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_venue, parent, false);
        ViewHolder holder = new ViewHolder(rowView);

        Venue venue = (Venue) getItem(position);
        String description = venue.getDescription();
        holder.title.setText(venue.getName());
        holder.subtitle.setText((description != null) ? description : "Description not available");
        holder.detail.setText("Some detail");

        Picasso.with(mContext)
                .load(IMG_URL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.thumbnail);

        return rowView;
    }

    static class ViewHolder {

        @BindView(R.id.venue_list_title) TextView title;
        @BindView(R.id.venue_list_subtitle) TextView subtitle;
        @BindView(R.id.venue_list_detail) TextView detail;
        @BindView(R.id.venue_list_thumbnail) ImageView thumbnail;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
