package com.example.smartstudent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.smartstudent.model.MilkTeaAttribute;
import com.example.smartstudent.model.ProductInfo;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Fragment_student_course extends Fragment {

    private LinearLayout layoutContent,layoutProgressBar;
    private ProgressBar loadingSpinner;

    private RecyclerView recyclerCategory, recyclerProducts;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    private Button btnStorePickup, btnDelivery;
    private TextView etSearch, cartBadge, cartTotal;

    private List<Category> categoryList = new ArrayList<>();
    private List<Object> productList = new ArrayList<>();

    private boolean isScrollByClick = false;
    private int currentOrderMode = OrderModeManager.getCurrentMode();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_course, container, false);

        ImageView cartIcon = view.findViewById(R.id.imgCartIcon);
        cartBadge = view.findViewById(R.id.tvCartBadge);
        cartTotal = view.findViewById(R.id.tvCartTotalPrice);
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

        btnCheckout.setOnClickListener(v -> {
            OrderModeManager.setCurrentMode(currentOrderMode);
            startActivity(new Intent(getActivity(), CheckoutActivity.class));
        });

        btnStorePickup.setOnClickListener(v -> setButtonState(true));
        btnDelivery.setOnClickListener(v -> setButtonState(false));


        //获取全部信息
        getMilkTeaInfo();

        //未获取到信息画面转圈圈
        layoutProgressBar = view.findViewById(R.id.layoutProgressBar);
        loadingSpinner = view.findViewById(R.id.loading_spinner);
        layoutContent = view.findViewById(R.id.layoutContent);

        layoutProgressBar.setVisibility(View.VISIBLE);
        loadingSpinner.setVisibility(View.VISIBLE);
        layoutContent.setVisibility(View.GONE);


        // 初始状态
        setButtonState(OrderModeManager.isPickup());



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次页面切换回来都刷新状态
        setButtonState(OrderModeManager.isPickup());
    }

    private void setButtonState(boolean isPickupSelected) {
        currentOrderMode = isPickupSelected ? OrderModeManager.PICKUP : OrderModeManager.DELIVERY;

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
        productAdapter.setOnAddToCartListener(this::updateCartBadge);

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
            updateCartBadge();
            tvTotal.setText("合计：" + CartManager.getTotalPrice());
        });

        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecycler.setAdapter(adapter);

        tvTotal.setText("合计：" + CartManager.getTotalPrice());

        btnCheckout.setOnClickListener(v -> {
            dialog.dismiss();
            OrderModeManager.setCurrentMode(currentOrderMode);
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

    private void getMilkTeaInfo() {
        // 启动子线程进行网络请求
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8083/Servlet_war_exploded/milk-tea-item");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoInput(true);

                int code = conn.getResponseCode();
                if (code == 200) {
                    // 读取响应
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    final StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                    is.close();
                    conn.disconnect();

                    // 更新 UI
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            String responseStr = result.toString();
                            Log.d("responseStr", responseStr);

                            try {
                                JSONObject responseJson = new JSONObject(responseStr);
                                JSONArray milkTeasArray = responseJson.getJSONArray("milkTeas");

                                // 分类结构：Map<class名, 对应的产品列表>
                                Map<String, List<ProductInfo>> categoryToProductsMap = new LinkedHashMap<>();

                                // 遍历所有奶茶项
                                for (int i = 0; i < milkTeasArray.length(); i++) {
                                    JSONObject teaObj = milkTeasArray.getJSONObject(i);

                                    ProductInfo product = new ProductInfo();
                                    product.id = teaObj.getInt("id");
                                    product.setName(teaObj.getString("name"));
                                    product.setPrice(teaObj.getString("price"));
                                    product.description = teaObj.getString("description");
                                    product.clazz = teaObj.getString("class");

                                    // Base64 去掉前缀（如果有）
                                    String base64 = teaObj.getString("image");
                                    if (base64.contains(",")) {
                                        base64 = base64.split(",")[1];
                                    }

                                    // 转成 Bitmap
                                    byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                                    // 设置到 ProductInfo 对象中
                                    product.image = bitmap; // 添加 Bitmap 字段

                                    // 解析属性列表
                                    JSONArray attrArray = teaObj.getJSONArray("attributes");
                                    product.attributes = new ArrayList<>();
                                    for (int j = 0; j < attrArray.length(); j++) {
                                        JSONObject attrObj = attrArray.getJSONObject(j);
                                        MilkTeaAttribute attr = new MilkTeaAttribute();
                                        attr.attribute = attrObj.getString("attribute");

                                        JSONArray valueArray = attrObj.getJSONArray("attribute_value");
                                        attr.attribute_value = new ArrayList<>();
                                        for (int k = 0; k < valueArray.length(); k++) {
                                            attr.attribute_value.add(valueArray.getString(k));
                                        }

                                        product.attributes.add(attr);
                                    }

                                    // 根据 class 分类存入 Map
                                    String clazz = product.clazz;
                                    categoryToProductsMap
                                            .computeIfAbsent(clazz, key -> new ArrayList<>())
                                            .add(product);
                                }


                                List<String> categoryList = new ArrayList<>(categoryToProductsMap.keySet());
                                List<List<ProductInfo>> productListByCategory = new ArrayList<>();
                                for (String category : categoryList) {
                                    productListByCategory.add(categoryToProductsMap.get(category));
                                }

                                // 你可以使用 categoryToProductsMap、categoryList、productListByCategory 来填充 UI

                                // 使用 categoryList 和 productListByCategory 更新界面
                                initAllDate(categoryToProductsMap);
                                setupCategoryRecycler();
                                setupProductRecycler();
                                updateCartBadge();

                                //获取到信息 画面切换回来
                                layoutProgressBar.setVisibility(View.GONE);
                                loadingSpinner.setVisibility(View.GONE);
                                layoutContent.setVisibility(View.VISIBLE);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "数据解析异常", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    // 请求失败时的 UI 提示
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "连接失败，错误码：" + code, Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "异常: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }
        }).start();
    }

    private void initAllDate(Map<String, List<ProductInfo>> categoryToProductsMap) {
        categoryList.clear();
        productList.clear();

        //{
        //  "经典奶茶": [
        //    ProductInfo{id=1, name="原味奶茶", price="¥12", description="香浓经典", ...},
        //    ProductInfo{id=2, name="珍珠奶茶", price="¥14", description="加了珍珠", ...}
        //  ],
        //  "水果茶": [
        //    ProductInfo{id=3, name="百香果茶", price="¥13", description="酸甜可口", ...}
        //  ],
        //  ...
        //}

        int index = 0;
        for (Map.Entry<String, List<ProductInfo>> entry : categoryToProductsMap.entrySet()) {
            String clazz = entry.getKey(); // 类别名称
            List<ProductInfo> products = entry.getValue(); // 该类下的产品列表

            // 添加到分类列表，第一个默认选中
            categoryList.add(new Category(clazz, index == 0));

            // 添加分类
            productList.add(clazz);
            // 添加分类下商品
            productList.addAll(products);
            index++;
        }
    }

//    private void initCategoryData() {
//        categoryList.clear();
//        String[] names = {"时令上新", "超级植物茶", "鲜果茶", "原叶茗茶", "浓郁牛乳茶", "灵感茶点小料"};
//        for (int i = 0; i < names.length; i++) {
//            categoryList.add(new Category(names[i], i == 0));
//        }
//    }
//
//    private void initProductData() {
//        productList.clear();
//        String[] categories = {"时令上新", "超级植物茶", "鲜果茶", "原叶茗茶", "浓郁牛乳茶", "灵感茶点小料"};
//        for (String category : categories) {
//            productList.add(category);
//            for (int i = 1; i <= 5; i++) {
//                productList.add(new ProductInfo(category + " 商品 " + i, "¥" + (10 + i), category));
//            }
//        }
//    }


}
