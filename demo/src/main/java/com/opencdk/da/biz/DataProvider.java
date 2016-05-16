package com.opencdk.da.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.opencdk.da.biz.video.Video;
import com.opencdk.dynamicaction.cfg.CfgDataType;

public class DataProvider {

	public static ConfigFileInfo[] getConfigFileInfo() {
		ConfigFileInfo[] configFileInfos = new ConfigFileInfo[4];

		ConfigFileInfo item0 = new ConfigFileInfo();
		item0.mFileName = "dynamic_action.cfg";
		item0.mDesc = "默认配置";
		item0.mCfgDataType = CfgDataType.XML;
		configFileInfos[0] = item0;

		ConfigFileInfo item1 = new ConfigFileInfo();
		item1.mFileName = "dynamic_action_transfer.cfg";
		item1.mDesc = "动态修复";
		item1.mCfgDataType = CfgDataType.XML;
		configFileInfos[1] = item1;

		ConfigFileInfo item2 = new ConfigFileInfo();
		item2.mFileName = "dynamic_action_interceptor.cfg";
		item2.mDesc = "启用拦截器";
		item2.mCfgDataType = CfgDataType.XML;
		configFileInfos[2] = item2;

		ConfigFileInfo item3 = new ConfigFileInfo();
		item3.mFileName = "dynamic_action_json.cfg";
		item3.mDesc = "JSON配置";
		item3.mCfgDataType = CfgDataType.JSON;
		configFileInfos[3] = item3;

		return configFileInfos;
	}

	public static List<Video> getVideo(int category) {
		if (category < Video.CATEGORY_FREE || category > Video.CATEGORY_RANDOM) {
			return new ArrayList<Video>();
		}

		final int count = 20;
		Video video = null;
		Random rd = new Random();

		List<Video> videos = new ArrayList<Video>();
		for (int i = 0; i < count; i++) {
			video = new Video();
			video.mId = "v_" + i;
			video.mName = "Demo Video " + (i + 1);
			video.mScore = 6 + rd.nextInt(5);

			if (Video.CATEGORY_RANDOM == category) {
				int index = rd.nextInt(Integer.MAX_VALUE);
				video.mCategory = Video.CATEGORYS[index % Video.CATEGORYS.length];
			} else {
				video.mCategory = category;
			}

			videos.add(video);
		}

		return videos;
	}

}
