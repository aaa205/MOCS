package com.mocs.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 懒加载碎片的抽象类，可以实现只有第一次显示给用户时加载数据
 */
public abstract class BaseLazyFragment extends Fragment {
    private boolean isLoaded;//数据是否已加载
    private boolean isViewPrepared;//UI是否已准备好

    public BaseLazyFragment() {
        isLoaded=false;
        isViewPrepared=false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=super.onCreateView(inflater, container, savedInstanceState);
        isViewPrepared=true;
        lazyLoad();
        return rootView;
    }
    private void lazyLoad(){
        //当前对用户可见，UI已准备好，未加载数据时，加载数据
        Log.d("Lazy","run lazyLoad");
        if (getUserVisibleHint()&& isViewPrepared &&!isLoaded){
            loadData();
            isLoaded=true;
        }
    }
    /**加载数据放在这里*/
    protected abstract void loadData();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            lazyLoad();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoaded=false;
        isViewPrepared=false;
    }
}
