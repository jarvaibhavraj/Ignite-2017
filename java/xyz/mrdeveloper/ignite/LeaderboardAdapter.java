package xyz.mrdeveloper.ignite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Vaibhav on 14-02-2017.
 */

public class LeaderboardAdapter extends BaseAdapter {

    public Context mContext;
    private LayoutInflater mInflater;
    ArrayList<EventData> mDataSource;

    LeaderboardAdapter(Context context) {
        mContext = context;
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

        ViewHolderForLeaderboard holder;

        // check if the view already exists if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.game_leaderboard_item, parent, false);

            // create a new "Holder" with subviews
            holder = new ViewHolderForLeaderboard();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.leaderboard_name);
            //holder.titleTextView.setTypeface(typeface);
            holder.rankTextView = (TextView) convertView.findViewById(R.id.leaderboard_rank);
            holder.scoreTextView = (TextView) convertView.findViewById(R.id.leaderboard_score);

            // hang onto this eventDetailsHolder for future recycle
            convertView.setTag(holder);
        } else {
            // skip all the expensive inflation/findViewById and just get the eventDetailsHolder you already made
            holder = (ViewHolderForLeaderboard) convertView.getTag();
        }

        //Get corresponding event for row
        final EventData eventData = (EventData) getItem(position);

        // Update row view's textviews to display recipe information

        String rank = Integer.toString(position + 1);
        holder.nameTextView.setText(eventData.name);
        holder.rankTextView.setText(rank);
        holder.scoreTextView.setText(Long.toString(eventData.score));

        return convertView;
    }

    private class ViewHolderForLeaderboard {
        TextView nameTextView;
        TextView rankTextView;
        TextView scoreTextView;
    }
}
