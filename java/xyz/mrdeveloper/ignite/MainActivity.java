package xyz.mrdeveloper.ignite;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;

import java.util.Random;

import static xyz.mrdeveloper.ignite.UpdateFromFirebase.allEventsList;
import static xyz.mrdeveloper.ignite.UpdateFromFirebase.areResultsAnnounced;
import static xyz.mrdeveloper.ignite.UpdateFromFirebase.isScheduleFinalized;

public class MainActivity extends AppCompatActivity {

//    public static Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Quicksand-Regular.ttf");

    private CharSequence mTitle;
    public ActionBar actionBar;
    public TabLayout mTabLayout;
    public TabLayout tabLayoutHome1;
    public TabLayout tabLayoutHome2;
    public TabLayout tabLayoutHome3;
    View gameLink;
//    TextView textViewComingSoon;
    ImageView imageViewHome;

    View coolStuff;

    public static int pos;
    public static boolean isDetailsOpened = false;
    public static boolean isChildDetailsOpened = false;
    public boolean doubleBackToExitPressedOnce = false;
    public static int dayTabSelected = 0;
    public static int eventCategoryTabSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        setContentView(R.layout.main_layout);

       /* String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d("debug", "hehe Refreshed token: " + refreshedToken);*/

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatusBar));
        }

        UpdateFromFirebase updateFromFirebase = new UpdateFromFirebase(this, this);
        updateFromFirebase.Update();

        CharSequence mDrawerTitle;
        mTitle = mDrawerTitle = getTitle();
        String[] mOptionTitles = getResources().getStringArray(R.array.options_array);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_actionbar);
        myToolbar.setTitle("Ignite");

//        ViewGroup vg = myToolbar;
        setSupportActionBar(myToolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Ignite");


        // set a custom shadow that overlays the main content when the drawer opens
//        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
//        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.drawer_item, mOptionTitles));
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//        mDrawerList.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        // enable ActionBar app icon to behave as action to toggle nav drawer

        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ignite_logo_flame_cool_small);

        gameLink = findViewById(R.id.gameLink);
//        textViewComingSoon = (TextView) findViewById(R.id.textViewComingSoon);
//        textViewComingSoon.setVisibility(View.GONE);

        coolStuff = findViewById(R.id.doingCoolStuff);
        coolStuff.setVisibility(View.GONE);

        gameLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mTabLayout.setVisibility(View.GONE);
//                mTabLayout.getTabAt(4).select();
//                actionBar.hide();
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.content_frame, Game.newInstance(), "Game")
//                        .addToBackStack("Game")
//                        .commitAllowingStateLoss();
//                actionBar.setTitle("Game");

                ShowCoolStuffForGame();

                Intent intent = new Intent(MainActivity.this, Game.class);
                startActivity(intent);

//                if (textViewComingSoon.getVisibility() == View.GONE)
//                    textViewComingSoon.setVisibility(View.VISIBLE);
//                else
//                    textViewComingSoon.setVisibility(View.GONE);

//                  Log.d("Check", "hehe " + Integer.toString(pos));
            }
        });

//        Log.d("Check", "hehe Here Intent Extra: " + Integer.toString(getIntent().getIntExtra("ToOpen", 0)));

        tabLayoutHome1 = (TabLayout) findViewById(R.id.tabLayoutHome1);
        tabLayoutHome2 = (TabLayout) findViewById(R.id.tabLayoutHome2);
//        tabLayoutHome3 = (TabLayout) findViewById(R.id.tabLayoutHome3);

        ImageView tempImageView;
        TextView tempTextView;

        tempImageView = (ImageView) tabLayoutHome1.getTabAt(0).getCustomView().findViewById(R.id.imageHomeTab);
        tempImageView.setImageResource(R.mipmap.old_categories);
        tempTextView = (TextView) tabLayoutHome1.getTabAt(0).getCustomView().findViewById(R.id.textHomeTab);
        tempTextView.setText("Categories");

        tempImageView = (ImageView) tabLayoutHome1.getTabAt(1).getCustomView().findViewById(R.id.imageHomeTab);
        tempImageView.setImageResource(R.mipmap.old_schedule);
        tempTextView = (TextView) tabLayoutHome1.getTabAt(1).getCustomView().findViewById(R.id.textHomeTab);
        tempTextView.setText("Schedule");

        tempImageView = (ImageView) tabLayoutHome1.getTabAt(2).getCustomView().findViewById(R.id.imageHomeTab);
        tempImageView.setImageResource(R.mipmap.old_results);
        tempTextView = (TextView) tabLayoutHome1.getTabAt(2).getCustomView().findViewById(R.id.textHomeTab);
        tempTextView.setText("Results");

        tempImageView = (ImageView) tabLayoutHome2.getTabAt(0).getCustomView().findViewById(R.id.imageHomeTab);
        tempImageView.setImageResource(R.mipmap.old_wishlist);
        tempTextView = (TextView) tabLayoutHome2.getTabAt(0).getCustomView().findViewById(R.id.textHomeTab);
        tempTextView.setText("Wishlist");

        tempImageView = (ImageView) tabLayoutHome2.getTabAt(1).getCustomView().findViewById(R.id.imageHomeTab);
        tempImageView.setImageResource(R.mipmap.old_about);
        tempTextView = (TextView) tabLayoutHome2.getTabAt(1).getCustomView().findViewById(R.id.textHomeTab);
        tempTextView.setText("About");

