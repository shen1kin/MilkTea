package com.example.smartstudent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.ItemListAdapter;
import com.example.smartstudent.adapter.SpinnerAdapter;
import com.example.smartstudent.model.ItemInfo;
import com.example.smartstudent.utils.ImageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.List;

public class Fragment_admin_item_control extends Fragment {

    private RecyclerView recyclerView;
    private ItemListAdapter itemListAdapter;
    private List<ItemInfo> itemInfoList = new ArrayList<>();
    private List<ItemInfo> filteredList = new ArrayList<>();
    private ItemInfo selectedItem = null;

    private Spinner spinner;
    private EditText etSearch;
    private Button butSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_item_control, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        itemListAdapter = new ItemListAdapter(filteredList);
        recyclerView.setAdapter(itemListAdapter);

        itemListAdapter.setOnItemClickListener(item -> {
            selectedItem = item;
            Toast.makeText(requireContext(), "选中商品：" + item.getItem_name(), Toast.LENGTH_SHORT).show();
        });

        // 初始化搜索相关组件
        spinner = view.findViewById(R.id.spSearchList);
        etSearch = view.findViewById(R.id.etSearch);
        butSearch = view.findViewById(R.id.butSearch);

        List<String> searchOptions = new ArrayList<>();
        searchOptions.add("名称");
        searchOptions.add("ID");
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(requireContext(), searchOptions);
        spinner.setAdapter(spinnerAdapter);

        butSearch.setOnClickListener(v -> filterList());

        // 添加商品按钮
        Button butAddItem = view.findViewById(R.id.butAddItem);
        butAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), Activity_admin_AddItem.class);
            startActivity(intent);
        });

        // 删除/上架按钮
        Button butDelItem = view.findViewById(R.id.butDelItem);
        butDelItem.setOnClickListener(v -> {
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

        // 滑动隐藏按钮布局
        LinearLayout buttonLayout = view.findViewById(R.id.buttonLayout);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) buttonLayout.setVisibility(View.GONE);
                else if (dy < 0) buttonLayout.setVisibility(View.VISIBLE);
            }
        });

        fetchMilkTeaData();

        return view;
    }

    private void filterList() {
        String query = etSearch.getText().toString().trim();
        int selectedPos = spinner.getSelectedItemPosition();

        filteredList.clear();
        for (ItemInfo item : itemInfoList) {
            if (selectedPos == 0 && item.getItem_name().contains(query)) {
                filteredList.add(item);
            } else if (selectedPos == 1) {
                try {
                    if (Integer.parseInt(query) == item.getItem_id()) {
                        filteredList.add(item);
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        itemListAdapter.notifyDataSetChanged();
    }

    private void fetchMilkTeaData() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8083/Servlet_war_exploded/milk-tea-item-admin");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                int code = conn.getResponseCode();
                if (code == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) result.append(line);
                    reader.close();
                    conn.disconnect();

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                JSONObject responseJson = new JSONObject(result.toString());
                                JSONArray milkTeas = responseJson.getJSONArray("milkTeas");

                                itemInfoList.clear();
                                for (int i = 0; i < milkTeas.length(); i++) {
                                    JSONObject obj = milkTeas.getJSONObject(i);
                                    int id = obj.getInt("id");
                                    String name = obj.getString("name");
                                    String price = obj.getString("price");
                                    String base64 = obj.getString("image");
                                    String imageWay = ImageUtils.decodeBase64AndSaveImage(getContext(), base64);

                                    boolean isDeleted = obj.optBoolean("is_deleted", false);
                                    String status = isDeleted ? "下架" : "上架";
                                    ItemInfo item = new ItemInfo(id, name, status, "$" + price,imageWay);
                                    item.setDeleted(isDeleted);
                                    itemInfoList.add(item);
                                }
                                filterList();
                            } catch (JSONException e) {
                                Toast.makeText(requireContext(), "数据解析失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(requireContext(), "连接失败 错误码: " + code, Toast.LENGTH_SHORT).show());
                    }
                }
            } catch (Exception e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "异常: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            }
        }).start();
    }

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
                json.put("is_deleted", !currentDeleted);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = json.toString().getBytes("UTF-8");
                    os.write(input, 0, input.length);
                    os.flush();
                }

                int code = conn.getResponseCode();
                if (code == 200) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "操作成功", Toast.LENGTH_SHORT).show();
                            selectedItem = null;
                            fetchMilkTeaData();
                        });
                    }
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(requireContext(), "操作失败 错误码: " + code, Toast.LENGTH_SHORT).show());
                    }
                }
                conn.disconnect();
            } catch (Exception e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "异常: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            }
        }).start();
    }

}
