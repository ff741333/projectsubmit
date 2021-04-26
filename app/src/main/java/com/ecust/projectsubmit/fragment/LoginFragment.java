/*
 * Copyright (C) 2021 xiaoxiao(1018982338@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ecust.projectsubmit.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.ecust.projectsubmit.adapter.entity.LoginInfo;
import com.ecust.projectsubmit.core.BaseFragment;
import com.ecust.projectsubmit.R;
import com.ecust.projectsubmit.activity.MainActivity;
import com.ecust.projectsubmit.utils.SettingUtils;
import com.ecust.projectsubmit.utils.TokenUtils;
import com.ecust.projectsubmit.utils.Utils;
import com.ecust.projectsubmit.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.data.SPUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;



/**
 * 登录页面
 *
 * @author xuexiang
 * @since 2019-11-17 22:15
 */
@Page(anim = CoreAnim.none)
public class LoginFragment extends BaseFragment {

    @BindView(R.id.et_student_number)
    MaterialEditText etStudentNumber;
    @BindView(R.id.et_password)
    MaterialEditText etPassword;
//    @BindView(R.id.btn_get_verify_code)
//    RoundButton btnGetVerifyCode;

    private CountDownButtonHelper mCountDownHelper;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle()
                .setImmersive(true);
        titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setTitle("");
//        titleBar.setLeftImageDrawable(ResUtils.getVectorDrawable(getContext(), R.drawable.ic_login_close));
        titleBar.setActionTextColor(ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
//        titleBar.addAction(new TitleBar.TextAction(R.string.title_jump_login) {
//            @Override
//            public void performAction(View view) {
//                onLoginSuccess();
//            }
//        });
        return titleBar;
    }

    @Override
    protected void initViews() {
//        mCountDownHelper = new CountDownButtonHelper(btnGetVerifyCode, 60);//验证吗模块

        //显示前一个用户的学号和密码
        SharedPreferences userInfo = SPUtils.getSharedPreferences("UserInfo");
        if(userInfo.contains("number")){
            etStudentNumber.setText(userInfo.getString("number",null));
        }
        //隐私政策弹窗
        if (!SettingUtils.isAgreePrivacy()) {
            Utils.showPrivacyDialog(getContext(), (dialog, which) -> {
                dialog.dismiss();
                SettingUtils.setIsAgreePrivacy(true);
            });
        }
    }

    @SingleClick
    @OnClick({ R.id.btn_login, R.id.tv_forget_password, R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.btn_get_verify_code:
//                if (etStudentNumber.validate()) {
//                    getVerifyCode(etStudentNumber.getEditValue());
//                }
//                break;
            case R.id.btn_login:
                if (etStudentNumber.validate()) {
                    if (etPassword.validate()) {
                        loginByVerifyCode(etStudentNumber.getEditValue(), etPassword.getEditValue());
                    }
                }
                break;
//            case R.id.tv_other_login:
//                XToastUtils.info("其他登录方式");
//                break;
            case R.id.tv_forget_password:
                XToastUtils.info("忘记密码");
                break;
            case R.id.tv_user_protocol:
                XToastUtils.info("用户协议");
                break;
            case R.id.tv_privacy_protocol:
                XToastUtils.info("隐私政策");
                break;
            default:
                break;
        }
    }

//    /**
//     * 获取验证码
//     */
//    private void getVerifyCode(String phoneNumber) {
//        // TODO: 2020/8/29 这里只是界面演示而已
//        XToastUtils.warning("只是演示，验证码请随便输");
//        mCountDownHelper.start();
//    }

    /**
     * 根据验证码登录
     *
     * @param number 学号
     * @param passWord  密码
     */
    private void loginByVerifyCode(String number, String passWord) {
        // TODO: 2020/8/29 这里只是界面演示而已
        HashMap<String,Object> map = new HashMap<>();
        map.put("username",number);
        map.put("password",passWord);
        Log.e("Tag",number+passWord);
        XHttp.post("/android/studentloginforandroid")
                .params(map)
                .execute(new SimpleCallBack<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo response) {
                        if (response != null ) {
                            SharedPreferences userInfo = SPUtils.getSharedPreferences("UserInfo");
                            SharedPreferences.Editor editor = userInfo.edit();
                            editor.putString("number",number);
                            editor.putString("password",passWord);
                            editor.commit();
                            onLoginSuccess(response);
                        } else {
                            ToastUtils.toast("课程请求失败！");
                        }
                    }
                    @Override
                    public void onError(ApiException e) {
                        ToastUtils.toast(e.getMessage());
                    }

                });
    }

    /**
     * 登录成功的处理
     */
    private void onLoginSuccess(LoginInfo response) {
        String token = response.getToken();
        if (TokenUtils.handleLoginSuccess(token)) {
            System.out.println(token);
            popToBack();
            ActivityUtils.startActivity(MainActivity.class);
        }
    }

    @Override
    public void onDestroyView() {
        if (mCountDownHelper != null) {
            mCountDownHelper.recycle();
        }
        super.onDestroyView();
    }
}

