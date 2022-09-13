package com.hualu.zlib.response;

public class RestResultVo<T> {

  private int status;

  private String msg;

  private T data;

  public static final int SUCCESS = 200;
  public static final int ERROR = 500;

  public static RestResultVo<String> success() {
    RestResultVo<String> result = new RestResultVo<>();
    result.set(SUCCESS, "操作成功");
    return result;
  }

  public static RestResultVo<String> error() {
    RestResultVo<String> result = new RestResultVo<>();
    result.set(ERROR, "操作失败");
    return result;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void set(int status, String msg) {
    this.status = status;
    this.msg = msg;
  }

  public void set(int status, String msg, T data) {
    this.status = status;
    this.msg = msg;
    this.data = data;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
