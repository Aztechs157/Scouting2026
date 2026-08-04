package com.example.roboticsscoutingmatchapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * Launcher activity for the app. Handles competition selection and data persistence checks.
 * Serves as the home screen with navigation to the Data Dashboard.
 */
public class ActivityCompetitionSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_competition_selection);

        // Sidebar/Navigation Drawer Setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Adjust for system status bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_bar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Centralized navigation logic
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) {
                // Open Data Dashboard
                startActivity(new Intent(this, activityDataShowing.class));
            } else if (id == R.id.nav_scout) {
                // Reset/Restart Scouting flow
                Intent intent = new Intent(this, ActivityCompetitionSelection.class);
                intent.putExtra("chooseNewCompetition", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        U u = new U();
        RadioGroup competitionRadioGroup = findViewById(R.id.competition_radio_group);
        Button saveButton = findViewById(R.id.save_button);
        Toast unfilledMessage = new Toast(this);
        unfilledMessage.setDuration(Toast.LENGTH_SHORT);

        String scoutName;
        boolean changeCompetition = false;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            scoutName = extras.getString("scoutName", "");
            changeCompetition = extras.getBoolean("chooseNewCompetition", false);
        } else {
            scoutName = "";
        }

        // Persistence check: If a competition was already selected today, skip this screen
        final String FILENAME = "matchAndDate";
        File file = new File(this.getFilesDir(), FILENAME);
        Calendar now = Calendar.getInstance();
        String currentDate = "";
        currentDate += now.get(Calendar.YEAR);
        currentDate += now.get(Calendar.MONTH);
        currentDate += now.get(Calendar.DAY_OF_MONTH);

        String dateAndMatchString = "";

        if(!changeCompetition){
            if (file.exists()) {
                try {
                    FileInputStream fis = this.openFileInput(FILENAME);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                    dateAndMatchString += reader.readLine();
                    reader.close();
                } catch (FileNotFoundException e) {
                    Log.e("ActivityComp", "Persisted file not found", e);
                } catch (IOException e) {
                    Log.e("ActivityComp", "Read error", e);
                }
            }
            if (!dateAndMatchString.isEmpty()) {
                String fileDate = u.untilNextComma(dateAndMatchString);
                if (fileDate.equals(currentDate)) {
                    // Fast-forward to scouting if date matches
                    Intent i = new Intent(this, activityPreMatch.class);
                    i.putExtra("competition", u.nextCommaOn(dateAndMatchString));
                    i.putExtra("scoutName", scoutName);
                    this.startActivity(i);
                }
            }
        }

        String finalCurrentDate = currentDate;
        saveButton.setOnClickListener((l)->{
            String response = "";
            if(u.getData(competitionRadioGroup).isEmpty()){
                response = "Please choose current competition to be scouting";
            }else{
                // Save selection locally
                String fileContents = finalCurrentDate + "," + u.getData(competitionRadioGroup);
                try(FileOutputStream fos = this.openFileOutput(FILENAME, Context.MODE_PRIVATE)){
                    fos.write(fileContents.getBytes());
                } catch(IOException e){
                    Log.e("ActivityComp", "Write error", e);
                }
                
                // Move to Pre-Match
                Intent i = new Intent(this, activityPreMatch.class);
                i.putExtra("competition", u.getData(competitionRadioGroup));
                i.putExtra("scoutName", scoutName);
                this.startActivity(i);
            }
            if(!response.isEmpty()){
                unfilledMessage.setText(response);
                unfilledMessage.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Handle Sidebar Drawer if open
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
