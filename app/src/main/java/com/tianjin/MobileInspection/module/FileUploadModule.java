package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.dialog.CustomDialog;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.DialogUnit;
import com.tianjin.MobileInspection.until.FileUtil;
import com.tianjin.MobileInspection.until.MediaFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by zhuangaoran on 2016-12-2.
 */
public class FileUploadModule {

    public final static int INT_UPLOAD_SUCCESS=0x445;
    public final static int INT_UPLOAD_FILED=0x446;

    //批量上传
    public static void upload(Context context,Handler handlers,String[]uploadFiles, String sturl) {
        String end = "/r/n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            DialogUnit.showDialog(context);
            URL url = new URL(sturl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // 发送POST请求必须设置如下两行
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setConnectTimeout(MyApplication.CONNECTION_TIME_OUT);
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            DataOutputStream ds =
                    new DataOutputStream(con.getOutputStream());
            for (int i = 0; i < uploadFiles.length; i++) {
                String uploadFile = uploadFiles[i];
                String filename = uploadFile.substring(uploadFile.lastIndexOf("//") + 1);
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; " + "/ + end");
                ds.writeBytes(end);
                FileInputStream fStream = new FileInputStream(uploadFile);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
                while ((length = fStream.read(buffer)) != -1) {
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(end);
                fStream.close();
            }
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.flush();
            // 定义BufferedReader输入流来读取URL的响应
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String s = b.toString();
            if (s.contains("successfully")) {
                // for (int i = 1; i < 5; i++) {
                int beginIndex = s.indexOf("url =") + 5;
                int endIndex = s.indexOf("/n", beginIndex);
                String urlStr = s.substring(beginIndex, endIndex).trim();
                System.out.println(urlStr);
                // }
            }
            ds.close();
            DialogUnit.closeDialog();
        } catch (Exception e) {
            DialogUnit.closeDialog();
        }
    }


    /**
     * 头像上传
     * @param handler
     * @param inputStream
     * @throws Exception
     */
    public static void postFile(final Context context, final Handler handler, final InputStream inputStream,final String filename) throws Exception {
        DialogUnit.showDialog(context);
        String jsessionid= MyApplication.getStringSharedPreferences("JSESSIONID","");
        String surl= ConnectionURL.STR_IMAGE_UPLOAD+jsessionid+ConnectionURL.HTTP_SERVER_END;
        KLog.d(surl);
        File file = new File(MyApplication.IMAGEFILEPATH,filename);
        FileOutputStream out=new FileOutputStream(file);
        byte[] b=new byte[512];
        int len=0;
        while ((len=inputStream.read(b))!=-1){
            out.write(b,0,len);
        }
        out.flush();
        out.close();
        inputStream.close();
        if (file.exists() && file.length() > 0) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("file", file);
            client.post(surl, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    DialogUnit.closeDialog();
                    String result=new String(responseBody,0,responseBody.length);
                    try {
                        JSONObject object=new JSONObject(result);
                        Message msg=new Message();
                        if(object.getBoolean("success")){
                            msg.what=INT_UPLOAD_SUCCESS;
                            msg.arg1=-1;
                        }else{
                            msg.what=INT_UPLOAD_FILED;
                            msg.obj="上传失败";
                            msg.arg1=object.getInt("errorCode");
                            KLog.d(object.getInt("errorCode")+"");
                        }
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                   DialogUnit.closeDialog();
                    Message msg=new Message();
                    msg.what=INT_UPLOAD_FILED;
                    msg.arg1=1;
                    msg.obj="网络连接错误";
                    handler.sendEmptyMessage(INT_UPLOAD_FILED);
                }
            });
        } else {
            KLog.d("文件不存在" );
        }

    }


    /**
     * 根据url获取网络图片的bitmap
     * @param urlString
     * @return
     */
    public static Bitmap getBitmapFromUrl(String urlString) {
        StringBuffer sb=new StringBuffer();
        sb.append(ConnectionURL.HTTP_SERVER_IMAGE).append(urlString);
        KLog.d(sb.toString());
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(sb.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            //从网络上获取图片
            bitmap = BitmapFactory.decodeStream(is);
            //资源释放
            connection.disconnect();
            return bitmap;
        } catch (IOException e) {
            KLog.d(e.getMessage());
            return null;
        } finally {
            try {
                if (is!=null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 根据url获取网络图片的bitmap
     * @param urlString
     * @return
     */
    public static Bitmap getBitmapFromUrlT(String urlString) {
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            //从网络上获取图片
            bitmap = BitmapFactory.decodeStream(is);
            //资源释放
            connection.disconnect();
            return bitmap;
        } catch (IOException e) {
            KLog.d(e.getMessage());
            return null;
        } finally {
            try {
                if (is!=null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public final static int INT_UPLOAD_FILES_SUCCESS=0x6640;
    public final static int INT_UPLOAD_FILES_FILED=0x6641;

    /**
     * 图片批量上传
     * @param handler
     * @param url
     * @param filename
     * @param accidentMain
     * @throws Exception
     */
    public static void uploadFiles(Context context,final Handler handler, String url, List<String> filename, String accidentMain) throws Exception {
        final CustomDialog dialog=new CustomDialog(context,"文件上传...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        File[] files = new File[filename.size()];
        KLog.d("upload");
        for(int i=0;i<filename.size();i++){
            StringBuffer sb=new StringBuffer(MyApplication.IMAGEFILEPATH).append(FileUtil.getFileNameForUrl(filename.get(i)));
            if(MediaFile.isImageFile(sb.toString()) ) {//如果是图片则压缩
                files[i] = new File(FileUtil.compressImage(filename.get(i), sb.toString(), 100));
            }else {
                files[i]=new File(filename.get(i));
            }
        }
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("files", files);
            params.put("id", accidentMain);
            KLog.d(accidentMain);
            client.post(url, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    dialog.cancel();
                    String str=new String(responseBody,0,responseBody.length);
                    KLog.d(str);
                    try {
                        JSONObject job=new JSONObject(str);
                        if (job.getBoolean("success")){
                            handler.sendEmptyMessage(INT_UPLOAD_FILES_SUCCESS);
                        }else {
                            handler.sendEmptyMessage(INT_UPLOAD_FILES_FILED);
                        }
                    } catch (JSONException e) {
                        handler.sendEmptyMessage(INT_UPLOAD_FILES_FILED);
                    }
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    dialog.cancel();
                    String str=new String(responseBody,0,responseBody.length);
                    KLog.d(str+"   "+error.getMessage());
                    handler.sendEmptyMessage(INT_UPLOAD_FILES_FILED);
                }
        });
    }

    /**
     * 图片上传
     * @param handler
     * @param url
     * @param filename
     * @param accidentMain
     * @throws Exception
     */
    public static void uploadFiles(Context context,final Handler handler, String url, String filename, String accidentMain) throws Exception {
        DialogUnit.showDialog(context);
        File file = new File(filename);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("file", file);
        params.put("id", accidentMain);
        KLog.d(accidentMain);
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                DialogUnit.closeDialog();
                String str=new String(responseBody,0,responseBody.length);
                KLog.d(str);
                try {
                    JSONObject job=new JSONObject(str);
                    if (job.getBoolean("success")){
                        handler.sendEmptyMessage(INT_UPLOAD_FILES_SUCCESS);
                    }else {
                        handler.sendEmptyMessage(INT_UPLOAD_FILES_FILED);
                    }
                } catch (JSONException e) {
                    handler.sendEmptyMessage(INT_UPLOAD_FILES_FILED);
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                DialogUnit.closeDialog();
                String str=new String(responseBody,0,responseBody.length);
                KLog.d(str);
                handler.sendEmptyMessage(INT_UPLOAD_FILES_FILED);
            }
        });
    }


    public final static int DOWNLOAD_SUCCESS=0x90;
    public final static int DOWNLOAD_FILED=0x91;
    /**
     * 文件下载
     * @param urlString
     * @return
     */
    public static void downLoadFile(Context context,Handler handler,String urlString,String filename) {
        KLog.d(urlString);
        File file=new File(MyApplication.DOWNLOAD_PATH,filename);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream out=null;
        InputStream is = null;
        HttpURLConnection connection = null;
        try {
            out=new FileOutputStream(file);
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(MyApplication.CONNECTION_TIME_OUT);
            connection.setRequestProperty("Accept-Encoding", "identity");
            is = connection.getInputStream();
            byte[] b=new byte[1024];
            int len=0;
            while ((len=is.read(b))!=-1){
                out.write(b,0,len);
            }
            out.flush();
            connection.disconnect();
            KLog.d("下载成功");
            Message msg=handler.obtainMessage();
            msg.what=DOWNLOAD_SUCCESS;
            msg.obj=file.getAbsolutePath();
            handler.sendMessage(msg);
        } catch (Exception e) {
            KLog.d("下载失败"+e.getMessage());
            if(connection!=null) {
                connection.disconnect();
            }
            Message msg=handler.obtainMessage();
            msg.what=DOWNLOAD_FILED;
            msg.obj="文件不存在";
            handler.sendMessage(msg);
        } finally {
            try {
                if (is!=null){
                    is.close();
                }
                if(out!=null){
                    out.close();
                }
            } catch (IOException e) {
                KLog.d(e.getMessage());
            }
        }
    }


    /**
     * 图片批量上传
     * @param handler
     * @param url
     * @param filename
     * @throws Exception
     */
    public static void uploadReport(Context context,final Handler handler, String url, List<String> filename, String id,String description) throws Exception {
        final CustomDialog dialog=new CustomDialog(context,"文件上传...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        File[] files = new File[filename.size()];
        KLog.d("upload");
        for(int i=0;i<filename.size();i++){
            StringBuffer sb=new StringBuffer(MyApplication.IMAGEFILEPATH).append(FileUtil.getFileNameForUrl(filename.get(i)));
            if(MediaFile.isImageFile(sb.toString()) ) {//如果是图片则压缩
                files[i] = new File(FileUtil.compressImage(filename.get(i), sb.toString(), 100));
            }else {
                files[i]=new File(filename.get(i));
            }
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("files", files);
        params.put("description", description);
        params.put("id",id);
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                dialog.cancel();
                String str=new String(responseBody,0,responseBody.length);
                KLog.d(str);
                try {
                    JSONObject job=new JSONObject(str);
                    if (job.getBoolean("success")){
                        handler.sendEmptyMessage(INT_UPLOAD_FILES_SUCCESS);
                    }else {
                        handler.sendEmptyMessage(INT_UPLOAD_FILES_FILED);
                    }
                } catch (JSONException e) {
                    handler.sendEmptyMessage(INT_UPLOAD_FILES_FILED);
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                dialog.cancel();
                String str=new String(responseBody,0,responseBody.length);
                KLog.d(str+"   "+error.getMessage());
                handler.sendEmptyMessage(INT_UPLOAD_FILES_FILED);
            }
        });
    }
}

