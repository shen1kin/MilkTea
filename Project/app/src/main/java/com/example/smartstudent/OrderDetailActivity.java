package com.example.smartstudent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudent.model.Order;
import com.example.smartstudent.model.ProductInfo;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageView imgStep1, imgStep2, imgStep3;
    private TextView tvTitle, tvOrderDesc;
    private TextView tvStore, tvTime, tvStatus, tvTotal, tvCount;
    private LinearLayout layoutProductContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // 返回按钮
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // 状态栏控件
        imgStep1 = findViewById(R.id.imgStep1);
        imgStep2 = findViewById(R.id.imgStep2);
        imgStep3 = findViewById(R.id.imgStep3);
        tvTitle = findViewById(R.id.tvOrderTitle);
        tvOrderDesc = findViewById(R.id.tvOrderDesc);

        // 信息区块控件
        tvStore = findViewById(R.id.tvStoreName);
        tvTime = findViewById(R.id.tvOrderTime);
        tvStatus = findViewById(R.id.tvOrderTitle);
        tvTotal = findViewById(R.id.tvTotalDetail);
        tvCount = findViewById(R.id.tvCountDetail);
        layoutProductContainer = findViewById(R.id.layoutProductContainer); // 新增容器控件

        // 接收订单数据
        Order order = (Order) getIntent().getSerializableExtra("order");
        if (order != null) {
            // 填充基础字段
            tvStore.setText(order.storeName);
            tvTime.setText(order.orderTime);
            tvStatus.setText(order.status);
            tvTotal.setText("商品总价 " + order.totalPrice);
            tvCount.setText("共 " + order.totalCount + " 件商品，合计 " + order.totalPrice);

            updateOrderStatusUI(order.status); // 设置状态栏

            // 动态添加商品项
            addProductItems(order.productList);
        }
    }

    /**
     * 根据状态设置顶部状态栏图标与文案
     */
    private void updateOrderStatusUI(String status) {
        switch (status) {
            case "制作中":
                imgStep1.setImageResource(R.drawable.jieshou);
                imgStep2.setImageResource(R.drawable.zhizuo);
                imgStep3.setImageResource(R.drawable.wancheng);
                tvTitle.setText("订单制作中");
                tvOrderDesc.setText("预计 6 分钟完成，请耐心等待");
                break;
            case "配送中":
                imgStep1.setImageResource(R.drawable.jieshou);
                imgStep2.setImageResource(R.drawable.peisong);
                imgStep3.setImageResource(R.drawable.wancheng);
                tvTitle.setText("配送中");
                tvOrderDesc.setText("预计 28 分钟送达，请耐心等待");
                break;
            case "退款中":
                imgStep1.setImageResource(R.drawable.tuokuan);
                imgStep2.setImageResource(R.drawable.shenhe);
                imgStep3.setImageResource(R.drawable.wancheng);
                tvTitle.setText("退款处理中");
                tvOrderDesc.setText("请耐心等待审核结果");
                break;
            case "已完成":
                imgStep1.setImageResource(R.drawable.wancheng);
                imgStep2.setVisibility(View.GONE);
                imgStep3.setVisibility(View.GONE);
                tvTitle.setText("订单已完成");
                tvOrderDesc.setText("感谢您对喜茶的支持，欢迎再次光临");
                break;
            default:
                tvTitle.setText("订单状态");
                tvOrderDesc.setText("");
                break;
        }
    }

    /**
     * 动态添加商品项视图
     */
    private void addProductItems(List<ProductInfo> products) {
        layoutProductContainer.removeAllViews(); // 清空旧内容

        LayoutInflater inflater = LayoutInflater.from(this);
        for (ProductInfo product : products) {
            View itemView = inflater.inflate(R.layout.item_checkout, layoutProductContainer, false);

            ImageView img = itemView.findViewById(R.id.itemImage);
            TextView name = itemView.findViewById(R.id.tvName);
            TextView spec = itemView.findViewById(R.id.tvSpec);
            TextView price = itemView.findViewById(R.id.tvPrice);
            TextView count = itemView.findViewById(R.id.tvCount);

            // 填充数据
            img.setImageBitmap(product.getImage());
            name.setText(product.getName());
            spec.setText(getSpecText(product));
            price.setText(product.getPrice());
            count.setText("×" + product.getCount());

            layoutProductContainer.addView(itemView);
        }
    }

    /**
     * 获取规格组合文本（比如 冷/中杯/去冰/芝士顶）
     */
    private String getSpecText(ProductInfo product) {
        StringBuilder sb = new StringBuilder();
        if (product.getAttributes() != null) {
            for (int i = 0; i < product.getAttributes().size(); i++) {
                List<String> values = product.getAttributes().get(i).attribute_value;
                if (values != null && !values.isEmpty()) {
                    sb.append(values.get(0));
                    if (i != product.getAttributes().size() - 1) sb.append("/");
                }
            }
        }
        return sb.toString();
    }
}
