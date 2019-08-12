package com.mocs.record.controller;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.mocs.R;
import com.mocs.common.bean.LocationInfo;
import com.mocs.common.loader.MyBoxingMediaLoader;
import com.mocs.record.adapter.ImageGridAdapter;

import java.util.ArrayList;
import java.util.List;

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
    private ImageGridAdapter mGridAdapter;
    private ArrayList<String> mPathList;//图片路径集合

    private static final int SELECT_IMAGE = 2;//选择图片
    private static final int GET_ADDRESS=3;//获取地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.bind(this);
        mPathList = new ArrayList<>();
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
    private void initFormListener(){
        //类型挑选
        String[] types = getResources().getStringArray(R.array.form_type);
        AlertDialog typeDialog=new AlertDialog.Builder(this)
                .setItems(types, (di,i)-> textType.setText(types[i])).create();
        layoutType.setOnClickListener((v)->typeDialog.show());
        //点击定位
        layoutAddress.setOnClickListener((v)->{
            Intent intent=new Intent(FormActivity.this,MapActivity.class);
            startActivityForResult(intent,GET_ADDRESS);
        });
        //提交按钮
        butSubmit.setOnClickListener((v)->{
            String type=textType.getText().toString();
            String address=textAddress.getText().toString();
            String desc=editDescription.getText().toString();
            if (desc.isEmpty()||type.isEmpty()||address.isEmpty())
                Toast.makeText(FormActivity.this,"有项目未填写",Toast.LENGTH_SHORT).show();
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
                case GET_ADDRESS:
                    LocationInfo locationInfo=data.getParcelableExtra("location_info");
                    textAddress.setText(locationInfo.getAddress());
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
    }
}
