package com.example.smartstudent;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.CartAdapter;
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

        List<CartItem> items = CartManager.getItems();
        CartAdapter adapter = new CartAdapter(items);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        tvTotalCount.setText("共 " + CartManager.getTotalCount() + " 件商品");
        tvTotalPrice.setText("总计：" + CartManager.getTotalPrice());
    }
}
