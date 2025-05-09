package com.example.smartstudent.adapter;

import android.view.*;
import android.widget.*;
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
        TextView name, price, count;
        Button btnAdd, btnMinus, btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            price = itemView.findViewById(R.id.tvPrice);
            count = itemView.findViewById(R.id.tvCount);
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
        holder.name.setText(item.product.getName());
        holder.price.setText(item.product.getPrice());
        holder.count.setText(String.valueOf(item.count));

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
