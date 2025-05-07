package com.example.smartstudent.model;
public class VipserveItem {
    private String name;
    private int iconRes;
    private Class<?> targetActivity;

    public VipserveItem(String name, int iconRes, Class<?> targetActivity) {
        this.name = name;
        this.iconRes = iconRes;
        this.targetActivity = targetActivity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public Class<?> getTargetActivity() {
        return targetActivity;
    }

    public void setTargetActivity(Class<?> targetActivity) {
        this.targetActivity = targetActivity;
    }
}