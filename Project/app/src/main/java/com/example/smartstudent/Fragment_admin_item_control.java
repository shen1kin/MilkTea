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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fragment_admin_item_control extends Fragment {

    private RecyclerView recyclerView;
    private ItemListAdapter itemListAdapter;
    private List<ItemInfo> itemInfoList = new ArrayList<>();
    private List<ItemInfo> filteredList = new ArrayList<>();  // 用于过滤后的显示
    private ItemInfo selectedItem = null;

    private Spinner spinner;
    private EditText etSearch;
    private Button butSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_item_control, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        spinner = view.findViewById(R.id.spSearchList);
        etSearch = view.findViewById(R.id.etSearch);
        butSearch = view.findViewById(R.id.butSearch);

        // 初始化Spinner数据（名称，ID）
        List<String> searchFields = Arrays.asList("名称", "ID");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, searchFields);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // 初始化RecyclerView和适配器
        filteredList.addAll(itemInfoList);
        itemListAdapter = new ItemListAdapter(filteredList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(itemListAdapter);

        itemListAdapter.setOnItemClickListener(item -> {
            selectedItem = item;
            Toast.makeText(requireContext(), "选中商品：" + item.getItem_name(), Toast.LENGTH_SHORT).show();
        });

        // 搜索按钮点击事件
        butSearch.setOnClickListener(v -> {
            String keyword = etSearch.getText().toString().trim();
            String filterType = (String) spinner.getSelectedItem();
            filterItems(filterType, keyword);
        });

        // 请求数据
        fetchMilkTeaData();

        return view;
    }

    // 过滤函数
    private void filterItems(String filterType, String keyword) {
        filteredList.clear();

        if (keyword.isEmpty()) {
            filteredList.addAll(itemInfoList);
        } else {
            for (ItemInfo item : itemInfoList) {
                if ("名称".equals(filterType)) {
                    if (item.getItem_name().contains(keyword)) {
                        filteredList.add(item);
                    }
                } else if ("ID".equals(filterType)) {
                    if (String.valueOf(item.getItem_id()).contains(keyword)) {
                        filteredList.add(item);
                    }
                }
            }
        }
        itemListAdapter.notifyDataSetChanged();
    }

    // 请求服务器数据
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

                                itemInfoList.clear();

                                for (int i = 0; i < milkTeasArray.length(); i++) {
                                    JSONObject teaObj = milkTeasArray.getJSONObject(i);

                                    int id = teaObj.getInt("id");
                                    String name = teaObj.getString("name");
                                    String price = teaObj.getString("price");
                                    boolean isDeleted = teaObj.optBoolean("is_deleted", false);
                                    String status = isDeleted ? "下架" : "上架";

                                    ItemInfo item = new ItemInfo(id, name, status, "$" + price);
                                    item.setDeleted(isDeleted);
                                    itemInfoList.add(item);
                                }

                                filteredList.clear();
                                filteredList.addAll(itemInfoList);
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
}
