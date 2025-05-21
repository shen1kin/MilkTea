package com.example.smartstudent;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.CartAdapter;
import com.example.smartstudent.adapter.CategoryAdapter;
import com.example.smartstudent.adapter.ProductAdapter;
import com.example.smartstudent.cart.CartOrderManager;
import com.example.smartstudent.model.CartItem;
import com.example.smartstudent.model.Category;
import com.example.smartstudent.model.MilkTeaAttribute;
import com.example.smartstudent.model.OrderItem;
import com.example.smartstudent.model.OrderModeManager;
import com.example.smartstudent.model.ProductInfo;
import com.example.smartstudent.utils.ImageUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Fragment_student_course extends Fragment {
    //判断是否已经从数据库中获取数据，获取了就不需要重新获取了
    private boolean dataLoaded = false;


    private LinearLayout layoutContent,layoutProgressBar;
    private ProgressBar loadingSpinner;

    private RecyclerView recyclerCategory, recyclerProducts;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    private Button btnStorePickup, btnDelivery;
    private TextView etSearch, cartBadge, cartTotal;

    private List<Category> categoryList = new ArrayList<>();
    private List<Object> productList = new ArrayList<>();
    //存储加入购物车的订单
    private List<OrderItem> orderItemList = new ArrayList<>();
    //创建购物车清单管理类,
    private CartOrderManager cartOrderManager =  new CartOrderManager();;


    private boolean isScrollByClick = false;
    private int currentOrderMode = OrderModeManager.getCurrentMode();

    private ActivityResultLauncher<Intent> productDetailLauncher;

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

        layoutProgressBar = view.findViewById(R.id.layoutProgressBar);
        loadingSpinner = view.findViewById(R.id.loading_spinner);
        layoutContent = view.findViewById(R.id.layoutContent);

        recyclerCategory = view.findViewById(R.id.recyclerCategory);
        recyclerProducts = view.findViewById(R.id.recyclerProducts);
        //监视购物车图标显示购物车清单
        cartIcon.setOnClickListener(v -> showCartDialog());



        // 然后传递给下一个界面
        etSearch.setOnClickListener(v -> {
            // 假设productList是 List<Object>
            List<ProductInfo> productInfoList = new ArrayList<>();

            for (Object obj : productList) {
                if (obj instanceof ProductInfo) {
                    productInfoList.add((ProductInfo) obj);
                }
            }

            if (!productInfoList.isEmpty()) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("productList", (Serializable) productInfoList);
                startActivityForResult(intent, 123);
            } else {
                Toast.makeText(getContext(), "未获取到数据", Toast.LENGTH_SHORT).show();
            }
        });



        btnCheckout.setOnClickListener(v -> {
            List<CartItem> items = cartOrderManager.getItems();
            OrderModeManager.setCurrentMode(currentOrderMode);
            Intent intent = new Intent(getActivity(), CheckoutActivity.class);
            intent.putExtra("cart_items", (Serializable) items); // 强转成 Serializable
            startActivity(intent);
        });

        btnStorePickup.setOnClickListener(v -> setButtonState(true));
        btnDelivery.setOnClickListener(v -> setButtonState(false));

        //获取全部信息
        getMilkTeaInfo();




        // 初始状态
        setButtonState(OrderModeManager.isPickup());

        //用于跳转页面，并等待返回结果
        productDetailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                resultLauncher -> {
                    if (resultLauncher.getResultCode() == RESULT_OK && resultLauncher.getData() != null) {
                        Intent data = resultLauncher.getData();
                        //返回加入购物车订单信息
                        OrderItem orderItemInfo = (OrderItem) data.getSerializableExtra("orderItemInfo");
                        Toast.makeText(requireContext(), "返回结果：" + orderItemInfo, Toast.LENGTH_SHORT).show();


                        //将返回订单信息，加入到购物车清单
                        Log.d("Test", "hashCode: " + orderItemInfo.hashCode());

                        CartOrderManager.add(orderItemInfo);
                        updateCartBadge();
                    }
                }
        );


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次页面切换回来都刷新状态
        setButtonState(OrderModeManager.isPickup());
    }
    //搜索回来定位商品信息
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            int id = data.getIntExtra("selectedProductId", -1);
            if (id != -1) {
                ProductInfo product = findProductById(id);
                if (product != null) {
                    // 直接跳转详情页
                    Intent intent = new Intent(requireContext(), Activity_student_ProductDetail.class);
                    intent.putExtra("product", product);
                    productDetailLauncher.launch(intent);
                }
            }
        }
    }



    // 根据商品名查找索引
    private ProductInfo findProductById(int id) {
        for (Object obj : productList) {
            if (obj instanceof ProductInfo) {
                ProductInfo p = (ProductInfo) obj;
                if (p.getId() == id) {
                    return p;
                }
            }
        }
        return null;
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
    //更新分类高亮，商品单独列表适配器
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

        //监听适配器回调，实现页面跳转
        productAdapter.setOnProductClickListener(product -> {
            Intent intent = new Intent(requireContext(), Activity_student_ProductDetail.class);
            intent.putExtra("product", product); // 把商品对象传给详情页面
            productDetailLauncher.launch(intent);
        });
    }
    //显示购物车清单弹窗
    private void showCartDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View sheetView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_cart, null);

        RecyclerView cartRecycler = sheetView.findViewById(R.id.recyclerCart);
        TextView tvTotal = sheetView.findViewById(R.id.tvCartTotal);
        Button btnCheckout = sheetView.findViewById(R.id.btnCheckoutCart);
        //获取订单信息
        List<CartItem> items = cartOrderManager.getItems();
        CartAdapter adapter = new CartAdapter(items);

        adapter.setOnCartChangeListener(() -> {
            updateCartBadge();
            tvTotal.setText("合计：" + cartOrderManager.getTotalPrice());
        });

        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecycler.setAdapter(adapter);

        tvTotal.setText("合计：" + cartOrderManager.getTotalPrice());

