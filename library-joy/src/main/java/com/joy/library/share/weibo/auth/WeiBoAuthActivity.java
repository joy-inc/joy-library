package com.joy.library.share.weibo.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.library.utils.ToastUtil;
import com.joy.library.R;
import com.joy.library.share.ShareConstant;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * 处理授权
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-12-09
 */
public class WeiBoAuthActivity extends Activity {

    private AuthInfo mAuthInfo;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthInfo = new AuthInfo(this, ShareConstant.getIns().getWeiboAppid(), ShareConstant.getIns().getWeiboUrl(), ShareConstant.getIns().getWeiboScope());
        mSsoHandler = new SsoHandler(WeiBoAuthActivity.this, mAuthInfo);
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
            //打开编辑界面
            startEditActivity();
        } else {
            mSsoHandler.authorize(new AuthListener());
        }
    }
    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }
    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息
            String phoneNum = mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                startEditActivity();

                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(WeiBoAuthActivity.this, mAccessToken);
            } else {
                ToastUtil.showToast(R.string.weibo_auth_error);
            }
        }

        @Override
        public void onCancel() {
            ToastUtil.showToast(R.string.weibo_auth_cancle);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastUtil.showToast(R.string.weibo_auth_error);

        }
    }

    private void startEditActivity() {

        WeiboEditActivitiy.startActivity(this,getIntent().getStringExtra("content"),getIntent().getStringExtra("imagePath"));
        finish();
    }

    public static void startActivity(Context context, String content, String imagePath, boolean openEdit) {

        Intent intent = new Intent();
        intent.setClass(context, WeiBoAuthActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("imagePath", imagePath);
        intent.putExtra("openEdit", openEdit);
        context.startActivity(intent);

    }

}
