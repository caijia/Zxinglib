package com.hualu.zlib.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

public class MD5Utils {

  public static String getDigest(TreeMap<String, String> map, String key, String charset) {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry entry : map.entrySet()) {
      sb = sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
    }
    sb.append("key").append("=").append(key);
    String sign = md5(getContentBytes(sb.toString(), charset));
    return sign;
  }

  public static String md5(byte[] srcBytes) {

    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
      byte[] bytes = md5.digest(srcBytes);
      String result = "";
      for (byte b : bytes) {
        String temp = Integer.toHexString(b & 0xff);
        if (temp.length() == 1) {
          temp = "0" + temp;
        }
        result += temp;
      }
      return result;
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }

  private static byte[] getContentBytes(String content, String charset) {
    if (charset == null || "".equals(charset)) {
      return content.getBytes();
    }
    try {
      return content.getBytes(charset);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
    }
  }
}