//        tempImageView = (ImageView) tabLayoutHome3.getTabAt(0).getCustomView().findViewById(R.id.imageHomeTab);
//        tempImageView.setImageResource(R.drawable.bird_11);
//        tempTextView = (TextView) tabLayoutHome3.getTabAt(0).getCustomView().findViewById(R.id.textHomeTab);
//        tempTextView.setText("Game");
//        typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Quicksand-Bold.ttf");
//        tempTextView.setTypeface(typeface);
//
//        tempImageView = (ImageView) tabLayoutHome3.getTabAt(1).getCustomView().findViewById(R.id.imageHomeTab);
//        tempImageView.setImageResource(R.drawable.about);
//        tempTextView = (TextView) tabLayoutHome3.getTabAt(1).getCustomView().findViewById(R.id.textHomeTab);
//        tempTextView.setText("About");
//        typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Quicksand-Bold.ttf");
//        tempTextView.setTypeface(typeface);

        tabLayoutHome1.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i("debug", " Listening*****. ....." + tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        mTabLayout.getTabAt(0).select();
                        ////imageViewHome.setVisibility(View.VISIBLE);
//                        ShowCoolStuffForEventCategories();
////                        Log.d("Check", "hehe !!!!!!!!! 2");
//
//                        mTabLayout.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mTabLayout.setVisibility(View.VISIBLE);
//                                mTabLayout.getTabAt(0).select();
////                        Log.i("debug", " Listening1");
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.content_frame, EventCategoriesFragment.newInstance(), "EventCategoryList")
//                                        .addToBackStack("EventCategoryList")
//                                        .commitAllowingStateLoss();
//                                actionBar.setTitle("Event Categories");
//                                pos = 0;
//                                mTabLayout.removeCallbacks(this);
//                            }
//                        }, 1007);

                        break;
                    case 1:
                        mTabLayout.getTabAt(1).select();
//                        ShowCoolStuffForSchedule();
//
//                        mTabLayout.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mTabLayout.setVisibility(View.VISIBLE);
//                                mTabLayout.getTabAt(1).select();
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.content_frame, Schedule.newInstance(), "Schedule")
//                                        .addToBackStack("Schedule")
//                                        .commitAllowingStateLoss();
//                                actionBar.setTitle("Schedule");
//                                pos = 1;
//                                mTabLayout.removeCallbacks(this);
//                            }
//                        }, 1007);
                        ////imageViewHome.setVisibility(View.VISIBLE);

//                        Log.d("Check", "hehe 3434" + Integer.toString(pos));
                        break;
                    case 2:
                        mTabLayout.getTabAt(2).select();
//                        ShowCoolStuffForResults();
//                        Log.i("debug", "1");
//                        mTabLayout.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mTabLayout.setVisibility(View.VISIBLE);
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.content_frame, ResultsFragment.newInstance(), "Results")
//                                        .addToBackStack("Results")
//                                        .commitAllowingStateLoss();
//                                actionBar.setTitle("Results");
//                                pos = 2;
//                                mTabLayout.removeCallbacks(this);
//                            }
//                        }, 1007);
                        //imageViewHome.setVisibility(View.VISIBLE);

//                        Log.d("Check", "hehe " + Integer.toString(pos));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mTabLayout.getTabAt(0).select();
                        //imageViewHome.setVisibility(View.VISIBLE);
//                        ShowCoolStuffForEventCategories();
////                        Log.d("Check", "hehe !!!!!!!!! 3");
//                        mTabLayout.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mTabLayout.setVisibility(View.VISIBLE);
//                                mTabLayout.getTabAt(0).select();
////                        Log.i("debug", " Listening1");
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.content_frame, EventCategoriesFragment.newInstance(), "EventCategoryList")
//                                        .addToBackStack("EventCategoryList")
//                                        .commitAllowingStateLoss();
//                                actionBar.setTitle("Event Categories");
//                                pos = 0;
//                                mTabLayout.removeCallbacks(this);
//                            }
//                        }, 1007);

                        break;
                    case 1:
                        mTabLayout.getTabAt(1).select();
