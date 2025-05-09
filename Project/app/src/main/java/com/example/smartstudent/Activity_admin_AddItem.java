package com.example.smartstudent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

public class Activity_admin_AddItem extends AppCompatActivity {
    private LinearLayout statusContainer;
    private Button btnAddStatus;

    private List<String> allOptions = Arrays.asList("选项1", "选项2", "选项3");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_item);
        //获取主页面 状态界面ID
        statusContainer = findViewById(R.id.statusContainer);
        btnAddStatus = findViewById(R.id.btnAddStatus);
        // 点击“添加状态”按钮，动态添加一个状态模块
        btnAddStatus.setOnClickListener(v -> {
            addStatusModule();
        });
    }

    @SuppressLint("SetTextI18n")
    public void addStatusModule() {
        LayoutInflater inflater = LayoutInflater.from(this);
        // 将 status_module.xml 实例化为 View 对象
        // 将布局 XML 文件实例化为其对应的View 对象。它不会直接使用。
        // 而是使用 Activity.getLayoutInflater()或 Context.getSystemService来获取一个已连接到当前上下文并针对您正在运行的设备正确配置的标准 LayoutInflater 实例。
        View moduleView = inflater.inflate(R.layout.item_project_state, statusContainer, false);

        // 获取子控件
        EditText etTitle = moduleView.findViewById(R.id.etTitle);
        Button btnAddOpt = moduleView.findViewById(R.id.btnAddOpt);
        // ChipGroup 是 Android 提供的一个用于显示多个 Chip（标签/标签按钮）组件的容器，
        // 它是 ViewGroup 的子类，通常和 com.google.android.material.chip.Chip 搭配使用。
        ChipGroup chipGroup = moduleView.findViewById(R.id.chipGroupOptions);

        // 设置标题 获取编号
        //etTitle.setText("状态");

        // 点击“添加选项”按钮时，弹出对话框选择选项
        btnAddOpt.setOnClickListener(v -> {
            showAddOptionDialog(chipGroup);
        });

        // 将新模块添加到容器中，添加到主界面中
        statusContainer.addView(moduleView);
    }

    // 弹出选项列表对话框，将所选选项添加到给定的 ChipGroup 中
    private void showAddOptionDialog(ChipGroup targetChipGroup) {
        // 使用 BottomSheetDialog 也可以，下面示例使用 AlertDialog 结合自定义布局
        //创建弹窗AlertDialog构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //加载 实例化 弹窗布局

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.activity_admin_add_select, null);
        dialog.setContentView(dialogView);

        // 确保获取正确的父容器
        ViewGroup parent = (ViewGroup) dialogView.getParent();

        // 使用正确的Behavior获取方式
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);

        // 设置高度参数
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        behavior.setPeekHeight((int)(screenHeight * 0.5));
        behavior.setMaxHeight((int)(screenHeight * 0.8)); // 设置最大高度
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // 确保窗口参数生效
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


        //获取弹窗内组件
        RecyclerView rvOptions = dialogView.findViewById(R.id.recyclerViewOptions);
        //设置布局为垂直线性排列。
        rvOptions.setLayoutManager(new LinearLayoutManager(this));
        // 创建列表适配器，传入所有选项数据和目标 ChipGroup
        //创建并设置一个自定义的 RecyclerView.Adapter（OptionAdapter）。
        //allOptions 是所有可供选择的选项（可能是一个 List<String>）。  后面从服务器获取
        //targetChipGroup 是要将选项添加到的 ChipGroup。
        //每当用户点击某个选项时，adapter 内部会创建一个 Chip 并添加到该 ChipGroup。
        OptionAdapter adapter = new OptionAdapter(this, allOptions, targetChipGroup);
        rvOptions.setAdapter(adapter);
        //设置对话框的内容视图为刚刚加载的 dialog_add_option.xml。
        //创建并展示对话框。
        builder.setView(dialogView);
        dialog.show();
    }

}

// 列表适配器，用于在对话框中显示选项列表
class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {
    private Context context;
    private List<String> optionList;
    private ChipGroup chipGroup;  // 目标 ChipGroup

    // 构造函数
    public OptionAdapter(Context context, List<String> optionList, ChipGroup chipGroup) {
        this.context = context;
        this.optionList = optionList;
        this.chipGroup = chipGroup;
    }

