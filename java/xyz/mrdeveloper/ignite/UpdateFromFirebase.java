package xyz.mrdeveloper.ignite;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static xyz.mrdeveloper.ignite.SignUp.appRunning;

/*
 * Created by Vaibhav on 19-01-2017.
 */

public class UpdateFromFirebase extends android.app.Application {

    public static ArrayList<EventData> allEventsList;
    public static ArrayList<EventData> resultsList;
    public static ArrayList<EventData> leaderboard;
    public static ArrayList<EventData> leaderboardToShow;
    public static int rank;
    public static boolean isRegistered;
    public Context mContext;
    public boolean mRunning;
    public boolean shouldUpdate;
    public boolean isDatabaseNull = false;
    public static boolean areEventDetailsUpdated = false;
    public static boolean areResultsUpdated = false;
    public static boolean isScheduleFinalized = true;
    public static boolean areResultsAnnounced = true;
    public MainActivity mMainActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public UpdateFromFirebase() {
    }

    public UpdateFromFirebase(MainActivity mainActivity, Context context) {
        mContext = context;
        mMainActivity = mainActivity;

      /*  pDialog = new ProgressDialog(getApplicationContext());
        pDialog.setMessage("Updating info...");
        pDialog.setCancelable(false);
        pDialog.show();*/
    }

    public void Update() {
        Log.d("Check", "Here I am, this is me 1");
        UpdateAllEvents("alleventsdetails");
        Log.d("Check", "Here I am, this is me 2");
        UpdateResults("results");
        Log.d("Check", "Here I am, this is me 3");
        UpdateLeaderboard("leaderboard");
    }

