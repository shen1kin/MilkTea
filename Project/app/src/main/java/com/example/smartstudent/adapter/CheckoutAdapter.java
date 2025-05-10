package com.example.smartstudent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartstudent.R;
import com.example.smartstudent.model.CartItem;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {

    private final List<CartItem> items;

    public CheckoutAdapter(List<CartItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        CartItem item = items.get(position);

        holder.tvName.setText(item.product.getName());
        holder.tvPrice.setText(item.product.getPrice());
        holder.tvCount.setText("×" + item.count);

        // 如果 ProductInfo 中有规格描述字段可以设置规格
        holder.tvSpec.setText("冷/黑杯/少冰/芝士云顶"); // ✅ 可替换为 item.product.getSpec()

        // 商品图片加载（如果你有图片字段）:
        // 使用 Glide 等加载器：
        // Glide.with(holder.itemView.getContext()).load(item.product.getImageUrl()).into(holder.itemImage);

        holder.itemImage.setImageResource(R.drawable.ic_launcher_background); // 默认图
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView tvName, tvSpec, tvPrice, tvCount;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvSpec = itemView.findViewById(R.id.tvSpec);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCount = itemView.findViewById(R.id.tvCount);
        }
    }
}
