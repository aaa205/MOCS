package com.mocs.record.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.mocs.R;
import com.mocs.record.adapter.ImageGridAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 美化了提交表单的界面
 * 2019-8-7
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
    private ImageGridAdapter mGridAdapter;
    private ArrayList<String> mPathList;//图片路径集合

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
        mGridAdapter = new ImageGridAdapter(this, mPathList, 6);//最多选择6张
        gridView.setAdapter(mGridAdapter);
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            if (i == adapterView.getChildCount() - 1) {//如果点击当前最后一张图
                if (mPathList.size() == mGridAdapter.getMaxSelected()) {
                    //如果当前图片已选满，查看大图
                    Toast.makeText(this, "click i see raw", Toast.LENGTH_SHORT).show();
                } else
                    //如果图片没选满，则继续添加
                    Toast.makeText(this, "remain " + (mGridAdapter.getMaxSelected() - mPathList.size()), Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "click i see raw", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * 初始化View
     */
    private void initView(){
        toolbar.setNavigationOnClickListener((v)->finish());
        initGridView();
    }
}
