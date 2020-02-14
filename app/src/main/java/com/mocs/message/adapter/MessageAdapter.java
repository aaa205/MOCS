package com.mocs.message.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mocs.R;
import com.mocs.message.controller.MessageFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private Context mContext;
    private List<MessageFragment.Message> mMessageList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemview;
        @BindView(R.id.message_avatar)
        CircleImageView avatar;
        @BindView(R.id.message_name)
        TextView name;
        @BindView(R.id.message_content)
        TextView content;
        @BindView(R.id.message_time)
        TextView time;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            itemview=view;
        }

    }

    public MessageAdapter(List<MessageFragment.Message> messagesList) {
        mMessageList=messagesList;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        MessageFragment.Message message=mMessageList.get(position);
        holder.name.setText(message.getName());
        holder.content.setText(message.getContent());
        holder.time.setText(message.getTime());
        //在adapter中设置item点击事件
        if (mItemClickListener!=null){
            holder.itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 这里利用回调来给RecyclerView设置点击事件
                    mItemClickListener.onItemClick(position);
                }
            });
        }
        //在adapter中设置item长按删除事件
        if (mItemLongClickListener!=null){
            holder.itemview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // 这里利用回调来给RecyclerView设置长按事件
                    mItemLongClickListener.onItemLongClick(position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    /**
     * 设置item的监听事件的接口
     */
    //点击
    private ItemClickListener mItemClickListener;
    public interface ItemClickListener{
        public void onItemClick(int position);
    }
    //需要外部访问，所以需要设置set方法，方便调用
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener=itemClickListener;
    }

    //长按
    private ItemLongClickListener mItemLongClickListener;
    public interface ItemLongClickListener{
        public boolean onItemLongClick(int position);
    }
    public void setOnItemLongClickListener(ItemLongClickListener itemLongClickListener){
        this.mItemLongClickListener=itemLongClickListener;
    }


}
