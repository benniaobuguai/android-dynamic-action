package com.opencdk.da.ui.video;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.opencdk.da.R;
import com.opencdk.da.biz.video.Video;
import com.opencdk.dynamicaction.DA;
import com.opencdk.dynamicaction.annotation.DAActivity;

@DAActivity(desc = "VIP视频列表", version = "1.0.0")
public class VideoVIPListActivity extends VideoBaseListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String title = DA.getActionTitle(intent);
		if (TextUtils.isEmpty(title)) {
			setTitle(R.string.vip_video);
		} else {
			setTitle(title);
		}
	}

	@Override
	public int obtainVideoCategory() {
		return Video.CATEGORY_VIP;
	}

}
