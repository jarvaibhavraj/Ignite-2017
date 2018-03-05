package xyz.mrdeveloper.ignite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Vaibhav on 31-01-2017.
 */

public class StartFirebaseAtBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, UpdateFromFirebase.class));
    }
}
