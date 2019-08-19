package com.mocs.record.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mocs.R;
import com.mocs.common.bean.Record;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 展示提交记录
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<Record> mRecordList;
    private String[] types;
    public RecordAdapter(List<Record> recordList, Context context) {
        this.mRecordList = recordList;
        this.types=context.getResources().getStringArray(R.array.form_type);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time_record)
        TextView timeText;
        @BindView(R.id.type_record)
        TextView typeText;
        @BindView(R.id.description_record)
        TextView descriptionText;
        @BindView(R.id.last_step_record)
        TextView lastStepText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_record,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Record record = mRecordList.get(i);
        viewHolder.timeText.setText(String.valueOf(record.getCreatedTime()));
        viewHolder.typeText.setText(types[record.getType()]);
        viewHolder.descriptionText.setText(record.getDescription());
        viewHolder.lastStepText.setText("1111");
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }

}