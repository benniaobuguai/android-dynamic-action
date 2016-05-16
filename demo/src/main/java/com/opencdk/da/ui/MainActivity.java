package com.opencdk.da.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.opencdk.da.R;
import com.opencdk.da.biz.ConfigFileInfo;
import com.opencdk.da.biz.DataProvider;
import com.opencdk.dynamicaction.DA;
import com.opencdk.dynamicaction.DALoader;
import com.opencdk.dynamicaction.DALoaderConfig;
import com.opencdk.dynamicaction.annotation.DAActivity;

@DAActivity(desc = "应用主页", version = "1.0.0")
public class MainActivity extends BaseActivity {

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayUseLogoEnabled(false);
			actionBar.setDisplayShowHomeEnabled(true);

			actionBar.setDisplayHomeAsUpEnabled(false);
		}

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}

		handleIntent();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		try {
			handleIntent();
		} catch (Exception e) {
			Log.e(TAG, "", e);
		}
	}

	private void handleIntent() {
		final Intent intent = getIntent();
		if (intent != null) {
			String scheme = intent.getScheme();
			if (scheme != null && scheme.equalsIgnoreCase(DALoader.getAppScheme())) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						String uriString = intent.getData().toString();
						new DA.Builder(MainActivity.this)
							.setUriString(uriString)
							.go();
					}
				}, 300);
			}
		}
	}

	private long mExitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "Press back twice to exit.", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

				return true;
			} else {
				finish();

				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Official website
			new DA.Builder(this)
				.setHost("0$Browser")
				.setUrl("http://www.opencdk.com/")
				.go();
			
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public static class PlaceholderFragment extends Fragment implements OnClickListener {

		private Button btnVideoFree;
		private Button btnVideoVIP;
		private Button btnVideoRandom;
		private Button btnUserDetail;

		private Button btnLogin;
		private Button btnHtml5;
		private Button btnRequestCode;

		private Spinner spConfig;

		public PlaceholderFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.layout_main_fm, container, false);

			btnVideoFree = (Button) rootView.findViewById(R.id.btn_video_free);
			btnVideoVIP = (Button) rootView.findViewById(R.id.btn_video_vip);
			btnVideoRandom = (Button) rootView.findViewById(R.id.btn_video_random);
			btnUserDetail = (Button) rootView.findViewById(R.id.btn_user_detail);
			
			
			btnLogin = (Button) rootView.findViewById(R.id.btn_app_site);
			btnHtml5 = (Button) rootView.findViewById(R.id.btn_html5);
			btnRequestCode = (Button) rootView.findViewById(R.id.btn_request_code);
			spConfig = (Spinner) rootView.findViewById(R.id.sp_da_config);

			SpinnerAdapter adapter = new ConfigArrayAdapter(getActivity(), R.layout.layout_main_fm_spinner_item,
					DataProvider.getConfigFileInfo());
			spConfig.setAdapter(adapter);
			spConfig.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					DALoaderConfig config = new DALoaderConfig(getActivity().getApplicationContext());

					ConfigFileInfo configFile = (ConfigFileInfo) parent.getItemAtPosition(position);
					config.setCfgFileName(configFile.mFileName);
					config.setCfgDataType(configFile.mCfgDataType);
					DALoader.getInstance().setUp(config);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			btnVideoFree.setOnClickListener(this);
			btnVideoVIP.setOnClickListener(this);
			btnVideoRandom.setOnClickListener(this);
			btnUserDetail.setOnClickListener(this);
			btnLogin.setOnClickListener(this);
			btnHtml5.setOnClickListener(this);
			btnRequestCode.setOnClickListener(this);

			return rootView;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_video_free:
				onVideoFreeClick();
				break;
			case R.id.btn_video_vip:
				onVideoVIPClick();
				break;
			case R.id.btn_video_random:
				onVideoRandomClick();
				break;
			case R.id.btn_user_detail:
				onUserDetailClick();
				break;
			case R.id.btn_app_site:
				onAppSiteClick();
				break;
			case R.id.btn_html5:
				onHtml5Click();
				break;
			case R.id.btn_request_code:
				onStartActivityForResult();
				break;
			}
		}

		private void onVideoFreeClick() {
			new DA.Builder(getActivity())
				.setPackageId("2")
				.setActionName("VideoFreeList")
				.setFrom("home_video_free_click")
				.go();
		}

		private void onVideoVIPClick() {
			new DA.Builder(getActivity())
				.setPackageId("2")
				.setActionName("VideoVIPList")
				.setFrom("home_video_vip_click")
				.setData("{\"id\":\"10001\", \"videoName\":\"火影\" }")
				.go();
		}
		
		private void onVideoRandomClick() {
			new DA.Builder(getActivity())
				.setPackageId("2")
				.setActionName("VideoRandomList")
				.setFrom("home_video_random_click")
				.setData("{\"id\":\"10001\", \"videoName\":\"火影\" }")
				.go();
		}
		
		private void onUserDetailClick() {
			new DA.Builder(getActivity())
				.setHost("1$UserDetail")
				.setFrom("home_user_detail_click")
				.go();
		}

		private void onAppSiteClick() {
			new DA.Builder(getActivity())
				.setPackageId("0")
				.setActionName("AppMap")
				.setFrom("home_app_site_click")
				.go();
		}
		
		/**
		 * 点击进入浏览器
		 */
		private void onHtml5Click() {
//			new DA.Builder(getActivity())
//				.setHost("0$Browser")
//				.setUrl("http://www.opencdk.com/")
//				.go();
			
			new DA.Builder(getActivity())
				//.setUriString("opencdk://0$Browser?url=http://www.opencdk.com/")
				.setUriString("opencdk://0$Browser?url=file:///android_asset/sample.html")
				.go();
			
		}
		
		private void onStartActivityForResult() {
			new DA.Builder(getActivity())
				.setHost("1$Login")
				.setRequestCode(REQUEST_CODE_LOGIN)
				.go();
		}

	}
	
	private static final int REQUEST_CODE_LOGIN = 2000;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CODE_LOGIN) {
				Toast.makeText(this, "ActivityResult: " + requestCode, Toast.LENGTH_SHORT).show();
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
