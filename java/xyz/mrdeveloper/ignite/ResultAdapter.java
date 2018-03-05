package xyz.mrdeveloper.ignite;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Vaibhav on 06-01-2017.
 */

public class ResultAdapter extends BaseAdapter {

    public String title;
    public Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<EventData> mDataSource;

    ImageView imageThird;

    public ResultAdapter(Activity context) {
        mContext = context;
    }

    public ResultAdapter(Activity context, ArrayList<EventData> items) {
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

        ViewHolderForResult holder;

        // check if the view already exists if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.result_item, parent, false);

            // create a new "Holder" with subviews
            holder = new ViewHolderForResult();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.event_thumbnail);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.result_list_title);
            holder.firstTextView = (TextView) convertView.findViewById(R.id.first);
            holder.secondTextView = (TextView) convertView.findViewById(R.id.second);
            holder.thirdTextView = (TextView) convertView.findViewById(R.id.third);

            imageThird = (ImageView) convertView.findViewById(R.id.image_third_position);

            // hang onto this eventDetailsHolder for future recycle
            convertView.setTag(holder);
        } else {
            // skip all the expensive inflation/findViewById and just get the eventDetailsHolder you already made
            holder = (ViewHolderForResult) convertView.getTag();
        }
        // Get relevant subviews of row view
        TextView titleTextView = holder.titleTextView;
        TextView firstTextView = holder.firstTextView;
        TextView secondTextView = holder.secondTextView;
        TextView thirdTextView = holder.thirdTextView;
        ImageView thumbnailImageView = holder.thumbnailImageView;

        //Get corresponding event for row
        EventData eventData = (EventData) getItem(position);

        // Update row view's textviews to display recipe information
        titleTextView.setText(eventData.title);
        firstTextView.setText(eventData.first);
        secondTextView.setText(eventData.second);

        if(eventData.third.equals("empty") || eventData.third.equals(""))
        {
            thirdTextView.setVisibility(View.GONE);
            imageThird.setVisibility(View.GONE);
        }
        else
        {
            thirdTextView.setText(eventData.third);
        }
        thumbnailImageView.setImageResource(eventData.mImageResIds);

        return convertView;
    }

    private class ViewHolderForResult {
        TextView titleTextView;
        TextView firstTextView;
        TextView secondTextView;
        TextView thirdTextView;
        ImageView thumbnailImageView;
    }
}
