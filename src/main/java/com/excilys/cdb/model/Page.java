package com.excilys.cdb.model;

import java.util.List;

public class Page<T> {
  private List<T> data;
  private int start;
  private int size;
  private int nbElements;

  public Page(List<T> pdata) {
    super();
    this.data = pdata;
    this.start = 0;
    this.size = data.size();
    this.nbElements = 10;
  }
  
  public Page(List<T> pdata, int nbElmt) {
    super();
    this.data = pdata;
    this.start = 0;
    this.size = data.size();
    this.nbElements = nbElmt;
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
    this.start = Math.min(this.start + this.nbElements, this.size - 1);
  }

  public void previous() {
    this.start = Math.max(this.start - this.nbElements,  0);
  }
  
  public List<T> getPageData(int page) {
	  this.start = page * this.nbElements;
	  return this.data.subList(this.start, Math.min(this.start + this.nbElements, this.size));
  }
  
  public int getMaxPages() {
	  return this.size / this.nbElements + 1;
  }

  public List<T> getPageData() {
    return this.data.subList(this.start, Math.min(this.start + this.nbElements, this.size));
  }
}
