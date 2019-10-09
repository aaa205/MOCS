package com.mocs.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mocs.R;
import com.mocs.common.bean.News;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 展示新闻列表
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> mNewsList;
    private Context mContext;
    public NewsAdapter(List<News> NewsList, Context context) {
        this.mNewsList = NewsList;
        mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_news,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        News news=mNewsList.get(i);
        viewHolder.timeText.setText(DateUtils.getRelativeTimeSpanString(news.getTime()));
        viewHolder.titleText.setText(news.getTitle());
        viewHolder.typeText.setText(news.getType());
        Glide.with(mContext).load(news.getBannerURL()).into(viewHolder.banner);
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.banner_news)
        ImageView banner;
        @BindView(R.id.time_news)
        TextView timeText;
        @BindView(R.id.title_news)
        TextView titleText;
        @BindView(R.id.type_news)
        TextView typeText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
