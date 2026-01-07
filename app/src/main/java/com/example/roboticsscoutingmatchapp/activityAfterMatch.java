package com.example.roboticsscoutingmatchapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class activityAfterMatch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Default code start
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_match);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        }); // Default code end

        U u = new U(); // defines new utilities object to be used

        // *---Defines all the components on the current page as variables---*

        CheckBox floorIntake = findViewById(R.id.floor_intake);
        CheckBox humanPlayerStation = findViewById(R.id.human_player_intake);

        RadioGroup stopReasonGroup = findViewById(R.id.why_robot_stopped);
        RadioButton diedButton = findViewById(R.id.died);
        RadioButton tippedButton = findViewById(R.id.tipped);
        RadioButton physicallyBrokeButton = findViewById(R.id.robot_break);
        RadioButton eStoppedButton = findViewById(R.id.e_stopped);
        RadioButton notStoppedButton = findViewById(R.id.never_stopped);

        RadioGroup defenseReceivedGroup = findViewById(R.id.defense_received);
        RadioButton noDefenseButton = findViewById(R.id.defense_none);
        RadioButton lightDefenseButton = findViewById(R.id.defense_light);
        RadioButton heavyDefenseButton = findViewById(R.id.defense_heavy);

        RadioGroup rankGroup = findViewById(R.id.ranking_of_teams);
        RadioButton rank1Button = findViewById(R.id.rank1);
        RadioButton rank2Button = findViewById(R.id.rank2);
        RadioButton rank3Button = findViewById(R.id.rank3);

        EditText finalText = findViewById(R.id.End_of_app_text);
        Button saveButton = findViewById(R.id.save_button);
        Button backButton = findViewById(R.id.back_button);
        Toast unfilledMessage = new Toast(this); // Creates a Toast (pop-up message object)
        unfilledMessage.setDuration(Toast.LENGTH_SHORT); // Sets it to only be angry a short while

        String preMatchSaveString, autoSaveString,  // Define savestring String objects
                teleOpSaveString, postMatchSaveString;
        Bundle extras = getIntent().getExtras(); // Gets the savestrings and stores them to extras
        if(extras != null){ // If savestrings exist, set individual strings to values
            preMatchSaveString = extras.getString("preMatch", "");
            autoSaveString = extras.getString("auto", "");
            teleOpSaveString = extras.getString("teleOp", "");
            postMatchSaveString = extras.getString("postMatch", "");
        } else { // If savestrings don't exist, no value
            preMatchSaveString = "";
            autoSaveString = "";
            teleOpSaveString = "";
            postMatchSaveString = "";
        }

        if(!postMatchSaveString.isEmpty()){ // Sets all the components to the values within the savestring
            // Coral floor pickup able | Coral Source pickup able | Defense received |
            // Stop reason | team rank among alliance | other comments questions or concerns ||
            floorIntake.setChecked(Boolean.parseBoolean(u.untilNextComma(postMatchSaveString))); // Sets the value to the parsed value in the savestring
            postMatchSaveString = u.nextCommaOn(postMatchSaveString); // removes the value from the savestring
            // So on an so forth
            humanPlayerStation.setChecked(Boolean.parseBoolean(u.untilNextComma(postMatchSaveString)));
            postMatchSaveString = u.nextCommaOn(postMatchSaveString);

            if(u.untilNextComma(postMatchSaveString).equals("No Defense")){
                noDefenseButton.toggle();
            }else if(u.untilNextComma(postMatchSaveString).equals("Light Defense")){
                lightDefenseButton.toggle();
            }else if(u.untilNextComma(postMatchSaveString).equals("Heavy Defense")){
                heavyDefenseButton.toggle();
            }
            postMatchSaveString = u.nextCommaOn(postMatchSaveString);

            String stopReasonString = u.untilNextComma(postMatchSaveString);
            switch (stopReasonString) {
                case "Died":
                    diedButton.toggle();
                    break;
                case "Tipped":
                    tippedButton.toggle();
                    break;
                case "Physically Broke":
                    physicallyBrokeButton.toggle();
                    break;
                case "E-stopped":
                    eStoppedButton.toggle();
                    break;
                case "Not Stopped":
                    notStoppedButton.toggle();
                    break;
            }
            postMatchSaveString = u.nextCommaOn(postMatchSaveString);

            String teamRank = u.untilNextComma(postMatchSaveString);
            switch(teamRank){
                case "Rank 1":
                    rank1Button.toggle();
                    break;
                case "Rank 2":
                    rank2Button.toggle();
                    break;
                case "Rank 3":
                    rank3Button.toggle();
                    break;
            }
            postMatchSaveString = u.nextCommaOn(postMatchSaveString);

            finalText.setText(u.untilNextComma(postMatchSaveString));
            postMatchSaveString = u.nextCommaOn(postMatchSaveString);
        }

        backButton.setOnClickListener((l)->{ // Sets current savestring to current values of components
            // Because it's the back button, these values can have no value
            // Coral floor pickup able | Coral Source pickup able | Defense received |
            // Stop reason | team rank among alliance | other comments questions or concerns ||
            String afterMatchInfo = "";

            afterMatchInfo += u.getData(floorIntake) + ",";
            afterMatchInfo += u.getData(humanPlayerStation) + ",";
            afterMatchInfo += u.getData(defenseReceivedGroup) + ",";
            afterMatchInfo += u.getData(stopReasonGroup) + ",";
            afterMatchInfo += u.getData(rankGroup) + ",";
            afterMatchInfo += u.stripText(finalText.getText().toString(), U.DELIMITER) + ",";

            Intent i = new Intent(this, activityTeleOp.class);
            i.putExtra("preMatch", preMatchSaveString);
            i.putExtra("auto", autoSaveString);
            i.putExtra("teleOp", teleOpSaveString);
            i.putExtra("postMatch", afterMatchInfo);

            this.startActivity(i);
        });

        saveButton.setOnClickListener((l)->{ // Sets current savestring to current component values
            // Checks that components that need to be filled in are filled in
            String response = "";
            if(u.getData(defenseReceivedGroup).isEmpty()) // If a component is not filled in
                response = "Please fill in defense received"; // Sets the toast message to error message
            else if(u.getData(rankGroup).isEmpty())
                response = "Please fill in rank";
            else if(u.getData(stopReasonGroup).isEmpty())
                response = "Please fill in stop reason";
            else{ // If nothing is wrong, keep filling everything in
                // Coral floor pickup able | Coral Source pickup able | Defense received |
                // Stop reason | team rank among alliance | other comments questions or concerns ||
                String postMatchInfo = "";
                postMatchInfo += u.getData(floorIntake) + ",";
                postMatchInfo += u.getData(humanPlayerStation) + ",";
                postMatchInfo += u.getData(defenseReceivedGroup) + ",";
                postMatchInfo += u.getData(stopReasonGroup) + ",";
                postMatchInfo += u.getData(rankGroup) + ",";
                postMatchInfo += u.stripText(u.getData(finalText), u.DELIMITER) + ",";

                // Competition Location | Save Version | Scout Name | Team # | Team Color |
                // Match Number | Preloaded coral ||
                // Filename format: "match_scouting_" + [team_number] + [match number]
                String teamNumber = u.untilNextComma(u.nextCommaOn(u.nextCommaOn(u.nextCommaOn(preMatchSaveString))));
                String matchNumber = u.untilNextComma(u.nextCommaOn(u.nextCommaOn(u.nextCommaOn(u.nextCommaOn(u.nextCommaOn(preMatchSaveString))))));
                String competitionLocation = u.untilNextComma(preMatchSaveString);
                String scoutName = u.untilNextComma(u.nextCommaOn(u.nextCommaOn(preMatchSaveString)));
                String fileName = "match_scouting_"+ competitionLocation + "_" + teamNumber + "_" + matchNumber + ".csv";
                Log.d("File Name: ", fileName);

                try {
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName);
                    if (!file.exists()) {
                        boolean fileCreated = file.createNewFile();
                        if(!fileCreated) {
                            Toast.makeText(this, "File " + fileName + " has not been created", Toast.LENGTH_SHORT).show();
                            Log.d("File written: ", "File not created");
                        }else {
                            Toast.makeText(this, "File " + fileName + " has been created", Toast.LENGTH_SHORT).show();
                            Log.d("File written: ", "File created");
                        }
                    }else{
                        Log.d("File already existed", "File already existed");
                    }

                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);

                    bw.write(preMatchSaveString);
                    bw.write(autoSaveString);
                    bw.write(teleOpSaveString);
                    bw.write(postMatchInfo);
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    Log.d("Error thrown:", "+==========+Error Thrown+==========+");
                    Log.getStackTraceString(e);
                }
                Intent i = new Intent(this, ActivityCompetitionSelection.class);
                i.putExtra("scoutName", scoutName);
                this.startActivity(i);
            }

            if(!response.isEmpty()){
                unfilledMessage.setText(response);
                unfilledMessage.show();
            }
        });
    }
}