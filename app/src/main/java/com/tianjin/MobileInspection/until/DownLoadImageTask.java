package com.tianjin.MobileInspection.until;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wuchang on 2017-1-3.
 *
 * 文件下载的线程
 */
public class DownLoadImageTask extends AsyncTask<String, Integer, byte[]> {//参数含义：传进去的值；后台百分比；返回的结果

    public final static int DOWN_LOAD_SUCCESS=0x653;
    public final static int DOWN_LOAD_FILED=0x654;


    private ProgressDialog progressDialog;//进度条
    private byte[] current = new byte[2 * 1024];//每次读到的字节数组
    private byte[] total;//下载图片后汇总的字节数组
    private boolean flag; //是否被取消
    private Context context;
    private Handler handler;
    private String filename;

    public DownLoadImageTask(Context context,Handler handler,String name){
        this.context=context;
        this.handler=handler;
        this.filename=name;
    }

    //任务执行之前回调
    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("正在操作中，请稍候……");
        progressDialog.setMax(100);//进度条最大值
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//水平样式
        progressDialog.setIndeterminate(false);//进度条的动画效果（有动画则无进度值）
        flag = true;
        //退出对话框事件
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                flag = false;
            }
        });
        progressDialog.show();
    }

    @Override
    protected  byte[] doInBackground(String... params) {//字符串数组
        try {
            //创建URL对象，用于访问网络资源
            URL url = new URL(params[0]);
            KLog.d(url.toString());
            //获得HttpUrlConnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置超时时间
            connection.setConnectTimeout(5000);
            connection.connect();
            if(connection.getResponseCode()==200) {
                //获得总长度
                int length = connection.getContentLength();
                KLog.d("=======", length + "");
                InputStream is = connection.getInputStream();
                File file = new File(MyApplication.DOWNLOAD_PATH, filename);
                FileOutputStream out = new FileOutputStream(file);
                int pointer = 0;//已用掉的索引
                if (length > 0) {
                    //为total分配空间
                    total = new byte[length];
                    //开始读取数据
                    int real = is.read(current); //读取当前批次的字节并保存到数组中
                    while (flag && real > 0) {
                        //如果读取到了字节，则保存到total中
                        for (int i = 0; i < real; i++) {
                            total[pointer + i] = current[i];
                        }
                        //指针向后移
                        pointer += real;
                        //计算进度
                        int progress = (int) ((double) pointer / length * 100);//先计算出百分比在转换成整型
                        //更新进度
                        publishProgress(progress, pointer, length);
                        //继续读取下一批
                        real = is.read(current);
                    }
                    out.write(total);
                } else {
                    total = new byte[1024];
                    int n=0;
                    while ((pointer = is.read(total)) != -1) {
                        out.write(total, 0, pointer);
                        //更新进度
                        n++;
                        if(n/50>=100){
                            publishProgress(99, 99, 0);
                        }else {
                            publishProgress(n/50, 99, 0);
                        }
                    }

                }
                //关闭流对象
                out.flush();
                out.close();
                is.close();
                //将获得的所有字节全部返回
                Message msg=new Message();
                msg.what=DOWN_LOAD_SUCCESS;
                msg.obj=file.getPath();
                handler.sendMessage(msg);
            }else {
                handler.sendEmptyMessage(DOWN_LOAD_FILED);
            }
            return total;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //更新进度条回调
    @Override
    protected void onProgressUpdate(Integer... values) {
        if(values[2]!=0) {
            if(values[1]>1024) {
                progressDialog.setMessage(
                        String.format("已读%d M, 共%d M",//字节为单位
                                values[1] / 1024/1024, values[2] / 1024 / 1024));//将values[1]赋给第一个%d,第二个同理
            }else {
                progressDialog.setMessage(
                        String.format("已读%d kb, 共%d M",//字节为单位
                                values[1] / 1024, values[2] / 1024 / 1024));//将values[1]赋给第一个%d,第二个同理
            }
        }else {
            progressDialog.setMessage(
                    String.format("正在下载... ..."));
        }

        progressDialog.setProgress(values[0]);//进度动态提示
        Message message001 = handler.obtainMessage(0x001);
        message001.obj=values[0];
        handler.sendMessage(message001);
    }

    //任务被取消时回调
    @Override
    protected void onCancelled() {
        super.onCancelled();
        Toast.makeText(context,"任务已取消",Toast.LENGTH_SHORT).show();
    }

    //任务结束后回调
    @Override
    protected void onPostExecute(byte[] result) {
        progressDialog.dismiss();//关闭对话框
//        Bitmap bitmap = null;
//        if(flag){
//            bitmap = BitmapFactory.decodeByteArray( result, 0, result.length);
//            //iv.setImageBitmap(bitmap);
//            Message message002 = handler.obtainMessage(0x002);
//            message002.obj=bitmap;
//            handler.sendMessage(message002);
//        }
//        flag = false;
//
//        //回收内存
//        if(!flag && !bitmap.isRecycled()){
//            //bitmap.recycle();
//        }
    }
}