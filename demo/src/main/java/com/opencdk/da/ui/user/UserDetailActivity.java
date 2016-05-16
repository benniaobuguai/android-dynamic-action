package com.opencdk.da.ui.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.opencdk.da.R;
import com.opencdk.da.biz.user.UserInfo;
import com.opencdk.da.biz.user.UserManager;
import com.opencdk.da.ui.BaseActivity;
import com.opencdk.dynamicaction.DA;
import com.opencdk.dynamicaction.annotation.DAActivity;

@DAActivity(desc = "个人中心", version = "1.0.0")
public class UserDetailActivity extends BaseActivity {

	private TextView tvLoginInfo;
	private TextView btnLogout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_user_detail_main);
		setTitle(R.string.user_detail);

		boolean isLogin = UserManager.getInstance().isLoginSuccess();
		if (!isLogin) {
			new DA.Builder(this)
				.setHost("1$Login")
				.go();
			
			finish();
			return;
		}

		UserInfo userInfo = UserManager.getInstance().getUserInfo();
		tvLoginInfo = $(R.id.tv_login_info);
		btnLogout = $(R.id.btn_logout);

		if (userInfo != null) {
			tvLoginInfo.setText("登录账号: " + userInfo.email);
		}

		btnLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserManager.getInstance().logout();
				finish();
			}
		});
	}

}
