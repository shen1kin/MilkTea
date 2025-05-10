package com.example.smartstudent;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.CheckoutAdapter;
import com.example.smartstudent.cart.CartManager;
import com.example.smartstudent.model.CartItem;

import java.util.List;

public class Activity_checkout extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvTotalCount, tvTotalPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        recyclerView = findViewById(R.id.recyclerCheckout);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        // 获取购物车数据
        List<CartItem> items = CartManager.getItems();

        // 设置新版适配器
        CheckoutAdapter adapter = new CheckoutAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 设置总件数和总价
        tvTotalCount.setText("共 " + CartManager.getTotalCount() + " 件商品");
        tvTotalPrice.setText("总计：" + CartManager.getTotalPrice());
    }
}
