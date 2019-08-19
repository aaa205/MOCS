package com.mocs.record.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mocs.R;
import com.mocs.common.base.BaseLazyFragment;
import com.mocs.common.bean.User;
import com.mocs.record.adapter.RecordAdapter;
import com.mocs.common.bean.Record;
import com.mocs.common.bean.RecordStep;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 修改了布局 使用CardView来展示
 * 待完成：
 * 下拉刷新
 * 按日期排序
 * 2019-8-6
 */

public class RecordFragment extends BaseLazyFragment {
    private static final String LOCAL_USER = "local_user";
    private Unbinder mUnbinder;
    @BindView(R.id.recyclerView_record)
    RecyclerView recyclerView;
    @BindView(R.id.add_button_record)
    FloatingActionButton floatingButton;
    private List<Record> mRecordList = new ArrayList<>();

    private User mLocalUser;

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
        }
        /**test*/
        initRecordList();

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
    }

    /**
     * test
     */
    private void initRecordList() {
        StringBuilder stringBuilder = new StringBuilder("description ");
        List<RecordStep> steps = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            RecordStep recordStep = new RecordStep();
            recordStep.setTime(System.currentTimeMillis());
            recordStep.setReportText("step " + 1);
            steps.add(recordStep);
        }
        for (int i = 0; i < 10; i++) {
            Record record = new Record();
            stringBuilder.append("description");
            record.setCreatedTime(System.currentTimeMillis());
            record.setType(0);
            record.setDescription(stringBuilder.toString());
            mRecordList.add(record);
        }
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecordAdapter adapter = new RecordAdapter(mRecordList,getContext());
        recyclerView.setAdapter(adapter);
        floatingButton.setOnClickListener((v) -> {
            Intent intent = new Intent(getContext(), FormActivity.class);
            intent.putExtras(getArguments());//传入mLocalUser
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
