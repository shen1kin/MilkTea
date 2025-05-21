package com.example.smartstudent.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.MainActivity;
import com.example.smartstudent.R;
import com.example.smartstudent.model.VipserveItem;

import java.util.List;

/**
 * 会员服务适配器：绑定每个服务项的图标、名称与点击行为
 */
public class VipServiceAdapter extends RecyclerView.Adapter<VipServiceAdapter.ViewHolder> {

    private final Context context; // 上下文环境
    private final List<VipserveItem> items; // 会员服务项数据

    public VipServiceAdapter(Context context, List<VipserveItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载 item 卡片布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_vipservice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VipserveItem item = items.get(position);
        holder.tvName.setText(item.getName());
        holder.ivIcon.setImageResource(item.getIconRes());

        holder.itemView.setOnClickListener(v -> {
            String name = item.getName(); // 获取点击项的名称

            // 未实现功能统一弹窗处理
            if (item.getTargetActivity() == null || item.getTargetActivity().getSimpleName().equals("hello")) {

                // 联系客服弹窗
                if (name.equals("联系客服")) {
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_contact_service, null);
                    new AlertDialog.Builder(context)
                            .setView(dialogView)
                            .setCancelable(true)
                            .create()
                            .show();
                } else if (name.equals("退出登录")) {
                    // 创建确认退出弹窗
                    new AlertDialog.Builder(context)
                            .setTitle("确认退出")
                            .setMessage("确定要退出登录吗？")
                            .setCancelable(true)
                            .setNegativeButton("取消", null) // 点击取消，不做任何事
                            .setPositiveButton("确定", (dialog, which) -> {
                                // 清除登录信息
                                SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("user");
                                editor.apply();

                                // 跳转到主界面并清空当前栈
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);

                                // 添加过渡动画
                                if (context instanceof android.app.Activity) {
                                    ((android.app.Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                            })
                            .show();
                } else {
                    // 其他未实现功能提示弹窗
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feature_not_ready, null);
                    new AlertDialog.Builder(context)
                            .setView(dialogView)
                            .setCancelable(true)
                            .create()
                            .show();
                }

            } else {
                // 已实现的功能：跳转目标Activity
                Intent intent = new Intent(context, item.getTargetActivity());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // 内部类：用于绑定item视图控件
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivIcon = itemView.findViewById(R.id.iv_icon);
        }
    }
}
