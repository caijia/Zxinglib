package com.hualu.zlib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static android.graphics.Bitmap.CompressFormat;
import static android.graphics.Bitmap.Config;
import static android.graphics.BitmapFactory.Options;
import static android.graphics.BitmapFactory.decodeFile;

/** 图片压缩工具 Created by cai.jia on 2017/12/30. */
public class ImageCompressor {

  private static volatile ImageCompressor instance;

  private ImageCompressor() {}

  public static ImageCompressor getInstance() {
    if (instance == null) {
      synchronized (ImageCompressor.class) {
        if (instance == null) {
          instance = new ImageCompressor();
        }
      }
    }
    return instance;
  }

  public Bitmap compressBitmap(String filePath, Config config, int targetWidth, int targetHeight) {
    Options options = new Options();
    options.inJustDecodeBounds = true;
    decodeFile(filePath, options);
    int bitmapWidth = options.outWidth;
    int bitmapHeight = options.outHeight;

    int inSampleSize = computeSampleSize(bitmapWidth, bitmapHeight, targetWidth, targetHeight);
    options.inJustDecodeBounds = false;
    options.inSampleSize = inSampleSize;
    options.inPreferredConfig = config;
    return decodeFile(filePath, options);
  }

  private int computeSampleSize(
      int imageWidth, int imageHeight, int targetWidth, int targetHeight) {
    int sampleSize = 1;
    while (imageWidth / sampleSize > targetWidth || imageHeight / sampleSize > targetHeight) {
      sampleSize = sampleSize << 1;
    }
    return sampleSize;
  }

  public void saveBitmapToFile(Context context, Bitmap bitmap, File file, int quality) {
    if (bitmap == null || file == null) {
      return;
    }

    BufferedOutputStream bos = null;
    try {
      bos = new BufferedOutputStream(new FileOutputStream(file));
      bitmap.compress(CompressFormat.JPEG, quality, bos);

    } catch (FileNotFoundException e) {
      e.printStackTrace();

    } finally {
      try {
        if (bos != null) {
          bos.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
