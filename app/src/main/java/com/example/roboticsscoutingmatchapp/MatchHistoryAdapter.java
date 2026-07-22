package com.example.roboticsscoutingmatchapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MatchHistoryAdapter extends RecyclerView.Adapter<MatchHistoryAdapter.ViewHolder> {

    public interface OnMatchClickListener {
        void onMatchClick(MatchData match);
    }

    private List<MatchData> matches;
    private OnMatchClickListener listener;

    public MatchHistoryAdapter(List<MatchData> matches, OnMatchClickListener listener) {
        this.matches = matches;
        this.listener = listener;
    }

    public void updateData(List<MatchData> newMatches) {
        this.matches = newMatches;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchData match = matches.get(position);
        holder.matchNum.setText("M #" + match.matchNum);
        holder.scoreSummary.setText(String.format("Auto: %d | Tele: %d", match.autoScored, match.teleopScored));
        holder.hangStatus.setText("Climb: " + match.hangStatus);
        holder.scouterName.setText(match.scout);
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMatchClick(match);
            }
        });
    }


    @Override
    public int getItemCount() {
        return matches.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView matchNum, scoreSummary, hangStatus, scouterName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            matchNum = itemView.findViewById(R.id.match_num_text);
            scoreSummary = itemView.findViewById(R.id.score_summary_text);
            hangStatus = itemView.findViewById(R.id.hang_status_text);
            scouterName = itemView.findViewById(R.id.scout_name_text);
        }
    }
}
