package com.example.movies.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.movies.R;
import com.example.movies.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        new Handler().postDelayed(new Runnable()
        {

            public void run()
            {


                SplashActivity.this.finish();
                // Create an Intent that will start the Popular Artists activity.
                Intent mainIntent = new Intent(SplashActivity.this, PopularArtistsActivity.class);
                SplashActivity.this.startActivity(mainIntent);

            }
        }, Constants.SPLASH_DELAY_TIME);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
