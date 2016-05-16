package com.opencdk.da.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.opencdk.da.R;
import com.opencdk.da.biz.ConfigFileInfo;

public class ConfigArrayAdapter extends ArrayAdapter<ConfigFileInfo> {

	private int mLayoutId;
	private LayoutInflater mInflater;

	public ConfigArrayAdapter(Context context, int resource, ConfigFileInfo[] objects) {
		super(context, resource, objects);
		this.mLayoutId = resource;
		mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mLayoutId);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mLayoutId);
	}

	private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
		if (convertView == null) {
			convertView = mInflater.inflate(resource, parent, false);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.text);

		ConfigFileInfo configFile = getItem(position);
		textView.setText(configFile.mDesc);
		return convertView;
	}
}
