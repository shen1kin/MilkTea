package com.example.smartstudent;

import android.app.AlertDialog;
import android.content.Intent;
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

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvTotalCount, tvTotalPrice, tvPayAmount;
    private TextView tvPaymentMethod, tvRemark;
    private EditText etPhone;
    private Button btnPay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // 初始化控件
        recyclerView = findViewById(R.id.recyclerCheckout);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvPayAmount = findViewById(R.id.tvPayAmount);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvRemark = findViewById(R.id.tvRemark);
        etPhone = findViewById(R.id.etPhone);
        btnPay = findViewById(R.id.btnPay);

        // 加载购物车数据
        List<CartItem> items = CartManager.getItems();
        CheckoutAdapter adapter = new CheckoutAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 显示总数和价格
        tvTotalCount.setText("共 " + CartManager.getTotalCount() + " 件商品");
        tvTotalPrice.setText("，小计 " + CartManager.getTotalPrice());
        tvPayAmount.setText("待支付 " + CartManager.getTotalPrice());

        // 支付按钮事件
        btnPay.setOnClickListener(v ->
                Toast.makeText(this, "模拟支付成功", Toast.LENGTH_SHORT).show()
        );

        // 点击支付方式弹出选择对话框
        tvPaymentMethod.setOnClickListener(v -> showCustomPaymentDialog());

        // 点击备注跳转输入页面
        tvRemark.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, RemarkActivity.class);
            startActivityForResult(intent, 101);
        });
    }

    // 自定义支付方式弹窗
    private void showCustomPaymentDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_payment_dialog, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();

        LinearLayout optionAlipay = dialogView.findViewById(R.id.optionAlipay);
        LinearLayout optionWechat = dialogView.findViewById(R.id.optionWechat);
        ImageView imgCheckAlipay = dialogView.findViewById(R.id.imgCheckAlipay);
        ImageView imgCheckWechat = dialogView.findViewById(R.id.imgCheckWechat);

        // 根据当前选择设置初始显示
        if (tvPaymentMethod.getText().toString().contains("微信")) {
            imgCheckWechat.setVisibility(View.VISIBLE);
            imgCheckAlipay.setVisibility(View.GONE);
        } else {
            imgCheckAlipay.setVisibility(View.VISIBLE);
            imgCheckWechat.setVisibility(View.GONE);
        }

        optionAlipay.setOnClickListener(v -> {
            tvPaymentMethod.setText("支付宝");
            imgCheckAlipay.setVisibility(View.VISIBLE);
            imgCheckWechat.setVisibility(View.GONE);
            dialog.dismiss();
        });

        optionWechat.setOnClickListener(v -> {
            tvPaymentMethod.setText("微信支付");
            imgCheckAlipay.setVisibility(View.GONE);
            imgCheckWechat.setVisibility(View.VISIBLE);
            dialog.dismiss();
        });

        dialog.show();
    }

    // 接收备注页面返回值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            String remark = data.getStringExtra("remark");
            tvRemark.setText(remark);
        }
    }
}