//        btnCheckout.setOnClickListener(v -> {
//            dialog.dismiss();
//            OrderModeManager.setCurrentMode(currentOrderMode);
//            startActivity(new Intent(getActivity(), CheckoutActivity.class));
//        });

        btnCheckout.setOnClickListener(v -> {
            dialog.dismiss();
            OrderModeManager.setCurrentMode(currentOrderMode);
            Intent intent = new Intent(getActivity(), CheckoutActivity.class);
            intent.putExtra("cart_items", (Serializable) items);
            startActivity(intent);
        });

        dialog.setContentView(sheetView);
        dialog.show();
    }

    public void updateCartBadge() {
        if (cartBadge == null || cartTotal == null) return;

        int count = cartOrderManager.getTotalCount();
        cartBadge.setText(String.valueOf(count));
        cartBadge.setVisibility(count > 0 ? View.VISIBLE : View.GONE);

        cartTotal.setText(cartOrderManager.getTotalPrice());
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
    //获取数据信息
    private void getMilkTeaInfo() {
        //未获取到信息画面转圈圈
        //判断是否获取过数据
        if (dataLoaded) {
            // 已经加载过数据，直接使用现有数据更新UI
            layoutProgressBar.setVisibility(View.GONE);
            loadingSpinner.setVisibility(View.GONE);
            layoutContent.setVisibility(View.VISIBLE);

            setupCategoryRecycler();
            setupProductRecycler();
            updateCartBadge();

            return;
        }else {
            layoutProgressBar.setVisibility(View.VISIBLE);
            loadingSpinner.setVisibility(View.VISIBLE);
            layoutContent.setVisibility(View.GONE);
        }






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

                                    String base64 = teaObj.getString("image");
                                    String imagePath = ImageUtils.decodeBase64AndSaveImage(getContext(), base64);
                                    if (imagePath != null) {
                                        product.image = imagePath;
                                    } else {
                                        Log.e("ImageSave", "Failed to decode and save Base64 image");
                                    }


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

                                dataLoaded = true;

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
    //初始化 将获取信息 填入组件
    private void initAllDate(Map<String, List<ProductInfo>> categoryToProductsMap) {
        categoryList.clear();
        productList.clear();

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


}
