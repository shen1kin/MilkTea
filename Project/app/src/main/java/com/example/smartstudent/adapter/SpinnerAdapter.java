package com.example.smartstudent.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.TextView;

import com.example.smartstudent.R;

import java.util.List;


public class SpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<String> datas;

    public SpinnerAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null)
        {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.spinner_item_search, viewGroup, false);
            holder.titleTxt = view.findViewById(R.id.tvSpinnerItem);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }

        holder.titleTxt.setText(datas.get(i));
        return view;
    }

    class ViewHolder {
        TextView titleTxt;
    }
}
