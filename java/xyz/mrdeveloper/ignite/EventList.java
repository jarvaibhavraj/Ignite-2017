package xyz.mrdeveloper.ignite;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;
import static xyz.mrdeveloper.ignite.MainActivity.eventCategoryTabSelected;
import static xyz.mrdeveloper.ignite.MainActivity.isDetailsOpened;
import static xyz.mrdeveloper.ignite.MainActivity.pos;
import static xyz.mrdeveloper.ignite.UpdateFromFirebase.allEventsList;
import static xyz.mrdeveloper.ignite.UpdateFromFirebase.isScheduleFinalized;

/**
 * Created by Vaibhav on 04-01-2017.
 */

public class EventList extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    public ListView mListView;
    //    public LayoutInflater mInflater;
//    public ViewGroup mContainer;
    public View view;
    public int mDecidingValue;
    public String mDecidingFactor;

    View eventDetails;

    Typeface typeface;
    ViewHolderForEventDetails eventDetailsHolder;

    TextView textViewScheduleNoInternet;
    ArrayList<EventData> timeTable;
    ScheduleAdapter adapter;
    EventData selectedEventData;

    public EventList() {
    }

    public void setEventList(String decidingFactor, int decidingValue) {
        //decidingValue 1 for Day, 2 for Category.
        mDecidingFactor = decidingFactor;
        mDecidingValue = decidingValue;
    }

    public static EventList newInstance() {
        return new EventList();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Calibri.ttf");
//            typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Calibri.ttf");

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.schedule_list_view, container, false);

