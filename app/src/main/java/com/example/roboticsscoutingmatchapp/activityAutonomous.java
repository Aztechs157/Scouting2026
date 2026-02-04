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

        RadioGroup positionGroup1 = findViewById(R.id.staring_position_radio_group1);
        RadioGroup positionGroup2 = findViewById(R.id.staring_position_radio_group2);
        RadioButton position1Button = findViewById(R.id.Position_1);
        RadioButton position2Button = findViewById(R.id.Position_2);
        RadioButton position3Button = findViewById(R.id.Position_3);
        RadioButton position4Button = findViewById(R.id.position_4);
        RadioButton position5Button = findViewById(R.id.position_5);
        RadioButton position6Button = findViewById(R.id.position_6);

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

        EditText FSField = findViewById(R.id.edit_text_fs);

        Spinner accuracyChoice = (Spinner) findViewById(R.id.accuracy_spinner);
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
            switch (position){
                case "Position 1":
                    position1Button.toggle();
                    break;
                case "Position 2":
                    position2Button.toggle();
                    break;
                case "Position 3":
                    position3Button.toggle();
                    break;
                case "Position 4":
                    position4Button.toggle();
                    break;
                case "Position 5":
                    position5Button.toggle();
                    break;
                case "Position 6":
                    position6Button.toggle();
                    break;
            }
            FSField.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString);
        }



        positionGroup1.setOnCheckedChangeListener((l, w)->clearGroup(positionGroup2, positionGroup1));
        positionGroup2.setOnCheckedChangeListener((l, w)->clearGroup(positionGroup1, positionGroup2));

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
        // Sets all the buttons to either increment or decrement their respective buttons.
        // Can be simplified. Not now.

        backButton.setOnClickListener((l)-> {
            // Starting Position | Left starting Position | #ACL1 | #ACL2 | #ACL3 | #ACL4 |
            // #SCL1 | #SCL2 | #SCL3 | #SCL4 | #Barge attempted | #barge scored | 
            // #processor attempted | #processor scored |#algae removed ||
            String autoInfo = "";

            if (!u.getData(positionGroup1).isEmpty() || !u.getData(positionGroup2).isEmpty()) {
                if (u.getData(positionGroup1).isEmpty()) {
                    autoInfo += u.getData(positionGroup2);
                } else {
                    autoInfo += u.getData(positionGroup1);
                }
            }
            autoInfo += ","; // Starting position # end

            autoInfo += u.getData(autoHang);

            autoInfo += u.getData(FSField);
            autoInfo += u.getData(accuracyChoice);

            Intent i = new Intent(this, activityPreMatch.class);
            i.putExtra("preMatch", preMatchSaveString);
            i.putExtra("auto", autoInfo);
            i.putExtra("teleOp", teleOpSaveString);
            i.putExtra("postMatch", postMatchSaveString);

            this.startActivity(i);
        });

        saveButton.setOnClickListener((l)-> {
            String response = "";


            if((u.getData(positionGroup1).isEmpty()) && (u.getData(positionGroup2).isEmpty()))
                response = "Please fill position";
            else{

                String autoInfo = "";


                if (u.getData(positionGroup1).isEmpty()) {
                    autoInfo += u.getData(positionGroup2);
                } else {
                    autoInfo += u.getData(positionGroup1);
                }
                autoInfo += ","; // Starting position # end

                autoInfo += u.getData(autoHang);

                autoInfo += u.getData(FSField);
                autoInfo += u.getData(accuracyChoice);

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