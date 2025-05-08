package com.example.smartstudent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_student_set extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_set);

        //退出登录
        Button exitLogin = findViewById(R.id.butExitLogin);
        exitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除本地存储信息
                SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("user");  // 删除键为 "user" 的数据
                editor.apply();

                Intent intent = new Intent(Activity_student_set.this, MainActivity.class);
                startActivity(intent);
                //关闭当前
                finish();
            }
        });

    }
}