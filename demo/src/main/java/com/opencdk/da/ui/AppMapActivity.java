package com.opencdk.da.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.opencdk.da.R;
import com.opencdk.da.util.Utils;
import com.opencdk.dynamicaction.DA;
import com.opencdk.dynamicaction.DAConfig;
import com.opencdk.dynamicaction.DALoader;
import com.opencdk.dynamicaction.DAPackage;
import com.opencdk.dynamicaction.annotation.DAActivity;
import com.opencdk.utils.Log;

/**
 * App地图
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2016-4-7
 * @Modify 2016-4-7
 */
@DAActivity(desc = "站点地图", version = "1.0.0")
public class AppMapActivity extends BaseActivity {

	private static final String TAG = "AppMapActivity";

	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_map);
		setContentView(R.layout.layout_common_list);

		mListView = $(R.id.listView);
		
		DAConfig daConfig = DALoader.getDAConfig();
		HashMap<String, DAPackage> hashMap = daConfig.getPackageMap();
		Collection<DAPackage> collections = hashMap.values();
		Iterator<DAPackage> iterators = collections.iterator();

		DAPackage daPackage = null;
		String packageId = null;
		String packageName = null;

		HashMap<String, String> tHashMap = new HashMap<String, String>();
		Package pkg = null;
		while (iterators.hasNext()) {
			daPackage = iterators.next();

			packageId = daPackage.getPackageId();
			packageName = daPackage.getPackageName();

			pkg = Package.getPackage(packageName);

			tHashMap.put(packageName, packageId);
		}

		ActivityInfo[] activities = null;
		try {
			activities = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES).activities;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		if (activities == null) {
			Log.E(TAG, "Not found activity in package [" + getPackageName() + "]");
			return;
		}

		List<AppMap> appMapList = new ArrayList<AppMap>();
		AppMap appMap = null;
		for (int i = 0; i < activities.length; i++) {
			appMap = new AppMap();

			String activityName = activities[i].name;
			String tempPkg = activities[i].packageName;

			int lastIndex1 = activityName.lastIndexOf(".");
			int lastIndex2 = activityName.lastIndexOf("Activity");

			String actionName = activityName.substring(lastIndex1 + 1, lastIndex2);
			String packageString = activityName.substring(0, lastIndex1);

			if (tHashMap.containsKey(packageString)) {
				appMap.uriString = DALoader.getScheme() + "://"
						+ DALoader.makeDAKey(tHashMap.get(packageString), actionName);
			}

			try {
				appMap.daActivity = Class.forName(activityName).getAnnotation(DAActivity.class);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			appMapList.add(appMap);
		}
		

//		PathClassLoader classLoader = (PathClassLoader) Thread.currentThread().getContextClassLoader();
//
//		List<Class<?>> classList = new ArrayList<Class<?>>();
//		try {
//			Field dexField = PathClassLoader.class.getDeclaredField("mDexs");
//			dexField.setAccessible(true);
//			DexFile[] dexs = (DexFile[]) dexField.get(classLoader);
//			for (DexFile dex : dexs) {
//				Enumeration<String> entries = dex.entries();
//				while (entries.hasMoreElements()) {
//					String entry = entries.nextElement();
//
//					Class<?> entryClass = dex.loadClass(entry, classLoader);
//
//					if (entryClass != null) {
//						Type type = entryClass.getGenericSuperclass();
//						
//						if (tHashMap.containsKey(entryClass.getPackage().getName())) {
//							classList.add(entryClass);
//						}
//					}
//					
//					if (entryClass != null) {
////					IBizAnno annotation = entryClass.getAnnotation(IBizAnno.class);
////					if (annotation != null) {
////						System.out.println("name=" + annotation.name() + "; class=" + entryClass.getName());
////					}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		BaseListAdapter<AppMap> adapter = new BaseListAdapter<AppMap>(this, appMapList) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.layout_app_map_list_item, parent, false);
				}

				TextView tvDAPath = Utils.getView(convertView, R.id.tv_da_path);
				TextView tvDesc = Utils.getView(convertView, R.id.tv_activity_desc);

				AppMap appMap = getItem(position);
				tvDAPath.setText(appMap.uriString + "");
				if (appMap.daActivity != null && appMap.daActivity.desc() != null) {
					tvDesc.setText(appMap.daActivity.desc());
				}

				return convertView;
			}
		};

		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AppMap appMap = (AppMap) parent.getItemAtPosition(position);
				new DA.Builder(AppMapActivity.this)
					.setUriString(appMap.getUriString())
					.go();
			}
		});
	}
	
	
	public static class AppMap {

		private String uriString;

		private DAActivity daActivity;

		public String getUriString() {
			return uriString;
		}

		public void setUriString(String uriString) {
			this.uriString = uriString;
		}

		public DAActivity getDaActivity() {
			return daActivity;
		}

		public void setDaActivity(DAActivity daActivity) {
			this.daActivity = daActivity;
		}

	}

}
