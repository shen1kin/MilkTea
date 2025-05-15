package com.example.smartstudent.model;

public class OrderModeManager {

    public static final int PICKUP = 0;    // 自取
    public static final int DELIVERY = 1;  // 喜外送

    private static int currentMode = PICKUP;

    public static void setMode(int mode) {
        currentMode = mode;
    }

    public static int getCurrentMode() {
        return currentMode;
    }

    public static boolean isPickup() {
        return currentMode == PICKUP;
    }

    public static void setCurrentMode(int mode) {
        currentMode = mode;
    }



    public static boolean isDelivery() {
        return currentMode == DELIVERY;
    }
}
