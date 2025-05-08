package com.example.smartstudent;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import com.example.smartstudent.Dao.UserDao;
import com.example.smartstudent.database.DatabaseHelper;
import com.example.smartstudent.model.User;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class  MainActivity extends AppCompatActivity {
    //管理员密码
    private  final String rolePassword = "123456";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1. 初始化DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // 2. 获取可写数据库实例
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        UserDao userDao = new UserDao(db);





//        登录事件
        Button btn_login = findViewById(R.id.btnToLogin);
        btn_login.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(this);
            View view = getLayoutInflater().inflate(R.layout.activity_login, null);
            dialog.setContentView(view);

            // 确保获取正确的父容器
            ViewGroup parent = (ViewGroup) view.getParent();

            // 使用正确的Behavior获取方式
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);

            // 设置高度参数
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            behavior.setPeekHeight((int)(screenHeight * 0.5));
            behavior.setMaxHeight((int)(screenHeight * 0.8)); // 设置最大高度
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            // 确保窗口参数生效
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );


            // 登录按钮点击事件
            EditText etUsername = view.findViewById(R.id.etUsername);
            EditText etPassword = view.findViewById(R.id.etPassword);
            Button  btnLogin = view.findViewById(R.id.butLogin);
            btnLogin.setOnClickListener(new View.OnClickListener() {

                //登录界面逻辑

                @Override
                public void onClick(View v) {

                    String username = etUsername.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    // 1. 输入验证
                    if (username.isEmpty()) {
                        etUsername.setError("用户名不能为空");
                        return;
                    }

                    if (password.isEmpty()) {
                        etPassword.setError("密码不能为空");
                        return;
                    }

                    // 2. 显示加载中状态（可选）
                    btnLogin.setEnabled(false);
                    btnLogin.setText("登录中...");

                    //与servlet交流
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL("http://10.0.2.2:8083/Servlet_war_exploded/login"); // 注意替换为实际地址
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("POST");
                                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                                conn.setDoOutput(true);
                                conn.setDoInput(true);

                                // 构造 JSON 数据
                                JSONObject json = new JSONObject();
                                json.put("username", username);
                                json.put("password", password);

                                // 发送数据
                                OutputStream os = conn.getOutputStream();
                                os.write(json.toString().getBytes("UTF-8"));
                                os.close();

                                // 读取响应
                                int code = conn.getResponseCode();
                                if (code == 200) {
                                    InputStream is = conn.getInputStream();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                                    final StringBuilder result = new StringBuilder();
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        result.append(line);
                                    }
                                    reader.close();

                                    // 更新 UI
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            btnLogin.setEnabled(true);
                                            btnLogin.setText("登录");
                                            String responseStr = result.toString();

//                                            Toast.makeText(getApplicationContext(), "登录结果: " + responseStr, Toast.LENGTH_LONG).show();
                                            Log.d("responseStr",responseStr);
                                            //成功登录
                                            try {
                                                JSONObject responseJson = new JSONObject(responseStr);
                                                JSONObject userJson = responseJson.getJSONObject("user");

                                                int id = userJson.getInt("id");
                                                String username = userJson.getString("username");
                                                String account = userJson.getString("account");
                                                String role = userJson.getString("role");

                                                User user = new User(id, username, account, role);
                                                //将登录信息存储起来(JSON格式)
                                                SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                                                sharedPreferences.edit()
                                                        .putString("user", new Gson().toJson(user))
                                                        .apply();


                                                //登录成功，跳转网页

                                                Toast.makeText(getApplicationContext(), "准备跳转", Toast.LENGTH_SHORT).show();

                                                boolean isAdmin = Objects.equals("admin",role);
                                                Intent intent = new Intent(MainActivity.this,
                                                        isAdmin ? Activity_admin_main.class : Activity_student_main.class);
                                                startActivity(intent);

//                                                Intent intent = new Intent(MainActivity.this, hello.class);
//                                                startActivity(intent);
                                                //关闭当前
                                                finish();


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    });
                                } else {
                                    // 错误处理
                                    runOnUiThread(() -> {
                                        btnLogin.setEnabled(true);
                                        btnLogin.setText("登录");
                                        Toast.makeText(getApplicationContext(), "连接失败，错误码：" + code, Toast.LENGTH_SHORT).show();
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(() -> {
                                    btnLogin.setEnabled(true);
                                    btnLogin.setText("登录");
                                    Toast.makeText(getApplicationContext(), "异常: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                            }
                        }
                    }).start();

                }

            });




            dialog.show();
        });


        // 读取数据
        // 读取 JSON 字符串
