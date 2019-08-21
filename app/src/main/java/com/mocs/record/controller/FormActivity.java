package com.mocs.record.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.mocs.R;
import com.mocs.common.bean.RecordForm;
import com.mocs.common.bean.User;
import com.mocs.common.loader.MyBoxingMediaLoader;
import com.mocs.record.adapter.ImageGridAdapter;
import com.mocs.record.model.RecordModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 完成了打开相册
 * 待完成：无法打开相机，没有查看原图,发送内容到服务器
 * 2019-8-8
 */
public class FormActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_form)
    Toolbar toolbar;
    @BindView(R.id.layout_address)
    LinearLayout layoutAddress;//在layout里注册点击事件，点击事件修改对应的text
    @BindView(R.id.text_address)
    TextView textAddress;
    @BindView(R.id.layout_type)
    LinearLayout layoutType;
    @BindView(R.id.text_type)
    TextView textType;
    @BindView(R.id.form_grid)
    GridView gridView;
    @BindView(R.id.but_submit)
    Button butSubmit;
    @BindView(R.id.edit_description)
    EditText editDescription;
    @BindArray(R.array.form_type)
    String[] types;//种类
    private ProgressDialog loadingProgress;
    private ImageGridAdapter mGridAdapter;
    private ArrayList<String> mPathList;//图片路径集合
    private User mLocalUser;
    private RecordForm mRecordForm;//表格信息
    private int mType;//种类编号
    private RecordModel mRecordModel;
    private static final int SELECT_IMAGE = 2;//选择图片
    private static final int GET_LOCATION_INFO = 3;//获取地址
    private static final String LOCAL_USER = "local_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.bind(this);
        mPathList = new ArrayList<>();
        mRecordForm = new RecordForm();
        mLocalUser = getIntent().getParcelableExtra(LOCAL_USER);
        mRecordModel=new RecordModel(this,mLocalUser);
        initView();
    }

    /**
     * 初始化照片网格
     */
    private void initGridView() {
        mGridAdapter = new ImageGridAdapter(this, mPathList, 3);//最多选择3张
        gridView.setAdapter(mGridAdapter);
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            if (i == adapterView.getChildCount() - 1) {//如果点击当前最后一张图
                if (mPathList.size() == mGridAdapter.getMaxSelected()) {
                    //如果当前图片已选满，查看大图
                    Toast.makeText(this, "click i see raw", Toast.LENGTH_SHORT).show();
                } else
                    //如果图片没选满，则继续添加
                    openImageSelector();
            } else
                Toast.makeText(this, "click i see raw", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * 注册表格点击事件
     */
    private void initFormListener() {
        //类型挑选

        AlertDialog typeDialog = new AlertDialog.Builder(this)
                .setItems(types, (di, i) -> {
                    textType.setText(types[i]);
                    mType=i;//存储选择类型编号
                }).create();
        layoutType.setOnClickListener((v) -> typeDialog.show());
        //点击定位
        layoutAddress.setOnClickListener((v) -> {
            Intent intent = new Intent(FormActivity.this, MapActivity.class);
            startActivityForResult(intent, GET_LOCATION_INFO);
        });
        //提交按钮
        butSubmit.setOnClickListener((v) -> {
            String type = textType.getText().toString();
            String address = textAddress.getText().toString();
            String desc = editDescription.getText().toString();
            //为空表示没有获取地理位置
            if (mRecordForm==null||desc.isEmpty() || type.isEmpty() || address.isEmpty()) {
                Toast.makeText(FormActivity.this, "有项目未填写", Toast.LENGTH_SHORT).show();
            } else {
                //注入Bean
                mRecordForm.setDescription(desc);
                mRecordForm.setCreatedTime(System.currentTimeMillis());
                mRecordForm.setType(mType);
                mRecordForm.setUserId(mLocalUser.getUserId());
                new FormAsyncTask().execute();//上传

            }
        });
    }

    /**
     * 打开图片选择器
     */
    private void openImageSelector() {
        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.MULTI_IMG);
        config.withMaxCount(mGridAdapter.getMaxSelected()).needCamera(R.drawable.ic_camera_alt_grey_400_48dp);
        BoxingMediaLoader.getInstance().init(new MyBoxingMediaLoader());//初始化
        Boxing.of(config).withIntent(this, BoxingActivity.class).start(this, SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_IMAGE:
                    List<BaseMedia> mediaList = Boxing.getResult(data);
                    if (mediaList != null) {
                        mPathList.clear();
                        for (BaseMedia m : mediaList)
                            mPathList.add(m.getPath());
                        mGridAdapter.notifyDataSetChanged();
                    }
                    break;
                case GET_LOCATION_INFO:
                    mRecordForm = data.getParcelableExtra("record_form");
                    textAddress.setText(mRecordForm.getAddress());
                    break;
            }
        }
    }

    /**
     * 初始化View
     */
    private void initView() {
        toolbar.setNavigationOnClickListener((v) -> finish());
        initGridView();
        initFormListener();
        loadingProgress=new ProgressDialog(FormActivity.this);
        loadingProgress.setIndeterminate(true);
        loadingProgress.setMessage("上传中...");
        loadingProgress.setCanceledOnTouchOutside(false);
    }

    /**
     * 执行上传表单工作
     */
    private class FormAsyncTask extends AsyncTask<Void,Integer,String>{

        @Override
        protected void onPreExecute() {
            loadingProgress.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String msg=null;
            try {
                //先检查图片能否上传
                if (mRecordModel.isUploadable(mPathList)) {
                    int id=mRecordModel.uploadRecordText(mRecordForm);
                    mRecordModel.uploadRecordImage(mPathList,id);
                }else{
                    msg="请勿上传大于2.5MB的图片";
                }
            }catch (IOException ioe){
                msg=getText(R.string.network_error_message).toString();
            }
            catch (Exception e) {
                msg=e.getMessage();
            }finally {
                return msg;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            loadingProgress.dismiss();
            if (s!=null)
                Toast.makeText(FormActivity.this, s, Toast.LENGTH_SHORT).show();
            else
                finish();
        }
    }
}
