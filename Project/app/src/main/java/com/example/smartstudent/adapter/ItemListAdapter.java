package com.example.smartstudent.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.R;
import com.example.smartstudent.model.ItemInfo;
import com.example.smartstudent.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListHolder> {
    private List<ItemInfo> item_info = new ArrayList<>();
    private OnItemClickListener itemClickListener;

    // 点击事件回调接口
    public interface OnItemClickListener {
        void onItemClick(ItemInfo item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public ItemListAdapter(List<ItemInfo> list) {
        this.item_info = list;
    }

    @NonNull
    @Override
    public ItemListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent, false);
        return new ItemListHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemListHolder holder, int position) {
        //绑定数据
        ItemInfo itemInfo = item_info.get(position);

        //设置数据
        holder.tvItemName.setText(itemInfo.getItem_name());
        holder.tvItemId.setText(String.valueOf(itemInfo.getItem_id()));
        holder.tvItemState.setText(itemInfo.getItem_state());
        holder.tvItemPrice.setText(itemInfo.getItem_price());
        // 从本地加载图片
        String savedPath = itemInfo.getImageWay();
        Bitmap bitmap = ImageUtils.loadImageFromPath(savedPath);
        if (bitmap != null) {
            holder.image.setImageBitmap(bitmap);
        }


        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(itemInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return item_info.size();
    }

    static class ItemListHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView tvItemId;
        TextView tvItemState;
        TextView tvItemNum;
        TextView tvItemPrice;
        ImageView image;
        public ItemListHolder(@NonNull View itemView) {
            super(itemView);

            //初始化控件
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemId = itemView.findViewById(R.id.tvItemId);
            tvItemState = itemView.findViewById(R.id.tvItemState);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            image = itemView.findViewById(R.id.image);


        }
    }
}
