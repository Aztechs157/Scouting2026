package com.example.roboticsscoutingmatchapp;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class activityDataShowing extends AppCompatActivity {

    private List<MatchData> allMatches = new ArrayList<>();
    private Set<String> teamNumbers = new HashSet<>();
    private MatchHistoryAdapter adapter;
    private TextView avgAutoText, avgTeleopText, climbRateText;

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

        avgAutoText = findViewById(R.id.avg_auto_scored);
        avgTeleopText = findViewById(R.id.avg_teleop_scored);
        climbRateText = findViewById(R.id.climb_success_rate);

        loadLocalData();
        setupDashboard();
    }

    private void loadLocalData() {
        File docDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File[] files = docDir.listFiles((dir, name) -> name.startsWith("match_scouting_") && name.endsWith(".csv"));

        if (files == null) return;

        for (File file : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = br.readLine();
                if (line != null) {
                    // Note: In your current app, you write multiple sections without newlines sometimes?
                    // Actually, bw.write(preMatchSaveString); bw.write(autoSaveString); ...
                    // If they don't have commas or newlines, parsing will be hard.
                    // But AfterMatchActivity writes bw.write(preMatchSaveString); etc.
                    // Let's assume it's one big comma-separated line as per your notebook logic.
                    String[] data = line.split(",", -1);
                    MatchData match = new MatchData(data);
                    if (match.teamNum != null && !match.teamNum.isEmpty()) {
                        allMatches.add(match);
                        teamNumbers.add(match.teamNum);
                    }
                }
            } catch (Exception e) {
                Log.e("DataShowing", "Error loading file: " + file.getName(), e);
            }
        }
    }

    private void setupDashboard() {
        AutoCompleteTextView searchView = findViewById(R.id.team_search_view);
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, new ArrayList<>(teamNumbers));
        searchView.setAdapter(teamAdapter);

        RecyclerView recyclerView = findViewById(R.id.match_history_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MatchHistoryAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        searchView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTeam = (String) parent.getItemAtPosition(position);
            updateDashboard(selectedTeam);
        });
    }

    private void updateDashboard(String teamNum) {
        List<MatchData> teamMatches = new ArrayList<>();
        double totalAuto = 0;
        double totalTeleop = 0;
        int climbs = 0;

        for (MatchData m : allMatches) {
            if (m.teamNum.equals(teamNum)) {
                teamMatches.add(m);
                totalAuto += m.getCalculatedAutoScore();
                totalTeleop += m.getCalculatedTeleopScore();
                if (m.hangStatus != null && !m.hangStatus.equalsIgnoreCase("None")) {
                    climbs++;
                }
            }
        }

        if (teamMatches.isEmpty()) return;

        double avgAuto = (double) totalAuto / teamMatches.size();
        double avgTele = (double) totalTeleop / teamMatches.size();
        double climbRate = (double) climbs / teamMatches.size() * 100;

        avgAutoText.setText(String.format("%.2f", avgAuto));
        avgTeleopText.setText(String.format("%.2f", avgTele));
        climbRateText.setText(String.format("%.0f%%", climbRate));

        adapter.updateData(teamMatches);
    }
}
