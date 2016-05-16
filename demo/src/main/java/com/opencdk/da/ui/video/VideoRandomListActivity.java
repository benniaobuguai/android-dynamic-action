package com.opencdk.da.ui.video;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;

import com.opencdk.da.R;
import com.opencdk.da.biz.video.Video;
import com.opencdk.dynamicaction.DA;
import com.opencdk.dynamicaction.annotation.DAActivity;

@DAActivity(desc = "视频随机推荐列表", version = "1.0.0")
public class VideoRandomListActivity extends VideoBaseListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String title = DA.getActionTitle(intent);
		if (TextUtils.isEmpty(title)) {
			setTitle(R.string.random_video);
		} else {
			setTitle(title);
		}
	}

	@Override
	public int obtainVideoCategory() {
		return Video.CATEGORY_RANDOM;
	}

	// 添加一个更多的按钮, 用于菜单推荐, 如本身推荐小说, 可修改成推荐音乐

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 1, 0, "More");
		return super.onCreateOptionsMenu(menu);
	}

}
