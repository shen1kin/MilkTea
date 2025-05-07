package com.example.smartstudent.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.R;
import com.example.smartstudent.model.VipserveItem;

import java.util.List;

public class VipServiceAdapter extends RecyclerView.Adapter<VipServiceAdapter.ViewHolder> {
    private List<VipserveItem> items;
    private Context context;

    public VipServiceAdapter(Context context, List<VipserveItem> items) {
        this.context = context;
        this.items = items;
    }

    //使用 LayoutInflater 加载单个项的布局 (item_vipservice.xml)
    //创建并返回 ViewHolder 实例
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vipservice, parent, false);
        return new ViewHolder(view);
    }

    //获取当前位置的数据项
    //将数据绑定到视图：
    //设置图标 (setImageResource)
    //设置文本 (setText)
    //设置点击事件，点击后跳转到目标Activity

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VipserveItem item = items.get(position);
        holder.icon.setImageResource(item.getIconRes());
        holder.name.setText(item.getName());

        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, item.getTargetActivity()));
        });
    }

    //返回数据项的总数，决定RecyclerView显示多少项
    @Override
    public int getItemCount() {
        return items.size();
    }

    //为了方便寻找item里的组件
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iv_icon);
            name = itemView.findViewById(R.id.tv_name);
        }
    }
}