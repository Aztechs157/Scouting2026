package com.example.roboticsscoutingmatchapp;

public class MatchData {
    public String competition;
    public int dataVersion;
    public String scout;
    public String teamNum;
    public String teamColor;
    public String matchNum;
    public boolean preloadedFuel;
    public String startingPos;
    public int autoScored;
    public String autoAccuracy;
    public int autoPassed;
    public boolean autoHang;
    public int teleopScored;
    public String teleopAccuracy;
    public int teleopPassed;
    public String hangStatus;
    public int hangTime;
    public String accuracyPos;
    public boolean overBump;
    public boolean underTrench;
    public boolean playedDefense;
    public boolean collectedFuel;
    public boolean passedFuel;
    public boolean didNothing;
    public boolean other;
    public String defense;
    public String stopReason;
    public String rank;
    public String comments;

    public MatchData(String[] csvRow) {
        if (csvRow.length < 29) return;
        
        this.competition = csvRow[0];
        this.dataVersion = tryParseInt(csvRow[1]);
        this.scout = csvRow[2];
        this.teamNum = csvRow[3];
        this.teamColor = csvRow[4];
        this.matchNum = csvRow[5];
        this.preloadedFuel = Boolean.parseBoolean(csvRow[6]);
        this.startingPos = csvRow[7];
        this.autoScored = tryParseInt(csvRow[8]);
        this.autoAccuracy = csvRow[9];
        this.autoPassed = tryParseInt(csvRow[10]);
        this.autoHang = Boolean.parseBoolean(csvRow[11]);
        this.teleopScored = tryParseInt(csvRow[12]);
        this.teleopAccuracy = csvRow[13];
        this.teleopPassed = tryParseInt(csvRow[14]);
        this.hangStatus = csvRow[15];
        this.hangTime = tryParseInt(csvRow[16]);
        this.accuracyPos = csvRow[17];
        this.overBump = Boolean.parseBoolean(csvRow[18]);
        this.underTrench = Boolean.parseBoolean(csvRow[19]);
        this.playedDefense = Boolean.parseBoolean(csvRow[20]);
        this.collectedFuel = Boolean.parseBoolean(csvRow[21]);
        this.passedFuel = Boolean.parseBoolean(csvRow[22]);
        this.didNothing = Boolean.parseBoolean(csvRow[23]);
        this.other = Boolean.parseBoolean(csvRow[24]);
        this.defense = csvRow[25];
        this.stopReason = csvRow[26];
        this.rank = csvRow[27];
        this.comments = csvRow[28];
    }

    public int getCalculatedAutoScoredWhole() {
        return (int) Math.round(autoScored * getAccuracyMultiplier(autoAccuracy));
    }

    public int getCalculatedTeleopScoredWhole() {
        return (int) Math.round(teleopScored * getAccuracyMultiplier(teleopAccuracy));
    }

    public double getCalculatedAutoScore() {
        return (double) getCalculatedAutoScoredWhole() + (autoHang ? 15 : 0);
    }

    public double getCalculatedTeleopScore() {
        return (double) getCalculatedTeleopScoredWhole() + getClimbPoints();
    }

    public double getTotalScore() {
        return getCalculatedAutoScore() + getCalculatedTeleopScore();
    }

    public int getClimbPoints() {
        if (hangStatus == null) return 0;
        switch (hangStatus) {
            case "Level 1": return 10;
            case "Level 2": return 20;
            case "Level 3": return 30;
            default: return 0;
        }
    }


    private double getAccuracyMultiplier(String accuracy) {
        if (accuracy == null) return 0;
        if (accuracy.contains("10%")) return 0.05;
        if (accuracy.contains("25%")) return 0.25;
        if (accuracy.contains("50%")) return 0.50;
        if (accuracy.contains("75%")) return 0.75;
        if (accuracy.contains("95%")) return 0.97;
        return 0;
    }

    private int tryParseInt(String val) {
        try {
            return Integer.parseInt(val.trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
