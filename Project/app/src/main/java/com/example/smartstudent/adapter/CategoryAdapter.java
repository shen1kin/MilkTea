package com.example.smartstudent.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.R;
import com.example.smartstudent.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private OnItemClickListener listener;

    // 定义点击回调接口
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public CategoryAdapter(List<Category> categoryList, OnItemClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载 item_category.xml 布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategory.setText(category.name);

        // 根据是否选中设置文本颜色与字体样式
        if (category.isSelected) {
            holder.tvCategory.setTextColor(0xFF000000); // 黑色
            holder.tvCategory.setTypeface(null, Typeface.BOLD); // 加粗
            holder.rightLine.setBackgroundColor(0xFF000000); // 右侧竖线变黑色
        } else {
            holder.tvCategory.setTextColor(0xFF888888); // 灰色
            holder.tvCategory.setTypeface(null, Typeface.NORMAL); // 正常字体
            holder.rightLine.setBackgroundColor(0xFFCCCCCC); // 右侧竖线为浅灰色
        }

        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    // 内部 ViewHolder 类，持有分类项视图
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;
        View rightLine; // 新增：分类项右侧竖线视图

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            rightLine = itemView.findViewById(R.id.viewRightLine); // 绑定右侧竖线控件
        }
    }
}
