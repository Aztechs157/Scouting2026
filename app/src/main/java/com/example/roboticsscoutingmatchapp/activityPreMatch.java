package com.example.roboticsscoutingmatchapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class activityPreMatch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Defines an object for the utility file because of weird compat with static methods
        U u = new U();

        /*
         Checks for if there is any data sent over with the intent when switching to current
         activity, save strings will be compiled and saved as csv in final activity page
         */
        String preMatchSaveString, autoSaveString,
                teleOpSaveString, postMatchSaveString, competitionString, scoutNameString;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            preMatchSaveString = extras.getString("preMatch", "");
            autoSaveString = extras.getString("auto", "");
            teleOpSaveString = extras.getString("teleOp", "");
            postMatchSaveString = extras.getString("postMatch", "");
            competitionString = extras.getString("competition", "Test");
            scoutNameString = extras.getString("scoutName", "");
        } else {
            preMatchSaveString = "";
            autoSaveString = "";
            teleOpSaveString = "";
            postMatchSaveString = "";
            competitionString = "Test";
            scoutNameString = "";
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pre_match);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Fix status bar overlap for the AppBar and the Navigation Drawer
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_bar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_scout) {
                Intent intent = new Intent(this, ActivityCompetitionSelection.class);
                intent.putExtra("chooseNewCompetition", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (id == R.id.nav_dashboard) {
                startActivity(new Intent(this, activityDataShowing.class));
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        // Defining all the relevant components in the activity
        EditText scoutName = findViewById(R.id.scout_name);
        EditText matchNumber = findViewById(R.id.match_number);
        EditText teamNumber = findViewById(R.id.team_number);
        RadioGroup teamColorRadioGroup = findViewById(R.id.team_color_radio_group);
        CheckBox preloadedFuel = findViewById(R.id.checkBox_preloaded_fuel);
        Button saveButton = findViewById(R.id.save_button);
        Button backButton = findViewById(R.id.back_button);
        if(!scoutNameString.isEmpty()){
            scoutName.setText(scoutNameString);
        }

        if(!preMatchSaveString.isEmpty()){
            competitionString = u.untilNextComma(preMatchSaveString);
            preMatchSaveString = u.nextCommaOn(preMatchSaveString); // remove competition
            preMatchSaveString = u.nextCommaOn(preMatchSaveString); // remove data version
            scoutName.setText(u.untilNextComma(preMatchSaveString));
            preMatchSaveString = u.nextCommaOn(preMatchSaveString); // remove scout name
            teamNumber.setText(u.untilNextComma(preMatchSaveString));
            preMatchSaveString = u.nextCommaOn(preMatchSaveString); // remove team number
            if(u.untilNextComma(preMatchSaveString).equalsIgnoreCase("red")){
                teamColorRadioGroup.check(R.id.team_color_red);
            }else{
                teamColorRadioGroup.check(R.id.team_color_blue);
            }
            preMatchSaveString = u.nextCommaOn(preMatchSaveString); // remove team color
            matchNumber.setText(u.untilNextComma(preMatchSaveString));
            preMatchSaveString = u.nextCommaOn(preMatchSaveString); // remove match number
            preloadedFuel.setChecked(Boolean.parseBoolean(u.untilNextComma(preMatchSaveString)));
            preMatchSaveString = u.nextCommaOn(preMatchSaveString); // Remove Checked

        }

        // Defines a toast (pop-up) to be used when a field is left unfilled
        Toast unfilledMessage = new Toast(this);
        unfilledMessage.setDuration(Toast.LENGTH_SHORT);

        String finalCompetitionString = competitionString;
        saveButton.setOnClickListener((l) -> {
            // Check if all fields are full
//            findViewById(R.id.scroll_view);
            String response = "";

            if(u.getData(scoutName).isEmpty()){
                response = getResources().getString(R.string.prompt_scout_name) + " " + getResources().getString(R.string.is_empty_identifier);
            }else if(u.getData(matchNumber).isEmpty()){
                response = getResources().getString(R.string.prompt_match_number) + " " + getResources().getString(R.string.is_empty_identifier);
            }else if(u.getData(teamNumber).isEmpty()){
                response = getResources().getString(R.string.prompt_team_number) + " " + getResources().getString(R.string.is_empty_identifier);
            }else if(u.getData(teamColorRadioGroup).isEmpty()){
                response = "Please choose a team color";
            }else{
                // Utilizes "savestrings"
                Intent i = new Intent(this, activityAutonomous.class);
                String preMatchInfo = "";
                preMatchInfo += finalCompetitionString + ","; //TODO: Add competition
                preMatchInfo += u.DATA_VERSION + ",";
                preMatchInfo += u.stripText(u.getData(scoutName), u.DELIMITER_AND_WHITESPACE) + ",";
                preMatchInfo += u.stripText(u.getData(teamNumber)) + ",";
                preMatchInfo += u.stripText(u.getData(teamColorRadioGroup)) + ",";
                preMatchInfo += u.stripText(u.getData(matchNumber)) + ",";
                preMatchInfo += u.stripText(u.getData(preloadedFuel)) + ",";

                i.putExtra("preMatch", preMatchInfo);
                i.putExtra("auto", autoSaveString);
                i.putExtra("teleOp", teleOpSaveString);
                i.putExtra("postMatch", postMatchSaveString);

                this.startActivity(i);
            }

            if(!response.isBlank()){
                unfilledMessage.setText(response);
                unfilledMessage.show();
            }
        });

        backButton.setOnClickListener((l)->{
           Intent i = new Intent(this, ActivityCompetitionSelection.class);
           i.putExtra("chooseNewCompetition", true);
           this.startActivity(i);
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
