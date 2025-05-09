package com.example.smartstudent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.Fragment_student_course;
import com.example.smartstudent.R;
import com.example.smartstudent.cart.CartManager;
import com.example.smartstudent.model.ProductInfo;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Object> itemList;

    public ProductAdapter(List<Object> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position) instanceof String ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mike, parent, false);
            return new ProductViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            String title = (String) itemList.get(position);
            ((HeaderViewHolder) holder).tvHeader.setText(title);
        } else {
            ProductInfo product = (ProductInfo) itemList.get(position);
            ProductViewHolder vh = (ProductViewHolder) holder;
            vh.tvName.setText(product.getName());
            vh.tvPrice.setText(product.getPrice());

            // 点击加入购物车
            vh.btnAddToCart.setOnClickListener(v -> {
                CartManager.add(product); // 添加商品到购物车
                Toast.makeText(v.getContext(), "已加入购物车", Toast.LENGTH_SHORT).show();

                // 通知 Fragment 更新角标
                Context context = v.getContext();
                if (context instanceof FragmentActivity) {
                    FragmentActivity activity = (FragmentActivity) context;
                    Fragment_student_course fragment = (Fragment_student_course)
                            activity.getSupportFragmentManager().findFragmentByTag("f1");
                    if (fragment != null) {
                        fragment.updateCartBadge();
                    }
                }
            });
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;

        HeaderViewHolder(View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvHeader);
        }
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        Button btnAddToCart;

        ProductViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