    public void UpdateAllEvents(final String arrayName) {
        final DatabaseReference mFirebaseDatabase;
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference(arrayName);

        if (mFirebaseDatabase == null) {
            isDatabaseNull = true;
        }

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Check", "Value: In the last!!!!!");
                AddAllEvents(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("Check", "Failed to read app title value.", error.toException());
            }
        });
    }

    public void AddAllEvents(DataSnapshot dataSnapshot) {
        // Get event categories names and descriptions.
        final Resources resources = mContext.getResources();
        allEventsList = new ArrayList<>();
        final TypedArray typedArray = resources.obtainTypedArray(R.array.images);
        Log.d("Check", "fatal Here I am, this is me 4");
        int i = 0;

        //Get Schedule objects from data
        int k = 0;
        for (DataSnapshot alert : dataSnapshot.getChildren()) {
            if (alert == null)
                Log.i("debug", "NULLLLLLLLL");
            EventData eventData = new EventData();

//            if(i==5)
//                break;

            if (alert != null && alert.child("title") != null) {
                eventData.title = alert.child("title").getValue(String.class);
                eventData.day = alert.child("day").getValue(String.class);

                if (!eventData.day.equals("TBA")) {
                    isScheduleFinalized = true;
                    k++;
                }

                eventData.category = alert.child("category").getValue(String.class);
                eventData.time = alert.child("time").getValue(String.class);
                eventData.date = alert.child("date").getValue(String.class);
                eventData.description = alert.child("description").getValue(String.class);
                eventData.fee = alert.child("fee").getValue(String.class);
                eventData.prize = alert.child("prize").getValue(String.class);
                eventData.venue = alert.child("venue").getValue(String.class);
                eventData.tagline = alert.child("tagline").getValue(String.class);
                eventData.update = alert.child("update").getValue(String.class);
                eventData.mImageResIds = typedArray.getResourceId(i++, 0);

                if (ExistInWishlist(eventData.title) && AreDetailsUpdated(eventData.title, eventData.update)) {
                    PushNotificationForEventDetails(eventData.title, "Details Changed!");
                    //Log.d("Check", "hehe updateValue: " + mFirebaseDatabase.child(Integer.toString(i)).child("title").toString());
                    areEventDetailsUpdated = true;
                }
                allEventsList.add(eventData);
            }
        }
        if (k == 0) {
            isScheduleFinalized = false;
        }
        typedArray.recycle();
    }

    public void UpdateResults(final String arrayName) {
        final DatabaseReference mFirebaseDatabase;
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference(arrayName);

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AddResults(dataSnapshot, mFirebaseDatabase);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("Check", "Failed to read app title value.", error.toException());
            }
        });
    }

    public void AddResults(DataSnapshot dataSnapshot, DatabaseReference mFirebaseDatabase) {
        // Get event categories names and descriptions.
        final Resources resources = mContext.getResources();
        resultsList = new ArrayList<>();
        final TypedArray typedArray = resources.obtainTypedArray(R.array.images);
        int i = 0;
        Log.d("Check", "Here I am, this is me 5");

        //Get Schedule objects from data
        for (DataSnapshot alert : dataSnapshot.getChildren()) {
            EventData eventData = new EventData();

            eventData.title = alert.child("title").getValue(String.class);
            eventData.first = alert.child("first").getValue(String.class);
            Log.d("Check", "Here I am, this is me 2334 " + eventData.first);
            if (!eventData.first.equals("TBA")) {
                areResultsAnnounced = true;
            } else {
                continue;
            }
            Log.d("Check", "Here I am, this is me 66345423");
            eventData.second = alert.child("second").getValue(String.class);
            eventData.third = alert.child("third").getValue(String.class);
            eventData.update = alert.child("update").getValue(String.class);
            eventData.mImageResIds = typedArray.getResourceId(i++, 0);

            if (ExistInWishlist(eventData.title) && AreResultsUpdated(eventData.title, eventData.update)) {
                Log.d("Check", "hehe Here Updated Updated Results Updated!!");
                areResultsUpdated = true;
                PushNotificationForResults(eventData.title, "Results Announced!");
                //Log.d("Check", "hehe updateValue: " + mFirebaseDatabase.child(Integer.toString(i)).child("title").toString());
            }
            resultsList.add(eventData);
        }
        if (i == 0) {
            areResultsAnnounced = false;
        }
        typedArray.recycle();
        Log.d("Check", "Here I am, this is me 5");
    }

    public void UpdateLeaderboard(String arrayName) {
        final DatabaseReference mFirebaseDatabase;
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference(arrayName);
        Query myTopPostsQuery = mFirebaseDatabase.orderByChild("score");

        myTopPostsQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Check", "hehe App Running: " + appRunning);
//                if (appRunning == 10) {
                    AddLeaderboardItems(dataSnapshot);
                //}
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("Check", "Failed to read app title value.", error.toException());
            }
        });
    }

    public void AddLeaderboardItems(DataSnapshot dataSnapshot) {
        // Get event categories names and descriptions.
        final Resources resources = mContext.getResources();
        leaderboard = new ArrayList<>();
        Log.d("Check", "Here I am, this is me 5");
        int i = 0;

        //Get Schedule objects from data
        for (DataSnapshot alert : dataSnapshot.getChildren()) {
            EventData eventData = new EventData();

            eventData.name = alert.child("name").getValue(String.class);
            eventData.sapid = alert.child("sapid").getValue(Long.class);
            Log.d("Check", "Here I am, this is me Name: " + eventData.name);
            eventData.score = alert.child("score").getValue(Long.class);
            Log.d("Check", "Here I am, this is me SAPID " + eventData.sapid);
            Log.d("Check", "Here I am, this is me Score " + eventData.score);

            if (eventData.name == null || eventData.sapid == null || eventData.score == null) {
                i++;
            }

            leaderboard.add(eventData);
        }
        shouldUpdate = i <= 0;

        if (shouldUpdate) {
            Collections.reverse(leaderboard);
            leaderboardToShow = new ArrayList<>();
            for (i = 0; i < leaderboard.size(); ++i) {
                Log.d("debug", "leaderboard item: " + i + "  " + leaderboard.get(i).title);
                if (i == 20) {
                    break;
                }
                leaderboardToShow.add(leaderboard.get(i));
            }

            DoRemainingTasks();

            Log.d("Check", "Here I am, this is me 5");
        }
    }

    private void DoRemainingTasks() {

        SharedPreferences pref = mContext.getSharedPreferences("PlayerDetails", MODE_PRIVATE);
        String playerDetailsInSP = pref.getString("PlayerDetails", null);

        if (playerDetailsInSP == null) {
            isRegistered = false;
        } else {
            isRegistered = true;
            String[] playerDetails = playerDetailsInSP.split(",");
            String playerSAPID = playerDetails[1];

            // get reference to 'users' node
            for (int i = 0; i < leaderboard.size(); ++i) {
                Log.d("Check", "hehe Score: " + leaderboard.get(i).score);
                Log.d("Check", "hehe LeaderBoard: " + leaderboard.size() + "  " + leaderboard.get(i).sapid + "  " + playerSAPID);
                if (leaderboard.get(i).sapid == Long.parseLong(playerSAPID)) {
                    rank = i + 1;
                    break;
                }
            }
        }
    }

    public boolean AreDetailsUpdated(String eventName, String currentUpdateValue) {
        SharedPreferences pref = mContext.getSharedPreferences("DetailsUpdateValue" + eventName, MODE_PRIVATE);
        String detailsUpdateValue = pref.getString("DetailsUpdateValue" + eventName, null);
        Log.d("Check", "hehe Details 2:" + currentUpdateValue + "  " + detailsUpdateValue);

        if (detailsUpdateValue == null) {
            mContext.getSharedPreferences("DetailsUpdateValue" + eventName, MODE_PRIVATE)
                    .edit()
                    .putString("DetailsUpdateValue" + eventName, currentUpdateValue)
                    .apply();
            Log.d("Check", "hehe Details1:" + currentUpdateValue);
            return false;
        } else if (!detailsUpdateValue.equals(currentUpdateValue)) {
            mContext.getSharedPreferences("DetailsUpdateValue" + eventName, MODE_PRIVATE)
                    .edit()
                    .putString("DetailsUpdateValue" + eventName, currentUpdateValue)
                    .apply();
            return true;
        }
        return false;
    }

    public boolean AreResultsUpdated(String eventName, String currentUpdateValue) {
        SharedPreferences pref = mContext.getSharedPreferences("ResultsUpdateValue" + eventName, MODE_PRIVATE);
        String resultsUpdateValue = pref.getString("ResultsUpdateValue" + eventName, null);

        if (resultsUpdateValue == null) {
            mContext.getSharedPreferences("ResultsUpdateValue" + eventName, MODE_PRIVATE)
                    .edit()
                    .putString("ResultsUpdateValue" + eventName, currentUpdateValue)
                    .apply();
            Log.d("Check", "hehe Results1:" + currentUpdateValue);
            return false;
        } else if (!resultsUpdateValue.equals(currentUpdateValue)) {
            mContext.getSharedPreferences("ResultsUpdateValue" + eventName, MODE_PRIVATE)
                    .edit()
                    .putString("ResultsUpdateValue" + eventName, currentUpdateValue)
                    .apply();
            return true;
        }
        return false;
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

    public void PushNotificationForEventDetails(String first, String second) {
        //create notification
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("ToOpen", 1);
        PendingIntent resultIntent = PendingIntent.getActivity(mContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ignite_logo_medium)
                .setSound(notificationSoundURI)
                .setContentTitle(first + " " + second)
                .setVibrate(new long[]{1000, 1000})
                .setLights(Color.RED, 0, 0)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(resultIntent)
                .setAutoCancel(true)
                .setContentText("Tap to view updates");

        Random random = new Random();

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(random.nextInt(), mBuilder.build());
    }

    public void PushNotificationForResults(String first, String second) {
        //create notification
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("ToOpen", 2);
        PendingIntent resultIntent = PendingIntent.getActivity(mContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ignite_logo_medium)
                .setSound(notificationSoundURI)
                .setContentTitle(first + " " + second)
                .setVibrate(new long[]{1000, 1000})
                .setLights(Color.RED, 0, 0)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(resultIntent)
                .setAutoCancel(true)
                .setContentText("Tap to view updates");

        Random random = new Random();

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(random.nextInt(), mBuilder.build());
    }
}