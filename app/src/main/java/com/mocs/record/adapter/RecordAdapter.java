package com.mocs.record.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mocs.R;
import com.mocs.common.bean.RecordInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 展示提交记录
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<RecordInfo> mRecordInfoList;
    private String[] types;
    private String[] states;
    private String[] stateColors;
    private OnItemClickListener mListener;
    public RecordAdapter(List<RecordInfo> recordInfoList, Context context) {
        this.mRecordInfoList = recordInfoList;
        this.types=context.getResources().getStringArray(R.array.record_type);
        this.states=context.getResources().getStringArray(R.array.record_state);
        this.stateColors=context.getResources().getStringArray(R.array.record_state_color);
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time_record)
        TextView timeText;
        @BindView(R.id.type_record)
        TextView typeText;
        @BindView(R.id.description_record)
        TextView descriptionText;
        @BindView(R.id.state_record)
        TextView stateText;
        View itemView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView=itemView;
        }
    }
    public void addOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_record,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RecordInfo recordInfo = mRecordInfoList.get(i);
        viewHolder.timeText.setText(DateUtils.getRelativeTimeSpanString(recordInfo.getCreatedTime()));
        viewHolder.typeText.setText(types[recordInfo.getType()]);
        viewHolder.descriptionText.setText(recordInfo.getDescription());
        viewHolder.stateText.setText(states[recordInfo.getState()]);
        viewHolder.stateText.setTextColor(Color.parseColor(stateColors[recordInfo.getState()]));
        viewHolder.itemView.setOnClickListener((v)->{
            if (mListener!=null)
                mListener.onItemClick(i);
        });
    }

    @Override
    public int getItemCount() {
        return mRecordInfoList.size();
    }

}