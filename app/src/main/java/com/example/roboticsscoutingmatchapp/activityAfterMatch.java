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

/**
 * Activity for collecting Post-Match qualitative data (Field navigation, robot state, stop reason).
 * Finalizes and saves the scouting report as a CSV file.
 */
public class activityAfterMatch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_match);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        U u = new U();

        // 2026 REBUILT Metric Collection
        CheckBox overBump = findViewById(R.id.over_bump);
        CheckBox underTrench = findViewById(R.id.under_trench);
        CheckBox playedDefense = findViewById(R.id.played_defense);
        CheckBox collectedFuel = findViewById(R.id.collected_fuel);
        CheckBox passedFuel = findViewById(R.id.passed_fuel);
        CheckBox inactive = findViewById(R.id.inactive);
        CheckBox other = findViewById(R.id.other);

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
        
        Toast unfilledMessage = new Toast(this);
        unfilledMessage.setDuration(Toast.LENGTH_SHORT);

        // Load intent data for UI state persistence
        String preMatchSaveString, autoSaveString,
                teleOpSaveString, postMatchSaveString;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            preMatchSaveString = extras.getString("preMatch", "");
            autoSaveString = extras.getString("auto", "");
            teleOpSaveString = extras.getString("teleOp", "");
            postMatchSaveString = extras.getString("postMatch", "");
        } else {
            preMatchSaveString = "";
            autoSaveString = "";
            teleOpSaveString = "";
            postMatchSaveString = "";
        }

        // Parse backward-passed post-match data strings to restore UI state
        if(!postMatchSaveString.isEmpty()){
            overBump.setChecked(Boolean.parseBoolean(u.untilNextComma(postMatchSaveString)));
            postMatchSaveString = u.nextCommaOn(postMatchSaveString);
            underTrench.setChecked(Boolean.parseBoolean(u.untilNextComma(postMatchSaveString)));
            postMatchSaveString = u.nextCommaOn(postMatchSaveString);
            playedDefense.setChecked(Boolean.parseBoolean(u.untilNextComma(postMatchSaveString)));
            postMatchSaveString = u.nextCommaOn(postMatchSaveString);
            collectedFuel.setChecked(Boolean.parseBoolean(u.untilNextComma(postMatchSaveString)));
            postMatchSaveString = u.nextCommaOn(postMatchSaveString);
            passedFuel.setChecked(Boolean.parseBoolean(u.untilNextComma(postMatchSaveString)));
            postMatchSaveString = u.nextCommaOn(postMatchSaveString);
            inactive.setChecked(Boolean.parseBoolean(u.untilNextComma(postMatchSaveString)));
            postMatchSaveString = u.nextCommaOn(postMatchSaveString);
            other.setChecked(Boolean.parseBoolean(u.untilNextComma(postMatchSaveString)));
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
                case "Died": diedButton.toggle(); break;
                case "Tipped": tippedButton.toggle(); break;
                case "Physically Broke": physicallyBrokeButton.toggle(); break;
                case "E-stopped": eStoppedButton.toggle(); break;
                case "Not Stopped": notStoppedButton.toggle(); break;
            }
            postMatchSaveString = u.nextCommaOn(postMatchSaveString);

            String teamRank = u.untilNextComma(postMatchSaveString);
            switch(teamRank){
                case "Rank 1": rank1Button.toggle(); break;
                case "Rank 2": rank2Button.toggle(); break;
                case "Rank 3": rank3Button.toggle(); break;
            }
            postMatchSaveString = u.nextCommaOn(postMatchSaveString);

            finalText.setText(u.untilNextComma(postMatchSaveString));
            postMatchSaveString = u.nextCommaOn(postMatchSaveString);
        }

        backButton.setOnClickListener((l)->{
            String afterMatchInfo = "";
            // Maintain data integrity for the 29-column schema even on back-nav
            afterMatchInfo += u.getData(overBump) + ",";
            afterMatchInfo += u.getData(underTrench) + ",";
            afterMatchInfo += u.getData(playedDefense) + ",";
            afterMatchInfo += u.getData(collectedFuel) + ",";
            afterMatchInfo += u.getData(passedFuel) + ",";
            afterMatchInfo += u.getData(inactive) + ",";
            afterMatchInfo += u.getData(other) + ",";
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

        saveButton.setOnClickListener((l)->{
            String response = "";
            if(u.getData(defenseReceivedGroup).isEmpty())
                response = "Please fill in defense received";
            else if(u.getData(rankGroup).isEmpty())
                response = "Please fill in rank";
            else if(u.getData(stopReasonGroup).isEmpty())
                response = "Please fill in stop reason";
            else{
                String postMatchInfo = "";
                postMatchInfo += u.getData(overBump) + ",";
                postMatchInfo += u.getData(underTrench) + ",";
                postMatchInfo += u.getData(playedDefense) + ",";
                postMatchInfo += u.getData(collectedFuel) + ",";
                postMatchInfo += u.getData(passedFuel) + ",";
                postMatchInfo += u.getData(inactive) + ",";
                postMatchInfo += u.getData(other) + ",";
                postMatchInfo += u.getData(defenseReceivedGroup) + ",";
                postMatchInfo += u.getData(stopReasonGroup) + ",";
                postMatchInfo += u.getData(rankGroup) + ",";
                postMatchInfo += u.stripText(u.getData(finalText), u.DELIMITER) + ",";

                // Compile final CSV string and write to external storage (Documents directory)
                // Filename format: match_scouting_[comp]_[team]_[match].csv
                String teamNumber = u.untilNextComma(u.nextCommaOn(u.nextCommaOn(u.nextCommaOn(preMatchSaveString))));
                String matchNumber = u.untilNextComma(u.nextCommaOn(u.nextCommaOn(u.nextCommaOn(u.nextCommaOn(u.nextCommaOn(preMatchSaveString))))));
                String competitionLocation = u.untilNextComma(preMatchSaveString);
                String scoutName = u.untilNextComma(u.nextCommaOn(u.nextCommaOn(preMatchSaveString)));
                String fileName = "match_scouting_"+ competitionLocation + "_" + teamNumber + "_" + matchNumber + ".csv";

                try {
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    
                    // Final Write: Pre + Auto + Tele + Post = 29 Columns
                    bw.write(preMatchSaveString);
                    bw.write(autoSaveString);
                    bw.write(teleOpSaveString);
                    bw.write(postMatchInfo);
                    bw.flush();
                    bw.close();
                    Toast.makeText(this, "File " + fileName + " Saved", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.e("activityAfterMatch", "File save failed", e);
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
