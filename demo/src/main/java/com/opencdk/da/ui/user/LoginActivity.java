package com.opencdk.da.ui.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.opencdk.da.R;
import com.opencdk.da.biz.user.UserInfo;
import com.opencdk.da.biz.user.UserManager;
import com.opencdk.da.ui.BaseActivity;
import com.opencdk.dynamicaction.annotation.DAActivity;

@DAActivity(desc = "用户登录", version = "1.0.0")
public class LoginActivity extends BaseActivity {

	private EditText etEmail;
	private EditText etPassword;
	private Button btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_user_login_main);
		setTitle(R.string.user_login);

		etEmail = $(R.id.et_email);
		etPassword = $(R.id.et_password);
		btnLogin = $(R.id.btn_login);

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserInfo userInfo = new UserInfo();
				String email = etEmail.getText().toString();
				String pwd = etPassword.getText().toString();
				if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
					email = getString(R.string.user_email);
					pwd = getString(R.string.user_pwd);
				}

				userInfo.email = email;
				userInfo.pwd = pwd;
				UserManager.getInstance().setLoginUserInfo(userInfo);

				setResult(RESULT_OK);
				finish();
			}
		});
	}

}
