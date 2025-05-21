package com.example.smartstudent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.OrderAdapter;
import com.example.smartstudent.model.MilkTeaAttribute;
import com.example.smartstudent.model.Order;
import com.example.smartstudent.model.OrderAttribute;
import com.example.smartstudent.model.OrderItem;
import com.example.smartstudent.model.OrderRepository;
import com.example.smartstudent.model.ProductInfo;
import com.example.smartstudent.model.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Fragment_student_score extends Fragment {

    private RecyclerView recyclerView;
    private List<Order> orderList = new ArrayList<>();

    private OrderAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student_score, container, false);
        recyclerView = view.findViewById(R.id.recyclerOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 获取订单列表数据
        // 设置适配器并传入点击事件
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 这里执行网络请求，获取数据
        // 启动子线程进行网络请求
        new Thread(() -> {
            try {
                // 读取数据
                // 读取 JSON 字符串
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                String userJson = sharedPreferences.getString("user", null);

                // 反序列化为 User 对象
                User user = null;
                if (userJson != null) {
                    Gson gson = new Gson();
                    user = gson.fromJson(userJson, User.class);
                }

                String userId = String.valueOf(user.getId()); // 真实代码中，这个userid应该是动态获得的

                // 编码userid，防止特殊字符导致URL错误
                String urlStr = "http://10.0.2.2:8083/Servlet_war_exploded/order?userid=" + URLEncoder.encode(userId, "UTF-8");

                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoInput(true);


                int code = conn.getResponseCode();
                if (code == 200) {
                    // 读取响应
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
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            String responseStr = result.toString();
                            Log.d("responseStr", responseStr);

                            //清空订单，重新获取
                            orderList.clear();
                            try {
                                JSONObject jsonObject = new JSONObject(responseStr);
                                JSONArray ordersArray = jsonObject.getJSONArray("orders");

                                for (int i = 0; i < ordersArray.length(); i++) {
                                    JSONObject orderObj = ordersArray.getJSONObject(i);
                                    int order_id = orderObj.getInt("order_id");
                                    int userid = orderObj.getInt("userid");
                                    String store_name = orderObj.getString("store_name");
                                    int total_count = orderObj.getInt("total_count");
                                    String total_price = orderObj.getString("total_price");
                                    String order_time = orderObj.getString("order_time");
                                    String pickup_method = orderObj.getString("pickup_method");
                                    String pay_method = orderObj.getString("pay_method");
                                    String status = orderObj.getString("status");
                                    String address = orderObj.getString("address");
                                    String order_num = orderObj.getString("order_num");
                                    String remark = orderObj.getString("remark");
                                    String order_time_end = orderObj.getString("order_time_end");
                                    JSONArray itemArray = orderObj.getJSONArray("orderItemInfos");
                                    List<OrderItem> itemList = new ArrayList<>();

                                    for (int j = 0; j < itemArray.length(); j++) {
                                        JSONObject itemObj = itemArray.getJSONObject(j);

                                        int milk_tea_id = itemObj.getInt("milk_tea_id");
                                        String name = itemObj.getString("name");
                                        String price = itemObj.getString("price");
                                        int count = itemObj.getInt("count");
                                        String imageWay = "";
                                        // 图片处理 Base64 解码部分
                                        String base64 = itemObj.getString("image64");
                                        if (base64 != null && base64.contains(",")) {
                                            base64 = base64.split(",")[1]; // 移除 Data URL 前缀
                                        }

                                        Bitmap bitmap = null;
                                        try {
                                            byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
                                            bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                                        } catch (Exception e) {
                                            Log.e("ImageDecode", "Base64 decode error", e);
                                        }

                                        // 必须检查是否为 null 再保存
                                        if (bitmap != null) {
                                            String imagePath = saveImage(bitmap);
                                            if (imagePath != null) {
                                                imageWay = imagePath;
                                            }
                                            bitmap.recycle(); // 及时释放内存
                                        } else {
                                            Log.e("ImageSave", "Failed to decode Base64 image");
                                        }



                                        JSONArray attrArray = itemObj.getJSONArray("attributes");
                                        List<OrderAttribute> attrList = new ArrayList<>();

                                        for (int k = 0; k < attrArray.length(); k++) {
                                            JSONObject attrObj = attrArray.getJSONObject(k);
                                            String attribute = attrObj.getString("attribute");
                                            String attribute_value = attrObj.getString("attribute_value");

                                            attrList.add(new OrderAttribute(attribute, attribute_value));
                                        }

                                        itemList.add(new OrderItem(milk_tea_id, name, price, count, imageWay, attrList));
                                    }

                                    Order order = new Order(order_id,userid, store_name, total_count, total_price, order_time, pickup_method, pay_method, status, address, order_num, remark, order_time_end, itemList);

                                    orderList.add(order);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // 将数据刷新出来
                            orderAdapter = new OrderAdapter(orderList, order -> {
                                // 点击跳转到订单详情页
                                Intent intent = new Intent(getContext(), OrderDetailActivity.class);
                                intent.putExtra("order", order);
                                startActivity(intent);
                            });

                            recyclerView.setAdapter(orderAdapter);

                        });
                    }
                } else {
                    // 请求失败时的 UI 提示
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

//    @Override
//    public void onResume() {
//        super.onResume();
//        // 每次回到页面时刷新订单数据，确保刚刚支付生成的订单能显示
//        if (orderAdapter != null) {
//            orderAdapter.setOrderList(OrderRepository.getOrders());
//            orderAdapter.notifyDataSetChanged();
//        }
//    }

    // 计算预计订单结束时间，例如当前时间加minutes分钟，格式同order_time
    //将图片缓存到本地，返回值为文件的绝对路径
    public String saveImage(Bitmap bitmap) {
        // 1. 检查Context可用性
        if (getContext() == null) return null;

        // 2. 获取应用专属缓存目录（无需权限）
        File cacheDir = getContext().getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = getContext().getCacheDir();
        }

        // 3. 清理旧文件（可选）
        for (File file : cacheDir.listFiles()) {
            file.delete();
        }

        // 4. 保存新文件
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
