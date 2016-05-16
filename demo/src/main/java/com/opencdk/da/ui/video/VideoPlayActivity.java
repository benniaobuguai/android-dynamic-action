package com.opencdk.da.ui.video;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.opencdk.da.R;
import com.opencdk.da.biz.video.Video;
import com.opencdk.da.ui.BaseActivity;
import com.opencdk.dynamicaction.DA;
import com.opencdk.dynamicaction.annotation.DAActivity;

/**
 * Demo for play video
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2016-4-5
 * @Modify 2016-4-5
 */
@DAActivity(desc = "视频播放主界面", version = "1.0.0")
public class VideoPlayActivity extends BaseActivity {

	private TextView mTvVideoName;
	private TextView mTvVideoType;
	private Button mBtnPlayVideo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_video_main);
		setTitle(R.string.video_main_ui);

		mTvVideoName = $(R.id.tv_video_name);
		mTvVideoType = $(R.id.tv_video_type);
		mBtnPlayVideo = $(R.id.btn_play_video);

		String data = DA.getActionData(getIntent());
		final Video video = JSON.parseObject(data, Video.class);
		if (video != null) {
			mTvVideoName.setText(video.mName);

			String categoryName = video.getCategoryName(this);
			if (categoryName == null) {
				categoryName = "";
			}
			String scoreString = "";
			if (video.mScore >= 0 && video.mScore <= 10) {
				scoreString = video.mScore + "";
			}
			String videoType = getString(R.string.video_name_ext, scoreString, categoryName);
			mTvVideoType.setText(videoType);
		}

		mBtnPlayVideo.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

			}

		});
	}

}
