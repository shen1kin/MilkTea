package com.example.smartstudent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.R;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    private List<String> classList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String state);
    }

    public ClassAdapter(List<String> classList, OnItemClickListener listener) {
        this.classList = classList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_add_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item  = classList.get(position);
        holder.tvClass.setText(item);

        holder.layoutButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvClass;
        LinearLayout layoutButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClass = itemView.findViewById(R.id.tvClass);
            layoutButton = itemView.findViewById(R.id.layoutButton);
        }
    }
}