//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.i("debug", "GETTING INPUT");
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                    if(detailsOpened) {
//
////                        ImageView addToWishlist = (ImageView) mListView.getRootView().findViewById(R.id.buttonAddToWishlist);
////                        if (ExistInWishlist(selectedEventData.title)) {
////                            Log.i("debug", "Exist in Wishlist");
////                            addToWishlist.setImageResource(R.drawable.home_white);
////                        } else {
////                            Log.i("debug", "Not Exist in Wishlist");
////                            addToWishlist.setImageResource(R.drawable.home);
////                        }
//
//                        mListView.invalidate();
//                        detailsOpened = false;
//
//                        eventDetails.setVisibility(View.GONE);
//                        getView().setFocusable(false);
//
//                        return true;
//                    }
//                    else {
//
//                    }
//                }
//
//                return false;
//            }
//        });

        eventDetails = view.findViewById(R.id.relativeLayoutEventDetails);
        eventDetails.setVisibility(View.GONE);

        // create a new "Holder" with subviews
        eventDetailsHolder = new ViewHolderForEventDetails();

        eventDetailsHolder.titleTextView = (TextView) eventDetails.findViewById(R.id.event_details_title);
        eventDetailsHolder.descriptionTextView = (TextView) eventDetails.findViewById(R.id.event_details_description);
        eventDetailsHolder.tagline = (TextView) eventDetails.findViewById(R.id.event_details_tagline);
        eventDetailsHolder.eventImageView = (ImageView) eventDetails.findViewById(R.id.imageViewEventPhoto);
        eventDetailsHolder.buttonAddToWishlist = (ImageView) eventDetails.findViewById(R.id.buttonAddToWishlist);

        eventDetailsHolder.titleTextView.setTypeface(typeface);
        eventDetailsHolder.descriptionTextView.setTypeface(typeface);
        eventDetailsHolder.tagline.setTypeface(typeface);

//            Log.d("Check", "hehe Here title: " + eventData.title);
//            Log.d("Check", "hehe Here description: " + eventData.description);

        eventDetailsHolder.tabLayout1 = (TabLayout) eventDetails.findViewById(R.id.tabLayout1);
        eventDetailsHolder.tabLayout2 = (TabLayout) eventDetails.findViewById(R.id.tabLayout2);
        eventDetailsHolder.tabLayout3 = (TabLayout) eventDetails.findViewById(R.id.tabLayout3);

        eventDetailsHolder.buttonAddToWishlist.setOnClickListener(this);

        textViewScheduleNoInternet = (TextView) view.findViewById(R.id.textViewScheduleNoInternet);

        TextView textViewScheduleNoInternet = (TextView) view.findViewById(R.id.textViewScheduleNoInternet);
        TextView textViewScheduleEmpty = (TextView) view.findViewById(R.id.textViewScheduleEmpty);

        Log.d("Check", "hehe " + isScheduleFinalized);
        if (allEventsList == null) {
            textViewScheduleNoInternet = (TextView) view.findViewById(R.id.textViewScheduleNoInternet);
            textViewScheduleNoInternet.setVisibility(View.VISIBLE);
            textViewScheduleEmpty.setVisibility(View.GONE);
            Log.d("Check", "hehe we3434" + isScheduleFinalized);
        } else if (!isScheduleFinalized && !(pos == 0)) {
            textViewScheduleEmpty = (TextView) view.findViewById(R.id.textViewScheduleEmpty);
            textViewScheduleEmpty.setVisibility(View.VISIBLE);
            textViewScheduleNoInternet.setVisibility(View.GONE);
            Log.d("Check", "hehe 2w344" + isScheduleFinalized);
        } else {
            textViewScheduleEmpty.setVisibility(View.GONE);
            textViewScheduleNoInternet.setVisibility(View.GONE);
            Log.d("Check", "hehe dfvgh5" + isScheduleFinalized);

            doRemainingTask();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        String newWishedEvent = selectedEventData.title;
        String wishedEvents;

        try {
            SharedPreferences pref = getContext().getSharedPreferences("WishList", MODE_PRIVATE);
            wishedEvents = pref.getString("WishList", null);

//            String[] events = new String[0];
            if (wishedEvents != null) {
                String[] events = wishedEvents.split(",");

                if (Arrays.asList(events).contains(newWishedEvent)) {
                    StringBuilder updatedWishedEvents = new StringBuilder();

                    Toast.makeText(getContext(), selectedEventData.title + " is removed from Wishlist!", Toast.LENGTH_SHORT).show();

                    for (String event : events) {
                        if (!event.equals(selectedEventData.title)) {
                            updatedWishedEvents.append(event).append(",");
                          /*  if (wishedEvents != null && !wishedEvents.isEmpty()) {
                                Log.d("Check", "WishList!! 1");
                                wishedEvents = wishedEvents.concat("," + removeEvent);
                            } else {
                                wishedEvents = newWishedEvent;
                                Log.d("Check", "WishList!! 2");
                            }*/
                        }
                        getContext().getSharedPreferences("WishList", MODE_PRIVATE)
                                .edit()
                                .putString("WishList", updatedWishedEvents.toString())
                                .apply();
                    }
                } else {
                    Toast.makeText(getContext(), newWishedEvent + " is added to Wishlist!", Toast.LENGTH_SHORT).show();
                    if (!wishedEvents.isEmpty()) {
                        Log.d("Check", "WishList!! 1");
                        wishedEvents = wishedEvents.concat("," + newWishedEvent);
                    } else {
                        wishedEvents = newWishedEvent;
                        Log.d("Check", "WishList!! 2");
                    }
                    getContext().getSharedPreferences("WishList", MODE_PRIVATE)
                            .edit()
                            .putString("WishList", wishedEvents)
                            .apply();
                }

                Log.d("Check", "Here I am, this is me WishList!! " + wishedEvents);
            } else {
                Toast.makeText(getContext(), newWishedEvent + " is added to Wishlist!", Toast.LENGTH_SHORT).show();
                wishedEvents = newWishedEvent;
                Log.d("Check", "WishList!! 2");
                getContext().getSharedPreferences("WishList", MODE_PRIVATE)
                        .edit()
                        .putString("WishList", wishedEvents)
                        .apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView imageAddToWishlist = (ImageView) v;
        if (ExistInWishlist(newWishedEvent)) {
//                    Log.i("debug", "" + eventData.title);
            imageAddToWishlist.setImageResource(R.mipmap.heart_filled);
//                    imageAddToWishlist.invalidate();
        } else {
            imageAddToWishlist.setImageResource(R.mipmap.heart);
//                    imageAddToWishlist.invalidate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Log.i("debug", "Fragment EXIT PRESSED");
                    if(isDetailsOpened) {

                        isDetailsOpened = false;
                        eventDetails.setVisibility(View.GONE);
                        getView().setFocusable(false);

//                        ImageView addToWishlist = (ImageView) mListView.getRootView().findViewById(R.id.buttonAddToWishlist);
//                        if (ExistInWishlist(selectedEventData.title)) {
//                            Log.i("debug", "Exist in Wishlist");
//                            addToWishlist.setImageResource(R.drawable.home_white);
//                        } else {
//                            Log.i("debug", "Not Exist in Wishlist");
//                            addToWishlist.setImageResource(R.drawable.home);
//                        }

//                        mListView.invalidate();

//                        adapter = new ScheduleAdapter(getActivity());
//
//                        timeTable = adapter.getEventDetailsFromFirebase(getActivity(), mDecidingFactor, mDecidingValue);
//
//                        adapter.mDataSource = timeTable;
//                        // Create list view
//                        mListView = (ListView) view.findViewById(R.id.schedule_list_view);
//                        mListView.setAdapter(adapter);

                        adapter.notifyDataSetChanged();

                        return true;
                    }
                    else {

                    }
                }

                return false;
            }
        });

//        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /*public void doRemainingTask(JSONArray output) {
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(getActivity());

        if (output == null) {
            Log.d("Check", "hehe null null 2");
        }

        final ArrayList<EventData> timeTable = scheduleAdapter.getScheduleFromFile(getActivity(), output, mDecidingFactor, mDecidingValue);

        final ScheduleAdapter adapter = new ScheduleAdapter(getActivity(), timeTable);
        // Create list view
        mListView = (ListView) view.findViewById(R.id.schedule_list_view);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EventData selectedEvent = timeTable.get(position);
                String selectedEventTitle = selectedEvent.title;

                Log.d("Check", "hehe01" +selectedEventTitle);

                EventDetailsAdapter
                        eventDetailsAdapter = new EventDetailsAdapter(getActivity(), view, mContainer, mInflater, mListView, selectedEventTitle);
                isDetailsOpened = true;
                isChildDetailsOpened = true;
            }
        });
    }*/

    public void doRemainingTask() {

        adapter = new ScheduleAdapter(getActivity());

        timeTable = adapter.getEventDetailsFromFirebase(getActivity(), mDecidingFactor, mDecidingValue);

        adapter.mDataSource = timeTable;
        // Create list view
        mListView = (ListView) view.findViewById(R.id.schedule_list_view);
        mListView.setAdapter(adapter);

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                selectedEventData = timeTable.get(position);
//
//                isDetailsOpened = true;
//
//                eventDetails.setVisibility(View.VISIBLE);
//                FillDetails(selectedEventData);
//
////                getChildFragmentManager()
////                        .beginTransaction()
////                        .replace(R.id.event_details_container, EventDetailsFragment.newInstance(selectedEventData), "EventData")
////                        .commitAllowingStateLoss();
//            }
//        });

        mListView.setOnItemClickListener(this);
    }

    public void HideDetails() {
        eventDetails.setVisibility(View.GONE);
    }

    public void FillDetails(EventData eventData) {
        eventDetailsHolder.titleTextView.setText(eventData.title);
        eventDetailsHolder.descriptionTextView.setText(eventData.description);
        eventDetailsHolder.tagline.setText(eventData.tagline);

        eventDetailsHolder.tabLayout1.getTabAt(0).setText(eventData.date);
        eventDetailsHolder.tabLayout1.getTabAt(1).setText(eventData.time);
        eventDetailsHolder.tabLayout2.getTabAt(0).setText(eventData.venue);
        eventDetailsHolder.tabLayout2.getTabAt(1).setText(eventData.fee);
        eventDetailsHolder.tabLayout3.getTabAt(0).setText(eventData.prize);

        changeTabsFont(eventDetailsHolder.tabLayout1, typeface);
        changeTabsFont(eventDetailsHolder.tabLayout2, typeface);
        changeTabsFont(eventDetailsHolder.tabLayout3, typeface);

        ImageView addToWishlist = eventDetailsHolder.buttonAddToWishlist;
        if (ExistInWishlist(eventData.title)) {
            addToWishlist.setImageResource(R.mipmap.heart_filled);
        } else {
            addToWishlist.setImageResource(R.mipmap.heart);
        }

    }

    public boolean ExistInWishlist(String title) {
        SharedPreferences pref = getContext().getSharedPreferences("WishList", MODE_PRIVATE);
        String wishedEvents = pref.getString("WishList", null);

        if (wishedEvents != null) {
            String[] events = wishedEvents.split(",");

            if (Arrays.asList(events).contains(title)) {
                return true;
            }
        }
        return false;
    }

    private void changeTabsFont(TabLayout tabLayout, Typeface typeface) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedEventData = timeTable.get(position);

        isDetailsOpened = true;

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        eventDetails.setVisibility(View.VISIBLE);
        FillDetails(selectedEventData);

//        mListView.setOnItemClickListener(null);

//                getChildFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.event_details_container, EventDetailsFragment.newInstance(selectedEventData), "EventData")
//                        .commitAllowingStateLoss();
    }

    private class ViewHolderForEventDetails {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView tagline;
        TextView timeTextView;
        ImageView buttonAddToWishlist;
        ImageView eventImageView;
        TextView dateTextView;
        TextView feeTextView;
        TextView prizeTextView;
        TextView venueTextView;

        TabLayout tabLayout1;
        TabLayout tabLayout2;
        TabLayout tabLayout3;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
