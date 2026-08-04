package com.example.roboticsscoutingmatchapp;

/**
 * Data model representing a single match scouting report for the 2026 REBUILT game.
 * Maps directly to the 29-column CSV schema used for data aggregation.
 */
public class MatchData {
    // Basic Match Info
    public String competition;
    public int dataVersion; // Incrementing version to track schema changes (e.g., 2026 = v2)
    public String scout;
    public String teamNum;
    public String teamColor;
    public String matchNum;
    
    // Pre-Match
    public boolean preloadedFuel;
    
    // Autonomous Period
    public String startingPos;
    public int autoScored; // Total fuel shots fired in Auto
    public String autoAccuracy; // Qualitative accuracy percentage (e.g., "75%")
    public int autoPassed; // Fuel passed to alliance partners
    public boolean autoHang; // Successful climb to Tower Level 1 during Auto
    
    // TeleOp Period
    public int teleopScored; // Total fuel shots fired in TeleOp
    public String teleopAccuracy;
    public int teleopPassed;
    public String hangStatus; // Endgame climb level (Level 1, 2, 3, or None)
    public int hangTime; // Time remaining when climb was completed
    public String accuracyPos; // Movement state with best accuracy
    
    // Field Navigation & Robot State (2026 specific)
    public boolean overBump;
    public boolean underTrench;
    public boolean playedDefense;
    public boolean collectedFuel;
    public boolean passedFuel;
    public boolean didNothing;
    public boolean other;
    
    // Post-Match Qualitative Data
    public String defense; // Description of defense received/given
    public String stopReason; // Reason if the robot stopped moving
    public String rank; // Relative rank in the alliance
    public String comments;

    /**
     * Constructs a MatchData object from a raw CSV row.
     * @param csvRow String array containing the 29 values in the schema order.
     */
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

    /**
     * Estimates the number of fuel pieces scored in Auto.
     * Calculation: Total Shots * Accuracy Multiplier
     */
    public int getCalculatedAutoScoredWhole() {
        return (int) Math.round(autoScored * getAccuracyMultiplier(autoAccuracy));
    }

    /**
     * Estimates the number of fuel pieces scored in TeleOp.
     */
    public int getCalculatedTeleopScoredWhole() {
        return (int) Math.round(teleopScored * getAccuracyMultiplier(teleopAccuracy));
    }

    /**
     * Calculates total points earned in Autonomous.
     * Includes fuel points and the 15pt Auto Climb bonus.
     */
    public double getCalculatedAutoScore() {
        return (double) getCalculatedAutoScoredWhole() + (autoHang ? 15 : 0);
    }

    /**
     * Calculates total points earned in TeleOp/Endgame.
     * Includes fuel points and Tower climb points (10, 20, or 30).
     */
    public double getCalculatedTeleopScore() {
        return (double) getCalculatedTeleopScoredWhole() + getClimbPoints();
    }

    /**
     * Total match score contribution by this robot.
     */
    public double getTotalScore() {
        return getCalculatedAutoScore() + getCalculatedTeleopScore();
    }

    /**
     * Maps the hangStatus string to specific point values for the 2026 REBUILT game.
     */
    public int getClimbPoints() {
        if (hangStatus == null) return 0;
        switch (hangStatus) {
            case "Level 1": return 10;
            case "Level 2": return 20;
            case "Level 3": return 30;
            default: return 0;
        }
    }

    /**
     * Converts qualitative accuracy strings to numeric multipliers.
     * Note: "More than 95%" is treated as 0.97 to account for occasional misses.
     */
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
