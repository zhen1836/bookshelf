package com.example.ruanjian.bookshelf.Widget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

/**
 * 因为Bitmap没有实现序列化，所以不能直接在序列化类(MyBitmap)中使用
 * BytesBitmap用于实现Bitmap和byte[]间的相互转换
 *
 */
public class BytesBitmap {
    public static Bitmap getBitmap(byte[] data) {
        return changeBitmapSize(BitmapFactory.decodeByteArray(data, 0, data.length));
    }

    public static byte[] getBytes(Bitmap bitmap) {
        bitmap = changeBitmapSize(bitmap);
        ByteArrayOutputStream baops = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baops);
        return baops.toByteArray();
    }

    public static Bitmap changeBitmapSize(Bitmap bad_bitmap) {
        Bitmap bitmap = bad_bitmap;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //设置想要的大小
        int newWidth=270;
        int newHeight=370;

        //计算压缩的比率
        float scaleWidth=((float)newWidth)/width;
        float scaleHeight=((float)newHeight)/height;

        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);

        //获取新的bitmap
        bitmap=Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        bitmap.getWidth();
        bitmap.getHeight();

        return bitmap;
    }
}
