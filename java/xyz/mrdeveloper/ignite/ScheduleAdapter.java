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

public class ScheduleAdapter extends BaseAdapter {

    public String title;
    public String description;
    public String time;
    public String date;
    public Context mContext;
    private LayoutInflater mInflater;
    ArrayList<EventData> mDataSource;
    private String wishedEvents;

    ImageView buttonAddToWishlist;

    ScheduleAdapter(Activity context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ScheduleAdapter(Activity context, ArrayList<EventData> items) {
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

        ViewHolderForSchedule holder;

        //Typeface typeface = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/Quicksand-Regular.ttf");
//        typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Quicksand-Regular.ttf");
        //typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Calibri.ttf");

        // check if the view already exists if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.event_list_item, parent, false);

            // create a new "Holder" with subviews
            holder = new ViewHolderForSchedule();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.event_thumbnail);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.event_list_title);
            //holder.titleTextView.setTypeface(typeface);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.event_list_subtitle);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.event_list_detail);
            holder.buttonAddToWishlist = (ImageView) convertView.findViewById(R.id.buttonEventsAddToWishlist);

            //holder.dateTextView.setTypeface(typeface);
            //holder.timeTextView.setTypeface(typeface);

            // hang onto this eventDetailsHolder for future recycle
            convertView.setTag(holder);
        } else {
            // skip all the expensive inflation/findViewById and just get the eventDetailsHolder you already made
            holder = (ViewHolderForSchedule) convertView.getTag();
        }

        // Get relevant subviews of row view
