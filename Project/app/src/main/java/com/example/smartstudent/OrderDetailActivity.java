package com.example.smartstudent;

import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudent.model.Order;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvStore, tvTime, tvStatus, tvTotal, tvCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        tvStore = findViewById(R.id.tvStoreDetail);
        tvTime = findViewById(R.id.tvTimeDetail);
        tvStatus = findViewById(R.id.tvStatusDetail);
        tvTotal = findViewById(R.id.tvTotalDetail);
        tvCount = findViewById(R.id.tvCountDetail);

        Order order = (Order) getIntent().getSerializableExtra("order");
        if (order != null) {
            tvStore.setText(order.storeName);
            tvTime.setText(order.orderTime);
            tvStatus.setText(order.status);
            tvTotal.setText(order.totalPrice);
            tvCount.setText(String.valueOf(order.totalCount));
        }
    }
}
