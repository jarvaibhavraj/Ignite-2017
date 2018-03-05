package xyz.mrdeveloper.ignite;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Vaibhav on 08-02-2017.
 */

public class SplashScreenActivity extends AppCompatActivity {

    TextView textSplashMrDeveloper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatusBar));
        }

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Regular.ttf");
//        typeface = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");

        textSplashMrDeveloper = (TextView) findViewById(R.id.textSplashMrDeveloper);
        textSplashMrDeveloper.setTypeface(typeface);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}