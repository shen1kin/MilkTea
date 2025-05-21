package com.example.smartstudent;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.VipServiceAdapter;
import com.example.smartstudent.model.User;
import com.example.smartstudent.model.VipserveItem;
import com.google.gson.Gson;
import com.jinrishici.sdk.android.JinrishiciClient;
import com.jinrishici.sdk.android.listener.JinrishiciCallback;
import com.jinrishici.sdk.android.model.JinrishiciRuntimeException;
import com.jinrishici.sdk.android.model.PoetySentence;

import java.util.ArrayList;
import java.util.List;

public class Fragment_student_profile extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_profile,container,false);

        // 准备数据
        List<VipserveItem> services = new ArrayList<>();
        services.add(new VipserveItem("会员码", R.drawable.vip_code, hello.class)); //接到功能未实现弹窗dialog_feature_not_ready.xml
        services.add(new VipserveItem("用户反馈", R.drawable.feedback, hello.class)); //接到功能未实现弹窗dialog_feature_not_ready.xml
        services.add(new VipserveItem("发票助手", R.drawable.receipt, hello.class)); //接到功能未实现弹窗dialog_feature_not_ready.xml
        services.add(new VipserveItem("联系客服", R.drawable.service, hello.class));
        services.add(new VipserveItem("设置", R.drawable.set, Activity_student_set.class));
        services.add(new VipserveItem("退出登录", R.drawable.tuichu, null));

        // 设置RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.menu_recycler);
        //显示列数
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        //适配器实现 参数是 界面 数据
        recyclerView.setAdapter(new VipServiceAdapter(requireContext(), services));



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //获取名字

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", null);

        User user = null;
        if (userJson != null) {
            Gson gson = new Gson();
            user = gson.fromJson(userJson, User.class);
        }

        TextView tvName = view.findViewById(R.id.tvName);
        tvName.setText(user.getAccount());



        //诗词展示
        TextView tvPoem = view.findViewById(R.id.tvPoem);
        TextView tvAuthor = view.findViewById(R.id.tvAuthor);

        JinrishiciClient.getInstance().init(getContext());
        JinrishiciClient client = JinrishiciClient.getInstance();
        client.getOneSentenceBackground(new JinrishiciCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void done(PoetySentence poetySentence) {
                // 设置诗句内容
                tvPoem.setText(poetySentence.getData().getContent());

                // 设置作者信息
                String author = poetySentence.getData().getOrigin().getAuthor();
                tvAuthor.setText("— " + author);
            }

            @Override
            public void error(JinrishiciRuntimeException e) {
                Log.w(TAG, "error: code = " + e.getCode() + " message = " + e.getMessage());
                //TODO do something else
                //手动设置
            }
        });


    }

}
