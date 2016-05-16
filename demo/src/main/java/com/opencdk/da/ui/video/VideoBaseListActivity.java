package com.opencdk.da.ui.video;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.opencdk.da.R;
import com.opencdk.da.biz.DataProvider;
import com.opencdk.da.biz.video.Video;
import com.opencdk.da.ui.BaseActivity;
import com.opencdk.da.ui.BaseListAdapter;
import com.opencdk.da.util.Utils;
import com.opencdk.dynamicaction.DA;

public abstract class VideoBaseListActivity extends BaseActivity {

	private ListView mListView;

	public abstract int obtainVideoCategory();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_common_list);
		setTitle(R.string.video_recommend);

		mListView = $(R.id.listView);

		List<Video> videoList = DataProvider.getVideo(obtainVideoCategory());
		BaseListAdapter<Video> adapter = new BaseListAdapter<Video>(this, videoList) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.layout_video_list_item, parent, false);
				}

				TextView tvName = Utils.getView(convertView, R.id.tv_name);
				TextView tvScore = Utils.getView(convertView, R.id.tv_score);
				ImageView ivCategory = Utils.getView(convertView, R.id.iv_category);

				Video video = getItem(position);
				tvName.setText(video.mName);
				tvScore.setText(video.mScore + "");

				if (Video.CATEGORY_FREE == video.mCategory) {
					ivCategory.setImageResource(R.drawable.video_category_free);
				} else if (Video.CATEGORY_VIP == video.mCategory) {
					ivCategory.setImageResource(R.drawable.video_category_vip);
				}

				return convertView;
			}
		};
		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Video video = (Video) parent.getItemAtPosition(position);
				new DA.Builder(VideoBaseListActivity.this)
					.setPackageId("2")
					.setActionName("VideoPlay")
					.setData(video.toJSONString())
					.go();
			}
		});
	}

}
