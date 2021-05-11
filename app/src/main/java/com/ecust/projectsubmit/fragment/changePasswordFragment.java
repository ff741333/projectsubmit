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

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import com.ecust.projectsubmit.R;
import com.ecust.projectsubmit.core.BaseFragment;
import com.ecust.projectsubmit.utils.TokenUtils;
import com.ecust.projectsubmit.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.edittext.PasswordEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @author xiaoxiao
 * @since 2021/5/3
 */
@Page(name = "修改密码")
public class changePasswordFragment extends BaseFragment {
    @BindView(R.id.et_local_password)
    MaterialEditText localPassword;
    @BindView(R.id.et_new_password1)
    MaterialEditText newPassword1;
    @BindView(R.id.et_new_password2)
    MaterialEditText newPassword2;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                handleBackPressed();
            }
        });

        titleBar.addAction(new TitleBar.TextAction("确认") {
                               @SingleClick
                               @Override
                               public void performAction(View view) {
                                    if(localPassword.validate() && newPassword1.validate() && newPassword2.validate()){
                                        if(newPassword1.getText().toString().equals(newPassword2.getText().toString())){
                                            XHttp.post("/android/updatepasswordforandroid")
                                                    .syncRequest(false) //异步请求
                                                    .onMainThread(true) //回到主线程
                                                    .params("oldpassword",localPassword.getText().toString())
                                                    .params("newpassword",newPassword1.getText().toString())
                                                    .params("token", TokenUtils.getToken())
                                                    .execute(new SimpleCallBack<String>() {
                                                        @Override
                                                        public void onSuccess(String response) {
                                                            if (response != null && response.length() > 0) {
                                                                ToastUtils.toast("密码修改成功！");
                                                                XUtil.getActivityLifecycleHelper().exit();
                                                                TokenUtils.handleLogoutSuccess();
                                                            } else {
                                                                ToastUtils.toast("密码修改失败！");
                                                            }
                                                        }
                                                        @Override
                                                        public void onError(ApiException e) {
                                                            ToastUtils.toast(e.getMessage());
                                                        }
                                                    });
                                        }
                                    }
                               }
                           });

        return titleBar;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_changepassword;
    }

    @Override
    protected void initViews() {
        newPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //System.out.println(newPassword1.getText()+"和"+newPassword2.getText());
                if(!newPassword1.getText().toString().equals(newPassword2.getText().toString()))
                    newPassword2.setError("两次密码输入不一致");
            }
        });
    }

    @Override
    protected void initListeners() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handleBackPressed();
        }
        return true;
    }

    private void handleBackPressed() {
        popToBack();
    }
}
