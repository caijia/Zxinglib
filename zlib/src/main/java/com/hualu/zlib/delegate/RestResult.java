package com.hualu.zlib.delegate;

/**
 * <p>
 * RestResult
 * </p>
 *
 * @author cai.jia
 * @since 2022-09-12 14:52
 */
public class RestResult {

  public String getFileId() {
    if (message != null) {
      return message.getId();
    }
    return null;
  }

  private Message message;

  public Message getMessage() {
    return message;
  }

  public void setMessage(Message message) {
    this.message = message;
  }

  public static class Message {
    private String filename;
    private String id;

    public String getFilename() {
      return filename;
    }

    public void setFilename(String filename) {
      this.filename = filename;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }
  }
}
