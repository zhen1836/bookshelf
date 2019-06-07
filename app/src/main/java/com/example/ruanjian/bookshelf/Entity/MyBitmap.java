package com.example.ruanjian.bookshelf.Entity;



import java.io.Serializable;

/**
 * MyBitmap是要被序列化的类
 * 其中包含了通过BytesBitmap类得到的Bitmap中数据的数组
 * 和一个保存位图的名字的字符串，用于标识图片
 *
 */
public class MyBitmap implements Serializable {

    private static final long serialVersionUID = 1L;
    private byte[] bitmapBytes = null;


    public MyBitmap(byte[] bitmapBytes) {
        // TODO Auto-generated constructor stub
        this.bitmapBytes = bitmapBytes;

    }

    public byte[] getBitmapBytes() {
        return this.bitmapBytes;
    }
}