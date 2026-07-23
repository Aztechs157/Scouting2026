package com.example.roboticsscoutingmatchapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class activityDataShowing extends AppCompatActivity {

    private List<MatchData> allMatches = new ArrayList<>();
    private Set<String> teamNumbers = new HashSet<>();
    private final Set<String> competitions = new HashSet<>();
    private MatchHistoryAdapter adapter;
    private TextView avgAutoText, avgTeleopText, avgPassingText, climbRateText;
    private LineChart scoringChart;
    private HorizontalBarChart leaderboardChart;
    private Spinner compFilterSpinner, metricSpinner;
    private TextView leaderboardPageText;
    private int leaderboardPage = 0;
    private static final int PAGE_SIZE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_showing);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Fix status bar overlap for the AppBar and the Navigation Drawer
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_bar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_scout) {
                Intent intent = new Intent(this, ActivityCompetitionSelection.class);
                intent.putExtra("chooseNewCompetition", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        avgAutoText = findViewById(R.id.avg_auto_scored);
        avgTeleopText = findViewById(R.id.avg_teleop_scored);
        avgPassingText = findViewById(R.id.avg_passing);
        climbRateText = findViewById(R.id.climb_success_rate);
        scoringChart = findViewById(R.id.scoring_line_chart);
        leaderboardChart = findViewById(R.id.leaderboard_bar_chart);
        compFilterSpinner = findViewById(R.id.comp_filter_spinner);
        metricSpinner = findViewById(R.id.leaderboard_metric_spinner);
        leaderboardPageText = findViewById(R.id.leaderboard_page_text);

        Button prevButton = findViewById(R.id.prev_leaderboard_button);
        Button nextButton = findViewById(R.id.next_leaderboard_button);

        prevButton.setOnClickListener(v -> {
            if (leaderboardPage > 0) {
                leaderboardPage--;
                refreshLeaderboard();
            }
        });

        nextButton.setOnClickListener(v -> {
            leaderboardPage++;
            refreshLeaderboard();
        });

        setupCharts();
        loadLocalData();
        setupDashboard();
        setupCompSpinner();
        setupMetricSpinner();
    }

    private void setupCharts() {
        // Line Chart Setup
        scoringChart.getDescription().setEnabled(false);
        scoringChart.setDrawGridBackground(false);
        scoringChart.getAxisRight().setEnabled(false);
        scoringChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        scoringChart.getLegend().setEnabled(false);
        scoringChart.setNoDataText("Select a team to view scoring trend");

        // Bar Chart Setup
        leaderboardChart.getDescription().setEnabled(false);
        leaderboardChart.setDrawGridBackground(false);
        leaderboardChart.getAxisRight().setEnabled(false);
        leaderboardChart.getAxisLeft().setAxisMinimum(0f);
        
        XAxis leaderX = leaderboardChart.getXAxis();
        leaderX.setPosition(XAxis.XAxisPosition.BOTTOM);
        leaderX.setDrawGridLines(false);
        leaderX.setGranularity(1f);
        leaderX.setLabelCount(5);
        
        leaderboardChart.setExtraOffsets(0, 0, 40, 0); 
        leaderboardChart.getLegend().setEnabled(false);
        leaderboardChart.setFitBars(true);
    }

    private void loadLocalData() {
        File docDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File[] files = docDir.listFiles((dir, name) -> name.startsWith("match_scouting_") && name.endsWith(".csv"));

        if (files == null) return;

        for (File file : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = br.readLine();
                if (line != null) {
                    String[] data = line.split(",", -1);
                    MatchData match = new MatchData(data);
                    if (match.teamNum != null && !match.teamNum.isEmpty()) {
                        allMatches.add(match);
                        teamNumbers.add(match.teamNum);
                        if (match.competition != null && !match.competition.isEmpty()) {
                            competitions.add(match.competition);
                        }
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
        adapter = new MatchHistoryAdapter(new ArrayList<>(), this::showMatchDetails);
        recyclerView.setAdapter(adapter);

        searchView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTeam = (String) parent.getItemAtPosition(position);
            updateDashboard(selectedTeam);
        });
    }

    private void showMatchDetails(MatchData match) {
        StringBuilder details = new StringBuilder();
        details.append("Scouter: ").append(match.scout).append("\n");
        details.append("Competition: ").append(match.competition).append("\n");
        details.append("Team Color: ").append(match.teamColor).append("\n\n");
        
        details.append("--- Autonomous ---\n");
        details.append("Fuel Scored: ").append(match.autoScored).append("\n");
        details.append("Fuel Passed: ").append(match.autoPassed).append("\n");
        details.append("Accuracy: ").append(match.autoAccuracy).append("\n");
        details.append("Auto Climb (L1): ").append(match.autoHang ? "Yes (+15 pts)" : "No").append("\n\n");
        
        details.append("--- TeleOp ---\n");
        details.append("Fuel Scored: ").append(match.teleopScored).append("\n");
        details.append("Fuel Passed: ").append(match.teleopPassed).append("\n");
        details.append("Accuracy: ").append(match.teleopAccuracy).append("\n");
        details.append("Climb Level: ").append(match.hangStatus).append(" (+").append(match.getClimbPoints()).append(" pts)\n");
        details.append("Climb Time: ").append(match.hangTime).append("s\n\n");
        
        details.append("--- Performance & Notes ---\n");
        details.append("Accuracy Pos: ").append(match.accuracyPos).append("\n");
        details.append("Played Defense: ").append(match.playedDefense ? "Yes" : "No").append("\n");
        details.append("Defense Notes: ").append(match.defense).append("\n");
        details.append("Stop Reason: ").append(match.stopReason).append("\n");
        details.append("Notes: ").append(match.comments);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Match #" + match.matchNum + " Details")
                .setMessage(details.toString())
                .setPositiveButton("Close", null)
                .show();
    }

    private void setupCompSpinner() {
        List<String> compList = new ArrayList<>();
        compList.add("All Competitions");
        compList.addAll(competitions);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, compList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        compFilterSpinner.setAdapter(spinnerAdapter);

        compFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                leaderboardPage = 0;
                refreshLeaderboard();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupMetricSpinner() {
        String[] metrics = {"Total Score", "Auto Fuel", "Tele Fuel", "Passing", "Accuracy %"};
        ArrayAdapter<String> metricAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, metrics);
        metricAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metricSpinner.setAdapter(metricAdapter);

        metricSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                leaderboardPage = 0;
                refreshLeaderboard();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void refreshLeaderboard() {
        String comp = (String) compFilterSpinner.getSelectedItem();
        String metric = (String) metricSpinner.getSelectedItem();
        displayTopTeams(comp.equals("All Competitions") ? null : comp, metric);
    }

    private void displayTopTeams(String competitionFilter, String metric) {
        Map<String, List<Double>> teamValues = new HashMap<>();
        for (MatchData m : allMatches) {
            if (competitionFilter == null || m.competition.equals(competitionFilter)) {
                double val = 0;
                switch (metric) {
                    case "Total Score": val = m.getTotalScore(); break;
                    case "Auto Fuel": val = m.autoScored; break;
                    case "Tele Fuel": val = m.teleopScored; break;
                    case "Passing": val = (m.autoPassed + m.teleopPassed); break;
                    case "Accuracy %": val = m.getCalculatedTeleopScore() / Math.max(1, m.teleopScored) * 100; break;
                }
                teamValues.computeIfAbsent(m.teamNum, k -> new ArrayList<>()).add(val);
            }
        }

        List<Map.Entry<String, Double>> sortedValues = teamValues.entrySet().stream()
                .map(entry -> {
                    double avg = entry.getValue().stream().mapToDouble(d -> d).average().orElse(0.0);
                    return new HashMap.SimpleEntry<>(entry.getKey(), avg);
                })
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .collect(Collectors.toList());

        int start = leaderboardPage * PAGE_SIZE;
        if (start >= sortedValues.size()) {
            if (leaderboardPage > 0) {
                leaderboardPage--;
                return;
            }
            leaderboardChart.clear();
            leaderboardChart.setNoDataText("No data for selection");
            leaderboardPageText.setText("No Data");
            return;
        }

        int end = Math.min(start + PAGE_SIZE, sortedValues.size());
        List<Map.Entry<String, Double>> pageValues = sortedValues.subList(start, end);

        leaderboardPageText.setText(String.format(Locale.getDefault(), "Rank %d-%d", start + 1, end));
        updateLeaderboardChart(pageValues, metric, start);
    }

    private void updateLeaderboardChart(List<Map.Entry<String, Double>> data, String metricLabel, int startRank) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> teamLabels = new ArrayList<>();

        // Deep copy and reverse for Horizontal Chart display (highest at top)
        List<Map.Entry<String, Double>> displayData = new ArrayList<>(data);
        Collections.reverse(displayData);

        for (int i = 0; i < displayData.size(); i++) {
            Map.Entry<String, Double> entry = displayData.get(i);
            entries.add(new BarEntry(i, entry.getValue().floatValue()));
            int rank = startRank + displayData.size() - i;
            teamLabels.add("#" + rank + " Team " + entry.getKey());
        }

        BarDataSet dataSet = new BarDataSet(entries, metricLabel);
        dataSet.setColor(ContextCompat.getColor(this, R.color.aztech_blue));
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        leaderboardChart.setData(barData);
        leaderboardChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(teamLabels));
        leaderboardChart.animateY(800);
        leaderboardChart.invalidate();
    }

    private void updateDashboard(String teamNum) {
        List<MatchData> teamMatches = new ArrayList<>();
        double totalAuto = 0;
        double totalTeleop = 0;
        double totalPassing = 0;
        int climbs = 0;

        for (MatchData m : allMatches) {
            if (m.teamNum.equals(teamNum)) {
                teamMatches.add(m);
            }
        }
        
        teamMatches.sort((m1, m2) -> {
            try {
                return Integer.compare(Integer.parseInt(m1.matchNum), Integer.parseInt(m2.matchNum));
            } catch (Exception e) {
                return m1.matchNum.compareTo(m2.matchNum);
            }
        });

        for (MatchData m : teamMatches) {
            totalAuto += m.getCalculatedAutoScore();
            totalTeleop += m.getCalculatedTeleopScore();
            totalPassing += (m.autoPassed + m.teleopPassed);
            if (m.hangStatus != null && !m.hangStatus.equalsIgnoreCase("None") && !m.hangStatus.equalsIgnoreCase("Nothing")) {
                climbs++;
            }
        }

        if (teamMatches.isEmpty()) return;

        double avgAuto = totalAuto / teamMatches.size();
        double avgTele = totalTeleop / teamMatches.size();
        double avgPassing = totalPassing / teamMatches.size();
        double climbRate = (double) climbs / teamMatches.size() * 100;

        avgAutoText.setText(String.format(Locale.getDefault(), "%.1f", avgAuto));
        avgTeleopText.setText(String.format(Locale.getDefault(), "%.1f", avgTele));
        avgPassingText.setText(String.format(Locale.getDefault(), "%.1f", avgPassing));
        climbRateText.setText(String.format(Locale.getDefault(), "%.0f%%", climbRate));

        updateTrendChart(teamMatches);
        adapter.updateData(teamMatches);
    }

    private void updateTrendChart(List<MatchData> teamMatches) {
        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < teamMatches.size(); i++) {
            MatchData m = teamMatches.get(i);
            entries.add(new Entry(i, (float) m.getTotalScore()));
            labels.add("M" + m.matchNum);
        }

        LineDataSet dataSet = new LineDataSet(entries, "Total Score");
        dataSet.setColor(ContextCompat.getColor(this, R.color.aztech_blue));
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.aztech_yellow));
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);
        dataSet.setDrawCircleHole(true);
        dataSet.setValueTextSize(10f);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(ContextCompat.getColor(this, R.color.aztech_blue_accent));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);
        scoringChart.setData(lineData);
        
        scoringChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        scoringChart.animateX(800);
        scoringChart.invalidate();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
