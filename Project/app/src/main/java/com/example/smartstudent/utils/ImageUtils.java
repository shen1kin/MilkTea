package com.example.smartstudent.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

    private static final String TAG = "ImageUtils";

    /**
     * 从本地文件路径加载Bitmap图片
     * @param imagePath 图片的本地路径
     * @return Bitmap对象，失败返回null
     */
    public static Bitmap loadImageFromPath(String imagePath) {
        if (imagePath == null || !new File(imagePath).exists()) {
            return null;
        }
        return BitmapFactory.decodeFile(imagePath);
    }

    /**
     * 将Bitmap保存到应用缓存目录，文件名自动生成
     * @param context 上下文
     * @param bitmap 要保存的Bitmap
     * @return 文件绝对路径，失败返回null
     */
    public static String saveImage(Context context, Bitmap bitmap) {
        if (context == null || bitmap == null) return null;

        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        if (cacheDir == null) {
            Log.e(TAG, "缓存目录不可用");
            return null;
        }

        // 可选：清理旧文件
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }

        String filename = "IMG_" + System.currentTimeMillis() + ".png";
        File newFile = new File(cacheDir, filename);

        try (FileOutputStream out = new FileOutputStream(newFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            return newFile.getAbsolutePath();
        } catch (IOException e) {
            Log.e(TAG, "保存图片失败", e);
            return null;
        }
    }

    /**
     * 将Base64字符串解码成Bitmap对象
     * @param base64Data Base64字符串（可包含Data URL前缀）
     * @return Bitmap对象，失败返回null
     */
    public static Bitmap decodeBase64ToBitmap(String base64Data) {
        if (base64Data == null) return null;

        if (base64Data.contains(",")) {
            base64Data = base64Data.split(",")[1]; // 移除Data URL前缀
        }

        try {
            byte[] decodedBytes = Base64.decode(base64Data, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Base64解码错误", e);
            return null;
        }
    }

    /**
     * 从Base64字符串解码并保存图片到缓存目录，返回图片路径
     * @param context 上下文
     * @param base64Data Base64字符串（可含Data URL前缀）
     * @return 保存的图片绝对路径，失败返回null
     */
    public static String decodeBase64AndSaveImage(Context context, String base64Data) {
        Bitmap bitmap = decodeBase64ToBitmap(base64Data);
        if (bitmap == null) {
            Log.e(TAG, "Base64解码失败，无法保存图片");
            return null;
        }
        String path = saveImage(context, bitmap);
        bitmap.recycle();
        return path;
    }

//      使用说明
//    // 从本地加载图片
//    String savedPath = product.getImage();
//    Bitmap bitmap = ImageUtils.loadImageFromPath(savedPath);
//    if (bitmap != null) {
//        ivImage.setImageBitmap(bitmap);
//    }
//
//    // 从后端Base64字符串获取图片，保存并更新路径
//    String base64 = teaObj.getString("image");
//    String imagePath = ImageUtils.decodeBase64AndSaveImage(getContext(), base64);
//    if (imagePath != null) {
//        product.setImage(imagePath);
//    }
}
