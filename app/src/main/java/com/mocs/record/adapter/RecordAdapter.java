package com.mocs.record.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.vipulasri.timelineview.TimelineView;
import com.mocs.R;
import com.mocs.record.bean.Record;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<Record> mRecordList;

    public RecordAdapter(List<Record> recordList) {
        this.mRecordList = recordList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.expand_button_record)
        ImageButton expand_button;
        @BindView(R.id.time_record)
        TextView time_text;
        @BindView(R.id.type_record)
        TextView type_text;
        @BindView(R.id.description_record)
        TextView description_text;
        @BindView(R.id.expandableLayout_record)
        ExpandableLayout expandableLayout;
        @BindView(R.id.timeLine_record)
        TimelineView timeLine;
        private boolean expanded;
        private View itemView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
            ButterKnife.bind(this, itemView);
            expanded = false;//默认不展开
        }
        /**设置是否展开*/
        public void setExpanded(boolean b) {
            if (b != expanded) {
                expanded = b;
                expandableLayout.setExpanded(expanded, true);
                if (!expanded)//换图标
                    Glide.with(itemView).load(R.drawable.ic_keyboard_arrow_down_grey_400_36dp).into(expand_button);
                else
                    Glide.with(itemView).load(R.drawable.ic_keyboard_arrow_up_grey_400_36dp).into(expand_button);
            }
        }
        /**点击箭头*/
        @OnClick(R.id.expand_button_record)
        void clickExpanded(){
            setExpanded(!expanded);
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
        viewHolder.time_text.setText(record.getTime());
        viewHolder.type_text.setText(record.getType());
        viewHolder.description_text.setText(record.getDescription());
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }

}