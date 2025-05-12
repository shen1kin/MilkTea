package com.example.smartstudent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudent.utils.KeyboardUtils;

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
import java.util.HashSet;
import java.util.Set;

public class Activity_admin_add_new_state extends AppCompatActivity {
    private LinearLayout statusContainer;
    private Button btnAddSelect;
    private Button butConfirmAddState;
    private EditText etStateName;

    private ArrayList<String> editTextValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_new_state);

        etStateName = findViewById(R.id.etStateName);

        btnAddSelect = findViewById(R.id.btnAddSelect);

        butConfirmAddState = findViewById(R.id.butConfirmAddState);

        statusContainer = findViewById(R.id.layoutSelectContainer);

        btnAddSelect.setOnClickListener(view -> addStatusModule());

        butConfirmAddState.setOnClickListener(view -> confirmAddState());

    }

    @SuppressLint("SetTextI18n")
    public void addStatusModule() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View moduleView = inflater.inflate(R.layout.item_admin_add_new_state_select, statusContainer, false);

        EditText etSelect = moduleView.findViewById(R.id.etSelect);
        Button butDelSelect = moduleView.findViewById(R.id.butDelSelect);

        // 给删除按钮设置点击监听，点击后删除该视图模块
        butDelSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除当前视图
                statusContainer.removeView(moduleView);
            }
        });


        // 添加到容器
        statusContainer.addView(moduleView);

        // 请求焦点并弹出软键盘
        etSelect.requestFocus();
        KeyboardUtils.showKeyboard(this, etSelect);

        // 焦点变化监听：若失去焦点且内容为空则删除模块
        etSelect.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String text = etSelect.getText().toString().trim();
                if (text.isEmpty()) {
                    statusContainer.removeView(moduleView);
                }
            }
        });
    }

    // 捕获点击空白区域取消焦点，触发焦点监听
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    KeyboardUtils.hideKeyboard(this, v);  // 使用工具类来隐藏软键盘
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    //获取所有EditText的值，
    private void getAllEditTextValues() {
        //清空
        editTextValues.clear();
        // 创建一个列表，用于存储所有 EditText 的值
        ArrayList<String> editTextValues = new ArrayList<>();

        // 遍历 statusContainer 中的所有子视图
        for (int i = 0; i < statusContainer.getChildCount(); i++) {
            View childView = statusContainer.getChildAt(i);

            // 检查当前子视图是否是一个 LinearLayout（包含 EditText 的视图）
            if (childView instanceof LinearLayout) {
                // 获取 LinearLayout 中的 EditText
                EditText editText = childView.findViewById(R.id.etSelect);

                if (editText != null) {
                    // 获取 EditText 的值并添加到列表
                    String value = editText.getText().toString().trim();
                    editTextValues.add(value);
                }
            }
        }

        // 打印所有 EditText 的值（可以根据需要进行处理）
        for (String value : editTextValues) {
            Log.d("EditText Value", value);
        }
    }

    //确认添加按钮
    public void confirmAddState() {
        getAllEditTextValues();
        //检查属性值是否合法
        //去除多余空格
        String text = etStateName.getText().toString().trim();
        //标题不能为空
        if (text.isEmpty())
        {
            Toast.makeText(this, "标题不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取所有 EditText 的值
        for (int i = 0; i < statusContainer.getChildCount(); i++) {
            View childView = statusContainer.getChildAt(i);

            if (childView instanceof LinearLayout) {
                EditText editText = childView.findViewById(R.id.etSelect);
                if (editText != null) {
                    String value = editText.getText().toString().trim();
                    editTextValues.add(value);
                }
            }
        }
        String is_repeat = hasDuplicateValues();
        // 检查是否有重复值
        if (is_repeat != null) {
            Toast.makeText(this, "存在重复的值: "+is_repeat, Toast.LENGTH_SHORT).show();
            return;
        }

        //没有重复，添加数据库
        //获取数据
        //用属性查询属性值
        //与servlet交流
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:8083/Servlet_war_exploded/milk-tea-attribute_and_value"); // 注意替换为实际地址
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    // 构造 JSON 数据
                    JSONObject json = new JSONObject();
                    json.put("attribute", text);
                    //打包成JSON
                    JSONArray jsonArray = new JSONArray(editTextValues);
                    json.put("attribute_value", jsonArray);

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

                                Toast.makeText(getApplicationContext(), "添加成功!", Toast.LENGTH_LONG).show();
                                finish();

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
    //判断是否有重复值
    public String hasDuplicateValues() {
        // 创建一个 Set 用于存储唯一值
        Set<String> uniqueValues = new HashSet<>();

        // 遍历 editTextValues 列表
        for (String value : editTextValues) {
            // 如果 Set 中已经包含该值，说明是重复的
            if (!uniqueValues.add(value)) {
                // 发现重复值
                return value;
            }
        }

        // 没有重复值，返回 null
        return null;
    }

}