//                        ShowCoolStuffForSchedule();
//                        //imageViewHome.setVisibility(View.VISIBLE);
//                        mTabLayout.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mTabLayout.setVisibility(View.VISIBLE);
//                                mTabLayout.getTabAt(1).select();
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.content_frame, Schedule.newInstance(), "Schedule")
//                                        .addToBackStack("Schedule")
//                                        .commitAllowingStateLoss();
//                                actionBar.setTitle("Schedule");
//                                pos = 1;
//                                mTabLayout.removeCallbacks(this);
//                            }
//                        }, 1007);
//                        Log.d("Check", "hehe 354546" + Integer.toString(pos));
                        break;
                    case 2:
                        mTabLayout.getTabAt(2).select();
//                        ShowCoolStuffForResults();
//                        Log.i("debug", "2");
//                        mTabLayout.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mTabLayout.setVisibility(View.VISIBLE);
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.content_frame, ResultsFragment.newInstance(), "Results")
//                                        .addToBackStack("Results")
//                                        .commitAllowingStateLoss();
//                                actionBar.setTitle("Results");
//                                pos = 2;
//                                mTabLayout.removeCallbacks(this);
//                            }
//                        }, 1007);
                        //imageViewHome.setVisibility(View.VISIBLE);

//                        Log.d("Check", "hehe " + Integer.toString(pos));
                        break;
                }
            }
        });

        tabLayoutHome2.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Log.i("debug", " Listening. ....." + tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        //imageViewHome.setVisibility(View.VISIBLE);
                        mTabLayout.setVisibility(View.VISIBLE);
                        mTabLayout.getTabAt(3).select();
                        Log.i("debug", " Listening2");
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, WishListFragment.newInstance(), "Wishlist")
                                .addToBackStack("Wishlist")
                                .commitAllowingStateLoss();
                        actionBar.setTitle("Wishlist");
                        pos = 3;
                        break;
                    case 1:
                        //imageViewHome.setVisibility(View.VISIBLE);
                        mTabLayout.setVisibility(View.VISIBLE);
                        mTabLayout.getTabAt(4).select();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, About.newInstance(), "About")
                                .addToBackStack("About")
                                .commitAllowingStateLoss();
                        actionBar.setTitle("About");
                        pos = 4;
//                        Log.d("Check", "hehe " + Integer.toString(pos));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        //imageViewHome.setVisibility(View.VISIBLE);
                        mTabLayout.setVisibility(View.VISIBLE);
                        mTabLayout.getTabAt(3).select();
                        Log.i("debug", " Listening2");
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, WishListFragment.newInstance(), "Wishlist")
                                .addToBackStack("Wishlist")
                                .commitAllowingStateLoss();
                        actionBar.setTitle("Wishlist");
                        pos = 3;
                        break;
                    case 1:
                        //imageViewHome.setVisibility(View.VISIBLE);
                        mTabLayout.setVisibility(View.VISIBLE);
                        mTabLayout.getTabAt(4).select();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, About.newInstance(), "About")
                                .addToBackStack("About")
                                .commitAllowingStateLoss();
                        actionBar.setTitle("About");
                        pos = 4;
//                        Log.d("Check", "hehe " + Integer.toString(pos));
                        break;
                }
            }
        });

//        tabLayoutHome3.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//                Log.i("debug", " Listening. ....." + tab.getPosition());
//                switch (tab.getPosition()) {
//                    case 0:
//                        mTabLayout.setVisibility(View.VISIBLE);
//                        mTabLayout.getTabAt(4).select();
//                        Log.i("debug", " Listening2");
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.content_frame, Game.newInstance(), "Game")
//                                .addToBackStack("Game")
//                                .commitAllowingStateLoss();
//                        actionBar.setTitle("Game");
//                        break;
//                    case 1:
//                        mTabLayout.setVisibility(View.VISIBLE);
//                        mTabLayout.getTabAt(5).select();
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.content_frame, About.newInstance(), "About")
//                                .addToBackStack("About")
//                                .commitAllowingStateLoss();
//                        actionBar.setTitle("About");
////                        Log.d("Check", "hehe " + Integer.toString(pos));
//                        break;
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                switch (tab.getPosition()) {
//                    case 0:
//                        mTabLayout.setVisibility(View.VISIBLE);
//                        mTabLayout.getTabAt(2).select();
//                        Log.i("debug", " Listening2");
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.content_frame, ResultsFragment.newInstance(), "Results")
//                                .addToBackStack("Results")
//                                .commitAllowingStateLoss();
//                        actionBar.setTitle("Results");
//                        break;
//                    case 1:
//                        mTabLayout.setVisibility(View.VISIBLE);
//                        mTabLayout.getTabAt(3).select();
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.content_frame, About.newInstance(), "About")
//                                .addToBackStack("About")
//                                .commitAllowingStateLoss();
//                        actionBar.setTitle("About");
////                        Log.d("Check", "hehe " + Integer.toString(pos));
//                        break;
//                }
//            }
//        });


        mTabLayout = (TabLayout) findViewById(R.id.main_tab_layout);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mTabLayout.getTabAt(0).getIcon().setTint(getResources().getColor(R.color.colorPrimaryDark));
