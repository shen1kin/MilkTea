package com.example.smartstudent.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.OrderDetailActivity;
import com.example.smartstudent.R;
import com.example.smartstudent.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    // 点击事件接口
    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    private List<Order> orderList;
    private final OnOrderClickListener listener;

    // 带点击事件回调的构造器
    public OrderAdapter(List<Order> orderList, OnOrderClickListener listener) {
        this.orderList = new ArrayList<>(orderList); // 防止外部修改
        this.listener = listener;
    }

    // 兼容旧调用方式
    public OrderAdapter(List<Order> orderList) {
        this(orderList, null);
    }

    // 用于更新订单数据
    public void setOrderList(List<Order> newOrders) {
        this.orderList.clear();
        this.orderList.addAll(newOrders);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Order order = orderList.get(position);

        // 顶部信息设置
        holder.tvStore.setText(order.storeName);
        holder.tvOrderTime.setText(order.orderTime);
        holder.tvStatus.setText(order.status);

        // 商品总价 + 件数
        holder.tvOrderTotal.setText("总价 " + order.totalPrice);
        holder.tvOrderCount.setText("共 " + order.totalCount + " 件商品");

        // 清空后添加商品图片
        holder.layoutProductImages.removeAllViews();
        if (order.productList != null) {
            for (int i = 0; i < Math.min(order.productList.size(), 3); i++) {
                ImageView img = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
                lp.setMargins(0, 0, 16, 0);
                img.setLayoutParams(lp);
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);

//                if (order.productList.get(i).getImage() != null) {
//                    img.setImageBitmap(order.productList.get(i).getImage());
//                    holder.layoutProductImages.addView(img);
//                }
            }
        }


        // 点击事件（优先使用外部监听器）
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderClick(order);
            } else {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("order", order);
                context.startActivity(intent);
            }
        });

        // 中间提示区域状态控制
        switch (order.status) {
            case "制作中":
                showStatusHint(holder, "订单制作中", "预计 5 分钟制作完成，请耐心等待");
                break;
            case "配送中":
                showStatusHint(holder, "订单配送中", "预计 28 分钟送达，请耐心等待");
                break;
            case "退款中":
                showStatusHint(holder, "退款处理中", "请耐心等待审核结果");
                break;
            default:
                holder.layoutOrderStatusHint.setVisibility(View.GONE);
                break;
        }

        // 客服按钮点击
        holder.btnContactService.setOnClickListener(v ->
                Toast.makeText(context, "联系客服功能未实现", Toast.LENGTH_SHORT).show()
        );
    }

    // 展示中间的状态提示条
    private void showStatusHint(OrderViewHolder holder, String title, String subText) {
        holder.layoutOrderStatusHint.setVisibility(View.VISIBLE);
        holder.tvStatusHintTitle.setText(title);
        holder.tvStatusHintSub.setText(subText);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // ViewHolder 内部类
    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvStore, tvOrderTime, tvStatus;
        LinearLayout layoutOrderStatusHint;
        TextView tvStatusHintTitle, tvStatusHintSub;
        Button btnContactService;
        LinearLayout layoutProductImages;
        TextView tvOrderTotal, tvOrderCount;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStore = itemView.findViewById(R.id.tvStore);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            layoutOrderStatusHint = itemView.findViewById(R.id.layoutOrderStatusHint);
            tvStatusHintTitle = itemView.findViewById(R.id.tvStatusHintTitle);
            tvStatusHintSub = itemView.findViewById(R.id.tvStatusHintSub);
            btnContactService = itemView.findViewById(R.id.btnContactService);

            layoutProductImages = itemView.findViewById(R.id.layoutProductImages);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderCount = itemView.findViewById(R.id.tvOrderCount);
        }
    }
}
