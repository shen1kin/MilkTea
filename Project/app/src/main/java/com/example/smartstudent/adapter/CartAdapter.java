package com.example.smartstudent.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.smartstudent.cart.CartOrderManager;
import com.example.smartstudent.model.CartItem;
import com.example.smartstudent.model.OrderAttribute;

import java.io.File;
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

        holder.tvName.setText(item.getOrderInfo().getName());
        holder.tvPrice.setText(item.getOrderInfo().getPrice());
        holder.tvCount.setText(String.valueOf(item.getCount()));

        // 设置规格字段，如无可使用默认描述
        holder.tvSpec.setText(item.getOrderInfo().getFormattedAttributes());

        // 显示图片（如有字段可接入 Glide 等加载器）
//        holder.itemImage.setImageResource(R.drawable.ic_launcher_background);
        //图片获取
        // 使用示例
        String savedPath = item.getOrderInfo().getImageWay(); // 之前保存的路径
        Bitmap bitmap = loadImageFromPath(savedPath);
        if (bitmap != null) {
            holder.itemImage.setImageBitmap(bitmap); // 正确方式显示 Bitmap
        } else {
            holder.itemImage.setImageResource(R.drawable.ic_launcher_background); // 加载失败用默认图
        }



        holder.btnAdd.setOnClickListener(v -> {
            CartOrderManager.add(item.getOrderInfo());
            refresh();
            if (cartChangeListener != null) cartChangeListener.onCartChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            CartOrderManager.decrease(item.getOrderInfo());
            refresh();
            if (cartChangeListener != null) cartChangeListener.onCartChanged();
        });

        holder.btnDelete.setOnClickListener(v -> {
            CartOrderManager.remove(item.getOrderInfo());
            refresh();
            Toast.makeText(v.getContext(), "已删除商品", Toast.LENGTH_SHORT).show();
            if (cartChangeListener != null) cartChangeListener.onCartChanged();
        });
    }

    private void refresh() {
        items = CartOrderManager.getItems();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Bitmap loadImageFromPath(String imagePath) {
        if (imagePath == null || !new File(imagePath).exists()) {
            return null;
        }
        return BitmapFactory.decodeFile(imagePath);
    }
}
