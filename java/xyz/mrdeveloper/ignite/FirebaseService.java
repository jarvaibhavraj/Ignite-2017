
package xyz.mrdeveloper.ignite;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*
 * Created by Vaibhav on 19-01-2017.
 */

public class FirebaseService extends Service {

    public static ArrayList<EventData> allEventsList;
    public static ArrayList<EventData> resultsList;
    public Context mContext;
    public boolean mRunning;
    public boolean isDatabaseNull = false;
    public static boolean isUpdated;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Log.d("Check", "hehe Here I am this is Service 1!!");
        final DatabaseReference mFirebaseDatabase;
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
//        mFirebaseDatabase = mFirebaseInstance.getReference("alleventsdetails");

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference(".info/connected");
        //mFirebaseDatabase.keepSynced(true);

        if (mFirebaseDatabase == null) {
            isDatabaseNull = true;
        }

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Check", "hehe Here I am this is Service 2!!");
                boolean connected = dataSnapshot.getValue(Boolean.class);
                Log.d("Check", "hehe Here onDataChange: " + connected);
                if (connected) {
                    System.out.println("connected");
                } else {
                    System.out.println("not connected");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("Check", "Failed to read app title value.", error.toException());
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}