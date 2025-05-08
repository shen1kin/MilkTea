package com.example.smartstudent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.R;
import com.example.smartstudent.model.ProductInfo;

import java.util.List;

public class ItemMikeAdapter extends RecyclerView.Adapter<ItemMikeAdapter.ItemMikeHolder> {
    private List<ProductInfo> productList;

    public ItemMikeAdapter(List<ProductInfo> list) {
        this.productList = list;
    }

    @NonNull
    @Override
    public ItemMikeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mike, parent, false);
        return new ItemMikeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMikeHolder holder, int position) {
        ProductInfo product = productList.get(position);
        holder.tvItemName.setText(product.getName());
        holder.tvItemPrice.setText(product.getPrice());
        holder.itemImage.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ItemMikeHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView tvItemPrice;
        ImageView itemImage;

        public ItemMikeHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
}
