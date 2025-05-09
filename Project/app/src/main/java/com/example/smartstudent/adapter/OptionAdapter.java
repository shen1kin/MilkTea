package com.example.smartstudent.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// 列表适配器，用于在对话框中显示选项列表
public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {
    private final Context context;
    private final List<String> optionList;
    private final ChipGroup chipGroup;
    private final Set<String> selectedOptions = new HashSet<>(); // 记录选中项

    public OptionAdapter(Context context, List<String> optionList, ChipGroup chipGroup) {
        this.context = context;
        this.optionList = optionList;
        this.chipGroup = chipGroup;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOptionName;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        SwitchCompat switchOption;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOptionName = itemView.findViewById(R.id.textOptionName);
            switchOption = itemView.findViewById(R.id.swhChange);
        }
    }

    @Override
    public OptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_admin_add_select_single_select, parent, false);
        return new OptionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OptionAdapter.ViewHolder holder, int position) {
        String option = optionList.get(position);
        holder.tvOptionName.setText(option);

        // 先移除监听器，避免旧的监听器干扰
        holder.switchOption.setOnCheckedChangeListener(null);

        // 初始化 selectedOptions（根据 chipGroup 的内容）
        if (!selectedOptions.contains(option)) {
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                View chipView = chipGroup.getChildAt(i);
                if (chipView instanceof Chip) {
                    Chip chip = (Chip) chipView;
                    if (chip.getText().toString().equals(option)) {
                        selectedOptions.add(option);
                        break;
                    }
                }
            }
        }

        // 设置初始状态
        holder.switchOption.setChecked(selectedOptions.contains(option));

        // 添加监听器（在初始化之后）
        holder.switchOption.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedOptions.contains(option)) {
                    selectedOptions.add(option);
                    Chip chip = new Chip(context);
                    chip.setText(option);
                    chip.setCloseIconVisible(true);
                    chip.setOnCloseIconClickListener(v -> {
                        chipGroup.removeView(chip);
                        selectedOptions.remove(option);
                        notifyItemChanged(position);
                    });
                    chipGroup.addView(chip);
                }
            } else {
                selectedOptions.remove(option);
                for (int i = 0; i < chipGroup.getChildCount(); i++) {
                    View chipView = chipGroup.getChildAt(i);
                    if (chipView instanceof Chip) {
                        Chip chip = (Chip) chipView;
                        if (chip.getText().toString().equals(option)) {
                            chipGroup.removeView(chip);
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }
}