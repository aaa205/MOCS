package com.mocs.record.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mocs.R;

import java.util.List;

/**
 * FormActivity中展示图片的适配器
 */
public class ImageGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mPathList;
    private LayoutInflater mInflater;
    private int mMaxSelected;//最大可选择数

    public ImageGridAdapter(Context Context, List<String> pathList,int MaxSelected) {
        this.mContext =Context;
        this.mMaxSelected = MaxSelected;
        this.mPathList=pathList;
        mInflater=LayoutInflater.from(mContext);
    }

    /**
     * 当图片没满时要多1显示加号，满了后不显示加号
     * @return
     */
    @Override
    public int getCount() {
        int count= mPathList ==null?1: mPathList.size()+1;
        if (count>mMaxSelected)
            return mPathList.size();
        else
            return count;
    }

    /**
     * 返回指定图片的url
     * @param i
     * @return
     */
    @Override
    public String getItem(int i) {
        return mPathList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * 加载图片
     * @param i
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=mInflater.inflate(R.layout.item_image_grid,viewGroup,false);
        ImageView imageView=view.findViewById(R.id.item_img_grid);
        if (i< mPathList.size()){
            //+号前的图
            String imgUrl= mPathList.get(i);
            Glide.with(mContext).load(imgUrl).into(imageView);
        }else
            imageView.setImageResource(R.drawable.ic_add_box_grey_400_48dp);
        return view;
    }

    /**
     * 还能添加多少图
     * @return
     */
    public int getRemainder(){
        return mMaxSelected- mPathList.size();
    }

    public int getMaxSelected() {
        return mMaxSelected;
    }

    public void setMaxSelected(int MaxSelected) {
        this.mMaxSelected = MaxSelected;
    }

    public List<String> getPathList() {
        return mPathList;
    }

    public void setPathList(List<String> PathList) {
        this.mPathList = PathList;
    }
}
