package com.joy.library.share.weibo.auth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.library.activity.BaseHttpUiActivity;
import com.android.library.httptask.ObjectRequest;
import com.android.library.utils.TextUtil;
import com.android.library.utils.ToastUtil;
import com.joy.library.R;
import com.joy.library.dialog.DialogUtil;
import com.joy.library.share.ShareConstant;
import com.joy.library.share.weibo.openapi.StatusesAPI;
import com.joy.library.share.weibo.openapi.UsersAPI;
import com.joy.library.share.weibo.openapi.models.StatusList;
import com.joy.library.share.weibo.openapi.models.User;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import java.io.File;

/**
 * 微博分享编辑
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-12-09
 */
public class WeiboEditActivitiy extends BaseHttpUiActivity<String> {

    private JoyOauth2AccessToken mAccessToken;

    private EditText mEtShareContent;
    private TextView mTvWordCount, mTvAccount;
    private int mMaxWordCount = 140;
    private int mNowWordCount = 0;
    private Bitmap mBitmap;
    private AlertDialog mExitDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_weibo_share_edit);
    }

    @Override
    protected void initData() {
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {


        } else {
            ToastUtil.showToast(R.string.weibo_auth_error);
            finish();
            AccessTokenKeeper.clear(this);
        }
        String path = getIntent().getStringExtra("imagePath");
        File file = new File(path);
        if (file.exists()) {
            mBitmap = BitmapFactory.decodeFile(path);
        }
    }

    @Override
    protected void initTitleView() {
        addTitleLeftBackView();
        addTitleRightView(R.drawable.selector_ic_public_send, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendShareContent();
            }
        });
        addTitleMiddleView(R.string.share_to_weibo);

    }

    @Override
    protected void initContentView() {
        mTvWordCount = (TextView) findViewById(R.id.tvWordCount);
        mTvWordCount.setText(String.valueOf(mMaxWordCount));

        mTvAccount = (TextView) findViewById(R.id.tvAccount);
        mEtShareContent = (EditText) findViewById(R.id.etShareContent);
        ImageView preView = (ImageView) findViewById(R.id.ivPic);

        mEtShareContent.requestFocus();


        mEtShareContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                mNowWordCount = mMaxWordCount - TextUtil.calculateWeiboLength(s);
                mTvWordCount.setText(mNowWordCount + "");
            }
        });

        mEtShareContent.setText(getIntent().getStringExtra("content"));
        mEtShareContent.setSelection(mEtShareContent.length());


        preView.setImageBitmap(mBitmap);
        String nickName = mAccessToken.getUserName();
        if (TextUtils.isEmpty(nickName)) {
            startGetSinsUserInfo();
        } else {
            setAccountClick(nickName);
        }
    }


    /**
     * 获取用户的新浪微博信息
     */
    private void startGetSinsUserInfo() {
        UsersAPI usersAPI = new UsersAPI(this, ShareConstant.getIns().getWeiboAppid(), mAccessToken);
        usersAPI.show(mAccessToken.getUid(), new RequestListener() {
            @Override
            public void onComplete(String response) {
                User user = User.parse(response);
                if (user != null) {
                    AccessTokenKeeper.writeUserName(WeiboEditActivitiy.this, user.screen_name);
                } else {
                    setAccountClick("");
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                setAccountClick("");

            }
        });
    }

    /**
     * 发送分享
     */
    private void sendShareContent() {

        if (mNowWordCount <= 0) {
            ToastUtil.showToast(R.string.share_null);
            return;
        }
        if (mNowWordCount > 140) {
            ToastUtil.showToast(R.string.share_to_long);
            return;
        }
        showLoading();
        StatusesAPI statusesAPI = new StatusesAPI(this, ShareConstant.getIns().getWeiboAppid(), mAccessToken);
        statusesAPI.upload(mEtShareContent.getText().toString(), mBitmap, "0.0", "0.0", new RequestListener() {
            @Override
            public void onComplete(String response) {
                hideLoading();
                if (!TextUtils.isEmpty(response)) {
                    if (response.startsWith("{\"statuses\"")) {
                        // 调用 StatusList#parse 解析字符串成微博列表对象
                        StatusList statuses = StatusList.parse(response);
                        if (statuses != null && statuses.total_number > 0) {
                            ToastUtil.showToast(R.string.share_success);
                            finish();
                            return;
                        }
                    } else if (response.startsWith("{\"created_at\"")) {
                        // 调用 Status#parse 解析字符串成微博对象
                        ToastUtil.showToast(R.string.share_success);
                        finish();
                        return;

                    }
                }
                ToastUtil.showToast(R.string.share_error);

            }

            @Override
            public void onWeiboException(WeiboException e) {
                hideLoading();

                ToastUtil.showToast(R.string.share_error);

            }
        });
    }

    private void setAccountClick(String text) {

        mTvAccount.setText(getString(R.string.logout_account_share) + "(" + text + ")");
        mTvAccount.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();
            }
        });
    }

    public static void startActivity(Context context, String content, String imagePath) {

        Intent intent = new Intent();
        intent.setClass(context, WeiboEditActivitiy.class);
        intent.putExtra("content", content);
        intent.putExtra("imagePath", imagePath);
        context.startActivity(intent);

    }

    private void showExitDialog() {

        if (mExitDialog == null) {
            mExitDialog = DialogUtil.getOkCancelDialog(this, R.string.ok, R.string.cancel, getString(R.string.dialog_logout_share_account), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==DialogInterface.BUTTON_POSITIVE) {
                        AccessTokenKeeper.clear(WeiboEditActivitiy.this);
                        WeiboEditActivitiy.this.finish();
                    }
                    mExitDialog.dismiss();
                }
            });
        }
        mExitDialog.show();

    }

    @Override
    protected boolean invalidateContent(String s) {
        return false;
    }

    @Override
    protected ObjectRequest<String> getObjectRequest() {
        return null;
    }
}
