package com.dev.alltrack.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.dev.alltrack.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initInstances();
    }

    private void initInstances() {

    }

}
