package com.example.smartstudent;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 用户端设置页 Activity，实现：
 * - 返回按钮
 * - 检查版本更新
 * - 清除缓存
 */
public class Activity_student_set extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_set);

        // 1. 返回按钮：点击后关闭当前页面
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> {
            finish(); // 关闭当前Activity，返回上一页
        });

        // 2. 检查版本更新按钮：点击后弹出“已是最新版本”提示
        LinearLayout btnCheckUpdate = findViewById(R.id.btnCheckUpdate);
        btnCheckUpdate.setOnClickListener(v -> {
            showCustomToast("已是最新版本");
        });

        // 3. 清除缓存按钮：点击后模拟清除缓存逻辑
        LinearLayout btnClearCache = findViewById(R.id.btnClearCache);
        btnClearCache.setOnClickListener(v -> {
            // 加载自定义弹窗布局
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_clear_cache, null);

            // 创建 AlertDialog 并设置内容视图
            final AlertDialog dialog = new AlertDialog.Builder(Activity_student_set.this)
                    .setView(dialogView)
                    .setCancelable(true)
                    .create();

            // 绑定按钮控件
            TextView btnCancel = dialogView.findViewById(R.id.btnCancel);
            TextView btnConfirm = dialogView.findViewById(R.id.btnConfirm);

            // 设置按钮点击事件
            btnCancel.setOnClickListener(v1 -> dialog.dismiss()); // 取消按钮，关闭弹窗

            btnConfirm.setOnClickListener(v2 -> {
                clearAppCache();
                showCustomToast("缓存已清除");
                dialog.dismiss();
            });


            dialog.show(); // 显示弹窗
        });

    }

    /**
     * 自定义样式的Toast（黑色背景 + 灰色文字）
     */
    private void showCustomToast(String message) {
        // 加载自定义布局
        View layout = getLayoutInflater().inflate(R.layout.toast_custom, null);
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        // 创建Toast并设置参数
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 0); // 居中显示
        toast.show();
    }


    /**
     * 清除缓存方法（可按需扩展为实际清除图片、数据库等）
     */
    private void clearAppCache() {
        // 示例：清除 SharedPreferences 中的临时缓存项（可根据项目扩展）
        SharedPreferences sp = getSharedPreferences("temp_cache", MODE_PRIVATE);
        sp.edit().clear().apply();

        // 你也可以使用 Glide、DiskLruCache 等库的清理接口
        // 如：Glide.get(this).clearDiskCache();
    }
}
