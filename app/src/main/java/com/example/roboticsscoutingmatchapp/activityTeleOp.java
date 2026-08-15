package com.example.roboticsscoutingmatchapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        String preMatchSaveString, autoSaveString,  // Gets all savestrings from wherever coming in from
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

        // Defining all the access-necessary components within the page
        Button backButton = findViewById(R.id.back_button);
        Button saveButton = findViewById(R.id.save_button);

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

        Spinner accuracyChoice = (Spinner) findViewById(R.id.accuracy_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.accuracy_estimate,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accuracyChoice.setAdapter(adapter);

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

        Spinner climbStatus = findViewById(R.id.climbStatus);
        ArrayAdapter<CharSequence> climbAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.climb_status_dropdown,
                android.R.layout.simple_spinner_item);
        climbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        climbStatus.setAdapter(climbAdapter);

        Spinner accuracyPosition = findViewById(R.id.accuracy_position);
        ArrayAdapter<CharSequence> accPosAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.accuracy_position,
                android.R.layout.simple_spinner_item);
        accPosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accuracyPosition.setAdapter(accPosAdapter);

        Spinner climbTime = findViewById(R.id.endgameTime);
        ArrayAdapter<CharSequence> climbTimeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.time_to_hang,
                android.R.layout.simple_spinner_item);
        climbTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        climbTime.setAdapter(climbTimeAdapter);

        Toast unfilledMessage = new Toast(this);
        unfilledMessage.setDuration(Toast.LENGTH_SHORT);

        // Setting all fields which have data
        if(!teleOpSaveString.isEmpty()){
            // #ACL1 | #ACL2 | #ACL3 | #ACL4 | # SCL1 | #SCL2 | #SCL3 | #SCL4 |
            // #Attempted processor | #Scored Processor | #Attempted Barge | #Scored Barge |
            // Park/Shallow/Deep | Time to hang | Algae Pickup | Coral Pickup ||
            FSField.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);
            FPField.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);

        }

        // Setting increment and decrement listeners for all buttons
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


        // Back button, which sends data backwards even if it's unfilled
        backButton.setOnClickListener((l)->{
            String teleOpInfo = "";
            // #ACL1 | #ACL2 | #ACL3 | #ACL4 | # SCL1 | #SCL2 | #SCL3 | #SCL4 |
            // #Attempted processor | #Scored Processor | #Attempted Barge | #Scored Barge |
            // Park/Shallow/Deep | Time to hang | Algae Pickup | Coral Pickup ||

            teleOpInfo += u.getData(FSField) + ",";
            teleOpInfo += u.getData(accuracyChoice) + ",";

            teleOpInfo += u.getData(FPField) + ",";

            teleOpInfo += u.getData(climbStatus) + ",";
            teleOpInfo += u.getData(accuracyPosition) + ",";
            teleOpInfo += u.getData(climbTime) + ",";

            Intent i = new Intent(this, activityAutonomous.class);
            i.putExtra("preMatch", preMatchSaveString);
            i.putExtra("auto", autoSaveString);
            i.putExtra("teleOp", teleOpInfo);
            i.putExtra("postMatch", postMatchSaveString);

            this.startActivity(i);
        });

        saveButton.setOnClickListener((l) -> {
            String response = "";
            if (u.getData(FSField).isEmpty())
                FSField.setText("0");
            if(u.getData(FPField).isEmpty())
                FPField.setText("0");
            else{
                String teleOpInfo = "";

                teleOpInfo += u.getData(FSField) + ",";
                teleOpInfo += u.getData(accuracyChoice) + ",";

                teleOpInfo += u.getData(FPField) + ",";

                teleOpInfo += u.getData(climbStatus) + ",";
                teleOpInfo += u.getData(accuracyPosition) + ",";
                teleOpInfo += u.getData(climbTime) + ",";

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