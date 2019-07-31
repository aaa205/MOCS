package com.mocs.record.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mocs.R;
import com.mocs.record.adapter.RecordAdapter;
import com.mocs.common.bean.Record;
import com.mocs.common.bean.RecordStep;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecordFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.recyclerView_record)
    RecyclerView recyclerView;
    @BindView(R.id.add_button_record)
    FloatingActionButton floatingButton;
    private List<Record> mRecordList=new ArrayList<>();

    public static RecordFragment newInstance() {
        RecordFragment fragment = new RecordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**test*/
        initRecordList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_record, container, false);
        unbinder= ButterKnife.bind(this,rootView);
        initView();
        return rootView;
    }
    /**test*/
    private void initRecordList(){
        SimpleDateFormat sdf=new SimpleDateFormat();
        sdf.applyPattern("MM-dd HH:mm");
        StringBuilder stringBuilder=new StringBuilder("description ");
        List<RecordStep> steps=new ArrayList<>();
        for (int i=0;i<6;i++){
            RecordStep recordStep=new RecordStep();
            recordStep.setTime(sdf.format(new Date()));
            recordStep.setReportText("step "+1);
            steps.add(recordStep);
        }
        for(int i=0;i<10;i++){
            Record record=new Record();
            stringBuilder.append("description");
            record.setTime(sdf.format(new Date()));
            record.setType("TYPE0-TEST");
            record.setDescription(stringBuilder.toString());
            record.setRecordStepList(steps);
            mRecordList.add(record);
        }
    }
    private void initView(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecordAdapter adapter=new RecordAdapter(getContext(),mRecordList);
        recyclerView.setAdapter(adapter);
        /**临时*/
        floatingButton.setOnClickListener((v)->{
            Intent intent=new Intent(getContext(),MapActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
