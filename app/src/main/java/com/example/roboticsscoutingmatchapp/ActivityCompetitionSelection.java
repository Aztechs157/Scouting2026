package com.example.roboticsscoutingmatchapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class ActivityCompetitionSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_competition_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        U u = new U();
        RadioGroup competitionRadioGroup = findViewById(R.id.competition_radio_group);
//        RadioButton wpiButton = findViewById(R.id.comp_wpi_button);
//        RadioButton uvmButton = findViewById(R.id.comp_uvm_button);
//        RadioButton dcmpButton = findViewById(R.id.comp_dcmp);
//        RadioButton worldsButton = findViewById(R.id.comp_worlds);
//        RadioButton testButton = findViewById(R.id.comp_test);
        Button saveButton = findViewById(R.id.save_button);
        Toast unfilledMessage = new Toast(this);
        unfilledMessage.setDuration(Toast.LENGTH_SHORT);


        String scoutName;
        boolean changeCompetition = false;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            scoutName = extras.getString("scoutName", "");
            changeCompetition = extras.getBoolean("chooseNewCompetition", false);
        } else {
            scoutName = "";
        }

        final String FILENAME = "matchAndDate";
        File file = new File(this.getFilesDir(), FILENAME);
        Calendar now = Calendar.getInstance();
        String currentDate = "";
        currentDate += now.get(Calendar.YEAR);
        currentDate += now.get(Calendar.MONTH);
        currentDate += now.get(Calendar.DAY_OF_MONTH);

        String dateAndMatchString = "";

        if(!changeCompetition){
            if (file.exists()) {
                try {
                    FileInputStream fis = this.openFileInput(FILENAME);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                    dateAndMatchString += reader.readLine();
                    reader.close();
                } catch (FileNotFoundException e) {
                    Log.e("File Not Found Exception", e.toString());
                } catch (IOException e) {
                    Log.e("IOException", e.toString());
                }
            }
            if (!dateAndMatchString.isEmpty()) {
                String fileDate = u.untilNextComma(dateAndMatchString);
                if (fileDate.equals(currentDate)) {
                    Intent i = new Intent(this, activityPreMatch.class);
                    i.putExtra("competition", u.nextCommaOn(dateAndMatchString));
                    i.putExtra("scoutName", scoutName);
                    this.startActivity(i);
                }
            }
        }

        String finalCurrentDate = currentDate;
        saveButton.setOnClickListener((l)->{
            String response = "";
            if(u.getData(competitionRadioGroup).isEmpty()){
                response = "Please choose current competition to be scouting";
            }else{
                String fileContents = finalCurrentDate + "," + u.getData(competitionRadioGroup);
                try(FileOutputStream fos = this.openFileOutput(FILENAME, Context.MODE_PRIVATE)){
                    fos.write(fileContents.getBytes());
                } catch(FileNotFoundException e){
                    Log.e("File Not Found Exception", e.toString());
                } catch(IOException e){
                    Log.e("IO Exception", e.toString());
                }
                Intent i = new Intent(this, activityPreMatch.class);
                i.putExtra("competition", u.getData(competitionRadioGroup));
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