package com.mocs.record.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mocs.R;
import com.mocs.common.bean.Record;
import com.mocs.common.bean.RecordInfo;
import com.mocs.common.bean.RecordStep;
import com.mocs.common.bean.User;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
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
    private User mLocalUser;

    public RecordModel(Context context, User user) {
        mContext = context;
        mServiceHost = context.getResources().getString(R.string.service_host);//服务器地址
        mLocalUser = user;
    }

    /**
     * 获取指定record的recordStep
     *
     * @param recordId
     * @return
     * @throws Exception
     */
    public List<RecordStep> getRecordStepList(int recordId) throws Exception {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = HttpUrl.parse(mServiceHost + "/records/steps").newBuilder()
                .addQueryParameter("id", String.valueOf(recordId))
                .build();
        Request request = new Request.Builder()
                .header("Authorization", mLocalUser.getAccessToken())
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new Exception(mContext.getResources().getString(R.string.network_error_message));
        JsonObject jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
        if (jsonObject.get("status").getAsInt() != 200)
            throw new Exception(jsonObject.get("msg").getAsString());
        JsonArray array = jsonObject.get("steps").getAsJsonArray();
        List<RecordStep> list = new Gson().fromJson(array, new TypeToken<List<RecordStep>>() {
        }.getType());
        return list;
    }

    /**
     * 根据经纬度，获取附近的RecordInfo
     *
     * @param latitude
     * @param longitude
     * @return
     * @throws Exception
     */
    public List<Record> getNearbyRecordInfoList(double latitude, double longitude) throws Exception {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = HttpUrl.parse(mServiceHost + "/records").newBuilder()
                .addQueryParameter("latitude", String.valueOf(latitude))
                .addQueryParameter("longitude", String.valueOf(longitude))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new Exception(mContext.getResources().getString(R.string.network_error_message));
        JsonArray jsonArray = new JsonParser().parse(response.body().string()).getAsJsonArray();
        List<Record> list = new Gson().fromJson(jsonArray, new TypeToken<List<Record>>() {
        }.getType());
        return list;
    }

    /**
     * 获取指定用户的记录的简单信息
     *
     * @param offset
     * @param rows
     * @return
     * @throws Exception
     */
    public List<RecordInfo> getRecordInfoList(int offset, int rows) throws Exception {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = HttpUrl.parse(mServiceHost + "/records").newBuilder()
                .addQueryParameter("userId", String.valueOf(mLocalUser.getUserId()))
                .addQueryParameter("offset", String.valueOf(offset))
                .addQueryParameter("rows", String.valueOf(rows))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new Exception(mContext.getResources().getString(R.string.network_error_message));
        JsonObject jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
        if (jsonObject.get("status").getAsInt() != 200)//查询失败
            throw new Exception(jsonObject.get("msg").getAsString());
        JsonArray jsonArr = jsonObject.get("records").getAsJsonArray();
        List<RecordInfo> list = new Gson().fromJson(jsonArr, new TypeToken<List<RecordInfo>>() {
        }.getType());
        return list;
    }


    /**
     * 上传记录中的文字部分，上传完成后获得record_id,用于上传对应image
     *
     * @param record
     * @return 返回生成的recordId
     * @throws Exception
     */
    public int uploadRecordText(Record record) throws Exception {
        String formJson = new Gson().toJson(record);
        MediaType mediaType = MediaType.parse("application/json");
        Request request = new Request.Builder()
                .header("Authorization", mLocalUser.getAccessToken())
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
     * 保存图片url到数据库
     * @param pathList
     * @param recordId
     * @return
     * @throws Exception 表示发送不成功或插入异常
     */
    public void uploadRecordImage(List<String> pathList, int recordId) throws Exception {
        List<String> imgUrls = new ArrayList<>(pathList.size());
        for (int i = 0; i < pathList.size(); i++) {
            File file = new File(pathList.get(i));
            imgUrls.add(uploadImg(file));
        }
        String body=new Gson().toJson(imgUrls);//转json
        Request request=new Request.Builder()
                .url(mServiceHost+"/records/"+recordId+"/images")
                .header("Authorization",mLocalUser.getAccessToken())
                .post(RequestBody.create(body,MediaType.parse("application/json")))
                .build();
        Response response=new OkHttpClient().newCall(request).execute();
        if(response.isSuccessful())
            Log.d("uploadImg","upload urls success");
        else
            throw new Exception("保存图片失败:\n"+response.code()+" "+response.message());

    }

    /**
     * 上传图片到图床
     *
     * @param file
     * @return 上传图片的url
     */
    private String uploadImg(File file) throws Exception {
        int extensionIndex = file.getName().lastIndexOf(".");
        MediaType mediaType = MediaType.parse("image/" + file.getName().substring(extensionIndex + 1));//获取类型
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("smfile", file.getName(), RequestBody.create(file, mediaType))
                .build();
        Request request = new Request.Builder()
                .url("https://sm.ms/api/v2/upload")
                .header("Authorization", "DiQMO10k4SkZk5caKQ4FmPnwRLLea1j1")
                .header("User-Agent", "PostmanRuntime/7.22.0")
                .post(multipartBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())//连接失败
            throw new Exception(mContext.getResources().getString(R.string.network_error_message));
        JsonObject jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
        String url;
        if (jsonObject.get("success").getAsBoolean()) {
            url = jsonObject.get("data").getAsJsonObject().get("url").getAsString();
        } else if (jsonObject.get("images").getAsString() != null) {
            //如果重复提交
            url = jsonObject.get("images").getAsString();
        } else {
            throw new Exception("无法上传至图床sm.ms");
        }
        Log.d("uploadImg", "uploaded img url:" + url);
        return url;
    }

    /**
     * 检查图片是否能够上传，在uploadRecordIamge前调用
     *
     * @param pathList
     * @return
     */
    public boolean isUploadable(List<String> pathList) {
        if (pathList == null || pathList.size() == 0)
            return true;//没有图片要上传
        int imageLimit = mContext.getResources().getInteger(R.integer.upload_image_limit);//最大文件上传限制
        for (int i = 0; i < pathList.size(); i++) {
            File file = new File(pathList.get(i));
            if (file.length() > imageLimit)
                return false;
        }
        return true;
    }
}