//            mTabLayout.getTabAt(1).getIcon().setTint(getResources().getColor(R.color.colorPrimaryDark));
//            mTabLayout.getTabAt(2).getIcon().setTint(getResources().getColor(R.color.colorPrimaryDark));
//            mTabLayout.getTabAt(3).getIcon().setTint(getResources().getColor(R.color.colorPrimaryDark));
//            mTabLayout.getTabAt(4).getIcon().setTint(getResources().getColor(R.color.colorPrimaryDark));
//        }
        Drawable drawable;

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            drawable = mTabLayout.getTabAt(i).getIcon();
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), getResources().getColor(R.color.colorPrimaryDark));
        }

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    tab.getIcon().setTintMode(null);
//                }

                Drawable drawable = tab.getIcon();
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTintMode(drawable.mutate(), null);

                pos = tab.getPosition();

                switch (tab.getPosition()) {
                    case 0:
                        //imageViewHome.setVisibility(View.VISIBLE);
                        ShowCoolStuffForEventCategories();
//                        Log.d("Check", "hehe !!!!!!!!! 4");
                        mTabLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTabLayout.setVisibility(View.VISIBLE);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, EventCategoriesFragment.newInstance(), "EventCategoryList")
                                        .addToBackStack("EventCategoryList")
                                        .commitAllowingStateLoss();
                                actionBar.setTitle("Event Categories");
//                                Log.d("Check", "hehe " + Integer.toString(pos));
                                pos = 0;
                                mTabLayout.removeCallbacks(this);
                            }
                        }, 1007);

                        break;
                    case 1:
                        //imageViewHome.setVisibility(View.VISIBLE);
                        ShowCoolStuffForSchedule();
                        mTabLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTabLayout.setVisibility(View.VISIBLE);
//                        Log.d("Check", "Here I am, this is me!!!!!!!");
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, Schedule.newInstance(), "Schedule")
                                        .addToBackStack("Schedule")
                                        .commitAllowingStateLoss();
                                actionBar.setTitle("Schedule");
//                                Log.d("Check", "hehe " + Integer.toString(pos));
                                pos = 1;
                                mTabLayout.removeCallbacks(this);
                            }
                        }, 1007);

                        break;
                    case 2:
                        //imageViewHome.setVisibility(View.VISIBLE);
                        ShowCoolStuffForResults();
                        Log.i("debug", "3");
                        mTabLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTabLayout.setVisibility(View.VISIBLE);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, ResultsFragment.newInstance(), "Results")
                                        .addToBackStack("Results")
                                        .commitAllowingStateLoss();
                                actionBar.setTitle("Results");
                                pos = 2;
                                mTabLayout.removeCallbacks(this);
                            }
                        }, 1007);

                        break;
                    case 3:
                        //imageViewHome.setVisibility(View.VISIBLE);
                        mTabLayout.setVisibility(View.VISIBLE);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, WishListFragment.newInstance(), "Wishlist")
                                .addToBackStack("Wishlist")
                                .commitAllowingStateLoss();
                        actionBar.setTitle("Wishlist");
                        pos = 3;
                        break;
                    case 4:
                        //imageViewHome.setVisibility(View.VISIBLE);
                        mTabLayout.setVisibility(View.VISIBLE);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, About.newInstance(), "About")
                                .addToBackStack("About")
                                .commitAllowingStateLoss();
                        actionBar.setTitle("About");
//                        Intent intent = new Intent(getApplicationContext(), PostEvents.class);
//                        startActivity(intent);
                        pos = 4;
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    tab.getIcon().setTintMode(PorterDuff.Mode.SRC_IN);
//                    tab.getIcon().setTint(getResources().getColor(R.color.colorPrimaryDark));
//                }
                Drawable drawable = tab.getIcon();
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTintMode(drawable.mutate(), PorterDuff.Mode.SRC_IN);
                DrawableCompat.setTint(drawable.mutate(), getResources().getColor(R.color.colorPrimaryDark));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                }

                Drawable drawable = tab.getIcon();
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTintMode(drawable.mutate(), null);

                pos = tab.getPosition();
                switch (tab.getPosition()) {
                    case 0:
                        //imageViewHome.setVisibility(View.VISIBLE);
                        ShowCoolStuffForEventCategories();
//                        Log.d("Check", "hehe !!!!!!!!! 4");
                        mTabLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTabLayout.setVisibility(View.VISIBLE);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, EventCategoriesFragment.newInstance(), "EventCategoryList")
                                        .addToBackStack("EventCategoryList")
                                        .commitAllowingStateLoss();
                                actionBar.setTitle("Event Categories");
//                                Log.d("Check", "hehe " + Integer.toString(pos));
                                pos = 0;
                                mTabLayout.removeCallbacks(this);
                            }
                        }, 1007);

                        break;
                    case 1:
                        //imageViewHome.setVisibility(View.VISIBLE);
                        ShowCoolStuffForSchedule();
                        mTabLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTabLayout.setVisibility(View.VISIBLE);
