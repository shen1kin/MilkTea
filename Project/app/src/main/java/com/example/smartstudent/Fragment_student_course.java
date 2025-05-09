package com.example.smartstudent;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.CartAdapter;
import com.example.smartstudent.adapter.CategoryAdapter;
import com.example.smartstudent.adapter.ProductAdapter;
import com.example.smartstudent.cart.CartManager;
import com.example.smartstudent.model.CartItem;
import com.example.smartstudent.model.Category;
import com.example.smartstudent.model.ProductInfo;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class Fragment_student_course extends Fragment {

    private RecyclerView recyclerCategory, recyclerProducts;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    private Button btnStorePickup, btnDelivery;
    private TextView etSearch;

    private List<Category> categoryList = new ArrayList<>();
    private List<Object> productList = new ArrayList<>(); // 包含标题（String）和商品（ProductInfo）

    private boolean isScrollByClick = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_course, container, false);

        ImageView cartIcon = view.findViewById(R.id.imgCartIcon);
        cartIcon.setOnClickListener(v -> showCartDialog());

        // 搜索按钮点击跳转
        etSearch = view.findViewById(R.id.etSearch);
        etSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });

        TextView cartTotal = view.findViewById(R.id.tvCartTotalPrice);

        cartIcon.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(getContext());
            View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cart, null);
            RecyclerView recycler = sheetView.findViewById(R.id.recyclerCart);
            recycler.setLayoutManager(new LinearLayoutManager(getContext()));
            recycler.setAdapter(new CartAdapter(CartManager.getItems()));
            dialog.setContentView(sheetView);
            dialog.show();
        });

        Button btnCheckout = view.findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), Activity_checkout.class));
        });

        // 自取/喜外送按钮绑定
        btnStorePickup = view.findViewById(R.id.btnStorePickup);
        btnDelivery = view.findViewById(R.id.btnDelivery);

        // 设置默认状态（自取被选中）
        setButtonState(true);

        btnStorePickup.setOnClickListener(v -> {
            setButtonState(true);
            // TODO: 加载“自取”相关数据
        });

        btnDelivery.setOnClickListener(v -> {
            setButtonState(false);
            // TODO: 加载“喜外送”相关数据
        });

        // 绑定 RecyclerView
        recyclerCategory = view.findViewById(R.id.recyclerCategory);
        recyclerProducts = view.findViewById(R.id.recyclerProducts);

        initCategoryData();
        initProductData();
        setupCategoryRecycler();
        setupProductRecycler();

        return view;
    }

    private void setButtonState(boolean isPickupSelected) {
        if (isPickupSelected) {
            btnStorePickup.setTextSize(23);
            btnStorePickup.setTypeface(Typeface.DEFAULT_BOLD);
            btnDelivery.setTextSize(16);
            btnDelivery.setTypeface(Typeface.DEFAULT);
        } else {
            btnDelivery.setTextSize(23);
            btnDelivery.setTypeface(Typeface.DEFAULT_BOLD);
            btnStorePickup.setTextSize(16);
            btnStorePickup.setTypeface(Typeface.DEFAULT);
        }
    }

    private void initCategoryData() {
        categoryList.clear();
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

    private void showCartDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View sheetView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_cart, null);

        RecyclerView cartRecycler = sheetView.findViewById(R.id.recyclerCart);
        TextView tvTotal = sheetView.findViewById(R.id.tvCartTotal);
        Button btnCheckout = sheetView.findViewById(R.id.btnCheckoutCart);

        List<CartItem> items = CartManager.getItems();
        CartAdapter adapter = new CartAdapter(items);
        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecycler.setAdapter(adapter);

        tvTotal.setText("合计：" + CartManager.getTotalPrice());
        btnCheckout.setOnClickListener(v -> {
            // 跳转结算页面
            dialog.dismiss();
            startActivity(new Intent(getActivity(), Activity_checkout.class));
        });

        dialog.setContentView(sheetView);
        dialog.show();
    }

    public void updateCartBadge() {
        int count = CartManager.getTotalCount();
        TextView badge = getView().findViewById(R.id.tvCartBadge); // 你需要在布局中定义这个角标 TextView
        badge.setText(String.valueOf(count));
        badge.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
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