    // ViewHolder 包含选项名称和添加按钮
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOptionName;
        Button btnAdd;
        public ViewHolder(View itemView) {
            super(itemView);
            tvOptionName = itemView.findViewById(R.id.textOptionName);
            btnAdd = itemView.findViewById(R.id.btnAddOptionItem);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String option = optionList.get(position);
        holder.tvOptionName.setText(option);
        holder.btnAdd.setOnClickListener(v -> {
            // 创建一个新的 Chip 并设置文本
            Chip chip = new Chip(context);
            chip.setText(option);
            chip.setClickable(false);  // 不可点击，仅显示
            chipGroup.addView(chip);  // 将 Chip 添加到目标 ChipGroup 中:contentReference[oaicite:8]{index=8}:contentReference[oaicite:9]{index=9}
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }
}















//旧版

//package com.example.smartstudent;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.material.chip.Chip;
//import com.google.android.material.chip.ChipGroup;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class Activity_admin_AddItem extends AppCompatActivity {
//    private LinearLayout statusContainer;
//    private Button btnAddStatus;
//
//    private List<String> allOptions = Arrays.asList("选项1", "选项2", "选项3");
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_admin_add_item);
//        //获取主页面 状态界面ID
//        statusContainer = findViewById(R.id.statusContainer);
//        btnAddStatus = findViewById(R.id.btnAddStatus);
//        // 点击“添加状态”按钮，动态添加一个状态模块
//        btnAddStatus.setOnClickListener(v -> {
//            addStatusModule();
//        });
//    }
//
//    @SuppressLint("SetTextI18n")
//    public void addStatusModule() {
//        LayoutInflater inflater = LayoutInflater.from(this);
//        // 将 status_module.xml 实例化为 View 对象
//        // 将布局 XML 文件实例化为其对应的View 对象。它不会直接使用。
//        // 而是使用 Activity.getLayoutInflater()或 Context.getSystemService来获取一个已连接到当前上下文并针对您正在运行的设备正确配置的标准 LayoutInflater 实例。
//        View moduleView = inflater.inflate(R.layout.item_project_state, statusContainer, false);
//
//        // 获取子控件
//        EditText etTitle = moduleView.findViewById(R.id.etTitle);
//        Button btnAddOpt = moduleView.findViewById(R.id.btnAddOpt);
//        // ChipGroup 是 Android 提供的一个用于显示多个 Chip（标签/标签按钮）组件的容器，
//        // 它是 ViewGroup 的子类，通常和 com.google.android.material.chip.Chip 搭配使用。
//        ChipGroup chipGroup = moduleView.findViewById(R.id.chipGroupOptions);
//
//        // 设置标题 获取编号
//        //etTitle.setText("状态");
//
//        // 点击“添加选项”按钮时，弹出对话框选择选项
//        btnAddOpt.setOnClickListener(v -> {
//            showAddOptionDialog(chipGroup);
//        });
//
//        // 将新模块添加到容器中，添加到主界面中
//        statusContainer.addView(moduleView);
//    }
//
//    // 弹出选项列表对话框，将所选选项添加到给定的 ChipGroup 中
//    private void showAddOptionDialog(ChipGroup targetChipGroup) {
//        // 使用 BottomSheetDialog 也可以，下面示例使用 AlertDialog 结合自定义布局
//        //创建弹窗AlertDialog构建器
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        //加载 实例化 弹窗布局
//        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_option, null);
//        //获取弹窗内组件
//        RecyclerView rvOptions = dialogView.findViewById(R.id.recyclerViewOptions);
//        //设置布局为垂直线性排列。
//        rvOptions.setLayoutManager(new LinearLayoutManager(this));
//        // 创建列表适配器，传入所有选项数据和目标 ChipGroup
//        //创建并设置一个自定义的 RecyclerView.Adapter（OptionAdapter）。
//        //allOptions 是所有可供选择的选项（可能是一个 List<String>）。  后面从服务器获取
//        //targetChipGroup 是要将选项添加到的 ChipGroup。
//        //每当用户点击某个选项时，adapter 内部会创建一个 Chip 并添加到该 ChipGroup。
//        OptionAdapter adapter = new OptionAdapter(this, allOptions, targetChipGroup);
//        rvOptions.setAdapter(adapter);
//        //设置对话框的内容视图为刚刚加载的 dialog_add_option.xml。
//        //创建并展示对话框。
//        builder.setView(dialogView);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//}
//
//// 列表适配器，用于在对话框中显示选项列表
//class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {
//    private Context context;
//    private List<String> optionList;
//    private ChipGroup chipGroup;  // 目标 ChipGroup
//
//    // 构造函数
//    public OptionAdapter(Context context, List<String> optionList, ChipGroup chipGroup) {
//        this.context = context;
//        this.optionList = optionList;
//        this.chipGroup = chipGroup;
//    }
//
//    // ViewHolder 包含选项名称和添加按钮
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView tvOptionName;
//        Button btnAdd;
//        public ViewHolder(View itemView) {
//            super(itemView);
//            tvOptionName = itemView.findViewById(R.id.textOptionName);
//            btnAdd = itemView.findViewById(R.id.btnAddOptionItem);
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_option, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        String option = optionList.get(position);
//        holder.tvOptionName.setText(option);
//        holder.btnAdd.setOnClickListener(v -> {
//            // 创建一个新的 Chip 并设置文本
//            Chip chip = new Chip(context);
//            chip.setText(option);
//            chip.setClickable(false);  // 不可点击，仅显示
//            chipGroup.addView(chip);  // 将 Chip 添加到目标 ChipGroup 中:contentReference[oaicite:8]{index=8}:contentReference[oaicite:9]{index=9}
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return optionList.size();
//    }
//}