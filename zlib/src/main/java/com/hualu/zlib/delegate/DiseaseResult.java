package com.hualu.zlib.delegate;

import java.util.List;

public class DiseaseResult {

  private PageVo pageVo;
  private List<Disease> data;

  public PageVo getPageVo() {
    return pageVo;
  }

  public void setPageVo(PageVo pageVo) {
    this.pageVo = pageVo;
  }

  public List<Disease> getData() {
    return data;
  }

  public void setData(List<Disease> data) {
    this.data = data;
  }
}
