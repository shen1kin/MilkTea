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

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
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

        // 顶部信息
        holder.tvStore.setText(order.storeName);
        holder.tvOrderTime.setText(order.orderTime);
        holder.tvStatus.setText(order.status);

        // 总价与件数
        holder.tvOrderTotal.setText("总价 " + order.totalPrice);
        holder.tvOrderCount.setText("共 " + order.totalCount + " 件商品");

        // 清空并添加商品图片
        holder.layoutProductImages.removeAllViews();
        for (int resId : order.productImageResIds) {
            ImageView img = new ImageView(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
            lp.setMargins(0, 0, 16, 0);
            img.setLayoutParams(lp);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setImageResource(resId); // 使用模拟图片资源
            holder.layoutProductImages.addView(img);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), OrderDetailActivity.class);
            intent.putExtra("order", order);
            v.getContext().startActivity(intent);
        });


        // 状态提示区处理
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

        // 客服按钮事件（可跳转或弹窗）
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
