package com.example.smartstudent;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvTotalCount, tvTotalPrice, tvPayAmount;
    private TextView tvPaymentMethod, tvRemark;
    private Button btnPay;

    private static final int REQUEST_REMARK = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // 绑定控件
        recyclerView = findViewById(R.id.recyclerCheckout);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvPayAmount = findViewById(R.id.tvPayAmount);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvRemark = findViewById(R.id.tvRemark);
        btnPay = findViewById(R.id.btnPay);

        // 设置 RecyclerView
        List<CartItem> items = CartManager.getItems();
        CheckoutAdapter adapter = new CheckoutAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 设置价格信息
        String totalPrice = CartManager.getTotalPrice();
        tvTotalCount.setText("共 " + CartManager.getTotalCount() + " 件商品");
        tvTotalPrice.setText("，小计 " + totalPrice);
        tvPayAmount.setText("待支付 " + totalPrice);

        // 支付按钮点击
        btnPay.setOnClickListener(v ->
                Toast.makeText(this, "模拟支付成功", Toast.LENGTH_SHORT).show()
        );

        // 点击支付方式弹窗
        tvPaymentMethod.setOnClickListener(v -> {
            showPaymentDialog();
        });

        // 点击备注跳转到 RemarkActivity
        tvRemark.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, RemarkActivity.class);
            startActivityForResult(intent, REQUEST_REMARK);
        });
    }

    // 接收备注返回数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REMARK && resultCode == RESULT_OK && data != null) {
            String remark = data.getStringExtra("remark");
            if (remark != null && !remark.isEmpty()) {
                tvRemark.setText(remark);
                tvRemark.setTextColor(getResources().getColor(android.R.color.black));
            } else {
                tvRemark.setText("请在这里写下您的备注");
                tvRemark.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
        }
    }

    // 支付方式选择弹窗
    private void showPaymentDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_payment_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        LinearLayout optionAlipay = dialogView.findViewById(R.id.optionAlipay);
        LinearLayout optionWechat = dialogView.findViewById(R.id.optionWechat);
        ImageView imgCheckAlipay = dialogView.findViewById(R.id.imgCheckAlipay);
        ImageView imgCheckWechat = dialogView.findViewById(R.id.imgCheckWechat);

        // 初始化当前选中状态
        if (tvPaymentMethod.getText().toString().contains("微信")) {
            imgCheckWechat.setVisibility(View.VISIBLE);
            imgCheckAlipay.setVisibility(View.GONE);
        } else {
            imgCheckAlipay.setVisibility(View.VISIBLE);
            imgCheckWechat.setVisibility(View.GONE);
        }

        // 选择支付宝
        optionAlipay.setOnClickListener(v -> {
            tvPaymentMethod.setText("支付宝");
            dialog.dismiss();
        });

        // 选择微信
        optionWechat.setOnClickListener(v -> {
            tvPaymentMethod.setText("微信支付");
            dialog.dismiss();
        });

        dialog.show();
    }
}
