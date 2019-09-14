package com.mocs.record.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.widget.TextView;
import android.widget.Toolbar;

import com.mocs.R;
import com.mocs.common.bean.RecordInfo;
import com.mocs.common.bean.RecordStep;
import com.mocs.common.bean.User;
import com.mocs.record.adapter.TimeLineAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordDetailActivity extends AppCompatActivity {
    @BindView(R.id.timeline_detail)
    RecyclerView timeLine;
    @BindView(R.id.description_detail)
    TextView descriptionText;
    @BindView(R.id.state_detail)
    TextView stateText;
    @BindView(R.id.time_detail)
    TextView timeText;
    @BindView(R.id.address_detail)
    TextView addressText;
    @BindView(R.id.toolbar_detail)
    Toolbar toolbar;
    @BindArray(R.array.record_type)
    String[] types;
    @BindArray(R.array.record_state)
    String[] states;
    private List<RecordStep> mStepList;
    private User mLocalUser;
    private RecordInfo mRecordInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        ButterKnife.bind(this);
        mLocalUser=getIntent().getParcelableExtra("local_user");
        mRecordInfo=getIntent().getParcelableExtra("record_info");
        initView();
        loadStepList();
        initTimeLine();
    }
    private void initView(){
        toolbar.setNavigationOnClickListener((v)->finish());
        loadRecordInfo();
    }
    private void initTimeLine(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        timeLine.setLayoutManager(layoutManager);
        timeLine.setAdapter(new TimeLineAdapter(mStepList,this));
    }

    /**
     * 加载RecordInfo的数据，填充上方的View
     */
    private void loadRecordInfo(){
        descriptionText.setText(mRecordInfo.getDescription());
        stateText.setText(states[mRecordInfo.getState()]);
        timeText.setText(DateUtils.getRelativeTimeSpanString(mRecordInfo.getCreatedTime()));
        addressText.setText("地址："+mRecordInfo.getAddress());
        toolbar.setTitle(types[mRecordInfo.getType()]);
    }
    private void loadStepList(){
        mStepList=new ArrayList<>();
        for(int i=0;i<3;i++){
            RecordStep step=new RecordStep();
            step.setTime(System.currentTimeMillis());
            step.setReportText("aaaa");
            mStepList.add(step);
        }
    }
}
