package com.example.roboticsscoutingmatchapp;

import android.content.Intent;
import android.os.Bundle;
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

        CheckBox leftStarting = findViewById(R.id.left_starting_area);

        Button incrementAC1 = findViewById(R.id.up_count_button_ac1);
        Button decrementAC1 = findViewById(R.id.down_count_button_ac1);
        EditText AC1Field = findViewById(R.id.edit_text_ac1);

        Button incrementRC1 = findViewById(R.id.up_count_button_rc1);
        Button decrementRC1 = findViewById(R.id.down_count_button_rc1);
        EditText RC1Field = findViewById(R.id.edit_text_rc1);

        Button incrementAC2 = findViewById(R.id.up_count_button_ac2);
        Button decrementAC2 = findViewById(R.id.down_count_button_ac2);
        EditText AC2Field = findViewById(R.id.edit_text_ac2);

        Button incrementRC2 = findViewById(R.id.up_count_button_rc2);
        Button decrementRC2 = findViewById(R.id.down_count_button_rc2);
        EditText RC2Field = findViewById(R.id.edit_text_rc2);

        Button incrementAC3 = findViewById(R.id.up_count_button_ac3);
        Button decrementAC3 = findViewById(R.id.down_count_button_ac3);
        EditText AC3Field = findViewById(R.id.edit_text_ac3);

        Button incrementRC3 = findViewById(R.id.up_count_button_rc3);
        Button decrementRC3 = findViewById(R.id.down_count_button_rc3);
        EditText RC3Field = findViewById(R.id.edit_text_rc3);

        Button incrementAC4 = findViewById(R.id.up_count_button_ac4);
        Button decrementAC4 = findViewById(R.id.down_count_button_ac4);
        EditText AC4Field = findViewById(R.id.edit_text_ac4);

        Button incrementRC4 = findViewById(R.id.up_count_button_rc4);
        Button decrementRC4 = findViewById(R.id.down_count_button_rc4);
        EditText RC4Field = findViewById(R.id.edit_text_rc4);

        Button incrementBA = findViewById(R.id.up_count_button_ba); // BA = Barge attempted
        Button decrementBA = findViewById(R.id.down_count_button_ba);
        EditText BAField = findViewById(R.id.edit_text_ba);

        Button incrementBS = findViewById(R.id.up_count_button_bs); // BS = Barge scored
        Button decrementBS = findViewById(R.id.down_count_button_bs);
        EditText BSField = findViewById(R.id.edit_text_bs);
        
        Button incrementPA = findViewById(R.id.up_count_button_pa); // PA = Processor attempted
        Button decrementPA = findViewById(R.id.down_count_button_pa);
        EditText PAField = findViewById(R.id.edit_text_pa);
        
        Button incrementPS = findViewById(R.id.up_count_button_ps);
        Button decrementPS = findViewById(R.id.down_count_button_ps);
        EditText PSField = findViewById(R.id.edit_text_ps);

        Button incrementREA = findViewById(R.id.up_count_button_rea); // REA = Algae Removed
        Button decrementREA = findViewById(R.id.down_count_button_rea);
        EditText REAField = findViewById(R.id.edit_text_rea);

        Button backButton = findViewById(R.id.back_button);
        Button saveButton = findViewById(R.id.save_button);

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
            autoSaveString = u.nextCommaOn(autoSaveString); // Removes starting position
            if(u.untilNextComma(autoSaveString).equals("True"))
                leftStarting.toggle();
            autoSaveString = u.nextCommaOn(autoSaveString); // Removes left starting
            AC1Field.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString); // Removes #ACL1
            AC2Field.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString); // Removes #ACL2
            AC3Field.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString); // Remove #ACL3
            AC4Field.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString); // Remove #ACL4
            RC1Field.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString); // Remove #SCL1
            RC2Field.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString); // Remove #SCL2
            RC3Field.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString); // Remove #SCL3
            RC4Field.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString); // Remove #SCL4
            BAField.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString); // Remove Attempted Barge
            BSField.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString); // Remove Scored Barge
            PAField.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString); // Remove Attempted Processor
            PSField.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString); // Remove Scored Processor
            REAField.setText(u.untilNextComma(autoSaveString));
            autoSaveString = u.nextCommaOn(autoSaveString); // Remove Algae removed
        }



        positionGroup1.setOnCheckedChangeListener((l, w)->clearGroup(positionGroup2, positionGroup1));
        positionGroup2.setOnCheckedChangeListener((l, w)->clearGroup(positionGroup1, positionGroup2));

        // Sets all the buttons to either increment or decrement their respective buttons.
        // Can be simplified. Not now.
        incrementAC1.setOnClickListener((l)->u.incrementText(AC1Field));
        decrementAC1.setOnClickListener((l)->u.incrementText(AC1Field, -1));
        incrementRC1.setOnClickListener((l)->u.incrementText(RC1Field));
        decrementRC1.setOnClickListener((l)->u.incrementText(RC1Field, -1));
        incrementAC2.setOnClickListener((l)->u.incrementText(AC2Field));
        decrementAC2.setOnClickListener((l)->u.incrementText(AC2Field, -1));
        incrementRC2.setOnClickListener((l)->u.incrementText(RC2Field));
        decrementRC2.setOnClickListener((l)->u.incrementText(RC2Field, -1));
        incrementAC3.setOnClickListener((l)->u.incrementText(AC3Field));
        decrementAC3.setOnClickListener((l)->u.incrementText(AC3Field, -1));
        incrementRC3.setOnClickListener((l)->u.incrementText(RC3Field));
        decrementRC3.setOnClickListener((l)->u.incrementText(RC3Field, -1));
        incrementAC4.setOnClickListener((l)->u.incrementText(AC4Field));
        decrementAC4.setOnClickListener((l)->u.incrementText(AC4Field, -1));
        incrementRC4.setOnClickListener((l)->u.incrementText(RC4Field));
        decrementRC4.setOnClickListener((l)->u.incrementText(RC4Field, -1));
        incrementBA.setOnClickListener((l)->u.incrementText(BAField));
        decrementBA.setOnClickListener((l)->u.incrementText(BAField, -1));
        incrementBS.setOnClickListener((l)->u.incrementText(BSField));
        decrementBS.setOnClickListener((l)->u.incrementText(BSField, -1));
        incrementPA.setOnClickListener((l)->u.incrementText(PAField));
        decrementPA.setOnClickListener((l)->u.incrementText(PAField, -1));
        incrementPS.setOnClickListener((l)->u.incrementText(PSField));
        decrementPS.setOnClickListener((l)->u.incrementText(PSField, -1));
        incrementREA.setOnClickListener((l)->u.incrementText(REAField));
        decrementREA.setOnClickListener((l)->u.incrementText(REAField, -1));

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
            autoInfo += u.getData(leftStarting) + ","; // Left starting end

            autoInfo += u.getData(AC1Field) + ",";
            autoInfo += u.getData(AC2Field) + ",";
            autoInfo += u.getData(AC3Field) + ",";
            autoInfo += u.getData(AC4Field) + ","; // Attempted Coral end

            autoInfo += u.getData(RC1Field) + ",";
            autoInfo += u.getData(RC2Field) + ",";
            autoInfo += u.getData(RC3Field) + ",";
            autoInfo += u.getData(RC4Field) + ","; // Scored Coral End

            autoInfo += u.getData(BAField) + ",";
            autoInfo += u.getData(BSField) + ",";
            autoInfo += u.getData(PAField) + ",";
            autoInfo += u.getData(PSField) + ",";
            autoInfo += u.getData(REAField) + ","; // Algae end

            Intent i = new Intent(this, activityPreMatch.class);
            i.putExtra("preMatch", preMatchSaveString);
            i.putExtra("auto", autoInfo);
            i.putExtra("teleOp", teleOpSaveString);
            i.putExtra("postMatch", postMatchSaveString);

            this.startActivity(i);
        });

        saveButton.setOnClickListener((l)-> {
            String response = "";

            if(u.getData(AC1Field).isEmpty()) {
                AC1Field.setText("0");
            }
            if(u.getData(RC1Field).isEmpty()) {
                RC1Field.setText("0");
            }
            if(u.getData(AC2Field).isEmpty()) {
                AC2Field.setText("0");
            }
            if(u.getData(RC2Field).isEmpty()) {
                RC2Field.setText("0");
            }
            if(u.getData(AC3Field).isEmpty()) {
                AC3Field.setText("0");
            }
            if(u.getData(RC3Field).isEmpty()) {
                RC3Field.setText("0");
            }
            if(u.getData(AC4Field).isEmpty()) {
                AC4Field.setText("0");
            }
            if(u.getData(RC4Field).isEmpty()) {
                RC4Field.setText("0");
            }
            if(u.getData(BAField).isEmpty()) {
                BAField.setText("0");
            }
            if(u.getData(BSField).isEmpty()) {
                BSField.setText("0");
            }
            if(u.getData(PAField).isEmpty()) {
                PAField.setText("0");
            }
            if(u.getData(PSField).isEmpty()) {
                PSField.setText("0");
            }
            if(u.getData(REAField).isEmpty()) {
                REAField.setText("0");
            }
            if((u.getData(positionGroup1).isEmpty()) && (u.getData(positionGroup2).isEmpty()))
                response = "Please fill position";
            else if(Integer.parseInt(u.getData(AC1Field)) < Integer.parseInt(u.getData(RC1Field)))
                response = "Attempted Coral L1 cannot be less than Scored Coral L1";
            else if(Integer.parseInt(u.getData(AC2Field)) < Integer.parseInt(u.getData(RC2Field)))
                response = "Attempted Coral L2 cannot be less than Scored Coral L2";
            else if(Integer.parseInt(u.getData(AC3Field)) < Integer.parseInt(u.getData(RC3Field)))
                response = "Attempted Coral L3 cannot be less than Scored Coral L3";
            else if(Integer.parseInt(u.getData(AC4Field)) < Integer.parseInt(u.getData(RC4Field)))
                response = "Attempted Coral L4 cannot be less than Scored Coral L4";
            else if(Integer.parseInt(u.getData(BAField)) < Integer.parseInt(u.getData(BSField)))
                response = "Attempted Barge cannot be less than Scored Barge";
            else if(Integer.parseInt(u.getData(PAField)) < Integer.parseInt(u.getData(PSField)))
                response = "Attempted Processor cannot be less than Scored Processor";
            else{

                String autoInfo = "";


                if (u.getData(positionGroup1).isEmpty()) {
                    autoInfo += u.getData(positionGroup2);
                } else {
                    autoInfo += u.getData(positionGroup1);
                }
                autoInfo += ","; // Starting position # end
                autoInfo += u.getData(leftStarting) + ","; // Left starting end

                autoInfo += u.getData(AC1Field) + ",";
                autoInfo += u.getData(AC2Field) + ",";
                autoInfo += u.getData(AC3Field) + ",";
                autoInfo += u.getData(AC4Field) + ","; // Attempted Coral end

                autoInfo += u.getData(RC1Field) + ",";
                autoInfo += u.getData(RC2Field) + ",";
                autoInfo += u.getData(RC3Field) + ",";
                autoInfo += u.getData(RC4Field) + ","; // Scored Coral End

                autoInfo += u.getData(BAField) + ",";
                autoInfo += u.getData(BSField) + ",";
                autoInfo += u.getData(PAField) + ",";
                autoInfo += u.getData(PSField) + ",";
                autoInfo += u.getData(REAField) + ","; // Algae end

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