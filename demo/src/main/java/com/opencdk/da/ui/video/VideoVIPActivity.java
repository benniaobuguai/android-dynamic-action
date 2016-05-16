package com.opencdk.da.ui.video;

import android.os.Bundle;

import com.opencdk.da.R;
import com.opencdk.da.ui.BaseActivity;
import com.opencdk.dynamicaction.annotation.DAActivity;

@DAActivity(desc = "视频VIP开通界面", version = "1.0.0")
public class VideoVIPActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_video_vip_main);

		setTitle(R.string.vip_video);
	}

}
