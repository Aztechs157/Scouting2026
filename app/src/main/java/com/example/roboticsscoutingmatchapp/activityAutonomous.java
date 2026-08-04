package com.example.roboticsscoutingmatchapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Activity for collecting Autonomous period data.
 * Tracks starting position, fuel shots fired, accuracy, and auto climb.
 */
public class activityAutonomous extends AppCompatActivity {

    /**
     * Helper to clear a RadioGroup when a button in another group is selected.
     * Used for multi-column starting position layouts.
     */
    public void clearGroup(RadioGroup field1, RadioGroup field2){
        field1.setOnCheckedChangeListener(null);
        field1.check(-1);
        field1.setOnCheckedChangeListener((l,w)->clearGroup(field2, field1));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        U u = new U();

        // Load existing match state
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


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_autonomous);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // UI Binding - Starting Position
        RadioGroup positionGroup1 = findViewById(R.id.staring_position_radio_group1);
        RadioButton position1Button = findViewById(R.id.Position_1);
        RadioButton position2Button = findViewById(R.id.Position_2);
        RadioButton position3Button = findViewById(R.id.Position_3);

        // UI Binding - Fuel Shot Counters (FS)
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

        // UI Binding - Fuel Passing Counters (FP)
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

        EditText FSField = findViewById(R.id.edit_text_fs);
        EditText FPField = findViewById(R.id.edit_text_fp);

        // UI Binding - Accuracy & Performance
        RadioGroup accuracyGroup = findViewById(R.id.accuracy_radio_group);
        RadioButton lessThanTen = findViewById(R.id.underTen);
        RadioButton twentyFivePercent = findViewById(R.id.twentyFivePercent);
        RadioButton fiftyPercent = findViewById(R.id.fiftyPercent);
        RadioButton seventyFivePercent = findViewById(R.id.seventyFivePercent);
        RadioButton overNinetyFive = findViewById(R.id.overNinetyFive);

        CheckBox autoHang = findViewById(R.id.checkBox_auto_hang);

        Button backButton = findViewById(R.id.back_button);
        Button saveButton = findViewById(R.id.save_button);

        Toast unfilledMessage = new Toast(this);
        unfilledMessage.setDuration(Toast.LENGTH_SHORT);


        // Restore UI state from saved data string
        if(!autoSaveString.isEmpty()){
            String position = u.untilNextComma(autoSaveString);
            switch (position){
                case "Position 1": position1Button.toggle(); break;
                case "Position 2": position2Button.toggle(); break;
                case "Position 3": position3Button.toggle(); break;
            }
            autoSaveString = u.nextCommaOn(autoSaveString);

            String accuracyChoice = u.untilNextComma(autoSaveString);
            switch(accuracyChoice){
                case "Less Than 10%": lessThanTen.toggle(); break;
                case "25%": twentyFivePercent.toggle(); break;
                case "50%": fiftyPercent.toggle(); break;
                case "75%": seventyFivePercent.toggle(); break;
                case "More Than 95%": overNinetyFive.toggle(); break;
            }
            autoSaveString = u.nextCommaOn(autoSaveString);

            FSField.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString);
        }

        // Setup Increment/Decrement Listeners
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

        backButton.setOnClickListener((l)-> {
            String autoInfo = "";
            autoInfo += u.getData(positionGroup1);
            autoInfo += u.getData(FSField);
            autoInfo += u.getData(accuracyGroup);
            autoInfo += u.getData(FPField);
            autoInfo += u.getData(autoHang);

            Intent i = new Intent(this, activityPreMatch.class);
            i.putExtra("preMatch", preMatchSaveString);
            i.putExtra("auto", autoInfo);
            i.putExtra("teleOp", teleOpSaveString);
            i.putExtra("postMatch", postMatchSaveString);

            this.startActivity(i);
        });

        saveButton.setOnClickListener((l)-> {
            String response = "";

            if((u.getData(positionGroup1).isEmpty()))
                response = "Please fill position";
            if ((u.getData(accuracyGroup).isEmpty()))
                response = "Please give accuracy";
            else{
                // Serialize auto data
                String autoInfo = "";
                autoInfo += u.getData(positionGroup1) + ",";
                autoInfo += u.getData(FSField) + ",";
                autoInfo += u.getData(accuracyGroup) + ",";
                autoInfo += u.getData(FPField) + ",";
                autoInfo += u.getData(autoHang) + ",";

                Intent i = new Intent(this, activityTeleOp.class);
                i.putExtra("preMatch", preMatchSaveString);
                i.putExtra("auto", autoInfo);
                i.putExtra("teleOp", teleOpSaveString);
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
