package com.example.smartstudent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.SearchAdapter;
import com.example.smartstudent.model.ProductInfo;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText editSearch;
    private RecyclerView recyclerSearch;
    private SearchAdapter adapter;
    private List<ProductInfo> allProducts = new ArrayList<>();
    private List<ProductInfo> searchResult = new ArrayList<>(); // 当前搜索结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editSearch = findViewById(R.id.editSearch);
        recyclerSearch = findViewById(R.id.recyclerSearch);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.tvDoSearch).setOnClickListener(v -> doSearch());

        recyclerSearch.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchAdapter(searchResult);
        recyclerSearch.setAdapter(adapter);

        initData();
    }

    private void initData() {
        allProducts.clear();
        // 可从数据库或静态数据加载
        allProducts.add(new ProductInfo("时令上新 商品 1", "¥11", "时令上新"));
        allProducts.add(new ProductInfo("浓郁牛乳茶 商品 2", "¥18", "浓郁牛乳茶"));
        allProducts.add(new ProductInfo("鲜果茶 商品 3", "¥15", "鲜果茶"));
        // ...添加更多测试数据
    }

    private void doSearch() {
        String keyword = editSearch.getText().toString().trim();
        if (keyword.isEmpty()) {
            Toast.makeText(this, "请输入关键词", Toast.LENGTH_SHORT).show();
            return;
        }

        List<ProductInfo> filtered = new ArrayList<>();
        for (ProductInfo product : allProducts) {
            if (product.getName().contains(keyword)) {
                filtered.add(product);
            }
        }

        searchResult.clear();
        searchResult.addAll(filtered);
        adapter.updateList(searchResult);
    }
}

