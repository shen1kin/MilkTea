package com.example.smartstudent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.OptionAdapter;
import com.example.smartstudent.adapter.StateAdapter;
import com.example.smartstudent.model.User;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Activity_admin_AddItem extends AppCompatActivity {
    private LinearLayout statusContainer;
    private Button btnAddStatus;
    private Button butAddNewState;

    private List<String> allSlectOption = new ArrayList<>();
    //包含
    private List<String> allStateOptions = new ArrayList<>();

    private Set<String> selectedStates = new HashSet<>(); // 用于记录已选择的状态

    public List<String> getAvailableStates() {
        return allStateOptions.stream()
                .filter(state -> !selectedStates.contains(state))
                .collect(Collectors.toList());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_item);
        //获取主页面 状态界面ID
        statusContainer = findViewById(R.id.statusContainer);
        btnAddStatus = findViewById(R.id.btnAddStatus);
        // 点击“添加状态”按钮，弹出"可添加状态"窗口
        btnAddStatus.setOnClickListener(v -> {
            addStatusView();
        });
    }

    //"可添加状态"窗口
    @SuppressLint("SetTextI18n")
    public void addStatusView() {
        //后端获取属性信息
        //与servlet交流
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:8083/Servlet_war_exploded/milk-tea-attribute"); // 注意替换为实际地址
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoInput(true);

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
                        is.close();
                        conn.disconnect();

                        // 更新 UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String responseStr = result.toString();

//                              Toast.makeText(getApplicationContext(), "登录结果: " + responseStr, Toast.LENGTH_LONG).show();
                                Log.d("responseStr",responseStr);
                                //成功登录
                                try {
                                    JSONObject responseJson = new JSONObject(responseStr);
                                    JSONObject attributeJson = responseJson.getJSONObject("attribute");
//                                  {"attribute":{"list":"[温度, 杯型, 甜度, 配料]"},"status":"success"}
                                    // 取出 list
                                    String listStr = attributeJson.getString("list");
                                    // 转换成 JSONArray（需要先去掉引号）
                                    JSONArray listArray = new JSONArray(listStr);
                                    // 清空旧数据
                                    allStateOptions.clear();
                                    // 加入新数据
                                    for (int i = 0; i < listArray.length(); i++) {
                                        allStateOptions.add(listArray.getString(i));
                                    }

                                    //获取到数据，显示界面
                                    showStateDialog();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        // 错误处理
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "连接失败，错误码：" + code, Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "异常: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }
        }).start();

    }
    //显示可添加状态 内的 信息列表
    //rcv显示数据，保证数据能刷新
    private void showStateDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.activity_admin_add_state, null);
        dialog.setContentView(dialogView);

        // 确保获取正确的父容器
        ViewGroup parent = (ViewGroup) dialogView.getParent();

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


        //创建弹窗AlertDialog构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //获取弹窗内组件
        RecyclerView rvOptions = dialogView.findViewById(R.id.rvStateOption);
        //设置布局为垂直线性排列。
        rvOptions.setLayoutManager(new LinearLayoutManager(this));
        // 创建列表适配器，传入所有选项数据和目标 ChipGroup
        //创建并设置一个自定义的 RecyclerView.Adapter（OptionAdapter）。
        //allStateOption 是所有可供选择的选项（可能是一个 List<String>）。  后面从服务器获取
        //targetChipGroup 是要将选项添加到的 ChipGroup。
        //每当用户点击某个选项时，adapter 内部会创建一个 Chip 并添加到该 ChipGroup。
        StateAdapter adapter = new StateAdapter(this, getAvailableStates(), state -> {
            // 当点击某个状态后：
            selectedStates.add(state); // 标记为已选
            dialog.dismiss();          // 关闭弹窗
            // 你可以在这里执行添加 Chip 的操作，或者刷新 UI 显示
            // 将状态添加界面
            addStatusModule(state);
        });

        rvOptions.setAdapter(adapter);
        //设置对话框的内容视图为刚刚加载的 dialog_add_option.xml。
        //创建并展示对话框。
        builder.setView(dialogView);

        butAddNewState = dialogView.findViewById(R.id.butAddNewState);
        //按钮添加新属性
        butAddNewState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_admin_AddItem.this, Activity_admin_add_new_state.class);
                startActivity(intent);
            }
        });



        dialog.show();
    }
    //参数 是 状态 ，将状态返回到主界面，并且在主界面动态创建新组件
    @SuppressLint("SetTextI18n")
    public void addStatusModule(String state) {
        LayoutInflater inflater = LayoutInflater.from(this);
        // 将 status_module.xml 实例化为 View 对象
        // 将布局 XML 文件实例化为其对应的View 对象。它不会直接使用。
        // 而是使用 Activity.getLayoutInflater()或 Context.getSystemService来获取一个已连接到当前上下文并针对您正在运行的设备正确配置的标准 LayoutInflater 实例。
        View moduleView = inflater.inflate(R.layout.item_project_state_text, statusContainer, false);

        // 获取子控件
        TextView tvTitle = moduleView.findViewById(R.id.tvTitle);
        Button btnAddOpt = moduleView.findViewById(R.id.btnAddOpt);
        // ChipGroup 是 Android 提供的一个用于显示多个 Chip（标签/标签按钮）组件的容器，
        // 它是 ViewGroup 的子类，通常和 com.google.android.material.chip.Chip 搭配使用。
        ChipGroup chipGroup = moduleView.findViewById(R.id.chipGroupOptions);

        // 设置标题 获取编号
        tvTitle.setText(state);




        // 点击“添加选项”按钮时，弹出对话框选择选项
        btnAddOpt.setOnClickListener(v -> {
            //获取数据
            //用属性查询属性值
            //与servlet交流
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://10.0.2.2:8083/Servlet_war_exploded/milk-tea-attribute-value"); // 注意替换为实际地址
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        // 构造 JSON 数据
                        JSONObject json = new JSONObject();
                        json.put("attribute", state);

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
                                    String responseStr = result.toString();

//                              Toast.makeText(getApplicationContext(), "登录结果: " + responseStr, Toast.LENGTH_LONG).show();
                                    Log.d("responseStr",responseStr);
                                    //成功登录
                                    try {
//                                  {"attribute_value":{"list":"[中杯, 大杯, 小杯]"},"status":"founded"}
                                        JSONObject responseJson = new JSONObject(responseStr);
                                        JSONObject attributeJson = responseJson.getJSONObject("attribute_value");
//                                  {"attribute":{"list":"[温度, 杯型, 甜度, 配料]"},"status":"success"}
                                        // 取出 list
                                        String listStr = attributeJson.getString("list");
                                        // 转换成 JSONArray（需要先去掉引号）
                                        JSONArray listArray = new JSONArray(listStr);
                                        // 清空旧数据
                                        allSlectOption.clear();
                                        // 加入新数据
                                        for (int i = 0; i < listArray.length(); i++) {
                                            allSlectOption.add(listArray.getString(i));
                                        }

                                        //显示界面 显示信息
                                        showAddOptionDialog(chipGroup);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            // 错误处理
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), "连接失败，错误码：" + code, Toast.LENGTH_SHORT).show();
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "异常: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    }
                }
            }).start();



        });

        // 将新模块添加到容器中，添加到主界面中
        statusContainer.addView(moduleView);
    }


    //在新窗口下，将选中的选项信息显示出来
    // 弹出选项列表对话框，将所选选项添加到给定的 ChipGroup 中
    private void showAddOptionDialog(ChipGroup targetChipGroup) {


        // 使用 BottomSheetDialog 也可以，下面示例使用 AlertDialog 结合自定义布局
        //创建弹窗AlertDialog构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //加载 实例化 弹窗布局

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.activity_admin_add_select, null);
        dialog.setContentView(dialogView);

        // 确保获取正确的父容器
        ViewGroup parent = (ViewGroup) dialogView.getParent();

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


        //获取弹窗内组件
        RecyclerView rvOptions = dialogView.findViewById(R.id.rvSelectOption);
        //设置布局为垂直线性排列。
        rvOptions.setLayoutManager(new LinearLayoutManager(this));
        // 创建列表适配器，传入所有选项数据和目标 ChipGroup
        //创建并设置一个自定义的 RecyclerView.Adapter（OptionAdapter）。
        //allSlectOption 是所有可供选择的选项（可能是一个 List<String>）。  后面从服务器获取
        //targetChipGroup 是要将选项添加到的 ChipGroup。
        //每当用户点击某个选项时，adapter 内部会创建一个 Chip 并添加到该 ChipGroup。
        OptionAdapter adapter = new OptionAdapter(this, allSlectOption, targetChipGroup);
        rvOptions.setAdapter(adapter);
        //设置对话框的内容视图为刚刚加载的 dialog_add_option.xml。
        //创建并展示对话框。
        builder.setView(dialogView);
        dialog.show();
    }

}
