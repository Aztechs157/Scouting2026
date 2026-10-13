package com.example.roboticsscoutingmatchapp;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class activityDataShowing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_showing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final String BASE = "match_scouting_2025";
        String base = BASE + ".csv";
        String comp = BASE + "_comp.csv";
        String radar = BASE + "_radar.csv";
        String aggregated = BASE + "_agg.csv";
        String delimiter = ",";

        ArrayList<String[]> aggArr = new ArrayList<>();


        boolean baseE = false;
        boolean compE = false;
        boolean radarE = false;
        boolean aggE = false;

        try{
            File baseF = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), base);
            File compF = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), comp);
            File radarF = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), radar);
            File aggF = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), aggregated);

            if(baseF.exists()){baseE = true;}
            if(compF.exists()){compE = true;}
            if(radarF.exists()){radarE = true;}
            if(aggF.exists()){aggE = true;}

            if(aggE) {
                BufferedReader brAgg = new BufferedReader(new FileReader(aggF));
                String line;
                while((line = brAgg.readLine()) != null){
                    aggArr.add(line.split(delimiter));
                }
            }
        } catch (Exception e) {
            Log.e("e", "onCreate: ", e);
        }

    }
}