//                        Log.d("Check", "Here I am, this is me!!!!!!!");
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, Schedule.newInstance(), "Schedule")
                                        .addToBackStack("Schedule")
                                        .commitAllowingStateLoss();
                                actionBar.setTitle("Schedule");
//                                Log.d("Check", "hehe " + Integer.toString(pos));
                                pos = 1;
                                mTabLayout.removeCallbacks(this);
                            }
                        }, 1007);

                        break;
                    case 2:
                        //imageViewHome.setVisibility(View.VISIBLE);
                        ShowCoolStuffForResults();
                        Log.i("debug", "3");
                        mTabLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTabLayout.setVisibility(View.VISIBLE);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, ResultsFragment.newInstance(), "Results")
                                        .addToBackStack("Results")
                                        .commitAllowingStateLoss();
                                actionBar.setTitle("Results");
                                pos = 2;
                                mTabLayout.removeCallbacks(this);
                            }
                        }, 1007);

                        break;
                    case 3:
                        //imageViewHome.setVisibility(View.VISIBLE);
                        mTabLayout.setVisibility(View.VISIBLE);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, WishListFragment.newInstance(), "Wishlist")
                                .addToBackStack("Wishlist")
                                .commitAllowingStateLoss();
                        actionBar.setTitle("Wishlist");
                        pos = 3;
                        break;
                    case 4:
                        //imageViewHome.setVisibility(View.VISIBLE);
                        mTabLayout.setVisibility(View.VISIBLE);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, About.newInstance(), "About")
                                .addToBackStack("About")
                                .commitAllowingStateLoss();
                        actionBar.setTitle("About");
//                        Intent intent = new Intent(getApplicationContext(), PostEvents.class);
//                        startActivity(intent);
                        pos = 4;
                        break;
                }
            }
        });

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
//        mDrawerToggle = new ActionBarDrawerToggle(
//                this,                  /* host Activity */
//                mDrawerLayout,         /* DrawerLayout object */
//                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
//                R.string.drawer_open,  /* "open drawer" description for accessibility */
//                R.string.drawer_close  /* "close drawer" description for accessibility */
//        ) {
//            public void onDrawerClosed(View view) {
//                actionBar.setTitle(mTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//
//            public void onDrawerOpened(View drawerView) {
//                actionBar.setTitle(mDrawerTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//        };
//        mDrawerLayout.setDrawerListener(mDrawerToggle);

        /**
         * key exist in the sharedPreferences.
         * get the data from the key (list) and new data into list .
         */
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d("Check", "hehe Refreshed token: " + refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic("ignite");

        mTabLayout.setVisibility(View.GONE);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.content_frame, Home.newInstance(), "Home")
//                .addToBackStack("Home")
//                .commitAllowingStateLoss();
        Log.d("Check", "hehe Here Intent Extra: " + Integer.toString(getIntent().getIntExtra("ToOpen", 0)));

        if (getIntent().getIntExtra("ToOpen", 0) == 1) {
            //imageViewHome.setVisibility(View.VISIBLE);
            mTabLayout.setVisibility(View.VISIBLE);
            Log.d("Check", "hehe Here Intent Extra: " + Integer.toString(getIntent().getIntExtra("ToOpen", 0)));
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, WishListFragment.newInstance(), "Wishlist")
                    .addToBackStack("Wishlist")
                    .commit();
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(WishListFragment.newInstance(), "Wishlist")
//                    .addToBackStack("Wishlist")
//                    .commit();
            actionBar.setTitle("Wishlist");
        } else if (getIntent().getIntExtra("ToOpen", 0) == 2) {
            //imageViewHome.setVisibility(View.VISIBLE);
            mTabLayout.setVisibility(View.VISIBLE);
            Log.d("Check", "hehe Here Intent Extra: " + Integer.toString(getIntent().getIntExtra("ToOpen", 0)));
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, ResultsFragment.newInstance(), "Results")
                    .addToBackStack("Results")
                    .commit();
            actionBar.setTitle("Results");
        }
        //pos = 10;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_home).setVisible(!drawerOpen);