//        TextView titleTextView = eventDetailsHolder.titleTextView;
//        TextView dateTextView = eventDetailsHolder.dateTextView;
//        TextView timeTextView = eventDetailsHolder.timeTextView;
//        ImageView thumbnailImageView = eventDetailsHolder.thumbnailImageView;
//        ImageView buttonAddToWishlist = eventDetailsHolder.buttonAddToWishlist;

        //Get corresponding event for row
        final EventData eventData = (EventData) getItem(position);

        // Update row view's textviews to display recipe information
        holder.titleTextView.setText(eventData.title);
        holder.dateTextView.setText(eventData.date);
        holder.timeTextView.setText(eventData.time);
        holder.thumbnailImageView.setImageResource(eventData.mImageResIds);

        buttonAddToWishlist = holder.buttonAddToWishlist;
        if (ExistInWishlist(eventData.title)) {
            buttonAddToWishlist.setImageResource(R.mipmap.heart_filled);
        } else {
            buttonAddToWishlist.setImageResource(R.mipmap.heart);
        }

        holder.buttonAddToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newWishedEvent = eventData.title;
                try {
                    SharedPreferences pref = mContext.getSharedPreferences("WishList", MODE_PRIVATE);
                    wishedEvents = pref.getString("WishList", null);

                    if (wishedEvents != null) {
                        String[] events = wishedEvents.split(",");

                        if (Arrays.asList(events).contains(newWishedEvent)) {
                            StringBuilder updatedWishedEvents = new StringBuilder();

                            Toast.makeText(mContext, eventData.title + " is removed from Wishlist!", Toast.LENGTH_SHORT).show();

                            for (String event : events) {
                                if (!event.equals(eventData.title)) {
                                    updatedWishedEvents.append(event).append(",");

                          /*  if (wishedEvents != null && !wishedEvents.isEmpty()) {
                                Log.d("Check", "WishList!! 1");
                                wishedEvents = wishedEvents.concat("," + removeEvent);
                            } else {
                                wishedEvents = newWishedEvent;
                                Log.d("Check", "WishList!! 2");
                            }*/
                                }
                                mContext.getSharedPreferences("WishList", MODE_PRIVATE)
                                        .edit()
                                        .putString("WishList", updatedWishedEvents.toString())
                                        .apply();
                            }
                        } else {
                            Toast.makeText(mContext, newWishedEvent + " is added to Wishlist!", Toast.LENGTH_SHORT).show();
                            if (wishedEvents != null && !wishedEvents.isEmpty()) {
                                Log.d("Check", "WishList!! 1");
                                wishedEvents = wishedEvents.concat("," + newWishedEvent);
                            } else {
                                wishedEvents = newWishedEvent;
                                Log.d("Check", "WishList!! 2");
                            }
                            mContext.getSharedPreferences("WishList", MODE_PRIVATE)
                                    .edit()
                                    .putString("WishList", wishedEvents)
                                    .apply();
                        }
                        Log.d("Check", "Here I am, this is me WishList!! " + wishedEvents);
                    } else {
                        Toast.makeText(mContext, newWishedEvent + " is added to Wishlist!", Toast.LENGTH_SHORT).show();
                        if (wishedEvents != null && !wishedEvents.isEmpty()) {
                            Log.d("Check", "WishList!! 1");
                            wishedEvents = wishedEvents.concat("," + newWishedEvent);
                        } else {
                            wishedEvents = newWishedEvent;
                            Log.d("Check", "WishList!! 2");
                        }
                        mContext.getSharedPreferences("WishList", MODE_PRIVATE)
                                .edit()
                                .putString("WishList", wishedEvents)
                                .apply();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                buttonAddToWishlist = (ImageView) v;
                if (ExistInWishlist(newWishedEvent)) {
//                    Log.i("debug", "" + eventData.title);
                    buttonAddToWishlist.setImageResource(R.mipmap.heart_filled);
//                    imageAddToWishlist.invalidate();
                } else {
                    buttonAddToWishlist.setImageResource(R.mipmap.heart);
//                    imageAddToWishlist.invalidate();
                }
            }
        });

        return convertView;
    }

    /*public ArrayList<EventData> getScheduleFromFile(Context mContext, JSONArray timeTable, String decidingFactor, int decidingValue) {
        final ArrayList<EventData> allEventsList = new ArrayList<>();

        try {
            // Get event categories names and descriptions.
            final Resources resources = mContext.getResources();
            final TypedArray typedArray = resources.obtainTypedArray(R.array.images);

            //Get EventData objects from data
            for (int i = 0; i < timeTable.length(); ++i) {
                EventData eventData = new EventData();

                eventData.day = timeTable.getJSONObject(i).getString("day");
                eventData.category = timeTable.getJSONObject(i).getString("category");

                if ((decidingValue == 1 && decidingFactor.equals(eventData.day)) || (decidingValue == 2 && decidingFactor.equals(eventData.category))) {
                    eventData.title = timeTable.getJSONObject(i).getString("title");
                    eventData.time = timeTable.getJSONObject(i).getString("time");
                    eventData.date = timeTable.getJSONObject(i).getString("date");
                    eventData.mImageResIds = typedArray.getResourceId(i, 0);

                    allEventsList.add(eventData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allEventsList;
    }*/

    ArrayList<EventData> getEventDetailsFromFirebase(Context mContext, String decidingFactor, int decidingValue) {
        final ArrayList<EventData> eventDataList = new ArrayList<>();

        // Get event categories names and descriptions.
        final Resources resources = mContext.getResources();
        final TypedArray typedArray = resources.obtainTypedArray(R.array.images);

        if (allEventsList != null) {

            //Get EventData objects from data
            for (int i = 0; i < allEventsList.size(); ++i) {
                EventData eventData = new EventData();

                eventData.day = allEventsList.get(i).day;
                eventData.category = allEventsList.get(i).category;

                if ((decidingValue == 1 && decidingFactor.equals(eventData.day)) || (decidingValue == 2 && decidingFactor.equals(eventData.category))) {
                    eventData.title = allEventsList.get(i).title;
                    eventData.time = allEventsList.get(i).time;
                    eventData.date = allEventsList.get(i).date;
                    eventData.description = allEventsList.get(i).description;
                    eventData.venue = allEventsList.get(i).venue;
                    eventData.prize = allEventsList.get(i).prize;
                    eventData.fee = allEventsList.get(i).fee;
                    eventData.tagline = allEventsList.get(i).tagline;
                    eventData.mImageResIds = typedArray.getResourceId(i, 0);

                    eventDataList.add(eventData);
                }
            }
        } else {

        }
        typedArray.recycle();
        return eventDataList;
    }

    public boolean ExistInWishlist(String title) {
        SharedPreferences pref = mContext.getSharedPreferences("WishList", MODE_PRIVATE);
        String wishedEvents = pref.getString("WishList", null);

        if (wishedEvents != null) {
            String[] events = wishedEvents.split(",");

            if (Arrays.asList(events).contains(title)) {
                return true;
            }
        }
        return false;
    }

    private class ViewHolderForSchedule {
        TextView titleTextView;
        TextView dateTextView;
        TextView timeTextView;
        ImageView thumbnailImageView;
        ImageView buttonAddToWishlist;
    }
}
