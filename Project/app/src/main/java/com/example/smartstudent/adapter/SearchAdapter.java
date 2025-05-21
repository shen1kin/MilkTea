package com.example.smartstudent.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.R;
import com.example.smartstudent.model.ProductInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<ProductInfo> productList;
    private OnAddToCartClickListener listener;

    // 接口：外部实现回调
    public interface OnAddToCartClickListener {
        void onAddToCartClick(ProductInfo product);
    }

    // 提供设置监听的方法
    public void setOnAddToCartClickListener(OnAddToCartClickListener listener) {
        this.listener = listener;
    }

    public SearchAdapter(List<ProductInfo> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mike, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductInfo product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice());

        Bitmap bitmap = loadImageFromPath(product.getImage());
        if (bitmap != null) {
            holder.itemImage.setImageBitmap(bitmap);
        }

        // ✅ 点击加入购物车时通知监听器
        holder.btnAddToCart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCartClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<ProductInfo> filteredList) {
        productList = new ArrayList<>(filteredList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView itemImage;
        Button btnAddToCart;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            itemImage = itemView.findViewById(R.id.itemImage);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }

    public Bitmap loadImageFromPath(String imagePath) {
        if (imagePath == null || !new File(imagePath).exists()) {
            return null;
        }
        return BitmapFactory.decodeFile(imagePath);
    }
}
