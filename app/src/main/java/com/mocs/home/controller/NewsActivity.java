package com.mocs.home.controller;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.mocs.R;
import com.mocs.common.bean.News;
import com.mocs.home.model.NewsModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity {
    @BindView(R.id.title_news)
    TextView titleText;
    @BindView(R.id.time_news)
    TextView timeText;
    @BindView(R.id.content_news)
    WebView contentView;
    private ProgressDialog loadingProgress;
    private int newsId;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsId = getIntent().getIntExtra("news_id", 1);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        initView();
        new FetchAsyncTask().execute();
    }

    private void initView() {
        //loading
        loadingProgress = new ProgressDialog(this);
        loadingProgress.setIndeterminate(true);
        loadingProgress.setMessage("加载中...");
        loadingProgress.setCanceledOnTouchOutside(false);
        //设置webview
        WebSettings settings = contentView.getSettings();
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        contentView.setBackgroundColor(Color.parseColor("#00000000"));

    }

    private void loadData() {
        //装载数据
        titleText.setText(news.getTitle());
        timeText.setText(DateUtils.getRelativeTimeSpanString(news.getCreatedTime()));
        contentView.loadDataWithBaseURL(null, getHtmlContent(news.getContent()),
                "text/html", "utf-8", null);
    }
    private String getHtmlContent(String content){
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:100%; height:auto;}*{margin:0px;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + content + "</body></html>";

    }
    private class FetchAsyncTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            loadingProgress.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String msg = null;
            try {
                news = NewsModel.fetchNewsById(NewsActivity.this.newsId);
            } catch (Exception e) {
                Log.d("fetchNews", e.getMessage());
                msg = e.getMessage();
            } finally {
                return msg;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
            } else {
                loadData();
            }
            loadingProgress.dismiss();
        }
    }
}