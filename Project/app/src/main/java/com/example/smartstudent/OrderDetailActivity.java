package com.example.smartstudent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudent.cart.CartOrderManager;
import com.example.smartstudent.model.Order;
import com.example.smartstudent.model.OrderItem;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageView imgStep1, imgStep2, imgStep3;
    private TextView tvTitle, tvOrderDesc;
    private TextView tvStore, tvTime, tvStatus, tvCount;
    private LinearLayout layoutOrderItemContainer;

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
        tvCount = findViewById(R.id.tvCountDetail);
        layoutOrderItemContainer = findViewById(R.id.layoutProductContainer); // 新增容器控件

        // 接收订单数据
        Order order = (Order) getIntent().getSerializableExtra("order");

        if (order != null) {
            // 填充基础字段
            tvStore.setText(order.storeName);
            tvTime.setText(order.orderTime);
            tvStatus.setText(order.status);
            tvCount.setText("共 " + order.totalCount + " 件商品，合计 " + order.totalPrice);
            updateOrderStatusUI(order.status); // 设置状态栏
            //判断是前端还是后端传输的数据
            //将List 转化为 Map，方便显示
            if(order.orderItemInfoList != null)
            {
                CartOrderManager cartOrderManager = new CartOrderManager();
                for(OrderItem orderItemInfo: order.orderItemInfoList) {
                    cartOrderManager.add(orderItemInfo);
                }
                order.orderItemInfos = cartOrderManager.getCartMap();
            }


            addProductItems(order.getOrderItemInfos());


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
    private void addProductItems(Map<OrderItem, Integer> orderItemInfos) {
        layoutOrderItemContainer.removeAllViews(); // 清空旧内容

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Map.Entry<OrderItem, Integer> entry : orderItemInfos.entrySet()) {
            OrderItem orderItem = entry.getKey();
            int countValue = entry.getValue(); // 或者你也可以用 orderItem.getCount()

            View itemView = inflater.inflate(R.layout.item_checkout, layoutOrderItemContainer, false);

            ImageView img = itemView.findViewById(R.id.itemImage);
            TextView name = itemView.findViewById(R.id.tvName);
            TextView spec = itemView.findViewById(R.id.tvSpec);
            TextView price = itemView.findViewById(R.id.tvPrice);
            TextView count = itemView.findViewById(R.id.tvCount);

            // 填充数据
            name.setText(orderItem.getName());
            spec.setText(orderItem.getFormattedAttributes()); // 注意这个方法参数类型需支持 OrderItem
            price.setText(orderItem.getPrice());
            count.setText("×" + countValue);
            //图片
            String savedPath = orderItem.getImageWay(); // 之前保存的路径
            Bitmap bitmap = loadImageFromPath(savedPath);
            if (bitmap != null) {
                img.setImageBitmap(bitmap);
            }

            layoutOrderItemContainer.addView(itemView);
        }
    }


    public Bitmap loadImageFromPath(String imagePath) {
        if (imagePath == null || !new File(imagePath).exists()) {
            return null;
        }
    return BitmapFactory.decodeFile(imagePath);
    }
}
