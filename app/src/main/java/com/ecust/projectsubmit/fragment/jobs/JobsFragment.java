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

package com.ecust.projectsubmit.fragment.jobs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.ecust.projectsubmit.R;
import com.ecust.projectsubmit.adapter.base.delegate.SimpleDelegateAdapter;
import com.ecust.projectsubmit.adapter.dropdownmenu.ListDropDownAdapter;
import com.ecust.projectsubmit.adapter.entity.Job;
import com.ecust.projectsubmit.core.BaseFragment;
import com.ecust.projectsubmit.utils.DemoDataProvider;
import com.ecust.projectsubmit.utils.TokenUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.spinner.DropDownMenu;
import com.xuexiang.xutil.tip.ToastUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @author xiaoxiao
 * @since 2021/4/19
 */
@Page(name = "", anim = CoreAnim.none)
public class JobsFragment extends BaseFragment {
    //???????????????
    @BindView(R.id.ddm_jobs_content)
    DropDownMenu mCourseMenu;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private String[] mHeaders = {"??????"};
    private List<View> mPopupViews = new ArrayList<>();

    private ListDropDownAdapter mCourseAdapter;
    private String[] mCourses;
    private int[] mCoursesid;
    private int nowposition = -1;

    private SimpleDelegateAdapter<Job> mJobsAdapter;

