package com.example.smartstudent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.ItemListAdapter;
import com.example.smartstudent.adapter.SpinnerAdapter;
import com.example.smartstudent.model.ItemInfo;
import com.example.smartstudent.model.MilkTeaAttribute;
import com.example.smartstudent.model.ProductInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fragment_admin_item_control extends Fragment {

    private ItemListAdapter itemListAdapter;
    private List<ItemInfo> itemInfoList = new ArrayList<>();
    private ItemInfo selectedItem = null;  // 选中的商品

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_item_control, container, false);

        RecyclerView rv_item_list_view = view.findViewById(R.id.recyclerView);

        // 先创建空数据的适配器
        itemListAdapter = new ItemListAdapter(itemInfoList);

        rv_item_list_view.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv_item_list_view.setAdapter(itemListAdapter);

        // 加载后端数据
        fetchMilkTeaData();

        // 设置点击监听
        itemListAdapter.setOnItemClickListener(item -> {
            selectedItem = item;
            Toast.makeText(requireContext(), "选中商品：" + item.getItem_name(), Toast.LENGTH_SHORT).show();
        });

        // 初始化搜索Spinner
        List<String> datas = new ArrayList<>();
        datas.add("名称");
        datas.add("ID");

        Spinner spinner = view.findViewById(R.id.spSearchList);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(requireContext(), datas);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = datas.get(position);
                // 可在此处实现搜索条件的变化逻辑
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        //删除按钮
        Button deleteButton = view.findViewById(R.id.butDelItem);
        deleteButton.setOnClickListener(v -> {
            if (selectedItem == null) {
                Toast.makeText(requireContext(), "请先选择一个商品", Toast.LENGTH_SHORT).show();
                return;
            }
            String action = selectedItem.isDeleted() ? "上架" : "下架";
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("确认")
                    .setMessage("确定要" + action + "商品：" + selectedItem.getItem_name() + " 吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        updateItemFromServer(selectedItem.getItem_id(), selectedItem.isDeleted());
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout buttonLayout = view.findViewById(R.id.buttonLayout);
        RecyclerView rvItemList = view.findViewById(R.id.recyclerView);

        // 滑动时隐藏/显示按钮布局
        rvItemList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    buttonLayout.setVisibility(View.GONE);
                } else if (dy < 0) {
                    buttonLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        // 添加商品按钮点击事件
        Button butAddItem = view.findViewById(R.id.butAddItem);
        butAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), Activity_admin_AddItem.class);
            startActivity(intent);
        });
    }
    //获取数据
    private void fetchMilkTeaData() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8083/Servlet_war_exploded/milk-tea-item-admin");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoInput(true);

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

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                JSONObject responseJson = new JSONObject(result.toString());
                                JSONArray milkTeasArray = responseJson.getJSONArray("milkTeas");

                                // 清空之前数据
                                itemInfoList.clear();

                                // 解析JSON数据时
                                for (int i = 0; i < milkTeasArray.length(); i++) {
                                    JSONObject teaObj = milkTeasArray.getJSONObject(i);

                                    int id = teaObj.getInt("id");
                                    String name = teaObj.getString("name");
                                    String price = teaObj.getString("price");

                                    boolean isDeleted = teaObj.optBoolean("is_deleted", false);
                                    String status = isDeleted ? "下架" : "上架";

                                    ItemInfo item = new ItemInfo(id, name, status, "$" + price);
                                    item.setDeleted(isDeleted);  // 关键，保存当前状态
                                    itemInfoList.add(item);
                                }



                                // 通知适配器数据已变更
                                itemListAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "数据解析异常", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "连接失败，错误码：" + code, Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "异常: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }
        }).start();
    }
    //下架商品
    private void updateItemFromServer(int itemId, boolean currentDeleted) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8083/Servlet_war_exploded/milk-tea-item-admin");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("id", itemId);
                json.put("is_deleted", !currentDeleted);  // 取反切换上下架状态
                String jsonBody = json.toString();

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonBody.getBytes("UTF-8");
                    os.write(input, 0, input.length);
                    os.flush();
                }

                int code = conn.getResponseCode();
                if (code == 200) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "操作成功", Toast.LENGTH_SHORT).show();
                            selectedItem = null;
                            fetchMilkTeaData(); // 刷新列表
                        });
                    }
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "操作失败，错误码：" + code, Toast.LENGTH_SHORT).show();
                        });
                    }
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "异常: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }
        }).start();
    }



    // 图片保存方法，保留你原先代码
    public String saveImage(Bitmap bitmap) {
        if (getContext() == null) return null;

        File cacheDir = getContext().getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = getContext().getCacheDir();
        }

        for (File file : cacheDir.listFiles()) {
            file.delete();
        }

        String filename = "IMG_" + System.currentTimeMillis() + ".png";
        File newFile = new File(cacheDir, filename);

        try (FileOutputStream out = new FileOutputStream(newFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return newFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
