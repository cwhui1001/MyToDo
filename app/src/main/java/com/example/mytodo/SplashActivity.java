package com.example.mytodo;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // create a scheduled task or process to close the current page with finish() method
        TimerTask task = new TimerTask( ) {

            @Override
            public void run() {
                finish(); // close this activity first
                startActivity(new Intent(SplashActivity.this, MainActivity.class));  // open a new activity
            }
        };

        // run the scheduled task or process after 5000ms or 5s
        Timer opening = new Timer( );
        opening.schedule(task,5000);
    }
}