package com.mocs.record.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mocs.R;
import com.mocs.common.base.BaseLazyFragment;
import com.mocs.common.bean.RecordInfo;
import com.mocs.common.bean.User;
import com.mocs.record.adapter.RecordAdapter;
import com.mocs.common.bean.RecordStep;
import com.mocs.record.model.RecordModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 修改了布局 使用CardView来展示,完成获取数据
 * 待完成：
 * 如果没有更多内容，加载后Footer会挡住最后一个item
 * 按日期排序
 * 2019-8-24
 */

public class RecordFragment extends BaseLazyFragment {
    private static final String LOCAL_USER = "local_user";
    private Unbinder mUnbinder;
    @BindView(R.id.recyclerView_record)
    RecyclerView recyclerView;
    @BindView(R.id.add_button_record)
    FloatingActionButton floatingButton;
    @BindView(R.id.refresh_layout_record)
    SmartRefreshLayout refreshLayout;
    private List<RecordInfo> mRecordInfoList = new ArrayList<>();
    private int mOffset = 0;//请求RecordInfo的偏移量
    private int mRows = 10;//一次请求多少条RecordInfo
    private User mLocalUser;
    private RecordModel mRecordModel;

    public static RecordFragment newInstance(User user) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        args.putParcelable(LOCAL_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLocalUser = getArguments().getParcelable(LOCAL_USER);
            mRecordModel = new RecordModel(getContext(), mLocalUser);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        super.onCreateView(inflater, container, savedInstanceState);//这句一定要有 才能调用loadData()
        return rootView;
    }

    @Override
    protected void loadData() {
        initView();
        refreshLayout.autoRefresh();
    }


    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecordAdapter adapter = new RecordAdapter(mRecordInfoList, getContext());
        recyclerView.setAdapter(adapter);
        floatingButton.setOnClickListener((v) -> {
            Intent intent = new Intent(getContext(), FormActivity.class);
            intent.putExtras(getArguments());//传入mLocalUser
            startActivity(intent);
        });
        refreshLayout.setOnRefreshListener((layout -> new RefreshAsyncTask().execute()));
        refreshLayout.setOnLoadMoreListener((layout->new LoadMoreAsyncTask().execute()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /**
     * 刷新用 每次获取最新rows条RecordInfo
     */
    private class RefreshAsyncTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String msg = null;
            try {
               mRecordInfoList.clear();
               mRecordInfoList.addAll(mRecordModel.getRecordInfoList(0,mRows));
                mOffset=0;//确定获取成功后再修改偏移量
            } catch (Exception e) {
                msg = e.getMessage();
            } finally {
                return msg;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s!=null){
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            refreshLayout.setNoMoreData(false);
            refreshLayout.finishRefresh();
            recyclerView.getAdapter().notifyDataSetChanged();//刷新列表

        }
    }
    /**
     * 下滑获取更多 每次额外获取rows条RecordInfo
     */
    private class LoadMoreAsyncTask extends AsyncTask<Void,Integer,String>{
        private int originalSize=mRecordInfoList.size();
        @Override
        protected String doInBackground(Void... voids) {
            String msg=null;
            try{
                mRecordInfoList.addAll(mRecordModel.getRecordInfoList(mOffset+mRows,mRows));//添加后rows条
                mOffset+=mRows;//确定获取成功后再修改偏移量
            }catch (Exception e){
                msg=e.getMessage();
            }finally {
                return msg;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s!=null){
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            if (originalSize==mRecordInfoList.size())
                refreshLayout.setNoMoreData(true);//没有更多数据了
            refreshLayout.finishLoadMore();
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

}