//                            SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
//                            String userJson = sharedPreferences.getString("user", null);
//
//                            // 反序列化为 User 对象
//                            User user = null;
//                            if (userJson != null) {
//                                Gson gson = new Gson();
//                                //获取到user
//                                user = gson.fromJson(userJson, User.class);
//                            }


        // 注册
        Button btn_Register = findViewById(R.id.btnToRegister);
        btn_Register.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(this);
            View view = getLayoutInflater().inflate(R.layout.activity_register, null);
            dialog.setContentView(view);

            // 确保获取正确的父容器
            ViewGroup parent = (ViewGroup) view.getParent();

            // 使用正确的Behavior获取方式
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);

            // 设置高度参数
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            behavior.setPeekHeight((int)(screenHeight * 0.5));
            behavior.setMaxHeight((int)(screenHeight * 0.8)); // 设置最大高度
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            // 确保窗口参数生效
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );



            //注册界面逻辑
            EditText etUsername = view.findViewById(R.id.etUsername);
            EditText etAccount = view.findViewById(R.id.etAccount);
            EditText etPassword = view.findViewById(R.id.etPassword);
            EditText etAgainPassword = view.findViewById(R.id.etAgainPassword);
            EditText etRole = view.findViewById(R.id.etRole);
            Button  butRegister = view.findViewById(R.id.butRegister);

            // 注册按钮点击事件
            butRegister.setOnClickListener(new View.OnClickListener() {



               @Override
               public void onClick(View v) {

                   final String username = etUsername.getText().toString().trim();
                   final String account = etAccount.getText().toString().trim();
                   final String password = etPassword.getText().toString().trim();
                   final String againPassword = etAgainPassword.getText().toString().trim();
                   String role = etRole.getText().toString().trim();

                   // 1. 输入验证
                   if (username.isEmpty()) {
                       etUsername.setError("用户名不能为空");
                       return;
                   }
                   if (account.isEmpty()) {
                       etAccount.setError("账号不能为空");
                       return;
                   }
                   if (password.isEmpty()) {
                       etPassword.setError("密码不能为空");
                       return;
                   }
                   if (againPassword.isEmpty()) {
                       etAgainPassword.setError("请确认密码");
                       return;
                   }

                   // 2. 密码一致性检查
                   if (!password.equals(againPassword)) {
                       etAgainPassword.setError("两次输入密码不一致");
                       etPassword.setText("");
                       etAgainPassword.setText("");
                       return;
                   }

                   final String finalRole;
                   //判断是用户还是管理员
                   if (role.equals(rolePassword)) {
                       finalRole = "admin";
                   }
                   else {
                       finalRole = "user";
                   }

                   // 3. 显示加载状态
                   butRegister.setEnabled(false);
                   butRegister.setText("注册中...");

                   // 4. 异步执行注册（避免主线程阻塞）
                   //与servlet交流
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           try {
                               URL url = new URL("http://10.0.2.2:8083/Servlet_war_exploded/register"); // 注册
                               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                               conn.setRequestMethod("POST");
                               conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                               conn.setDoOutput(true);
                               conn.setDoInput(true);


                               // 构造 JSON 数据
                               JSONObject json = new JSONObject();
                               json.put("username", username);
                               json.put("username", username);
                               json.put("account", account);
                               json.put("password", password);
                               json.put("role", finalRole);

                               // 发送数据
                               OutputStream os = conn.getOutputStream();
                               os.write(json.toString().getBytes("UTF-8"));
                               os.close();

                               // 读取响应
                               int code = conn.getResponseCode();
                               if (code == 200) {
                                   InputStream is = conn.getInputStream();
                                   BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                                   final StringBuilder result = new StringBuilder();
                                   String line;
                                   while ((line = reader.readLine()) != null) {
                                       result.append(line);
                                   }
                                   reader.close();

                                   // 更新 UI
                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           butRegister.setEnabled(true);
                                           butRegister.setText("注册");
                                           String responseStr = result.toString();

//                                            Toast.makeText(getApplicationContext(), "注册结果: " + responseStr, Toast.LENGTH_LONG).show();
                                           Log.d("responseStr",responseStr);
                                           //成功登录
                                           try {
                                               JSONObject responseJson = new JSONObject(responseStr);
                                               JSONObject userJson = responseJson.getJSONObject("user");

                                               int id = userJson.getInt("id");
                                               String username = userJson.getString("username");
                                               String account = userJson.getString("account");
                                               String role = userJson.getString("role");

                                               User user = new User(id, username, account, role);
                                               //将登录信息存储起来(JSON格式)
                                               SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                                               sharedPreferences.edit()
                                                       .putString("user", new Gson().toJson(user))
                                                       .apply();


                                               //登录成功，跳转网页

                                               Toast.makeText(getApplicationContext(), "准备跳转", Toast.LENGTH_SHORT).show();

                                               boolean isAdmin = Objects.equals("admin",role);
//                                                Intent intent = new Intent(MainActivity.this,
//                                                        isAdmin ? Activity_admin_main.class : Activity_student_main.class);
//                                                startActivity(intent);

                                               Intent intent = new Intent(MainActivity.this, hello.class);
                                               startActivity(intent);
                                               //关闭当前
                                               finish();


                                           } catch (JSONException e) {
                                               e.printStackTrace();
                                           }


                                       }
                                   });
                               } else {
                                   // 错误处理
                                   runOnUiThread(() -> {
                                       butRegister.setEnabled(true);
                                       butRegister.setText("注册");
                                       Toast.makeText(getApplicationContext(), "连接失败，错误码：" + code, Toast.LENGTH_SHORT).show();
                                   });
                               }
                           } catch (Exception e) {
                               e.printStackTrace();
                               runOnUiThread(() -> {
                                   butRegister.setEnabled(true);
                                   butRegister.setText("注册");
                                   Toast.makeText(getApplicationContext(), "异常: " + e.getMessage(), Toast.LENGTH_LONG).show();
                               });
                           }
                       }
                   }).start();

               }

            });

            dialog.show();
        });



    }





//        学生页面跳转
//        Intent intent = new Intent(MainActivity.this, Activity_student_main.class);
//        startActivity(intent);

//        管理员页面跳转
//    Intent intent = new Intent(MainActivity.this, Activity_admin_main.class);
//    startActivity(intent);
//    }

//    //        登录页面跳转
//    Intent intent = new Intent(MainActivity.this, Activity_login.class);
//    startActivity(intent);


}
