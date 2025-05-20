package com.example.smartstudent.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.OrderDetailActivity;
import com.example.smartstudent.R;
import com.example.smartstudent.model.Order;
import com.example.smartstudent.model.OrderItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OrderAdminAdapter extends RecyclerView.Adapter<OrderAdminAdapter.OrderViewHolder> {

    public interface OnOrderActionListener {
        void onRefund(Order order);
        void onDelivered(Order order);
        void onComplete(Order order);
        void onClick(Order order); // 点击整个 item
    }


    private final List<Order> orderList;
    private final OnOrderActionListener listener;

    public OrderAdminAdapter(List<Order> orderList, OnOrderActionListener listener) {
        this.orderList = new ArrayList<>(orderList);
        this.listener = listener;
    }

    public void setOrderList(List<Order> newOrderList) {
        this.orderList.clear();
        this.orderList.addAll(newOrderList);
        notifyDataSetChanged();
    }

    private Bitmap loadImageFromPath(String imagePath) {
        if (imagePath == null || !new File(imagePath).exists()) {
            return null;
        }
        return BitmapFactory.decodeFile(imagePath);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_admin, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        Context context = holder.itemView.getContext();

        holder.tvStore.setText(order.getStoreName());
        holder.tvOrderTime.setText(order.getOrderTime());
        holder.tvStatus.setText(order.status);
        holder.tvOrderTotal.setText("总价 " + order.getTotalPrice());
        holder.tvOrderCount.setText("共 " + order.getTotalCount() + " 件商品");

        holder.layoutProductImages.removeAllViews();
        if (order.orderItemInfoList != null) {
            for (int i = 0; i < Math.min(order.orderItemInfoList.size(), 3); i++) {
                OrderItem item = order.orderItemInfoList.get(i);
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
                params.setMargins(0, 0, 16, 0);
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Bitmap bitmap = loadImageFromPath(item.getImageWay());
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(R.drawable.ic_launcher_foreground);
                }

                holder.layoutProductImages.addView(imageView);
            }
        }

        // 按钮点击事件
        // 👉 按钮监听
        holder.btnRefund.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRefund(order);
            }
        });

        holder.btnDelivered.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelivered(order);
            }
        });

        holder.btnComplete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onComplete(order);
            }
        });

        // 👉 item 点击跳转
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvStore, tvOrderTime, tvStatus;
        LinearLayout layoutProductImages;
        TextView tvOrderTotal, tvOrderCount;
        Button btnRefund, btnDelivered, btnComplete;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStore = itemView.findViewById(R.id.tvStore);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            layoutProductImages = itemView.findViewById(R.id.layoutProductImages);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderCount = itemView.findViewById(R.id.tvOrderCount);

            btnRefund = itemView.findViewById(R.id.btnRefund);
            btnDelivered = itemView.findViewById(R.id.btnDelivered);
            btnComplete = itemView.findViewById(R.id.btnComplete);
        }
    }
}
