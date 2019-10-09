package com.mocs.record.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.mocs.R;
import com.mocs.common.bean.RecordStep;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**记录的时间线Adapter*/
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {
    private List<RecordStep> mRecordStepList;
    private Context mContext;
    public TimeLineAdapter( List<RecordStep> mRecordStepList ,Context context) {
        this.mRecordStepList = mRecordStepList;
        mContext=context;
    }

    @Override
    public int getItemCount() {
        return mRecordStepList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_timeline, viewGroup, false);
        return new TimeLineAdapter.ViewHolder(rootView,i);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RecordStep recordStep=mRecordStepList.get(i);
        viewHolder.report_text.setText(recordStep.getDescription());
        viewHolder.time_text.setText(DateUtils.getRelativeTimeSpanString(recordStep.getCreatedTime()));
        TimelineView timelineView=viewHolder.timeline;
        if (i==0) {
            //第一个的图标设成活动
            timelineView.setMarker(mContext.getDrawable(R.drawable.ic_marker_active),mContext.getColor(R.color.colorAccent));
            timelineView.setLineStyle(TimelineView.LineStyle.DASHED);

        }else
            timelineView.setMarker(mContext.getDrawable(R.drawable.ic_marker_active),timelineView.getStartLineColor());
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.timeline_item)
        TimelineView timeline;
        @BindView(R.id.timeline_time_text)
        TextView time_text;
        @BindView(R.id.timeline_report_text)
        TextView report_text;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            timeline.initLine(viewType);//不能缺
        }
    }
}
