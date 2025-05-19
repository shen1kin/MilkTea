package com.example.smartstudent.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.R;
import com.example.smartstudent.model.ProductInfo;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_PRODUCT = 1;

    private final List<Object> itemList;

    private OnProductClickListener listener;

    public interface OnAddToCartListener {
        void onAdd();
    }

    private OnAddToCartListener addToCartListener;

    public void setOnAddToCartListener(OnAddToCartListener listener) {
        this.addToCartListener = listener;
    }

    public ProductAdapter(List<Object> itemList) {
        this.itemList = itemList;
    }
    //定义接口,用于回调
    public interface OnProductClickListener {
        void onProductClick(ProductInfo product);
    }
    //设置监听方法
    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position) instanceof String ? TYPE_TITLE : TYPE_PRODUCT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new TitleViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mike, parent, false);
            return new ProductViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).title.setText((String) itemList.get(position));
        } else if (holder instanceof ProductViewHolder) {

            //{
            //  "经典奶茶": [
            //    ProductInfo{id=1, name="原味奶茶", price="¥12", description="香浓经典", ...},
            //    ProductInfo{id=2, name="珍珠奶茶", price="¥14", description="加了珍珠", ...}
            //  ],
            //  "水果茶": [
            //    ProductInfo{id=3, name="百香果茶", price="¥13", description="酸甜可口", ...}
            //  ],
            //  ...
            //}
            ProductInfo product = (ProductInfo) itemList.get(position);
            ProductViewHolder h = (ProductViewHolder) holder;

            h.name.setText(product.getName());
            h.price.setText(product.getPrice());
            h.description.setText(product.getDescription());

            String imagePath = product.getImage();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            h.image.setImageBitmap(bitmap);

            //点击之后页面跳转到详细界面
            h.btnAddToCart.setOnClickListener(v -> {

                if (listener != null) {
                    listener.onProductClick(product);
                }
//                Context context = holder.itemView.getContext();
//                Intent intent = new Intent(context, Activity_student_ProductDetail.class);
//                intent.putExtra("product", product);
//                context.startActivity(intent);
//
//                void onProductClick(Product product); // 或其他你需要传的数据类型

            });
        }
    }

//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof TitleViewHolder) {
//            ((TitleViewHolder) holder).title.setText((String) itemList.get(position));
//        } else if (holder instanceof ProductViewHolder) {
//
//            //{
//            //  "经典奶茶": [
//            //    ProductInfo{id=1, name="原味奶茶", price="¥12", description="香浓经典", ...},
//            //    ProductInfo{id=2, name="珍珠奶茶", price="¥14", description="加了珍珠", ...}
//            //  ],
//            //  "水果茶": [
//            //    ProductInfo{id=3, name="百香果茶", price="¥13", description="酸甜可口", ...}
//            //  ],
//            //  ...
//            //}
//            ProductInfo product = (ProductInfo) itemList.get(position);
//            ProductViewHolder h = (ProductViewHolder) holder;
//
//            h.name.setText(product.getName());
//            h.price.setText(product.getPrice());
//            h.description.setText(product.getDescription());
//            h.image.setImageBitmap(product.getImage());
//            // 如果有图片字段可以设置 h.image.setImageResource 或 setImageURI
//
//            h.btnAddToCart.setOnClickListener(v -> {
//                CartManager.add(product);
//                Toast.makeText(v.getContext(), "已加入购物车", Toast.LENGTH_SHORT).show();
//
//                // ✅ 通知外部刷新角标和总价
//                if (addToCartListener != null) {
//                    addToCartListener.onAdd();
//                }
//            });
//        }
//    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(android.R.id.text1);
        }
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, price,description;
        Button btnAddToCart;
        ImageView image;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            price = itemView.findViewById(R.id.tvPrice);
            description = itemView.findViewById(R.id.tvDescription);
            image = itemView.findViewById(R.id.itemImage);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
