package com.example.mytodo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavController navController;
    private Map<Integer, Integer> menuFragmentMap;

    // create a menu bar for the apps
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();  // pop up menu
        menuInflater.inflate(R.menu.main,menu); // get menu from xml
        return super.onCreateOptionsMenu(menu);  // return menu object
    }

    // what happen if the user select the menu item - add button
    // when the user select the menu item add in the menu bar
    // create an intent object, we used an explicit intent here
    // bring the user to another page or activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about_us) {
            // Inflate the about_us layout
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.about_us, null);

            // Find views within the inflated layout
            TextView header = view.findViewById(R.id.about_us_header);
            TextView content = view.findViewById(R.id.about_us_paragraph);
            // Add more views as needed

            // Example: Displaying the inflated view in an AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;  // Menu item selection handled

        } else if (item.getItemId() == R.id.productivity_tips) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.productivity_tips, null);

            // Find views within the inflated layout
            TextView header = view.findViewById(R.id.productive_header);
            TextView content1 = view.findViewById(R.id.productive1_paragraph);
            TextView content2 = view.findViewById(R.id.productive2_paragraph);
            TextView content3 = view.findViewById(R.id.productive3_paragraph);
            TextView content4 = view.findViewById(R.id.productive4_paragraph);
            TextView content5 = view.findViewById(R.id.productive5_paragraph);

            // Example: Displaying the inflated view in an AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);  // Fallback to superclass behavior
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // setup the shared preferences object in this app
        // for private use only, the SP aka file can be accessed by this app only
        SharedPreferences sharedPreferences =
                getApplicationContext().getSharedPreferences
                        ("com.example.mytodo", Context.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_task, R.id.nav_reminder, R.id.nav_events, R.id.nav_notes)
                .setOpenableLayout(drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Initialize the map
        menuFragmentMap = new HashMap<>();
        menuFragmentMap.put(R.id.nav_task, R.id.nav_task);
        menuFragmentMap.put(R.id.nav_reminder, R.id.nav_reminder);
        menuFragmentMap.put(R.id.nav_events, R.id.nav_events);
        menuFragmentMap.put(R.id.nav_notes, R.id.nav_notes);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Integer fragmentId = menuFragmentMap.get(itemId);

                if (fragmentId != null) {
                    navController.navigate(fragmentId);
                    drawerLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });

        // Set default fragment
        if (savedInstanceState == null) {
            navController.navigate(R.id.nav_task);
            navigationView.setCheckedItem(R.id.nav_task);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
