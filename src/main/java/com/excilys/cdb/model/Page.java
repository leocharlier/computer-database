package com.excilys.cdb.model;

import java.util.List;

public class Page<T> {
  private List<T> data;
  private int start;
  private int size;
  private static final int NB_ELEMENTS = 20;

  public Page(List<T> pdata) {
    super();
    this.data = pdata;
    this.start = 0;
    this.size = data.size();
  }

  public List<T> getData() {
    return data;
  }
  
  public void setData(List<T> data) {
    this.data = data;
  }
  
  public int getSize() {
    return size;
  }
  
  public void setSize(int size) {
    this.size = size;
  }

  public void next() {
    this.start = Math.min(this.start + NB_ELEMENTS, this.size - 1);
  }

  public void previous() {
    this.start = Math.max(this.start - NB_ELEMENTS,  0);
  }
  
  public List<T> getPageData(int page) {
	  this.start = page * NB_ELEMENTS;
	  return this.data.subList(this.start, Math.min(this.start + NB_ELEMENTS, this.size));
  }
  
  public int getMaxPages() {
	  return this.size / NB_ELEMENTS + 1;
  }

  public List<T> getPageData() {
    return this.data.subList(this.start, Math.min(this.start + NB_ELEMENTS, this.size));
  }
}
