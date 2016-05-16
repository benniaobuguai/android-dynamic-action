package com.opencdk.da.biz.video;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.opencdk.da.R;

public class Video {

	public final static int CATEGORY_FREE = 0x1;
	public final static int CATEGORY_VIP = 0x2;
	public final static int CATEGORY_RANDOM = 0x3;

	public final static int[] CATEGORYS = { CATEGORY_FREE, CATEGORY_VIP };

	public String mId;
	public String mName;
	public int mScore;
	public int mCategory = CATEGORY_FREE;

	public String toJSONString() {
		return JSON.toJSON(this).toString();
	}

	public String getCategoryName(Context context) {
		String categoryName = "";
		switch (mCategory) {
		case CATEGORY_FREE:
			categoryName = context.getString(R.string.free_video);
			break;
		case CATEGORY_VIP:
			categoryName = context.getString(R.string.vip_video);
			break;
		case CATEGORY_RANDOM:
			categoryName = context.getString(R.string.random_video);
			break;
		}

		return categoryName;
	}

}
