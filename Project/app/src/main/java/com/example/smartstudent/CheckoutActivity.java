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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private static final int REQUEST_REMARK = 1001;

    // 配送方式相关控件
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

        // === 1. 初始化顶部配送方式图文按钮 ===
        imgPickupBg = findViewById(R.id.imgPickupBg);
        imgDeliveryBg = findViewById(R.id.imgDeliveryBg);
        tvPickupLabel = findViewById(R.id.tvPickupLabel);
        tvDeliveryLabel = findViewById(R.id.tvDeliveryLabel);

        FrameLayout layoutPickup = findViewById(R.id.layoutPickup);
        FrameLayout layoutDelivery = findViewById(R.id.layoutDelivery);

        layoutPickup.setOnClickListener(v -> {
            switchToPickup();
            OrderModeManager.setCurrentMode(OrderModeManager.PICKUP);
        });

        layoutDelivery.setOnClickListener(v -> {
            switchToDelivery();
            OrderModeManager.setCurrentMode(OrderModeManager.DELIVERY);
        });

        // === 2. 门店信息显示 ===
        tvStoreName = findViewById(R.id.tvStoreName);
        tvPickupTime = findViewById(R.id.tvPickupTime);

        // ✅ 根据 OrderModeManager 设置初始取餐方式
        if (OrderModeManager.isPickup()) {
            switchToPickup();
        } else {
            switchToDelivery();
        }

        // === 3. 返回按钮 ===
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // === 4. 商品列表 ===
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

        // === 5. 支付方式弹窗 ===
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvPaymentMethod.setOnClickListener(v -> showPaymentBottomSheet());

        // === 6. 备注跳转 ===
        tvRemark = findViewById(R.id.tvRemark);
        tvRemark.setOnClickListener(v -> {
            Intent intent = new Intent(this, RemarkActivity.class);
            startActivityForResult(intent, REQUEST_REMARK);
        });

        // === 7. 支付按钮 ===
        btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(v -> {
            Toast.makeText(this, "模拟支付成功", Toast.LENGTH_SHORT).show();
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
}
