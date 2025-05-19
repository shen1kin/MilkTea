package com.example.smartstudent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.CheckoutAdapter;
import com.example.smartstudent.cart.CartOrderManager;
import com.example.smartstudent.model.CartItem;
import com.example.smartstudent.model.Order;
import com.example.smartstudent.model.OrderAttribute;
import com.example.smartstudent.model.OrderItem;
import com.example.smartstudent.model.OrderModeManager;
import com.example.smartstudent.model.OrderRepository;
import com.example.smartstudent.model.User;
import com.example.smartstudent.utils.TimeUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CheckoutActivity extends AppCompatActivity {

    private static final int REQUEST_REMARK = 1001;

    private ImageView imgPickupBg, imgDeliveryBg;
    private TextView tvPickupLabel, tvDeliveryLabel;
    private TextView tvStoreName, tvPickupTime;
    private TextView tvTotalCount, tvTotalPrice, tvPayAmount;
    private TextView tvPaymentMethod, tvRemark;
    private RecyclerView recyclerView;
    private Button btnPay;
    private ImageView btnBack;
    //商品配送方式
    private String pickup_method;
    //配送地点
    private String address;
    //支付方式
    private String pay_method;
    private String self_pickup;
    private String status = "制作中";;

    // 店铺名称（如：校园茶饮店）
    private String storeNameSelfPickup = "广州软件学院 · 校园茶饮店（自提）";
    private String storeNameDelivery = "广州软件学院 · 学生宿舍外送";

    // 地址信息（用于配送）
    private String deliveryAddress = "广州软件学院宿舍 3 栋";
    private String pickupAddress = "软件学院 A 栋自提柜";

    // 配送时间提示
    private String pickupTimeTip = "现在下单，预计 6 分钟后取茶";
    private String deliveryTimeTip = "现在下单，预计 28 分钟后送达";






    private boolean isDeliveryMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //接受传递的数据，从course，订单信息
        Intent get_intent = getIntent();
        List<CartItem> cartItems = (List<CartItem>) get_intent.getSerializableExtra("cart_items");

        setContentView(R.layout.activity_checkout);

        imgPickupBg = findViewById(R.id.imgPickupBg);
        imgDeliveryBg = findViewById(R.id.imgDeliveryBg);
        tvPickupLabel = findViewById(R.id.tvPickupLabel);
        tvDeliveryLabel = findViewById(R.id.tvDeliveryLabel);

        FrameLayout layoutPickup = findViewById(R.id.layoutPickup);
        FrameLayout layoutDelivery = findViewById(R.id.layoutDelivery);

        layoutPickup.setOnClickListener(v -> {
            OrderModeManager.setMode(OrderModeManager.PICKUP);
            switchToPickup();
        });
        layoutDelivery.setOnClickListener(v -> {
            OrderModeManager.setMode(OrderModeManager.DELIVERY);
            switchToDelivery();
        });

        tvStoreName = findViewById(R.id.tvStoreName);
        tvPickupTime = findViewById(R.id.tvPickupTime);
        if (OrderModeManager.isPickup()) {
            switchToPickup();
        } else {
            switchToDelivery();
        }

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerCheckout);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvPayAmount = findViewById(R.id.tvPayAmount);
        //从获取的信息，显示到列表
        List<CartItem> items = cartItems;
        CheckoutAdapter adapter = new CheckoutAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        String totalPrice = CartOrderManager.getTotalPrice();
        tvTotalCount.setText("共 " + CartOrderManager.getTotalCount() + " 件商品");
        tvTotalPrice.setText("，小计 " + totalPrice);
        tvPayAmount.setText("待支付 " + totalPrice);

        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvPaymentMethod.setOnClickListener(v -> showPaymentBottomSheet());

        tvRemark = findViewById(R.id.tvRemark);
        tvRemark.setOnClickListener(v -> {
            Intent intent = new Intent(this, RemarkActivity.class);
            startActivityForResult(intent, REQUEST_REMARK);
        });

        btnPay = findViewById(R.id.btnPay);
        // ✅ CheckoutActivity.java 中修改构造订单部分
        btnPay.setOnClickListener(v -> {
            Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();


            // 构造订单数据（真实商品信息）
//            String storeName = isDeliveryMode ? self_pickup + " >" : delivery_service + " >";
//            String status = isDeliveryMode ? "配送中" : "制作中";
            String storeName = isDeliveryMode ? storeNameDelivery : storeNameSelfPickup;
            address = isDeliveryMode ? deliveryAddress : pickupAddress;


            String time = TimeUtil.getCurrentTime();
            String price = CartOrderManager.getTotalPrice();
            int count = CartOrderManager.getTotalCount();
            Map<OrderItem, Integer> orderItemInfos = CartOrderManager.getCartMap();
            String orderNum = UUID.randomUUID().toString();
            // 配送方式，自提或配送
            String  pickup_method = isDeliveryMode ? "配送" : "自提";

            // 支付方式，取当前显示文字
            String  pay_method = tvPaymentMethod.getText().toString().replace(" >", "");

            // 取货或送货地址
            String  address = isDeliveryMode ? deliveryAddress : pickupAddress;
            // 备注信息
            String remarkText = tvRemark.getText().toString();
            if (remarkText.equals("请在这里写下您的备注 >")) {
                remarkText = "";
            }
            String  remark = remarkText;



            // 读取数据
            // 读取 JSON 字符串
            SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
            String userJson = sharedPreferences.getString("user", null);

            // 反序列化为 User 对象
            User user = null;
            if (userJson != null) {
                Gson gson = new Gson();
                //获取到user
                user = gson.fromJson(userJson, User.class);
            }

            Order order = new Order(storeName, time, status, price, count, orderItemInfos,user.getId(),orderNum,pickup_method,pay_method,address,remark);

            //与servlet交流
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://10.0.2.2:8083/Servlet_war_exploded/order"); // 注意替换为实际地址
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        // 构造 JSON 数据
                        JSONObject json = buildOrderJson(order);


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

            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);
            finish();
        });


    }

    private void switchToPickup() {
        isDeliveryMode = false;
        imgPickupBg.setImageResource(R.drawable.zxj);
        imgDeliveryBg.setImageResource(R.drawable.ywxz);

        tvPickupLabel.setTextColor(Color.BLACK);
        tvDeliveryLabel.setTextColor(Color.GRAY);

//        tvStoreName.setText("广州软件学院店 >");
//        tvPickupTime.setText("现在下单，预计 6 分钟后取茶");

        tvStoreName.setText(storeNameSelfPickup);
        tvPickupTime.setText(pickupTimeTip);

    }

    private void switchToDelivery() {
        isDeliveryMode = true;
        imgPickupBg.setImageResource(R.drawable.zwxz);
        imgDeliveryBg.setImageResource(R.drawable.yxj);

        tvPickupLabel.setTextColor(Color.GRAY);
        tvDeliveryLabel.setTextColor(Color.BLACK);

//        tvStoreName.setText("广州软件学院宿舍 >");
//        tvPickupTime.setText("现在下单，预计 28 分钟后送达");

        tvStoreName.setText(storeNameDelivery);
        tvPickupTime.setText(deliveryTimeTip);
    }

    private void showPaymentBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_payment_dialog, null);

        LinearLayout optionAlipay = dialogView.findViewById(R.id.optionAlipay);
        LinearLayout optionWechat = dialogView.findViewById(R.id.optionWechat);
        ImageView imgCheckAlipay = dialogView.findViewById(R.id.imgCheckAlipay);
        ImageView imgCheckWechat = dialogView.findViewById(R.id.imgCheckWechat);
        ImageView btnClose = dialogView.findViewById(R.id.btnClose);

        boolean isWechat = tvPaymentMethod.getText().toString().contains("微信");
        imgCheckWechat.setImageResource(isWechat ? R.drawable.xuanzhong : R.drawable.ic_radio_unchecked);
        imgCheckAlipay.setImageResource(!isWechat ? R.drawable.xuanzhong : R.drawable.ic_radio_unchecked);

        optionAlipay.setOnClickListener(v -> {
            tvPaymentMethod.setText("支付宝 >");
            bottomSheetDialog.dismiss();
        });

        optionWechat.setOnClickListener(v -> {
            tvPaymentMethod.setText("微信支付 >");
            bottomSheetDialog.dismiss();
        });

        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
    }

    private String getCurrentTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REMARK && resultCode == RESULT_OK && data != null) {
            String remark = data.getStringExtra("remark");
            if (remark != null && !remark.isEmpty()) {
                tvRemark.setText(remark);
                tvRemark.setTextColor(getResources().getColor(android.R.color.black));
            } else {
                tvRemark.setText("请在这里写下您的备注 >");
                tvRemark.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
        }
    }

    //将对象转化为JSON传给后端
    public JSONObject buildOrderJson(Order order) {
        try {
            JSONObject json = new JSONObject();

            // 基本订单信息
            json.put("userid", order.getUserid());
            json.put("store_name", order.getStoreName()); // 这里用店铺名做订单名称，视需求调整
            json.put("total_count", order.getTotalCount());
            json.put("total_price", order.getTotalPrice());
            json.put("store_name", order.getStoreName());
            json.put("order_time", order.getOrderTime());

            // 预计完成时间，比如当前时间加 多少  分钟
//            String orderTimeEnd = getEstimatedOrderEndTime(5);
//            json.put("order_time_end", orderTimeEnd);

            // 配送方式，自提或配送
            json.put("pickup_method", order.getPickupMethod());


            // 支付方式，取当前显示文字
            json.put("pay_method", order.getPayMethod());

            json.put("status", order.getStatus());

            // 取货或送货地址
            json.put("address", order.getAddress());

            // 订单号，用UUID或者时间戳
            json.put("order_num", order.getOrderNum());

            // 备注信息
            json.put("remark", order.getRemark());

            // 商品信息
            JSONArray itemArray = new JSONArray();
            for (Map.Entry<OrderItem, Integer> entry : order.getOrderItemInfos().entrySet()) {
                OrderItem item = entry.getKey();
                int quantity = entry.getValue();

                JSONObject itemJson = new JSONObject();
                itemJson.put("name", item.getName());
                itemJson.put("price", item.getPrice());
                itemJson.put("count", quantity);
                itemJson.put("milk_tea_id", item.getMilk_tae_id());

                // 规格属性列表
                JSONArray attrsArray = new JSONArray();
                List<OrderAttribute> attributes = item.getAttributes();
                if (attributes != null) {
                    for (OrderAttribute attr : attributes) {
                        JSONObject attrJson = new JSONObject();
                        attrJson.put("attribute", attr.getAttribute());
                        attrJson.put("attribute_value", attr.getAttribute_value());
                        attrsArray.put(attrJson);
                    }
                }
                itemJson.put("attributes", attrsArray);

                itemArray.put(itemJson);
            }
            json.put("orderItemInfos", itemArray);

            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 计算预计订单结束时间，例如当前时间加minutes分钟，格式同order_time
    private String getEstimatedOrderEndTime(int minutes) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        long currentMillis = System.currentTimeMillis();
        long endMillis = currentMillis + minutes * 60 * 1000;
        return sdf.format(new Date(endMillis));
    }
}
