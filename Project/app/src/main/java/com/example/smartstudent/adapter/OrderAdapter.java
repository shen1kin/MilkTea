package com.example.smartstudent.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.OrderDetailActivity;
import com.example.smartstudent.R;
import com.example.smartstudent.model.Order;
import com.example.smartstudent.model.OrderItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    private final List<Order> orderList;
    private final OnOrderClickListener listener;

    public OrderAdapter(List<Order> orderList, OnOrderClickListener listener) {
        this.orderList = new ArrayList<>(orderList); // 正确使用 Order 列表
        this.listener = listener;
    }

    public void setOrderList(List<Order> newOrderList) {
        this.orderList.clear();
        this.orderList.addAll(newOrderList);
        notifyDataSetChanged();
    }

    public Bitmap loadImageFromPath(String imagePath) {
        if (imagePath == null || !new File(imagePath).exists()) {
            return null;
        }
        return BitmapFactory.decodeFile(imagePath);
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

        holder.tvStore.setText(order.getStoreName());
        holder.tvOrderTime.setText(order.getOrderTime());
        holder.tvStatus.setText(order.status);
        holder.tvOrderTotal.setText("总价 " + order.getTotalPrice());
        holder.tvOrderCount.setText("共 " + order.getTotalCount() + " 件商品");

        // 设置商品图片缩略（最多显示3张）
        holder.layoutProductImages.removeAllViews();
        if (order.orderItemInfoList != null) {
            for (int i = 0; i < Math.min(order.orderItemInfoList.size(), 3); i++) {
                OrderItem item = order.orderItemInfoList.get(i);
                ImageView img = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
                lp.setMargins(0, 0, 16, 0);
                img.setLayoutParams(lp);
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);

                // 正确设置图片
                String savedPath = item.getImageWay(); // 每个 item 有自己的图片路径
                Bitmap bitmap = loadImageFromPath(savedPath);
                if (bitmap != null) {
                    img.setImageBitmap(bitmap);
                } else {
                    img.setImageResource(R.drawable.ic_launcher_foreground); // 默认图
                }

                holder.layoutProductImages.addView(img);
            }
        }


        // 点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderClick(order);
            } else {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("order", order); // 传递整个 Order 对象（需要实现 Serializable）
                context.startActivity(intent);
            }
        });

        // 状态提示条
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

        holder.btnContactService.setOnClickListener(v ->
                Toast.makeText(context, "联系客服功能未实现", Toast.LENGTH_SHORT).show()
        );
    }

    private void showStatusHint(OrderViewHolder holder, String title, String subText) {
        holder.layoutOrderStatusHint.setVisibility(View.VISIBLE);
        holder.tvStatusHintTitle.setText(title);
        holder.tvStatusHintSub.setText(subText);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

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
