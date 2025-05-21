package com.example.smartstudent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private List<ProductInfo> productList;  // 传入的完整列表
    private List<ProductInfo> searchResult = new ArrayList<>(); // 搜索结果

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

        Intent intent = getIntent();
        productList = (List<ProductInfo>) intent.getSerializableExtra("productList");

        if (productList == null || productList.isEmpty()) {
            Toast.makeText(this, "未接收到产品数据", Toast.LENGTH_SHORT).show();
            // 禁用搜索按钮或搜索框（选做）
            findViewById(R.id.tvDoSearch).setEnabled(false);
            editSearch.setEnabled(false);
        }

        adapter.setOnAddToCartClickListener(product -> {
            Log.d("回传调试", "选中商品ID：" + product.getId());
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedProductId", product.getId());
            setResult(RESULT_OK, resultIntent);
            finish();
        });


    }

    private void doSearch() {
        if (productList == null || productList.isEmpty()) {
            Toast.makeText(this, "没有产品数据可搜索", Toast.LENGTH_SHORT).show();
            return;
        }

        String keyword = editSearch.getText().toString().trim();
        if (keyword.isEmpty()) {
            Toast.makeText(this, "请输入关键词", Toast.LENGTH_SHORT).show();
            return;
        }

        List<ProductInfo> filtered = new ArrayList<>();
        for (ProductInfo product : productList) {
            if (product.getName() != null && product.getName().contains(keyword)) {
                filtered.add(product);
            }
        }

        // 打印搜索结果名称
        for (ProductInfo p : filtered) {
            Log.d("SearchActivity", "匹配产品：" + p.getName());
        }

        Toast.makeText(this, "找到 " + filtered.size() + " 条结果", Toast.LENGTH_SHORT).show();

        searchResult.clear();
        searchResult.addAll(filtered);
        adapter.updateList(searchResult);

        if (filtered.isEmpty()) {
            Toast.makeText(this, "未找到匹配的产品", Toast.LENGTH_SHORT).show();
        }
    }

}


