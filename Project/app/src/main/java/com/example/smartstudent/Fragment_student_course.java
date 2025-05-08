package com.example.smartstudent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.CategoryAdapter;
import com.example.smartstudent.adapter.ProductAdapter;
import com.example.smartstudent.model.Category;
import com.example.smartstudent.model.ProductInfo;
import com.example.smartstudent.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_student_course extends Fragment {

    private RecyclerView recyclerCategory, recyclerProducts;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    private List<Category> categoryList = new ArrayList<>();
    private List<Object> productList = new ArrayList<>(); // 包含标题（String）和商品（ProductInfo）

    private boolean isScrollByClick = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_course, container, false);

        recyclerCategory = view.findViewById(R.id.recyclerCategory);
        recyclerProducts = view.findViewById(R.id.recyclerProducts);

        initCategoryData();
        initProductData();

        setupCategoryRecycler();
        setupProductRecycler();

        return view;
    }

    private void initCategoryData() {
        categoryList.clear(); // ✅ 避免重复添加
        String[] names = {"时令上新", "超级植物茶", "鲜果茶", "原叶茗茶", "浓郁牛乳茶", "灵感茶点小料"};
        for (int i = 0; i < names.length; i++) {
            categoryList.add(new Category(names[i], i == 0));
        }
    }

    private void initProductData() {
        productList.clear();
        String[] categories = {"时令上新", "超级植物茶", "鲜果茶", "原叶茗茶", "浓郁牛乳茶", "灵感茶点小料"};
        for (String category : categories) {
            productList.add(category); // 标题
            for (int i = 1; i <= 5; i++) {
                productList.add(new ProductInfo(category + " 商品 " + i, "¥" + (10 + i), category));
            }
        }
    }

    private void setupCategoryRecycler() {
        categoryAdapter = new CategoryAdapter(categoryList, position -> {
            isScrollByClick = true;
            updateCategorySelection(position);

            // 滚动商品列表到对应分类
            String selectedCategory = categoryList.get(position).name;
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i) instanceof String) {
                    if (productList.get(i).equals(selectedCategory)) {
                        recyclerProducts.scrollToPosition(i);
                        break;
                    }
                }
            }
        });

        recyclerCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCategory.setAdapter(categoryAdapter);
    }

    private void setupProductRecycler() {
        productAdapter = new ProductAdapter(productList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerProducts.setLayoutManager(layoutManager);
        recyclerProducts.setAdapter(productAdapter);

        // 联动左侧菜单高亮
        recyclerProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (isScrollByClick) {
                    isScrollByClick = false;
                    return;
                }

                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (lm != null) {
                    int firstVisible = lm.findFirstVisibleItemPosition();
                    if (firstVisible >= 0 && productList.get(firstVisible) instanceof String) {
                        String currentCategory = (String) productList.get(firstVisible);
                        updateCategoryHighlight(currentCategory);
                    }
                }
            }
        });
    }

    private void updateCategorySelection(int selectedIndex) {
        for (int i = 0; i < categoryList.size(); i++) {
            categoryList.get(i).isSelected = (i == selectedIndex);
        }
        categoryAdapter.notifyDataSetChanged();
    }

    private void updateCategoryHighlight(String currentCategory) {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).name.equals(currentCategory)) {
                updateCategorySelection(i);
                recyclerCategory.scrollToPosition(i);
                break;
            }
        }
    }
}