//        menu.findItem(R.id.action_home).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        // Handle action buttons
        int fragmentCount;
        switch (item.getItemId()) {
//            case android.R.id.home:
//                mTabLayout.setVisibility(View.VISIBLE);
////                this.recreate();
////                fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
////                while(fragmentCount-- > 0)
////                    getSupportFragmentManager().popBackStack();
//                getSupportFragmentManager().popBackStack();
//                LoadPreviousFragment();
//                fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
//                Log.d("debug", "" + fragmentCount);
//                if (fragmentCount == 0)
//                    mTabLayout.setVisibility(View.GONE);
////                getSupportFragmentManager().popBackStack();
////                getSupportFragmentManager().popBackStack();
////                actionBar.setTitle("Ignite");
//                return true;
            case R.id.action_home:
                mTabLayout.setVisibility(View.GONE);
//                imageViewHome.setVisibility(View.GONE);

//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.content_frame, WishListFragment.newInstance(), "Wishlist")
//                        .addToBackStack("Wishlist")
//                        .commitAllowingStateLoss();

                fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
                while (fragmentCount-- > 0)
                    getSupportFragmentManager().popBackStack();

//                getSupportFragmentManager().popBackStack();
//                getSupportFragmentManager().popBackStack();
//                getSupportFragmentManager().popBackStack();
//                this.recreate();
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .add(WishListFragment.newInstance(), "Wishlist")
//                        .addToBackStack("Wishlist")
//                        .commitAllowingStateLoss();

                actionBar.setTitle("Ignite");
                pos = 6;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    public void UpdateFragment() {
//        String currentFragmentTag;
//
//        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
//        Log.d("Check", "hehe fatal Here " + fragmentManager.getBackStackEntryCount());
//        if (fragmentManager.getBackStackEntryCount() > 0) {
//            fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
//            currentFragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
//            Log.d("Check", "hehe fatal Here " + currentFragmentTag);
//
//            switch (currentFragmentTag) {
//                case "EventCategoryList":
//                    //imageViewHome.setVisibility(View.VISIBLE);
//                    //imageViewHome.setVisibility(View.VISIBLE);
//                    ShowCoolStuffForEventCategories();
//                    Log.d("Check", "hehe !!!!!!!!! 5");
//                    mTabLayout.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mTabLayout.setVisibility(View.GONE);
//                            getSupportFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.content_frame, EventCategoriesFragment.newInstance(), "EventCategoryList")
//                                    .addToBackStack("EventCategoryList")
//                                    .commitAllowingStateLoss();
//                            actionBar.setTitle("Event Categories");
//                            pos = 0;
//                            mTabLayout.removeCallbacks(this);
//                        }
//                    }, 1007);
//
//                    break;
//
//                case "Schedule":
//                    //imageViewHome.setVisibility(View.VISIBLE);
//                    //imageViewHome.setVisibility(View.VISIBLE);
//                    ShowCoolStuffForSchedule();
//                    mTabLayout.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mTabLayout.setVisibility(View.VISIBLE);
//                            getSupportFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.content_frame, Schedule.newInstance(), "Schedule")
//                                    .addToBackStack("Schedule")
//                                    .commitAllowingStateLoss();
//                            actionBar.setTitle("Schedule");
//                            pos = 1;
//                            mTabLayout.removeCallbacks(this);
//                        }
//                    }, 1007);
//
//                    break;
//
//                case "Results":
//                    //imageViewHome.setVisibility(View.VISIBLE);
//                    //imageViewHome.setVisibility(View.VISIBLE);
//                    ShowCoolStuffForResults();
//                    mTabLayout.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mTabLayout.setVisibility(View.VISIBLE);
//                            getSupportFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.content_frame, ResultsFragment.newInstance(), "Results")
//                                    .addToBackStack("Results")
//                                    .commitAllowingStateLoss();
//                            actionBar.setTitle("Results");
//                            pos = 2;
//                            mTabLayout.removeCallbacks(this);
//                        }
//                    }, 1007);
//
//                    break;
//
//                case "Wishlist":
//                    //imageViewHome.setVisibility(View.VISIBLE);
//                    //imageViewHome.setVisibility(View.VISIBLE);
//                    mTabLayout.setVisibility(View.VISIBLE);
//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.content_frame, WishListFragment.newInstance(), "Wishlist")
//                            .addToBackStack("Wishlist")
//                            .commitAllowingStateLoss();
////                getSupportFragmentManager()
////                        .beginTransaction()
////                        .add(WishListFragment.newInstance(), "Wishlist")
////                        .addToBackStack("Wishlist")
////                        .commitAllowingStateLoss();
//                    actionBar.setTitle("Wishlist");
//                    pos = 3;
//                    break;
//
//                case "About":
//                    //imageViewHome.setVisibility(View.VISIBLE);
//                    //imageViewHome.setVisibility(View.VISIBLE);
//                    mTabLayout.setVisibility(View.VISIBLE);
//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.content_frame, About.newInstance(), "About")
//                            .addToBackStack("About")
//                            .commitAllowingStateLoss();
//                    actionBar.setTitle("About");
//                    pos = 4;
//                    break;
//
//                default:
////                mTabLayout.setVisibility(View.VISIBLE);
////                getSupportFragmentManager()
////                        .beginTransaction()
////                        .replace(R.id.content_frame, Home.newInstance(), "Home")
////                        .addToBackStack("Home")
////                        .commitAllowingStateLoss();
////                actionBar.setTitle("Ignite");
////                pos = 0;
////                break;
//            }
//
//        }
//    }

    public void ShowCoolStuffForEventCategories() {
        String coolText = "";

        if (allEventsList != null) {
            Random randomNumber = new Random();
            switch (randomNumber.nextInt(19)) {
                case 0:
                    coolText = "Solving NP-hard problems";
                    break;
                case 1:
                    coolText = "Distorting Space-Time Continuum";
                    break;
                case 2:
                    coolText = "Moving satellites into position";
                    break;
                case 3:
                    coolText = "Manipulating Time Fabric";
                    break;
                case 4:
                    coolText = "Cracking 2048-bit encryption";
                    break;
                case 5:
                    coolText = "Creating Neural Bridge";
                    break;
                case 6:
                    coolText = "Thinking about new cool stuff";
                    break;
                case 7:
                    coolText = "Negotiating with processor";
                    break;
                case 8:
                    coolText = "Requesting data from Illuminati";
                    break;
                case 9:
                    coolText = "Opening new wormholes";
                    break;
                case 10:
                    coolText = "Error 404\ndid not occur";
                    break;
                case 11:
                    coolText = "Calculating the answer to Life, Universe and Everything";
                    break;
                case 12:
                    coolText = "....INITIATING SELF-DESTRUCT SEQUENCE....\n5.. 4.. 3....";
                    break;
                case 13:
                    coolText = "Increasing Entropy of the Universe";
                    break;
                case 14:
                    coolText = "Stealing planes from Bermuda's Triangle";
                    break;
                case 15:
                    coolText = "Searching for Higgs Boson";
                    break;
                case 16:
                    coolText = "Deciding whether to load or not...";
                    break;
                case 17:
                    coolText = "Discovering what happened to Schrodinger's cat";
                    break;
                case 18:
                    coolText = "Finding Nemo\n  again";
                    break;
            }
            coolStuff.setVisibility(View.VISIBLE);
            TextView textViewCoolStuff = (TextView) coolStuff.findViewById(R.id.textViewCoolStuff);
            coolStuff.postDelayed(new Runnable() {
                @Override
                public void run() {
                    coolStuff.setVisibility(View.GONE);
                }
            }, 1207);

            textViewCoolStuff.setText(coolText);
//            Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Calibri.otf");
//            textViewCoolStuff.setTypeface(typeface);
        }
    }

    public void ShowCoolStuffForSchedule() {
        String coolText = "";

        if (isScheduleFinalized) {
            Random randomNumber = new Random();
            switch (randomNumber.nextInt(19)) {
                case 0:
                    coolText = "Solving NP-hard problems";
                    break;
                case 1:
                    coolText = "Distorting Space-Time Continuum";
                    break;
                case 2:
                    coolText = "Moving satellites into position";
                    break;
                case 3:
                    coolText = "Manipulating Time Fabric";
                    break;
                case 4:
                    coolText = "Cracking 2048-bit encryption";
                    break;
                case 5:
                    coolText = "Creating Neural Bridge";
                    break;
                case 6:
                    coolText = "Thinking about new cool stuff";
                    break;
                case 7:
                    coolText = "Negotiating with processor";
                    break;
                case 8:
                    coolText = "Requesting data from Illuminati!";
                    break;
                case 9:
                    coolText = "Opening new wormholes";
                    break;
                case 10:
                    coolText = "Error 404!\ndid not occur";
                    break;
                case 11:
                    coolText = "Calculating the answer to Life, Universe and Everything";
                    break;
                case 12:
                    coolText = "....INITIATING SELF-DESTRUCT SEQUENCE....\n5.. 4.. 3....";
                    break;
                case 13:
                    coolText = "Increasing Entropy of Universe!";
                    break;
                case 14:
                    coolText = "Stealing planes from Bermuda's Triangle";
                    break;
                case 15:
                    coolText = "Searching for Higgs Boson";
                    break;
                case 16:
                    coolText = "Deciding whether to load or not...";
                    break;
                case 17:
                    coolText = "Discovering what happened to Schrodinger's cat";
                    break;
                case 18:
                    coolText = "Finding Nemo\n  again";
                    break;
            }
            coolStuff.setVisibility(View.VISIBLE);
            TextView textViewCoolStuff = (TextView) coolStuff.findViewById(R.id.textViewCoolStuff);
            coolStuff.postDelayed(new Runnable() {
                @Override
                public void run() {
                    coolStuff.setVisibility(View.GONE);
                }
            }, 1207);

            textViewCoolStuff.setText(coolText);
        }
    }

    public void ShowCoolStuffForResults() {
        String coolText = "";

        if (areResultsAnnounced) {
            Random randomNumber = new Random();
            switch (randomNumber.nextInt(19)) {
                case 0:
                    coolText = "Solving NP-hard problems";
                    break;
                case 1:
                    coolText = "Distorting Space-Time Continuum";
                    break;
                case 2:
                    coolText = "Moving satellites into position";
                    break;
                case 3:
                    coolText = "Manipulating Time Fabric!";
                    break;
                case 4:
                    coolText = "Cracking 2048-bit encryption";
                    break;
                case 5:
                    coolText = "Creating Neural Bridge";
                    break;
                case 6:
                    coolText = "Thinking about new cool stuff";
                    break;
                case 7:
                    coolText = "Negotiating with processor";
                    break;
                case 8:
                    coolText = "Requesting data from Illuminati";
                    break;
                case 9:
                    coolText = "Opening new wormholes";
                    break;
                case 10:
                    coolText = "Error 404\ndid not occur";
                    break;
                case 11:
                    coolText = "Calculating the answer to Life, Universe and Everything";
                    break;
                case 12:
                    coolText = "....INITIATING SELF-DESTRUCT SEQUENCE....\n5.. 4.. 3....";
                    break;
                case 13:
                    coolText = "Increasing Entropy of Universe";
                    break;
                case 14:
                    coolText = "Stealing planes from Bermuda's Triangle";
                    break;
                case 15:
                    coolText = "Searching for Higgs Boson";
                    break;
                case 16:
                    coolText = "Deciding whether to load or not...";
                    break;
                case 17:
                    coolText = "Discovering what happened to Schrodinger's cat";
                    break;
                case 18:
                    coolText = "Finding Nemo\n  again";
                    break;
            }
            coolStuff.setVisibility(View.VISIBLE);
            TextView textViewCoolStuff = (TextView) coolStuff.findViewById(R.id.textViewCoolStuff);
            coolStuff.postDelayed(new Runnable() {
                @Override
                public void run() {
                    coolStuff.setVisibility(View.GONE);
                }
            }, 1207);

            textViewCoolStuff.setText(coolText);
        }
    }

    public void ShowCoolStuffForGame() {
        String coolText = "";

        Random randomNumber = new Random();
        switch (randomNumber.nextInt(19)) {
            case 0:
                coolText = "Solving NP-hard problems";
                break;
            case 1:
                coolText = "Distorting Space-Time Continuum";
                break;
            case 2:
                coolText = "Moving satellites into position";
                break;
            case 3:
                coolText = "Manipulating Time Fabric";
                break;
            case 4:
                coolText = "Cracking 2048-bit encryption";
                break;
            case 5:
                coolText = "Creating Neural Bridge";
                break;
            case 6:
                coolText = "Thinking about new cool stuff";
                break;
            case 7:
                coolText = "Negotiating with processor";
                break;
            case 8:
                coolText = "Requesting data from Illuminati";
                break;
            case 9:
                coolText = "Opening new wormholes";
                break;
            case 10:
                coolText = "Error 404\ndid not occur";
                break;
            case 11:
                coolText = "Calculating the answer to Life, Universe and Everything";
                break;
            case 12:
                coolText = "....INITIATING SELF-DESTRUCT SEQUENCE....\n5.. 4.. 3....";
                break;
            case 13:
                coolText = "Increasing Entropy of the Universe";
                break;
            case 14:
                coolText = "Stealing planes from Bermuda's Triangle";
                break;
            case 15:
                coolText = "Searching for Higgs Boson";
                break;
            case 16:
                coolText = "Deciding whether to load or not...";
                break;
            case 17:
                coolText = "Discovering what happened to Schrodinger's cat";
                break;
            case 18:
                coolText = "Finding Nemo\n  again";
                break;
        }

        coolStuff.setVisibility(View.VISIBLE);
        TextView textViewCoolStuff = (TextView) coolStuff.findViewById(R.id.textViewCoolStuff);
        coolStuff.postDelayed(new Runnable() {
            @Override
            public void run() {
                coolStuff.setVisibility(View.GONE);
            }
        }, 1207);

        textViewCoolStuff.setText(coolText);
//            Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Calibri.otf");
//            textViewCoolStuff.setTypeface(typeface);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        actionBar.setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
//        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
//        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (pos == 0 && isDetailsOpened) {
            //imageViewHome.setVisibility(View.VISIBLE);
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.content_frame, EventCategoriesFragment.newInstance(), "EventCategoryList")
//                    .addToBackStack("EventCategoryList")
//                    .commitAllowingStateLoss();
//            actionBar.setTitle("Event Categories");
//            isDetailsOpened = false;
            Log.d("Check", "hehe Schedule");
        } else if (pos == 1 && isDetailsOpened) {
            //imageViewHome.setVisibility(View.VISIBLE);
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.content_frame, Schedule.newInstance(), "Schedule")
//                    .commitAllowingStateLoss();
//            actionBar.setTitle("Schedule");
//            Log.d("Check", "hehe Schedule");
//            isDetailsOpened = false;
        } else if (pos != 6) {
           /* finish();
            startActivity(getIntent());*/
            mTabLayout.setVisibility(View.GONE);
            int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
            while (fragmentCount-- > 0)
                getSupportFragmentManager().popBackStack();
            actionBar.setTitle("Ignite");
            pos = 6;
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public interface AsyncResponse {
        void processFinish(JSONArray output, String SPName);
    }
}


