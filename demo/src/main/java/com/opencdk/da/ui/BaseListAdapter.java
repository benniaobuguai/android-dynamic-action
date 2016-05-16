package com.opencdk.da.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseListAdapter<T> extends BaseAdapter {

	private LayoutInflater mLayoutInflater;
	private List<T> mList;

	public abstract View getView(int position, View convertView, ViewGroup parent, LayoutInflater inflater);

	public BaseListAdapter(Context context, List<T> list) {
		this.mLayoutInflater = LayoutInflater.from(context);
		if (list == null) {
			list = new ArrayList<T>();
		}

		this.mList = list;
	}

	public void notifyDataSetChanged(List<T> list) {
		this.mList = list;
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return this.mList.size();
	}

	@Override
	public T getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent, mLayoutInflater);
	}

}