package com.opencdk.da.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;

import com.opencdk.da.GApplication;
import com.opencdk.da.R;
import com.opencdk.dynamicaction.DA;
import com.opencdk.dynamicaction.DALoader;
import com.opencdk.dynamicaction.DALoaderConfig;
import com.opencdk.dynamicaction.annotation.DAActivity;
import com.opencdk.dynamicaction.cfg.CfgDataType;

/**
 * Splash Activity
 * 
 * @author Administrator
 * 
 */
@DAActivity(desc = "应用闪屏", version = "1.0.0")
public class SplashActivity extends BaseActivity {

	private static final String TAG = "SplashActivity";

	private HandlerThread mSubThread;
	private Handler mSubHandler;
	private Handler mUiHandler;

	private static final int MSG_SUB_INIT_TASK = 1000;
	private static final int MSG_UI_INIT_FINISH = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_splash);

		mUiHandler = new Handler(this.getMainLooper(), mUiHandlerCallback);
		if (GApplication.isEntry()) {
			getUiHandler().sendEmptyMessage(MSG_UI_INIT_FINISH);
			return;
		}

		mSubThread = new HandlerThread("application_setup_thread");
		mSubThread.start();

		mSubHandler = new Handler(mSubThread.getLooper(), mSubHandlerCallback);

		mSubHandler.sendEmptyMessage(MSG_SUB_INIT_TASK);
	}
	
	@Override
	protected void onDestroy() {
		if (mSubThread != null) {
			mSubThread.quit();
			mSubThread = null;
		}

		super.onDestroy();
	}

	public Handler getSubHandler() {
		return mSubHandler;
	}

	public Handler getUiHandler() {
		return mUiHandler;
	}

	protected boolean uiHandlerCallback(Message msg) {
		switch (msg.what) {
		case MSG_UI_INIT_FINISH:
			GApplication.setEntryFlag(true);

			final Intent intent = getIntent();
			if (intent != null && intent.getScheme() != null
					&& intent.getScheme().equalsIgnoreCase(DALoader.getAppScheme())) {
				Intent newIntent = new Intent(intent);
				newIntent.setClass(SplashActivity.this, MainActivity.class);
				startActivity(newIntent);
			} else {
				new DA.Builder(this)
					.setHost("0$Main")
					.go();
			}

			finish();
			break;
		}

		return false;
	}

	protected boolean subHandlerCallback(Message msg) {
		switch (msg.what) {
		case MSG_SUB_INIT_TASK:
			DALoaderConfig config = new DALoaderConfig(getApplicationContext());
			config.setCfgDataType(CfgDataType.getDefault());
			config.setCfgFileName("dynamic_action.cfg");
			DALoader.getInstance().setUp(config);

			// SystemClock.sleep(2 * 1000);
			SystemClock.sleep(600);

			getUiHandler().sendEmptyMessage(MSG_UI_INIT_FINISH);
			break;
		}

		return false;
	}

	private Handler.Callback mUiHandlerCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			return uiHandlerCallback(msg);
		}
	};

	private Handler.Callback mSubHandlerCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			return subHandlerCallback(msg);
		}
	};

}