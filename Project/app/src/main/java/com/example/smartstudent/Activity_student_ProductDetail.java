package com.example.smartstudent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudent.model.MilkTeaAttribute;
import com.example.smartstudent.model.OrderItem;
import com.example.smartstudent.model.OrderAttribute;
import com.example.smartstudent.model.ProductInfo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Activity_student_ProductDetail extends AppCompatActivity {

    private ImageView ivImage;
    private TextView tvTitle, tvDescription,tvPrice;
    private Button btnAddToCart;

    private List<OrderAttribute> attributes;
    //用于获取全部按钮点击
    private final List<MaterialButtonToggleGroup> toggleGroupList = new ArrayList<>();



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_product_detail);
        ProductInfo product = (ProductInfo) getIntent().getSerializableExtra("product");

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvPrice = findViewById(R.id.tvPrice);
        ivImage = findViewById(R.id.ivImage);

        // product 不能为空
        if (product != null) {
            tvTitle.setText(product.getName());
            tvDescription.setText(product.getDescription());
            tvPrice.setText(product.getPrice());

            //图片获取
            // 使用示例
            String savedPath = product.getImage(); // 之前保存的路径
            Bitmap bitmap = loadImageFromPath(savedPath);
            if (bitmap != null) {
                ivImage.setImageBitmap(bitmap);
            }

//            public Bitmap loadImageFromPath(String imagePath) {
//                if (imagePath == null || !new File(imagePath).exists()) {
//                    return null;
//                }
//                return BitmapFactory.decodeFile(imagePath);
//            }

            LinearLayout parentLayout = findViewById(R.id.layoutAttributeContainer);
            List<MilkTeaAttribute> attributeList = product.getAttributes();

            for (int i = 0; i < attributeList.size(); i++) {
                String attribute = attributeList.get(i).getAttribute();
                List<String> attribute_valueList = attributeList.get(i).getAttribute_value();

                LayoutInflater inflater = LayoutInflater.from(this);
                View itemView = inflater.inflate(R.layout.item_product_detail_attribute, parentLayout, false);

                TextView tvAttribute = itemView.findViewById(R.id.tvAttribute);
                tvAttribute.setText(attribute);

                MaterialButtonToggleGroup toggle_group = itemView.findViewById(R.id.toggle_group);
                int margin = dpToPx(20);
                int buttonHeight = dpToPx(50);

                toggle_group.setSingleSelection(true);
                toggle_group.setSelectionRequired(true);

                // 保存 toggle_group 引用
                toggleGroupList.add(toggle_group);

                // 动态添加属性按钮
                for (int avIndex = 0; avIndex < attribute_valueList.size(); avIndex++) {
                    MaterialButton button = new MaterialButton(this);
                    button.setText(attribute_valueList.get(avIndex));
                    button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    button.setId(View.generateViewId());

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            buttonHeight
                    );
                    if (avIndex > 0) {
                        lp.setMarginStart(margin);
                    }
                    button.setLayoutParams(lp);

                    // 默认样式
                    if (avIndex == 0) {
                        button.setChecked(true);
                        button.setBackgroundColor(Color.BLACK);
                        button.setTextColor(Color.WHITE);
                        button.setTypeface(null, Typeface.BOLD);
                    } else {
                        button.setChecked(false);
                        button.setBackgroundColor(Color.LTGRAY);
                        button.setTextColor(Color.WHITE);
                        button.setTypeface(null, Typeface.NORMAL);
                    }

                    toggle_group.addView(button);
                }

                // 再次默认选中第一个按钮
                if (toggle_group.getChildCount() > 0) {
                    MaterialButton firstButton = (MaterialButton) toggle_group.getChildAt(0);
                    firstButton.setChecked(true);
                }

                // 设置样式监听器
                toggle_group.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
                    if (!isChecked) return;
                    for (int j = 0; j < group.getChildCount(); j++) {
                        MaterialButton btn = (MaterialButton) group.getChildAt(j);
                        if (btn.getId() == checkedId) {
                            btn.setBackgroundColor(Color.BLACK);
                            btn.setTextColor(Color.WHITE);
                            btn.setTypeface(null, Typeface.BOLD);
                        } else {
                            btn.setBackgroundColor(Color.LTGRAY);
                            btn.setTextColor(Color.WHITE);
                            btn.setTypeface(null, Typeface.NORMAL);
                        }
                    }
                });

                parentLayout.addView(itemView);
            }

        }


        //购物数量
        ImageButton btnMinus = findViewById(R.id.btnMinus);
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        TextView tvCount = findViewById(R.id.tvCount);

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(tvCount.getText().toString());
                if (count > 1) {
                    count--;
                    tvCount.setText(String.valueOf(count));
                }
                // 如果需要，点击减少按钮到1时可以禁用减少按钮或提示用户
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(tvCount.getText().toString());
                count++;
                tvCount.setText(String.valueOf(count));
            }
        });


        //加入购物车，点击返回传递信息
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product != null) {
                    //创建返回数据
                    //获取按钮组内容的属性与属性值
                    collectSelectedAttributesToList();


                    double price = Double.parseDouble(product.getPrice());
                    int count = Integer.parseInt(tvCount.getText().toString());
                    double totalPriceInt =price * count;
                    String totalPrice = String.valueOf(totalPriceInt);
                    //没有门店信息 下单时间 订单结束时间 配送方式 支付方式 订单状态 地址 订单号码 备注
                    //返回属性
                    OrderItem orderItemInfo = new OrderItem(product.getName(),count,totalPrice,product.getId(),product.getClazz(),attributes,product.getImage());

                    //页面返回跳转，将order返回
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("orderItemInfo", orderItemInfo); // 返回的数据
                    setResult(RESULT_OK, resultIntent);
                    finish(); // 关闭当前页面
                }
            }
        });


    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public Bitmap loadImageFromPath(String imagePath) {
        if (imagePath == null || !new File(imagePath).exists()) {
            return null;
        }
        return BitmapFactory.decodeFile(imagePath);
    }
    //获取按钮组内的信息，获取选择的属性
    private void collectSelectedAttributesToList() {
        attributes = new ArrayList<>();

        for (MaterialButtonToggleGroup group : toggleGroupList) {
            int checkedId = group.getCheckedButtonId();
            if (checkedId != View.NO_ID) {
                MaterialButton selectedButton = group.findViewById(checkedId);
                String selectedValue = selectedButton.getText().toString();

                // 获取属性名（在父 View 中）
                View parentView = (View) group.getParent();
                TextView tvAttribute = parentView.findViewById(R.id.tvAttribute);
                String attributeName = tvAttribute.getText().toString();

                attributes.add(new OrderAttribute(attributeName, selectedValue));
            }
        }
    }



}




