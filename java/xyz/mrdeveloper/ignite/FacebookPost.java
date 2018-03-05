package xyz.mrdeveloper.ignite;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class FacebookPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post_events);
        PostToFacebook();
    }

    /*
        compile 'com.jakewharton:butterknife:6.1.0'
        compile 'com.facebook.android:facebook-android-sdk:4.19.0'
     */
    public void PostToFacebook() {
//        ShareDialog shareDialog;
//        FacebookSdk.sdkInitialize(this);
//        shareDialog = new ShareDialog(this);
//        ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                .setContentTitle("Download the Official Ignite app")
//                .setContentDescription(
//                        "Please Please Please Please Please Please Please Please Please Please!")
//                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=xyz.mrdeveloper.ignite")).build();
//        shareDialog.show(linkContent);
        ShareDialog shareDialog;
        shareDialog = new ShareDialog(this);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=xyz.mrdeveloper.ignite"))
                .setQuote("ThingsWithWings gaming event is now live on Official Ignite app. Tap to feel the heat! Bring a mask. High risk of Bird flu.")
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#ignite2k17")
                        .build())
                .build();
        shareDialog.show(content);
    }
}
