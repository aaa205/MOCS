package com.mocs.record.controller;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.mocs.R;
import com.mocs.common.bean.RecordInfo;
import com.mocs.common.bean.RecordStep;
import com.mocs.common.bean.User;
import com.mocs.record.adapter.TimeLineAdapter;
import com.mocs.record.model.RecordModel;

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
    private ProgressDialog loadingProgress;
    private List<RecordStep> mStepList;
    private User mLocalUser;
    private RecordInfo mRecordInfo;
    private RecordModel mRecordModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        ButterKnife.bind(this);
        mLocalUser=getIntent().getParcelableExtra("local_user");
        mRecordInfo=getIntent().getParcelableExtra("record_info");
        mStepList=new ArrayList<>();
        mRecordModel=new RecordModel(this,mLocalUser);
        initView();
        loadRecordInfo();
        new StepListAsyncTask().execute();
    }
    private void initView(){
        toolbar.setNavigationOnClickListener((v)->finish());

        loadingProgress=new ProgressDialog(this);
        loadingProgress.setIndeterminate(true);
        loadingProgress.setMessage("加载中...");
        loadingProgress.setCanceledOnTouchOutside(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        timeLine.setLayoutManager(layoutManager);
        RecordStep step=new RecordStep();
        step.setDescription("111");
        step.setCreatedTime(11);
        mStepList.add(step);
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

    /**
     * 获取时间线上的step列表
     */
    private class StepListAsyncTask extends AsyncTask<Void,Integer,String>{
        @Override
        protected void onPreExecute() {
            loadingProgress.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            int id=mRecordInfo.getRecordId();
            String msg=null;
            try {
                //mStepList.clear();
                mStepList.addAll(mRecordModel.getRecordStepList(id));
                mStepList.sort((rs,t)->{
                    if(rs.getCreatedTime()==t.getCreatedTime())
                        return 0;
                    if (rs.getCreatedTime()>t.getCreatedTime())
                        return -1;
                    else
                        return 1;
                });
            }catch (Exception e) {
                msg=e.getMessage();
            }
            return msg;
        }

        /**
         * 往timeline中填充数据
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            if (s==null){
                timeLine.getAdapter().notifyDataSetChanged();
                loadingProgress.dismiss();
            }else{
                Toast.makeText(RecordDetailActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
