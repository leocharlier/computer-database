package com.excilys.model;

import java.util.List;

public class Page<T> {
	private List<T> data;
	private int start;
	private int size;
	private final int NB_ELEMENTS = 10;
	
	public Page(List<T> data){
		super();
		this.data = data;
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
		this.start = Math.min( this.start + NB_ELEMENTS, this.size - 1 );
	}
	
	public void previous() {
		this.start = Math.max( this.start - NB_ELEMENTS,  0);
	}
	
	public List<T> getPageData(){
		return this.data.subList( this.start, Math.min( this.start + NB_ELEMENTS, this.size) );
	}
	
}
