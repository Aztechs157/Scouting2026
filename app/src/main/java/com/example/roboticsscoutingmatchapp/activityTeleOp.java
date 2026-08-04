package com.example.roboticsscoutingmatchapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Activity for collecting TeleOperated period data.
 * Tracks fuel scoring, accuracy position, and endgame climb status.
 */
public class activityTeleOp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tele_op);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        U u = new U();

        // Load intent state
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

        // UI Binding - Scoring & Accuracy
        EditText FSField = findViewById(R.id.edit_text_fs);
        Button FS1plus = findViewById(R.id.up_count_button_fs1);
        Button FS5plus = findViewById(R.id.up_count_button_fs5);
        Button FS10plus = findViewById(R.id.up_count_button_fs10);
        Button FS15plus = findViewById(R.id.up_count_button_fs15);
        Button FS20plus = findViewById(R.id.up_count_button_fs20);

        Button FS1minus = findViewById(R.id.down_count_button_fs1);
        Button FS5minus = findViewById(R.id.down_count_button_fs5);
        Button FS10minus = findViewById(R.id.down_count_button_fs10);
        Button FS15minus = findViewById(R.id.down_count_button_fs15);
        Button FS20minus = findViewById(R.id.down_count_button_fs20);

        RadioGroup accuracyGroup = findViewById(R.id.accuracy_radio_group);
        RadioButton lessThanTen = findViewById(R.id.underTen);
        RadioButton twentyFivePercent = findViewById(R.id.twentyFivePercent);
        RadioButton fiftyPercent = findViewById(R.id.fiftyPercent);
        RadioButton seventyFivePercent = findViewById(R.id.seventyFivePercent);
        RadioButton overNinetyFive = findViewById(R.id.overNinetyFive);

        // UI Binding - Passing & Endgame
        EditText FPField = findViewById(R.id.edit_text_fp);
        Button FP1plus = findViewById(R.id.up_count_button_fp1);
        Button FP5plus = findViewById(R.id.up_count_button_fp5);
        Button FP10plus = findViewById(R.id.up_count_button_fp10);
        Button FP15plus = findViewById(R.id.up_count_button_fp15);
        Button FP20plus = findViewById(R.id.up_count_button_fp20);

        Button FP1minus = findViewById(R.id.down_count_button_fp1);
        Button FP5minus = findViewById(R.id.down_count_button_fp5);
        Button FP10minus = findViewById(R.id.down_count_button_fp10);
        Button FP15minus = findViewById(R.id.down_count_button_fp15);
        Button FP20minus = findViewById(R.id.down_count_button_fp20);

        RadioGroup parkRadioGroup = findViewById(R.id.endgame_location);
        RadioButton level1Button = findViewById(R.id.level1_climb);
        RadioButton level2Button = findViewById(R.id.level2_climb);
        RadioButton level3Button = findViewById(R.id.level3_climb);
        RadioButton noneButton = findViewById(R.id.nothing);

        RadioGroup accuracyRadioGroup = findViewById(R.id.accuracy_position);
        RadioButton pacManButton = findViewById(R.id.pac_manning);
        RadioButton standStillButton = findViewById(R.id.stand_still);
        RadioButton noDifferenceButton = findViewById(R.id.no_difference);
        RadioButton badAccuracyButton = findViewById(R.id.bad_accuracy);

        RadioGroup endgameTimeGroup = findViewById(R.id.endgame_time);
        RadioButton twentyFiveButton = findViewById(R.id.twenty_five);
        RadioButton twentyButton = findViewById(R.id.twenty);
        RadioButton fifteenButton = findViewById(R.id.fifteen);
        RadioButton tenButton = findViewById(R.id.ten);
        RadioButton fiveButton = findViewById(R.id.five);
        RadioButton zeroButton = findViewById(R.id.zero);

        Button backButton = findViewById(R.id.back_button);
        Button saveButton = findViewById(R.id.save_button);

        Toast unfilledMessage = new Toast(this);
        unfilledMessage.setDuration(Toast.LENGTH_SHORT);

        // Parse saved teleop data to restore UI state
        if(!teleOpSaveString.isEmpty()){
            FSField.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);
            FPField.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);

            String currentButton = u.untilNextComma(teleOpSaveString);
            switch(currentButton){
                case "Level 1": level1Button.toggle(); break;
                case "Level 2": level2Button.toggle(); break;
                case "Level 3": level3Button.toggle(); break;
                case "None": noneButton.toggle(); break;
            }
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);

            String timeToHang = u.untilNextComma(teleOpSaveString);
            switch(timeToHang){
                case "25": twentyFiveButton.toggle(); break;
                case "20": twentyButton.toggle(); break;
                case "15": fifteenButton.toggle(); break;
                case "10": tenButton.toggle(); break;
                case "5": fiveButton.toggle(); break;
                case "0": zeroButton.toggle(); break;
            }
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);

            String accuracyPos = u.untilNextComma(teleOpSaveString);
            switch(accuracyPos){
                case "When moving": pacManButton.toggle(); break;
                case "When unmoving": standStillButton.toggle(); break;
                case "About the same": noDifferenceButton.toggle(); break;
                case "Consistently bad accuracy": badAccuracyButton.toggle(); break;
            }
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);

            String accuracyChoice = u.untilNextComma(teleOpSaveString);
            switch(accuracyChoice){
                case "Less Than 10%": lessThanTen.toggle(); break;
                case "25%": twentyFivePercent.toggle(); break;
                case "50%": fiftyPercent.toggle(); break;
                case "75%": seventyFivePercent.toggle(); break;
                case "More Than 95%": overNinetyFive.toggle(); break;
            }
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);
        }

        // Increment/Decrement Listeners for TeleOp Fuel
        FS1plus.setOnClickListener((l)->u.incrementText(FSField));
        FS1minus.setOnClickListener((l)->u.incrementText(FSField, -1));
        FS5plus.setOnClickListener((l)->u.incrementText(FSField, +5));
        FS5minus.setOnClickListener((l)->u.incrementText(FSField, -5));
        FS10plus.setOnClickListener((l)->u.incrementText(FSField, +10));
        FS10minus.setOnClickListener((l)->u.incrementText(FSField, -10));
        FS15plus.setOnClickListener((l)->u.incrementText(FSField, +15));
        FS15minus.setOnClickListener((l)->u.incrementText(FSField, -15));
        FS20plus.setOnClickListener((l)->u.incrementText(FSField, +20));
        FS20minus.setOnClickListener((l)->u.incrementText(FSField, -20));

        FP1plus.setOnClickListener((l)->u.incrementText(FPField));
        FP1minus.setOnClickListener((l)->u.incrementText(FPField, -1));
        FP5plus.setOnClickListener((l)->u.incrementText(FPField, +5));
        FP5minus.setOnClickListener((l)->u.incrementText(FPField, -5));
        FP10plus.setOnClickListener((l)->u.incrementText(FPField, +10));
        FP10minus.setOnClickListener((l)->u.incrementText(FPField, -10));
        FP15plus.setOnClickListener((l)->u.incrementText(FPField, +15));
        FP15minus.setOnClickListener((l)->u.incrementText(FPField, -15));
        FP20plus.setOnClickListener((l)->u.incrementText(FPField, +20));
        FP20minus.setOnClickListener((l)->u.incrementText(FPField, -20));

        backButton.setOnClickListener((l)->{
            String teleOpInfo = "";
            teleOpInfo += u.getData(FSField) + ",";
            teleOpInfo += u.getData(accuracyGroup) + ",";
            teleOpInfo += u.getData(FPField) + ",";
            teleOpInfo += u.getData(parkRadioGroup) + ",";
            teleOpInfo += u.getData(endgameTimeGroup) + ",";
            teleOpInfo += u.getData(accuracyRadioGroup) + ",";

            Intent i = new Intent(this, activityAutonomous.class);
            i.putExtra("preMatch", preMatchSaveString);
            i.putExtra("auto", autoSaveString);
            i.putExtra("teleOp", teleOpInfo);
            i.putExtra("postMatch", postMatchSaveString);
            this.startActivity(i);
        });

        saveButton.setOnClickListener((l) -> {
            String response = "";
            if (u.getData(FSField).isEmpty()) FSField.setText("0");
            if(u.getData(FPField).isEmpty()) FPField.setText("0");
            
            if ((u.getData(accuracyGroup).isEmpty()))
                response = "Please give accuracy";
            else if ((u.getData(accuracyRadioGroup).isEmpty()))
                response = "Please give accuracy position";
            else if(u.getData(parkRadioGroup).isEmpty())
                response = "Please select an endgame position";
            else if(u.getData(endgameTimeGroup).isEmpty())
                response = "Please select park time";
            else{
                String teleOpInfo = "";
                teleOpInfo += u.getData(FSField) + ",";
                teleOpInfo += u.getData(accuracyGroup) + ",";
                teleOpInfo += u.getData(FPField) + ",";
                teleOpInfo += u.getData(parkRadioGroup) + ",";
                teleOpInfo += u.getData(endgameTimeGroup) + ",";
                teleOpInfo += u.getData(accuracyRadioGroup) + ",";

                Intent i = new Intent(this, activityAfterMatch.class);
                i.putExtra("preMatch", preMatchSaveString);
                i.putExtra("auto", autoSaveString);
                i.putExtra("teleOp", teleOpInfo);
                i.putExtra("postMatch", postMatchSaveString);
                this.startActivity(i);
            }
            if(!response.isEmpty()){
                unfilledMessage.setText(response);
                unfilledMessage.show();
            }
        });
    }
}
