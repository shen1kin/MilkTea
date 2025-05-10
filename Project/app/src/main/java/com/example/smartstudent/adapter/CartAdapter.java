package com.example.smartstudent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.R;
import com.example.smartstudent.cart.CartManager;
import com.example.smartstudent.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    private OnCartChangeListener cartChangeListener;

    public void setOnCartChangeListener(OnCartChangeListener listener) {
        this.cartChangeListener = listener;
    }

    private List<CartItem> items;

    public CartAdapter(List<CartItem> items) {
        this.items = items;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvCount, tvSpec;
        ImageView itemImage;
        ImageButton btnAdd, btnMinus, btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvSpec = itemView.findViewById(R.id.tvSpec);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = items.get(position);

        holder.tvName.setText(item.product.getName());
        holder.tvPrice.setText(item.product.getPrice());
        holder.tvCount.setText(String.valueOf(item.count));

        // 设置规格字段，如无可使用默认描述
        holder.tvSpec.setText("默认规格");

        // 显示图片（如有字段可接入 Glide 等加载器）
        holder.itemImage.setImageResource(R.drawable.ic_launcher_background);

        holder.btnAdd.setOnClickListener(v -> {
            CartManager.add(item.product);
            refresh();
            if (cartChangeListener != null) cartChangeListener.onCartChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            CartManager.decrease(item.product);
            refresh();
            if (cartChangeListener != null) cartChangeListener.onCartChanged();
        });

        holder.btnDelete.setOnClickListener(v -> {
            CartManager.remove(item.product);
            refresh();
            Toast.makeText(v.getContext(), "已删除商品", Toast.LENGTH_SHORT).show();
            if (cartChangeListener != null) cartChangeListener.onCartChanged();
        });
    }

    private void refresh() {
        items = CartManager.getItems();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
