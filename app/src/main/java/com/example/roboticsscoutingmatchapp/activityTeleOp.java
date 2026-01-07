package com.example.roboticsscoutingmatchapp;

import android.content.Intent;
import android.os.Bundle;
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
        Button AC1plus = findViewById(R.id.up_count_button_ac1); // Attempted Coral L1
        Button AC1minus = findViewById(R.id.down_count_button_ac1);
        EditText AC1field = findViewById(R.id.edit_text_ac1);
        Button RC1plus = findViewById(R.id.up_count_button_rc1); // Scored Coral L1
        Button RC1minus = findViewById(R.id.down_count_button_rc1);
        EditText RC1field = findViewById(R.id.edit_text_rc1);
        Button AC2plus = findViewById(R.id.up_count_button_ac2); // Attempted Coral L2
        Button AC2minus = findViewById(R.id.down_count_button_ac2);
        EditText AC2field = findViewById(R.id.edit_text_ac2);
        Button RC2plus = findViewById(R.id.up_count_button_rc2); // Scored Coral L2
        Button RC2minus = findViewById(R.id.down_count_button_rc2);
        EditText RC2field = findViewById(R.id.edit_text_rc2);
        Button AC3plus = findViewById(R.id.up_count_button_ac3); // Attempted Coral L3
        Button AC3minus = findViewById(R.id.down_count_button_ac3);
        EditText AC3field = findViewById(R.id.edit_text_ac3);
        Button RC3plus = findViewById(R.id.up_count_button_rc3); // Scored Coral L3
        Button RC3minus = findViewById(R.id.down_count_button_rc3);
        EditText RC3field = findViewById(R.id.edit_text_rc3);
        Button AC4plus = findViewById(R.id.up_count_button_ac4); // Attempted Coral L4
        Button AC4minus = findViewById(R.id.down_count_button_ac4);
        EditText AC4field = findViewById(R.id.edit_text_ac4);
        Button RC4plus = findViewById(R.id.up_count_button_rc4); // Scored Coral L4
        Button RC4minus = findViewById(R.id.down_count_button_rc4);
        EditText RC4field = findViewById(R.id.edit_text_rc4);
        Button APplus = findViewById(R.id.up_count_button_pa); // Attempted Processor
        Button APminus = findViewById(R.id.down_count_button_pa);
        EditText APfield = findViewById(R.id.edit_text_pa);
        Button SPplus = findViewById(R.id.up_count_button_ps); // Scored Processor
        Button SPminus = findViewById(R.id.down_count_button_ps);
        EditText SPfield = findViewById(R.id.edit_text_ps);
        Button ABplus = findViewById(R.id.up_count_button_ba); // Attempted Barge
        Button ABminus = findViewById(R.id.down_count_button_ba);
        EditText ABfield = findViewById(R.id.edit_text_ba);
        Button SBplus = findViewById(R.id.up_count_button_bs); // Scored Barge
        Button SBminus = findViewById(R.id.down_count_button_bs);
        EditText SBfield = findViewById(R.id.edit_text_bs);
        RadioGroup parkRadioGroup = findViewById(R.id.endgame_location); // Endgame RadioGroup
        RadioButton hangShallowButton = findViewById(R.id.hang_shallow);
        RadioButton hangDeepButton = findViewById(R.id.hang_deep);
        RadioButton parkButton = findViewById(R.id.park);
        RadioButton noneButton = findViewById(R.id.nothing);
        RadioGroup endgameTimeGroup = findViewById(R.id.endgame_time);
        RadioButton twentyFiveButton = findViewById(R.id.twenty_five);
        RadioButton twentyButton = findViewById(R.id.twenty);
        RadioButton fifteenButton = findViewById(R.id.fifteen);
        RadioButton tenButton = findViewById(R.id.ten);
        RadioButton zeroButton = findViewById(R.id.zero);
        CheckBox algaeBox = findViewById(R.id.pickup_algae);
        CheckBox coralBox = findViewById(R.id.pickup_coral);

        Toast unfilledMessage = new Toast(this);
        unfilledMessage.setDuration(Toast.LENGTH_SHORT);

        // Setting all fields which have data
        if(!teleOpSaveString.isEmpty()){
            // #ACL1 | #ACL2 | #ACL3 | #ACL4 | # SCL1 | #SCL2 | #SCL3 | #SCL4 |
            // #Attempted processor | #Scored Processor | #Attempted Barge | #Scored Barge |
            // Park/Shallow/Deep | Time to hang | Algae Pickup | Coral Pickup ||
            AC1field.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);
            AC2field.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);
            AC3field.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);
            AC4field.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);

            RC1field.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);
            RC2field.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);
            RC3field.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);
            RC4field.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);

            APfield.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);
            SPfield.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);
            ABfield.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);
            SBfield.setText(u.untilNextComma(teleOpSaveString));
            teleOpSaveString  = u.nextCommaOn(teleOpSaveString);

            String currentButton = u.untilNextComma(teleOpSaveString);
            switch(currentButton){
                case "Hang Shallow":
                    hangShallowButton.toggle();
                    break;
                case "Hang Deep":
                    hangDeepButton.toggle();
                    break;
                case "Park":
                    parkButton.toggle();
                    break;
                case "None":
                    noneButton.toggle();
                    break;
            }
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);

            String timeToHang = u.untilNextComma(teleOpSaveString);
            switch(timeToHang){
                case "25":
                    twentyFiveButton.toggle();
                    break;
                case "20":
                    twentyButton.toggle();
                    break;
                case "15":
                    fifteenButton.toggle();
                    break;
                case "10":
                    tenButton.toggle();
                    break;
                case "0":
                    zeroButton.toggle();
                    break;
            }
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);

            algaeBox.setChecked(Boolean.parseBoolean(u.untilNextComma(teleOpSaveString)));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);
            coralBox.setChecked(Boolean.parseBoolean(u.untilNextComma(teleOpSaveString)));
            teleOpSaveString = u.nextCommaOn(teleOpSaveString);
        }

        // Setting increment and decrement listeners for all buttons
        AC1plus.setOnClickListener((l)->u.incrementText(AC1field));
        AC1minus.setOnClickListener((l)->u.incrementText(AC1field, -1));
        RC1plus.setOnClickListener((l)->u.incrementText(RC1field));
        RC1minus.setOnClickListener((l)->u.incrementText(RC1field, -1));

        AC2plus.setOnClickListener((l)->u.incrementText(AC2field));
        AC2minus.setOnClickListener((l)->u.incrementText(AC2field, -1));
        RC2plus.setOnClickListener((l)->u.incrementText(RC2field));
        RC2minus.setOnClickListener((l)->u.incrementText(RC2field, -1));

        AC3plus.setOnClickListener((l)->u.incrementText(AC3field));
        AC3minus.setOnClickListener((l)->u.incrementText(AC3field, -1));
        RC3plus.setOnClickListener((l)->u.incrementText(RC3field));
        RC3minus.setOnClickListener((l)->u.incrementText(RC3field, -1));

        AC4plus.setOnClickListener((l)->u.incrementText(AC4field));
        AC4minus.setOnClickListener((l)->u.incrementText(AC4field, -1));
        RC4plus.setOnClickListener((l)->u.incrementText(RC4field));
        RC4minus.setOnClickListener((l)->u.incrementText(RC4field, -1));

        APplus.setOnClickListener((l)->u.incrementText(APfield));
        APminus.setOnClickListener((l)->u.incrementText(APfield, -1));
        SPplus.setOnClickListener((l)->u.incrementText(SPfield));
        SPminus.setOnClickListener((l)->u.incrementText(SPfield, -1));

        ABplus.setOnClickListener((l)->u.incrementText(ABfield));
        ABminus.setOnClickListener((l)->u.incrementText(ABfield, -1));
        SBplus.setOnClickListener((l)->u.incrementText(SBfield));
        SBminus.setOnClickListener((l)->u.incrementText(SBfield, -1));


        // Back button, which sends data backwards even if it's unfilled
        backButton.setOnClickListener((l)->{
            String teleOpInfo = "";
            // #ACL1 | #ACL2 | #ACL3 | #ACL4 | # SCL1 | #SCL2 | #SCL3 | #SCL4 |
            // #Attempted processor | #Scored Processor | #Attempted Barge | #Scored Barge |
            // Park/Shallow/Deep | Time to hang | Algae Pickup | Coral Pickup ||

            teleOpInfo += u.getData(AC1field) + ",";
            teleOpInfo += u.getData(AC2field) + ",";
            teleOpInfo += u.getData(AC3field) + ",";
            teleOpInfo += u.getData(AC4field) + ","; // Attempted Done

            teleOpInfo += u.getData(RC1field) + ",";
            teleOpInfo += u.getData(RC2field) + ",";
            teleOpInfo += u.getData(RC3field) + ",";
            teleOpInfo += u.getData(RC4field) + ","; // Scored Done

            teleOpInfo += u.getData(APfield) + ",";
            teleOpInfo += u.getData(SPfield) + ",";
            teleOpInfo += u.getData(ABfield) + ",";
            teleOpInfo += u.getData(SBfield) + ","; // Algae Done

            teleOpInfo += u.getData(parkRadioGroup) + ",";
            teleOpInfo += u.getData(endgameTimeGroup) + ",";

            teleOpInfo += u.getData(algaeBox) + ",";
            teleOpInfo += u.getData(coralBox) + ",";

            Intent i = new Intent(this, activityAutonomous.class);
            i.putExtra("preMatch", preMatchSaveString);
            i.putExtra("auto", autoSaveString);
            i.putExtra("teleOp", teleOpInfo);
            i.putExtra("postMatch", postMatchSaveString);

            this.startActivity(i);
        });

        saveButton.setOnClickListener((l) -> {
            String response = "";
            if (u.getData(AC1field).isEmpty())
                AC1field.setText("0");
            if(u.getData(RC1field).isEmpty())
                RC1field.setText("0");
            if(u.getData(AC2field).isEmpty())
                AC2field.setText("0");
            if(u.getData(RC2field).isEmpty())
                RC2field.setText("0");
            if(u.getData(AC3field).isEmpty())
                AC3field.setText("0");
            if(u.getData(RC3field).isEmpty())
                RC3field.setText("0");
            if(u.getData(AC4field).isEmpty())
                AC4field.setText("0");
            if(u.getData(RC4field).isEmpty())
                RC4field.setText("0");
            if(u.getData(APfield).isEmpty())
                APfield.setText("0");
            if(u.getData(SPfield).isEmpty())
                SPfield.setText("0");
            if(u.getData(ABfield).isEmpty())
                ABfield.setText("0");
            if(u.getData(SBfield).isEmpty())
                SBfield.setText("0");
            if(u.getData(parkRadioGroup).isEmpty())
                response = "Please select an endgame position";
            else if(u.getData(endgameTimeGroup).isEmpty())
                response = "Please select park time";
            else if(Integer.parseInt(u.getData(AC1field)) < Integer.parseInt(u.getData(RC1field)))
                response = "Attempted Coral L1 cannot be less than Scored Coral L1";
            else if(Integer.parseInt(u.getData(AC2field)) < Integer.parseInt(u.getData(RC2field)))
                response = "Attempted Coral L2 cannot be less than Scored Coral L2";
            else if(Integer.parseInt(u.getData(AC3field)) < Integer.parseInt(u.getData(RC3field)))
                response = "Attempted Coral L3 cannot be less than Scored Coral L3";
            else if(Integer.parseInt(u.getData(AC4field)) < Integer.parseInt(u.getData(RC4field)))
                response = "Attempted Coral L4 cannot be less than Scored Coral L4";
            else if(Integer.parseInt(u.getData(APfield)) < Integer.parseInt(u.getData(SPfield)))
                response = "Attempted Processor cannot be less than Scored Processor";
            else if(Integer.parseInt(u.getData(ABfield)) < Integer.parseInt(u.getData(SBfield)))
                response = "Attempted Barge cannot be less than Scored Barge";
            else{
                String teleOpInfo = "";

                teleOpInfo += u.getData(AC1field) + ",";
                teleOpInfo += u.getData(AC2field) + ",";
                teleOpInfo += u.getData(AC3field) + ",";
                teleOpInfo += u.getData(AC4field) + ","; // Attempted Done

                teleOpInfo += u.getData(RC1field) + ",";
                teleOpInfo += u.getData(RC2field) + ",";
                teleOpInfo += u.getData(RC3field) + ",";
                teleOpInfo += u.getData(RC4field) + ","; // Scored Done

                teleOpInfo += u.getData(APfield) + ",";
                teleOpInfo += u.getData(SPfield) + ",";
                teleOpInfo += u.getData(ABfield) + ",";
                teleOpInfo += u.getData(SBfield) + ","; // Algae Done

                teleOpInfo += u.getData(parkRadioGroup) + ",";
                teleOpInfo += u.getData(endgameTimeGroup) + ",";

                teleOpInfo += u.getData(algaeBox) + ",";
                teleOpInfo += u.getData(coralBox) + ",";

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