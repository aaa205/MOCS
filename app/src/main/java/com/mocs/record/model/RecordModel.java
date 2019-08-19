package com.mocs.record.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mocs.R;
import com.mocs.common.bean.RecordForm;
import com.mocs.common.bean.User;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 获取与上传Record信息的Model类，在新建时需要传入User
 */
public class RecordModel {
    private final String mServiceHost;
    private Context mContext;
    private User mLocalUesr;

    public RecordModel(Context context, User user) {
        mContext = context;
        mServiceHost = context.getResources().getString(R.string.service_host);//服务器地址
        mLocalUesr = user;
    }


    /**
     * 为用户上传填写的记录，先上传文字部分，完成后服务器创建record_id并返回
     * 然后再用record_id作为参数上传图片
     *
     * @param recordForm 要上传的记录文字
     * @param pathList   要上传的图片路径
     */
    public void uploadForm(RecordForm recordForm, List<String> pathList) {
        //todo
        new Thread(() -> {
            try {
                int recordId = uploadRecordText(recordForm);//上传文字部分，获取record_id
                uploadRecordImage(pathList, recordId);//上传图片

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 上传记录中的文字部分，上传完成后获得record_id,用于上传对应image
     * @param recordForm
     * @return 返回生成的recordId
     * @throws Exception
     */
    private int uploadRecordText(RecordForm recordForm) throws Exception {
        String formJson = new Gson().toJson(recordForm);
        MediaType mediaType = MediaType.parse("application/json");
        Request request = new Request.Builder()
                .header("Authorization", mLocalUesr.getAccessToken())
                .url(mServiceHost + "/records/text")
                .post(RequestBody.create(formJson, mediaType))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(request).execute();//同步
        if (!response.isSuccessful())//连接失败
            throw new Exception(mContext.getResources().getString(R.string.network_error_message));
        JsonObject jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
        if (jsonObject.get("status").getAsInt() != 200)//插入失败
            throw new Exception(jsonObject.get("msg").getAsString());
        return jsonObject.get("record_id").getAsInt();//插入成功返回record_id
    }

    /**
     *
     * @param pathList
     * @param recordId
     * @return true表示上传了图片或没有图片要上传，false表示因图片过大没有上传，
     * @throws Exception 表示发送不成功或插入异常
     */
    private boolean uploadRecordImage(List<String> pathList, int recordId) throws Exception {
        if (pathList == null || pathList.size() == 0)
            return true;//没有图片要上传
        MediaType mediaType = MediaType.parse("image/png,image/jpeg");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("record_id", String.valueOf(recordId));
        int imageLimit = mContext.getResources().getInteger(R.integer.upload_image_limit);//最大文件上传限制
        for (int i = 0; i < pathList.size(); i++) {
            File file = new File(pathList.get(i));
            if (file.length() > imageLimit)
                return false;
            String fileName = file.getName();
            String extension = fileName.substring(fileName.lastIndexOf("."));//获取后缀名
            builder.addFormDataPart("photo", recordId + "-" + i + extension, RequestBody.create(file, mediaType));
        }
        Request request = new Request.Builder().url(mServiceHost + "/records/images").post(builder.build()).build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())//连接失败
            throw new Exception(mContext.getResources().getString(R.string.network_error_message));
        JsonObject jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
        if (jsonObject.get("status").getAsInt() != 200)//插入失败
            throw new Exception(jsonObject.get("msg").getAsString());
        return true;
    }
}

