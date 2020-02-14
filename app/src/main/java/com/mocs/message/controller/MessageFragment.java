package com.mocs.message.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.mocs.R;
import com.mocs.message.adapter.MessageAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MessageFragment extends Fragment {
    private View view;//定义view用来设置fragment的layout
    public RecyclerView messageRecyclerView;//定义RecyclerView
    public RefreshLayout refreshLayout;//定义RefreshLayout
    //定义以Message实体类为对象的数据集合
    private List<Message> messagesList = new ArrayList<Message>();
    //自定义recyclerveiw的适配器
    private MessageAdapter adapter;
    private Unbinder unbinder;

    public MessageFragment() {
        // Required empty public constructor
    }


    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_message, container, false);
        //返回一个Unbinder值（进行解绑），注意这里的this不能使用getActivity()
        unbinder = ButterKnife.bind(this, view);
        //对recycleview进行配置
        initRecyclerView();
        initRefreshLayout();
        return view;
    }

    /**
     * onDestroyView中进行解绑操作
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 对recycleview进行配置
     */
    private void initRecyclerView() {
        //获取RecyclerView
        messageRecyclerView=(RecyclerView)view.findViewById(R.id.message_recycleView);
        //创建adapter
        adapter=new MessageAdapter(getMessages());
        //给RecyclerView设置adapter
        messageRecyclerView.setAdapter(adapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        messageRecyclerView.setLayoutManager(layoutManager);
        //设置item的分割线
        messageRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        //点击
        adapter.setOnItemClickListener(new MessageAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(),"点击事件"+(position+1),Toast.LENGTH_SHORT).show();
            }
        });
        //长按
        adapter.setOnItemLongClickListener(new MessageAdapter.ItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final int position) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder (getContext());
                dialog.setMessage("删除后，将清空该聊天的全部信息记录？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"你点击了删除",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"你点击了取消",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

                return true;
            }
        });
    }


    /**
     * 对refreshLayout下拉刷新进行配置
     */
    public void initRefreshLayout(){
        refreshLayout = (RefreshLayout)view.findViewById(R.id.message_refresh);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }

    /**
     * 测试之用
     */
    public class Message {
        private int tag;
        private String name;
        private String content;
        private String time;

        public int getTag() {
            return tag;
        }

        public void setTag(int tag) {
            this.tag = tag;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

    }

    private List<Message> getMessages(){
        for(int i=1;i<=1;i++){
            Message message=new Message();
            message.setTag(R.mipmap.ic_launcher);
            message.setName("小助手");
            message.setContent("你好");
            message.setTime("11月13日 12:51");
            messagesList.add(message);
        }
        return messagesList;
    }


}
