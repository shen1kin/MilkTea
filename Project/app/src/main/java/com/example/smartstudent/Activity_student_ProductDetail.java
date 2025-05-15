package com.example.smartstudent;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartstudent.model.MilkTeaAttribute;
import com.example.smartstudent.model.ProductInfo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.File;
import java.util.List;

public class Activity_student_ProductDetail extends AppCompatActivity {

    private ImageView ivImage;
    private TextView tvTitle, tvDescription,tvPrice;



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

                    // 第一个按钮默认选中
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
                // 默认选中第一个按钮
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

}




