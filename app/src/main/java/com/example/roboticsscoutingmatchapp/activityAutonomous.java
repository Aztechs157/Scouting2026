package com.example.roboticsscoutingmatchapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class activityAutonomous extends AppCompatActivity {

    public void clearGroup(RadioGroup field1, RadioGroup field2){
        field1.setOnCheckedChangeListener(null);
        field1.check(-1);
        field1.setOnCheckedChangeListener((l,w)->clearGroup(field2, field1));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        U u = new U();

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

        Spinner positionChoice = findViewById(R.id.position_spinner);
        ArrayAdapter<CharSequence> posAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.position_array,
                android.R.layout.simple_spinner_item);
        posAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionChoice.setAdapter(posAdapter);

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

        Spinner accuracyChoice = findViewById(R.id.accuracy_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.accuracy_estimate,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accuracyChoice.setAdapter(adapter);

        Button backButton = findViewById(R.id.back_button);
        Button saveButton = findViewById(R.id.save_button);

        CheckBox autoHang = findViewById(R.id.checkBox_auto_hang);

        Toast unfilledMessage = new Toast(this);
        unfilledMessage.setDuration(Toast.LENGTH_SHORT);


        if(!autoSaveString.isEmpty()){
            // Starting Position | Left starting Position | #ACL1 | #ACL2 | #ACL3 | #ACL4 |
            // #SCL1 | #SCL2 | #SCL3 | #SCL4 | #Barge attempted | #barge scored |
            // #processor attempted | #processor scored |#algae removed ||
            String position = u.untilNextComma(autoSaveString);
//            Log.d(position, position);
            FSField.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString);
        }

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
        // Sets all the buttons to either increment or decrement their respective buttons.
        // Can be simplified. Not now.

        backButton.setOnClickListener((l)-> {
            String autoInfo = "";
            autoInfo += ","; // Starting position # end
            autoInfo += u.getData(positionChoice);

            autoInfo += u.getData(FSField);
            autoInfo += u.getData(accuracyChoice);

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

                String autoInfo = "";
                autoInfo += ","; // Starting position # end

                autoInfo += u.getData(positionChoice) + ",";

                autoInfo += u.getData(FSField) + ",";
                autoInfo += u.getData(accuracyChoice) + ",";

                autoInfo += u.getData(FPField) + ",";

                autoInfo += u.getData(autoHang) + ",";

                Intent i = new Intent(this, activityTeleOp.class);
                i.putExtra("preMatch", preMatchSaveString);
                i.putExtra("auto", autoInfo);
                i.putExtra("teleOp", teleOpSaveString);
                i.putExtra("postMatch", postMatchSaveString);

                this.startActivity(i);


            if(!response.isEmpty()){
                unfilledMessage.setText(response);
                unfilledMessage.show();
            }
        });
    }
}