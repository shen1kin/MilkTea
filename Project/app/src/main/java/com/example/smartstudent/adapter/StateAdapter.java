package com.example.smartstudent.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.LinearLayout;

import com.example.smartstudent.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder> {

    private Context context;
    private List<String> stateList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String state);
    }

    public StateAdapter(Context context, List<String> stateList, OnItemClickListener listener) {
        this.context = context;
        this.stateList = stateList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_add_state, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String state = stateList.get(position);
        holder.tvState.setText(state);

        holder.layoutButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(state);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stateList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvState;
        LinearLayout layoutButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvState = itemView.findViewById(R.id.tvState);
            layoutButton = itemView.findViewById(R.id.layoutButton);
        }
    }
}