    /**
     * @return ????????? null????????????????????????
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected void initArgs() {
        mCourses = null;
        mCoursesid = null;
        //???????????????????????????
        XHttp.get("/android/getcoursename")
                .syncRequest(false) //????????????
                .onMainThread(true) //???????????????
                .params("token", TokenUtils.getToken())
                .execute(new SimpleCallBack<List<Map<String,Object>>>() {
                    @Override
                    public void onSuccess(List<Map<String,Object>> response) {
                        refreshLayout.finishRefresh(true);
                        if (response != null && response.size() > 0) {
                            mCourses = new String[response.size()];
                            mCoursesid = new int[response.size()];
                            int i=0;
                            for(Map<String,Object> x:response){
                                mCourses[i] = (String) x.get("coursename");
                                mCoursesid[i] =(int) Double.parseDouble(x.get("teachclassid").toString());
                                i++;
                            }
                        } else {
                            ToastUtils.toast("?????????????????????");
                        }
                    }
                    @Override
                    public void onError(ApiException e) {
                        refreshLayout.finishRefresh(false);
                        ToastUtils.toast("?????????????????????");
                    }
                });
        //mCourses = ResUtils.getStringArray(R.array.course_entry); //????????????
    }

    /**
     * ???????????????id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_jobs;
    }

    /**
     * ???????????????
     */
    @Override
    protected void initViews() {

        System.out.println(mCourses);
        //????????????????????????
        final ListView courseView = new ListView(getContext());
        mCourseAdapter = new ListDropDownAdapter(getContext(), mCourses);
        courseView.setDividerHeight(0);
        courseView.setAdapter(mCourseAdapter);

        //init mPopupViews
        mPopupViews.add(courseView);

        //add item click event
        courseView.setOnItemClickListener((parent, view, position, id) -> {
            mCourseAdapter.setSelectPosition(position);
            mCourseMenu.setTabMenuText(mCourses[position]);
            this.nowposition = position;
            mCourseMenu.closeMenu();
            refreshLayout.autoRefresh();
        });

        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getContext());
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);

        //??????
        mJobsAdapter = new SimpleDelegateAdapter<Job>(R.layout.adapter_job_card_view_item, new LinearLayoutHelper()) {
            @Override
            protected void bindData(@NonNull RecyclerViewHolder holder, int position, Job model) {
                if (model != null) {
                    holder.text(R.id.tv_title, model.getTitle());
                    holder.text(R.id.tv_count, "???????????????"+ model.getCount());
                    holder.text(R.id.tv_create_date, "????????????:" + model.getCreateDate());
                    holder.text(R.id.tv_due_data, "????????????:" + model.getDueDate());
                    if(model.getStatus() == 2)
                        holder.text(R.id.tv_score, "??????:" + model.getScore() + "/" + model.getSum());
                    holder.click(R.id.card_view, v -> {
                        System.out.println(model.getTitle());
                        if(model.getStatus() == 0 ||
                                model.getStatus() == 1 ||
                                model.getStatus() == 2)
                            openQuestions(model.getID(),model.getStatus());
                    });
                }
            }
        };

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        delegateAdapter.addAdapter(mJobsAdapter);

        recyclerView.setAdapter(delegateAdapter);
        ViewGroup parent = (ViewGroup) refreshLayout.getParent();
        if (parent != null) {
            parent.removeView(refreshLayout);
        }
        //init dropdownview
        mCourseMenu.setDropDownMenu(mHeaders, mPopupViews, refreshLayout);

    }

    @Override
    protected void initListeners() {
        //????????????
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            // ??????
            refreshLayout.getLayout().postDelayed(() -> {
                mCourseAdapter.setData(mCourses);

                //????????????????????????
                if(nowposition >= 0){
                    //???????????????????????????
                    XHttp.get("/android/getcoursejob")
                            .params("token", TokenUtils.getToken())
                            .params("teachclassid",mCoursesid[nowposition])
                            .execute(new SimpleCallBack<List<Map<String,Object>>>() {
                                @Override
                                public void onSuccess(List<Map<String,Object>> response) {
                                    refreshLayout.finishRefresh(true);
                                    if (response != null && response.size() > 0) {
                                        List<Job> list = new ArrayList<>();
                                        Job job;
                                        for(Map<String,Object> x:response){
                                            //job = new Job(1,(String) x.get("title"),1,(String) x.get("duedate"),"1212",1);

                                            job = new Job((int)Double.parseDouble(x.get("id").toString())
                                                    ,(String) x.get("title")
                                                    ,(int) Double.parseDouble(x.get("type").toString())
                                                    ,(String) x.get("duedate"),(String) x.get("createtime")
                                                    ,(int) Double.parseDouble(x.get("count").toString())
                                                    ,(int) Double.parseDouble(x.get("sum").toString()));
                                            if(x.get("score")!=null)
                                                job.setScore((int) Double.parseDouble(x.get("score").toString()));
                                            if(x.get("status") == null || (int) Double.parseDouble(x.get("status").toString()) == 0)
                                                job.setStatus(0);
                                            else if((int) Double.parseDouble(x.get("status").toString()) == 1)
                                                job.setStatus(1);
                                            else if((int) Double.parseDouble(x.get("status").toString()) == 2)
                                                job.setStatus(2);
                                            else
                                                job.setStatus(3);
                                            list.add(job);
                                        }
                                        mJobsAdapter.refresh(list);
                                    } else {
                                        mJobsAdapter.refresh(new ArrayList<Job>());
                                        ToastUtils.toast("?????????????????????");
                                    }
                                }
                                @Override
                                public void onError(ApiException e) {
                                    refreshLayout.finishRefresh(false);
                                    ToastUtils.toast("?????????????????????");
                                }

                            });
                }
                else{
                    mJobsAdapter.refresh(DemoDataProvider.getDemoJobs());
                }

                refreshLayout.finishRefresh();
            }, 1000);
        });
        //????????????
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            // ??????
            refreshLayout.getLayout().postDelayed(() -> {
                mJobsAdapter.loadMore(DemoDataProvider.getDemoJobs());
                refreshLayout.finishLoadMore();
            }, 1000);
        });
        refreshLayout.autoRefresh();//????????????????????????????????????????????????
    }

    //??????QuestionsFragment
    private void openQuestions(int id,int status){
        PageOption.to("??????")
                .putInt("IDJOB",id)
                .putInt("status",status)
                .setRequestCode(500)
                .setAnim(CoreAnim.fade)
                //?????????????????????????????????????????????
                .setNewActivity(true)
                .open(this);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        refreshLayout.autoRefresh();
        super.onFragmentResult(requestCode, resultCode, data);
    }
}
