package com.opencdk.da.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.opencdk.da.R;
import com.opencdk.dynamicaction.DA;
import com.opencdk.dynamicaction.DALoader;
import com.opencdk.dynamicaction.annotation.DAActivity;


@DAActivity(desc = "浏览器", version = "1.0.0")
public class BrowserActivity extends BaseActivity {
	
	private static final String TAG = "BrowserActivity";

	private Context mContext;
	private WebView mWebView; // WebView

	/**
	 * 当前访问的地址
	 */
	private String mLoadUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usage_browser_main);
		this.mContext = this;

		Intent intent = getIntent();
		mLoadUrl = DA.getActionUrl(intent);
		String title = DA.getActionTitle(intent);
		if (!TextUtils.isEmpty(title)) {
			setTitle(title);
		}

		setUpViews();

		if (!TextUtils.isEmpty(mLoadUrl)) {
			mWebView.loadUrl(mLoadUrl);
		}
	}

	protected void setUpViews() {
		mWebView = (WebView) findViewById(R.id.webView);

		WebSettings settings = mWebView.getSettings();
		settings.setDefaultTextEncodingName("utf-8");
		// 支持JS
		settings.setJavaScriptEnabled(true);
		// 设置可以支持缩放
		settings.setSupportZoom(true);
		// 设置默认缩放方式尺寸是far
		//settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		// 设置出现缩放工具
		// settings.setBuiltInZoomControls(true);

		String packageName = getPackageName();
		settings.setRenderPriority(RenderPriority.HIGH);
		settings.setDatabasePath("/data/data/" + packageName + "/databases");
		settings.setPluginState(PluginState.ON);//设置webview支持插件, 如flash插件等
		settings.setUseWideViewPort(true);
	
		// 显示放大或缩小按钮
		// settings.setBuiltInZoomControls(true);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		settings.setLoadWithOverviewMode(true);
		settings.setSavePassword(true);
		settings.setSaveFormData(true);
		// enable navigator.geolocation
		// settings.setGeolocationEnabled(true);
		// settings.setGeolocationDatabasePath("/data/data/" + packageName + ".html5webview/databases/");
		// enable Web Storage: localStorage, sessionStorage
		settings.setDomStorageEnabled(true);
		
		// 垂直滚动条总是显示白色轨迹底图(无法消掉)
		mWebView.setScrollbarFadingEnabled(true);
		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		mWebView.requestFocus();
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url != null && url.startsWith(DALoader.getScheme())) {
					// DAController.doActionScheme(mContext, url);
					
					new DA.Builder(mContext)
						.setUriString(url)
						.go();
					
					return true;
				}

				return super.shouldOverrideUrlLoading(view, url);
			}
		});

		mWebView.setWebChromeClient(new MyWebChromeClient(this));

		// 添加桥
		mWebView.addJavascriptInterface(new AndroidBridge(), "Android");

		// 下载
		mWebView.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
					long contentLength) {
			}
		});

	}

	/**
	 * 组合代替继承
	 * 
	 * @author 笨鸟不乖
	 * @email ixueyongjia@gmail.com
	 * @version 1.0.0
	 * @date 2014-9-24
	 */
	public static class MyWebChromeClient extends WebChromeClient {

		private BrowserActivity mWebViewActivity;

		public MyWebChromeClient(BrowserActivity webViewActivity) {
			this.mWebViewActivity = webViewActivity;
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			// this.mWebViewActivity.onReceivedTitle(view, title);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// mWebViewActivity.onProgressChanged(view, newProgress);
		}

		@Override
		public View getVideoLoadingProgressView() {
			Log.i(TAG, "getVideoLoadingProgressView--> Callback()");
			return super.getVideoLoadingProgressView();
			// return this.mWebViewActivity.getVideoLoadingProgressView();
		}

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			// this.mWebViewActivity.onShowCustomView(view, callback);
		}

		@Override
		public void onHideCustomView() {
			// this.mWebViewActivity.onHideCustomView();
		}

	}

	/**
	 * JS桥
	 * 
	 * @author 笨鸟不乖
	 * @email ixueyongjia@gmail.com
	 * @version 1.0.0
	 * @date 2014-9-26
	 */
	private class AndroidBridge {

		@JavascriptInterface
		public void testCallback() {

		}

	}

}
