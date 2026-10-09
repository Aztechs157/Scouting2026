# In-Depth PR Analysis: Data Hub & 2026 REBUILT Initialization

This document provides a comprehensive breakdown of the technical and functional changes introduced in the `data-hub` branch. It is intended to guide the PR reviewer through the architecture of the new Dashboard and the critical fixes applied for the 2026 season.

---

## 1. Architectural Overhaul: The `MatchData` Model
To support the **Data Hub Dashboard**, I transitioned from simple string manipulation to a structured data model.

### Key Changes:
*   **Schema Mapping**: `MatchData.java` now explicitly maps all 29 columns of the 2026 CSV schema. This ensures the app is a "source of truth" that matches your Python aggregation scripts.
*   **Scoring Logic**:
    *   **Old**: The app previously reported raw "Shots Fired".
    *   **New**: Added `getCalculatedAutoScoredWhole()` and `getCalculatedTeleopScoredWhole()`. These use a multiplier (e.g., 0.75 for "75%") to estimate actual **Fuel Pieces Scored**.
*   **Point Calculation**: Added methods like `getTotalScore()` that accurately weigh 2026 points:
    *   **Auto Climb**: 15 pts.
    *   **Endgame Tower**: 10 (L1), 20 (L2), or 30 (L3) pts.

---

## 2. Feature Deep Dive: Data Hub Dashboard
The `activityDataShowing` was transformed from a skeleton into a full-featured analytics suite.

### Data Lifecycle:
1.  **Scanning**: The activity performs an I/O scan of `Environment.DIRECTORY_DOCUMENTS` for files matching `match_scouting_*.csv`.
2.  **Parsing**: Uses a robust split (`split(",", -1)`) to handle trailing empty comments or notes without throwing `IndexOutOfBoundsException`.
3.  **Aggregation**: Data is filtered by `Team Number` and aggregated using Java Streams to calculate averages and success rates.

### Visualizations:
*   **Team Search**: Implemented a `MaterialAutoCompleteTextView` allowing scouters to jump to any team present in the local database.
*   **Leaderboard**: A `HorizontalBarChart` (using MPAndroidChart) that ranks robots by metric. It includes pagination (Rank 1-5, 6-10) to keep the UI clean on smaller tablets.
*   **Trend Analysis**: A `LineChart` showing "Total Match Score" over time, allowing drive coaches to see if a robot is improving or becoming unreliable.

---

## 3. Critical Bug Fixes & Stability

### Resource ID & Layout Sync
*   **Pre-Match Fix**: Resolved a crash where the code searched for a 2025 `coral` ID while the XML had been updated to `fuel`.
*   **After-Match Overhaul**: The Java code was attempting to access four IDs that had been removed from the 2026 layout. I synced the controller to now correctly track:
    *   `over_bump`
    *   `under_trench`
    *   `played_defense`
    *   `collected_fuel`
    *   `passed_fuel`

### Storage & Permissions (SDK 35)
*   The app now targets **Android 15 (API 35)**.
*   **Permissions**: Added `READ_EXTERNAL_STORAGE` (max SDK 32) and modern media permissions to ensure the app can read its own CSVs on newer tablets.
*   **Legacy Storage**: Enabled `requestLegacyExternalStorage` as a stop-gap for reliable file creation in shared directories.

---

## 4. UI/UX Improvements
*   **Sidebar Navigation**: Implemented a `NavigationDrawer` (Sidebar) to allow quick switching between the Scouting flow and the Dashboard without hitting "Back" 5 times.
*   **Match Details**: Tapping a match in the history list now opens a `MaterialAlertDialog` with a full qualitative breakdown of that specific match, including scouter comments.

---

## 5. Schema Integrity Summary
Every CSV written now follows this exact order to prevent "Column Drift" in the aggregation notebooks:
`Competition, Version, Scout, Team#, Color, Match#, Preload, StartPos, AutoScored, AutoAccuracy, AutoPassed, AutoHang, TeleScored, TeleAccuracy, TelePassed, HangStatus, HangTime, AccuracyPos, OverBump, UnderTrench, PlayedDefense, CollectedFuel, PassedFuel, DidNothing, Other, DefenseNotes, StopReason, Rank, Comments`

> [!TIP]
> **Recommendation for Reviewer**: Pay close attention to the `getAccuracyMultiplier` method in `MatchData.java`. It defines how we translate qualitative scouter feedback (e.g., "75%") into the quantitative data used by the charts.
