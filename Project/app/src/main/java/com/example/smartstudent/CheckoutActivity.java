package com.example.smartstudent;

import android.content.Intent;
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
import com.example.smartstudent.cart.CartManager;
import com.example.smartstudent.model.CartItem;
import com.example.smartstudent.model.Order;
import com.example.smartstudent.model.OrderModeManager;
import com.example.smartstudent.model.OrderRepository;
import com.example.smartstudent.model.ProductInfo;
import com.example.smartstudent.utils.TimeUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private boolean isDeliveryMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        List<CartItem> items = CartManager.getItems();
        CheckoutAdapter adapter = new CheckoutAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        String totalPrice = CartManager.getTotalPrice();
        tvTotalCount.setText("共 " + CartManager.getTotalCount() + " 件商品");
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
            Toast.makeText(this, "模拟支付成功", Toast.LENGTH_SHORT).show();

            // 构造订单数据（真实商品信息）
            String storeName = isDeliveryMode ? "广州软件学院宿舍 >" : "广州软件学院店 >";
            String status = isDeliveryMode ? "配送中" : "制作中";
            String time = TimeUtil.getCurrentTime();
            String price = CartManager.getTotalPrice();
            int count = CartManager.getTotalCount();
            List<ProductInfo> productInfos = CartManager.getProductInfoListWithCount();

            Order order = new Order(storeName, time, status, price, count, productInfos);
            OrderRepository.addOrder(order);

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

        tvStoreName.setText("广州软件学院店 >");
        tvPickupTime.setText("现在下单，预计 6 分钟后取茶");
    }

    private void switchToDelivery() {
        isDeliveryMode = true;
        imgPickupBg.setImageResource(R.drawable.zwxz);
        imgDeliveryBg.setImageResource(R.drawable.yxj);

        tvPickupLabel.setTextColor(Color.GRAY);
        tvDeliveryLabel.setTextColor(Color.BLACK);

        tvStoreName.setText("广州软件学院宿舍 >");
        tvPickupTime.setText("现在下单，预计 28 分钟后送达");
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
}
