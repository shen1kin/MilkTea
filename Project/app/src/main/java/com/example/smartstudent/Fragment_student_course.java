package com.example.smartstudent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.ItemMikeAdapter;
import com.example.smartstudent.model.ProductInfo;

import java.util.Arrays;
import java.util.List;

public class Fragment_student_course extends Fragment {
    private RecyclerView recyclerProducts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_course, container, false);

        recyclerProducts = view.findViewById(R.id.recyclerProducts);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ProductInfo> productList = getProductList();
        ItemMikeAdapter adapter = new ItemMikeAdapter(productList);
        recyclerProducts.setAdapter(adapter);

        return view;
    }

    private List<ProductInfo> getProductList() {
        return Arrays.asList(
                new ProductInfo("商品 1", "￥10.00"),
                new ProductInfo("商品 2", "￥20.00")
        );
    }
}
