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
    private TextView etSearch, cartBadge, cartTotal;

    private List<Category> categoryList = new ArrayList<>();
    private List<Object> productList = new ArrayList<>();

    private boolean isScrollByClick = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_course, container, false);

        ImageView cartIcon = view.findViewById(R.id.imgCartIcon);
        cartBadge = view.findViewById(R.id.tvCartBadge);
        cartTotal = view.findViewById(R.id.tvCartTotalPrice); // 总价 TextView 初始化
        etSearch = view.findViewById(R.id.etSearch);
        btnStorePickup = view.findViewById(R.id.btnStorePickup);
        btnDelivery = view.findViewById(R.id.btnDelivery);
        Button btnCheckout = view.findViewById(R.id.btnCheckout);

        recyclerCategory = view.findViewById(R.id.recyclerCategory);
        recyclerProducts = view.findViewById(R.id.recyclerProducts);

        cartIcon.setOnClickListener(v -> showCartDialog());

        etSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });

        btnCheckout.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), CheckoutActivity.class))
        );

        setButtonState(true);
        btnStorePickup.setOnClickListener(v -> setButtonState(true));
        btnDelivery.setOnClickListener(v -> setButtonState(false));

        initCategoryData();
        initProductData();
        setupCategoryRecycler();
        setupProductRecycler();

        updateCartBadge(); // 页面初始化时更新角标和价格

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
            productList.add(category);
            for (int i = 1; i <= 5; i++) {
                productList.add(new ProductInfo(category + " 商品 " + i, "¥" + (10 + i), category));
            }
        }
    }

    private void setupCategoryRecycler() {
        categoryAdapter = new CategoryAdapter(categoryList, position -> {
            isScrollByClick = true;
            updateCategorySelection(position);
            String selectedCategory = categoryList.get(position).name;
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i) instanceof String &&
                        productList.get(i).equals(selectedCategory)) {
                    recyclerProducts.scrollToPosition(i);
                    break;
                }
            }
        });
        recyclerCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCategory.setAdapter(categoryAdapter);
    }

    private void setupProductRecycler() {
        productAdapter = new ProductAdapter(productList);
        productAdapter.setOnAddToCartListener(() -> updateCartBadge());

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

        adapter.setOnCartChangeListener(() -> {
            updateCartBadge(); // 更新角标和主界面价格
            tvTotal.setText("合计：" + CartManager.getTotalPrice()); // 更新弹窗总价
        });

        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecycler.setAdapter(adapter);

        tvTotal.setText("合计：" + CartManager.getTotalPrice());

        btnCheckout.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(getActivity(), CheckoutActivity.class));
        });

        dialog.setContentView(sheetView);
        dialog.show();
    }

    public void updateCartBadge() {
        if (cartBadge == null || cartTotal == null) return;

        int count = CartManager.getTotalCount();
        cartBadge.setText(String.valueOf(count));
        cartBadge.setVisibility(count > 0 ? View.VISIBLE : View.GONE);

        cartTotal.setText(CartManager.getTotalPrice());
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
