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

package com.ecust.projectsubmit.fragment.questions;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.ecust.projectsubmit.R;
import com.ecust.projectsubmit.adapter.dropdownmenu.ConstellationAdapter;
import com.ecust.projectsubmit.adapter.entity.Question;
import com.ecust.projectsubmit.core.BaseFragment;
import com.ecust.projectsubmit.utils.TokenUtils;
import com.ecust.projectsubmit.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.spinner.DropDownMenu;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;



/**
 * @author xiaoxiao
 * @since 2021/4/20
 */
@Page(name = "作业")
public class QuestionsFragment extends BaseFragment {

    @BindView(R.id.ddm_questions_content)
    DropDownMenu mDropDownMenu;
    @BindView(R.id.question_content)
    NestedScrollView mQuestionContent;
    @BindView(R.id.tv_question_content)
    TextView tvquestioncontent;
    @BindView(R.id.et_answer)
    MaterialEditText etanswer;
    @BindView(R.id.spinner_answer)
    MaterialSpinner spinneranswer;


    int idjob;
    boolean finished;

    private String[] mHeaders = {"题号"};
    private List<View> mPopupViews = new ArrayList<>();

    private ConstellationAdapter mConstellationAdapter;

    private String[] mQuestionsNo;
    private int[] mQuestionsID;
    private Question nowquestion;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_questions;
    }

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

        //未完成可以提交
        if(!finished){
            titleBar.addAction(new TitleBar.TextAction("提交") {
                @SingleClick
                @Override
                public void performAction(View view) {
                    DialogLoader.getInstance().showConfirmDialog(
                            getContext(),
                            getString(R.string.lab_submit),
                            getString(R.string.lab_yes),
                            (dialog, which) -> {
                                XToastUtils.toast("同意提交！");
                                dialog.dismiss();
                                handleBackPressed();
                            },
                            getString(R.string.lab_no),
                            (dialog, which) -> {
                                XToastUtils.toast("不同意提交！");
                                dialog.dismiss();
                            }
                    );
                }
            });
        }

        return titleBar;
    }

    @Override
    protected void initArgs() {
        Bundle arguments = getArguments();
        if(arguments != null){
            idjob = arguments.getInt("IDJOB"); //获取JobFragment传过来的IDJOB
            finished = arguments.getBoolean("status");
        }

        //请求当前用户的课程
        XHttp.get("/android/getquestionsno")
                .params("token", TokenUtils.getToken())
                .params("idjob",idjob)
                .execute(new SimpleCallBack<List<Map<String,Object>>>() {
                    @Override
                    public void onSuccess(List<Map<String,Object>> response) {
                        if (response != null && response.size() > 0) {
                            mQuestionsNo = new String[response.size()];
                            mQuestionsID = new int[response.size()];
                            int i=0;
                            for(Map<String,Object> x:response){
                                mQuestionsNo[i] = (int) Double.parseDouble(x.get("no").toString())+"";
                                mQuestionsID[i] = (int) Double.parseDouble(x.get("idquestion").toString());
                                i++;
                            }
                            mConstellationAdapter.setData(mQuestionsNo);
                        } else {
                            ToastUtils.toast("题目请求失败！");
                        }
                    }
                    @Override
                    public void onError(ApiException e) {
                        ToastUtils.toast("题目请求错误！");
                    }
                });

        //mQuestionsNo = ResUtils.getStringArray(R.array.constellation_entry); //模拟数据

    }

    @Override
    protected void initViews() {
        System.out.println(finished);
        if(finished){
            etanswer.setEnabled(false);
            spinneranswer.setEnabled(false);
        }

        //init constellation
        final View constellationView = getLayoutInflater().inflate(R.layout.layout_drop__down_question, null);
        GridView constellation = constellationView.findViewById(R.id.constellation);

        mConstellationAdapter = new ConstellationAdapter(getContext(), mQuestionsNo);
        constellation.setAdapter(mConstellationAdapter);
        constellationView.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            mDropDownMenu.setTabMenuText(mConstellationAdapter.getSelectPosition() < 0 ? mHeaders[0] : "第"+mConstellationAdapter.getSelectItem()+"题");
            if(mConstellationAdapter.getSelectPosition()>=0){
                setQuestion(mQuestionsID[mConstellationAdapter.getSelectPosition()]);

            }

            mDropDownMenu.closeMenu();
        });

        //init mPopupViews
        mPopupViews.add(constellationView);

        //add item click event
        constellation.setOnItemClickListener((parent, view, position, id) -> mConstellationAdapter.setSelectPosition(position));


        ViewGroup parent = (ViewGroup) mQuestionContent.getParent();
        if (parent != null) {
            parent.removeView(mQuestionContent);
        }

        parent = (ViewGroup) spinneranswer.getParent();
        if (parent != null) {
            parent.removeView(spinneranswer);
        }

        parent = (ViewGroup) etanswer.getParent();
        if (parent != null) {
            parent.removeView(etanswer);
        }

        LinearLayout linearLayout = (LinearLayout) mQuestionContent.getChildAt(0);
        //spinneranswer.setItems(ResUtils.getStringArray(R.array.course_entry));
        //spinneranswer.setItems(getOption(4));
        //linearLayout.removeView(etanswer);
        //linearLayout.addView(contentView);
        linearLayout.addView(spinneranswer);
        linearLayout.removeView(spinneranswer);
        linearLayout.addView(etanswer);


        //linearLayout.addView(spinneranswer);
        //init dropdownview
        mDropDownMenu.setDropDownMenu(mHeaders, mPopupViews, mQuestionContent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handleBackPressed();
        }
        return true;
    }

    private void handleBackPressed() {
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            popToBack();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 初始化监听
     */
    @Override
    protected void initListeners() {

    }

    /**
     * 得到某道题
     * @param idquestion
     * @return
     */
    private void setQuestion(int idquestion){
        nowquestion = null;
        ViewGroup parent;
        parent = (ViewGroup) spinneranswer.getParent();
        if (parent != null) {
            parent.removeView(spinneranswer);
        }

        parent = (ViewGroup) etanswer.getParent();
        if (parent != null) {
            parent.removeView(etanswer);
        }

        LinearLayout linearLayout = (LinearLayout) mQuestionContent.getChildAt(0);

        XHttp.get("/android/getonesquestion")
                .params("token", TokenUtils.getToken())
                .params("idquestion", idquestion)
                .execute(new SimpleCallBack<Map<String,Object>>() {
                    @Override
                    public void onSuccess(Map<String,Object> response) {
                        if (response != null && response.size() > 0) {
                            nowquestion = new Question((int)Double.parseDouble(response.get("idquestion").toString())
                                    , (int)Double.parseDouble(response.get("no").toString())
                                    , (int)Double.parseDouble(response.get("type").toString())
                                    , (String) response.get("content")
                                    , (String) response.get("answer"));
                            nowquestion.setYourAnswer((String) response.get("youranswer"));
                            tvquestioncontent.setText(nowquestion.getContent());
                            switch (nowquestion.getType()){
                                case 1:
                                    etanswer.setText(nowquestion.getYourAnswer());
                                    linearLayout.addView(etanswer);
                                    break;
                                case 2:
                                    nowquestion.setOption((int)Double.parseDouble(response.get("option").toString()));
                                    spinneranswer.setItems(getOption(nowquestion.getOption()));
                                    if(nowquestion.getYourAnswer()!=null)
                                        spinneranswer.setSelectedIndex(nowquestion.getYourAnswer().charAt(0)-65);
                                    linearLayout.addView(spinneranswer);
                                    break;
                                default:break;
                            }
                        } else {
                            ToastUtils.toast("课程请求失败！");
                        }
                    }
                    @Override
                    public void onError(ApiException e) {
                        ToastUtils.toast("课程请求错误！");
                    }
                });
    }


    //根据选项个数获取Item
    private String[] getOption(int n){
        String[] op = new String[n];
        for (int i = 0;i < n;i++){
            op[i] = (char)(65+i)+"";
        }
        return op;
    }
}
