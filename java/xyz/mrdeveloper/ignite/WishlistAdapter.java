package xyz.mrdeveloper.ignite;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;
import static xyz.mrdeveloper.ignite.UpdateFromFirebase.allEventsList;

/**
 * Created by Vaibhav on 06-01-2017.
 */

class WishlistAdapter extends BaseAdapter {

    public String title;
    public String description;
    public String time;
    public String date;
    public Context mContext;
    private LayoutInflater mInflater;
    private WishListFragment mWishListFragment;
    private ArrayList<EventData> mDataSource;

    WishlistAdapter(Activity context) {
        mContext = context;
    }

    WishlistAdapter(Activity context, ArrayList<EventData> items, WishListFragment wishListFragment) {
        mContext = context;
        mDataSource = items;
        mWishListFragment = wishListFragment;
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

        ViewHolderForSchedule holder;
//        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Calibri.ttf");

        // check if the view already exists if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.wishlist_item, parent, false);

            // create a new "Holder" with subviews
            holder = new ViewHolderForSchedule();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.wish_thumbnail);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.wish_list_title);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.wish_details_date);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.wish_details_time);
            holder.feeTextView = (TextView) convertView.findViewById(R.id.wish_details_fee);
            holder.prizeTextView = (TextView) convertView.findViewById(R.id.wish_details_prize);
            holder.venueTextView = (TextView) convertView.findViewById(R.id.wish_details_venue);
            holder.deleteFromWishList = (ImageButton) convertView.findViewById(R.id.buttonRemoveFromWishlist);

            // hang onto this eventDetailsHolder for future recycle
            convertView.setTag(holder);
        } else {
            // skip all the expensive inflation/findViewById and just get the eventDetailsHolder you already made
            holder = (ViewHolderForSchedule) convertView.getTag();
        }

        // Get relevant subviews of row view
        TextView titleTextView = holder.titleTextView;
        TextView dateTextView = holder.dateTextView;
        TextView timeTextView = holder.timeTextView;
        TextView feeTextView = holder.feeTextView;
        TextView venueTextView = holder.venueTextView;
        TextView prizeTextView = holder.prizeTextView;
        ImageView thumbnailImageView = holder.thumbnailImageView;
        ImageButton imageButton = holder.deleteFromWishList;

        //Get corresponding event for row
        final EventData eventData = (EventData) getItem(position);

        // Update row view's textviews to display recipe information
        titleTextView.setText(eventData.title);
        dateTextView.setText(eventData.date);
        timeTextView.setText(eventData.time);
        feeTextView.setText(eventData.fee);
        prizeTextView.setText(eventData.prize);
        venueTextView.setText(eventData.venue);

//        titleTextView.setTypeface(typeface);
//        timeTextView.setTypeface(typeface);
//        dateTextView.setTypeface(typeface);
//        feeTextView.setTypeface(typeface);
//        venueTextView.setTypeface(typeface);
//        prizeTextView.setTypeface(typeface);
        thumbnailImageView.setImageResource(eventData.mImageResIds);
        imageButton.setImageResource(R.mipmap.cross_button);

        ImageButton deleteFromWishList = holder.deleteFromWishList;
        deleteFromWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String removeEvent = eventData.title;
                Log.d("Check", "hehe " + eventData.title);
                try {
                    SharedPreferences pref = mContext.getSharedPreferences("WishList", MODE_PRIVATE);
                    String wishedEvents = pref.getString("WishList", null);

                    if (wishedEvents != null) {
                        String[] events = wishedEvents.split(",");
                        StringBuilder updatedWishedEvents = new StringBuilder();

                        Toast.makeText(mContext, removeEvent + " is removed from Wishlist!", Toast.LENGTH_SHORT).show();

                        for (String event : events) {
                            if (!event.equals(removeEvent)) {
                                updatedWishedEvents.append(event).append(",");
                          /*  if (wishedEvents != null && !wishedEvents.isEmpty()) {
                                Log.d("Check", "WishList!! 1");
                                wishedEvents = wishedEvents.concat("," + removeEvent);
                            } else {
                                wishedEvents = newWishedEvent;
                                Log.d("Check", "WishList!! 2");
                            }*/
                            }
                        }
                        mContext.getSharedPreferences("WishList", MODE_PRIVATE)
                                .edit()
                                .putString("WishList", updatedWishedEvents.toString())
                                .apply();

                        mWishListFragment.doRemainingTasks();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return convertView;
    }

    ArrayList<EventData> getWishlistFromFile(String wishedEvents, Context mContext) {
        final ArrayList<EventData> scheduleList = new ArrayList<>();

        // Get event categories names and descriptions.
        final Resources resources = mContext.getResources();
        final TypedArray typedArray = resources.obtainTypedArray(R.array.images);

        Log.d("Check", "hehe " + wishedEvents);

        //Get wished events from SharedPreferences list
        String[] events;
        if (wishedEvents != null) {
            events = wishedEvents.split(",");

            if(allEventsList!=null)
            {
            for (int i = 0; i < allEventsList.size(); ++i) {
                EventData eventData = new EventData();
                eventData.title = allEventsList.get(i).title;

                if (Arrays.asList(events).contains(eventData.title)) {
                    eventData.time = allEventsList.get(i).time;
                    eventData.date = allEventsList.get(i).date;
                    eventData.fee = allEventsList.get(i).fee;
                    eventData.venue = allEventsList.get(i).venue;
                    eventData.prize = allEventsList.get(i).prize;
                    eventData.mImageResIds = typedArray.getResourceId(i, 0);
                    scheduleList.add(eventData);
                }
            }
            }
        }
        typedArray.recycle();
        return scheduleList;
    }

    private class ViewHolderForSchedule {
        TextView titleTextView;
        TextView dateTextView;
        TextView timeTextView;
        TextView feeTextView;
        TextView venueTextView;
        TextView prizeTextView;
        ImageView thumbnailImageView;
        ImageButton deleteFromWishList;
    }
}
