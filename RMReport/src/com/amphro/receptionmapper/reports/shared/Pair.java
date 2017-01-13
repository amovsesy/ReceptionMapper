package com.amphro.receptionmapper.reports.shared;

import java.io.Serializable;

public class Pair<T, V> implements Serializable
{
	private static final long serialVersionUID = 1L;
	T left;
	V right;
	
	public Pair() {
		left = null;
		right = null;
	}
	
	public Pair(T left, V right) {
		this.left = left;
		this.right = right;
	}

	public T getLeft() {
		return left;
	}

	public void setLeft(T left) {
		this.left = left;
	}

	public V getRight() {
		return right;
	}

	public void setRight(V right) {
		this.right = right;
	}
	
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		return o != null 
			&& o.getClass() == this.getClass()
			&& (((Pair) o).getLeft().equals(left))
			&& (((Pair) o).getRight().equals(right));
	}
	
	public int hashCode() {
		int hc = 1;
		if (left != null)
			hc = left.hashCode();
		if(right != null)
			hc *= right.hashCode();
		return hc;
	}
}
