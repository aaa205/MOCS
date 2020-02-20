package com.mocs.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Base64;
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
    private OnItemClickListener itemClickListener;

    public NewsAdapter(List<News> NewsList, Context context) {
        this.mNewsList = NewsList;
        mContext = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_news, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        News news = mNewsList.get(i);
        viewHolder.timeText.setText(DateUtils.getRelativeTimeSpanString(news.getCreatedTime()));
        viewHolder.titleText.setText(news.getTitle());
        viewHolder.typeText.setText("新闻");
        byte[] imgBytes=Base64.decode(news.getCover().split(",")[1],Base64.DEFAULT);
        Glide.with(mContext).load(imgBytes).into(viewHolder.banner);
        viewHolder.rootView.setOnClickListener((v)->{
            if (itemClickListener!=null)
                itemClickListener.onItemClick(i);
        });
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.banner_news)
        ImageView banner;
        @BindView(R.id.time_news)
        TextView timeText;
        @BindView(R.id.title_news)
        TextView titleText;
        @BindView(R.id.type_news)
        TextView typeText;
        View rootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.rootView=itemView;
        }
    }
}
