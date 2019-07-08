package com.mocs.main.controller;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.mocs.R;
import com.mocs.home.controller.HomeFragment;
import com.mocs.message.controller.MessageFragment;
import com.mocs.my.controller.MyFragment;
import com.mocs.record.controller.RecordFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener,
        ViewPager.OnPageChangeListener,  MyFragment.OnFragmentInteractionListener {
    @BindView(R.id.bottomBar)
    BottomNavigationBar bottomNavigationBar;
    @BindView(R.id.viewPager)
    ViewPager mPager;
    private static int NUM_ITEMS = 4;//底部导航栏item数
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);//绑定视图
        init();
    }

    //初始化底部导航栏
    private void init() {
        initNavigationBar();
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mAdapter.mList.add(HomeFragment.newInstance());
        mAdapter.mList.add(RecordFragment.newInstance());
        mAdapter.mList.add(MessageFragment.newInstance());
        mAdapter.mList.add(MyFragment.newInstance(null, null));
        mPager.setAdapter(mAdapter);//将fragment装入viewpager
        mPager.setCurrentItem(0);//设置默认页面，要接在setAdapter后
        mPager.addOnPageChangeListener(this);//监听器
        mPager.setOffscreenPageLimit(3);//缓存半径

    }

    //初始化导航栏
    private void initNavigationBar() {
        bottomNavigationBar.setActiveColor(R.color.colorAccent)
                .setInActiveColor(R.color.colorGreyTab)
                .setMode(BottomNavigationBar.MODE_DEFAULT)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .addItem(new BottomNavigationItem(R.drawable.ic_home_grey_400_24dp, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.ic_explore_grey_400_24dp, "记录"))
                .addItem(new BottomNavigationItem(R.drawable.ic_message_grey_400_24dp, "消息"))
                .addItem(new BottomNavigationItem(R.drawable.ic_account_box_grey_400_24dp, "我的"))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position) {
        mPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        bottomNavigationBar.selectTab(i);//滑动换页后同步导航栏
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public static class MyAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> mList = new ArrayList<>(NUM_ITEMS);

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int i) {
            return mList.get(i);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

    }
}
