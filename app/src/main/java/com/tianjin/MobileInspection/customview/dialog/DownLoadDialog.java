package com.tianjin.MobileInspection.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.until.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by 吴昶 on 2017-4-21.
 */
public class DownLoadDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private View view;
    private TextView mtvProgress;
    private TextView mtvHasDown;
    private TextView mtvFileSize;
    private Button mbtnCancel;
    private ProgressBar mpbBar;

    private String url;
    private String savePath;
    private String fileName;
    private int filesize=1;
    private int hasdown;

    private DownloadAsyncTas download;
    private DownLoadListener listener;
    private File file;
    /**
     * 数据格式化
     */
    private DecimalFormat dsize=new DecimalFormat("0.00");

    public DownLoadDialog(Context context){
        super(context);
    }

    public DownLoadDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    Handler handler=new Handler();

    /**
     * UI更新
     */
    Runnable runable=new Runnable() {
        @Override
        public void run() {
            Log.d("data", "run: "+hasdown);
            mpbBar.setProgress(hasdown);
            mtvHasDown.setText(dsize.format((double)hasdown/1024/1024)+"MB");
            mtvProgress.setText(dsize.format((double) hasdown/(filesize/100))+"%");
        }
    };

    /**
     *
     * @param context
     * @param url       下载链接地址
     * @param savePath  文件保存地址
     * @param listener  下载监听
     */
    public DownLoadDialog(Context context, String url, String savePath, DownLoadListener listener){
        super(context);
        this.context=context;
        this.url=url;
        this.savePath=savePath;
        this.fileName= FileUtil.getFileNameForUrl(url);
        this.listener=listener;
        view= LayoutInflater.from(context).inflate(R.layout.dialog_download,null);
        setContentView(view);
        initDialogView();
    }

    /**
     * 初始化弹框界面
     */
    private void initDialogView() {
        mtvProgress=(TextView)view.findViewById(R.id.tv_download_progress);
        mtvHasDown=(TextView)view.findViewById(R.id.tv_has_down);
        mtvFileSize=(TextView)view.findViewById(R.id.tv_size);

        mbtnCancel=(Button)view.findViewById(R.id.btn_cancel);

        mpbBar=(ProgressBar)view.findViewById(R.id.pb_down_load);
        mbtnCancel.setOnClickListener(this);
        download=new DownloadAsyncTas(this);
        download.execute(url);
        this.show();
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_cancel){
            download.cancel(true);
            //终止下载，判断文件是否已经存在，若存在则将之删除
            if(file!=null && file.exists()){
                file.delete();
            }
            this.cancel();
        }
    }

    public interface DownLoadListener{
        void DownSuccessListener(String filePath);
        void DownDefeatedListener(String failMsg);
    }

    /**
     * AsyncTask  下载
     */
    class DownloadAsyncTas extends AsyncTask<String, Integer, Integer> {

        private Integer FILE_SIZE=0x35;
        private int DOWNLOAD_PROGRESS=0x36;
        private int DOWNLOAD_SUCCESS=0x37;
        private DownLoadDialog dialog;

        public DownloadAsyncTas(DownLoadDialog dialog) {
            this.dialog=dialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //设置起始进度为0
            mpbBar.setProgress(0);
        }

        @Override
        protected Integer doInBackground(String... params) {
            //execute方法中传过来的下载的地址
            String s = params[0];
            try {
                URL url = new URL(s);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                int size = conn.getContentLength();//获取文件的大小
                publishProgress(FILE_SIZE, size);//设置进度条的最大值

                byte[] bytes = new byte[1024];
                int len = -1;
                int downlen=1;
                InputStream in = conn.getInputStream();
                file=new File(savePath,fileName);
                if(!file.exists()){
                    file.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(file);
                while( (len = in.read(bytes)) != -1 ){
                    out.write(bytes, 0, len);
                    downlen+=len;
                    publishProgress(DOWNLOAD_PROGRESS, downlen);//累计读取的长度，更新进度条
                    out.flush();
                }
                out.close();
                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                //调用监听接口告知下载失败，退出弹框
                listener.DownDefeatedListener(e.getMessage());
                dialog.cancel();
            } catch (IOException e) {
                e.printStackTrace();
                //调用监听接口告知下载失败，退出弹框
                listener.DownDefeatedListener(e.getMessage());
                dialog.cancel();
            }

            return DOWNLOAD_SUCCESS;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int value=values[0];
            if(value==FILE_SIZE){
                filesize=values[1];
                mpbBar.setMax(filesize);
                mtvFileSize.setText(dsize.format((double)filesize/1024/1024)+"MB");
            }else if(value==DOWNLOAD_PROGRESS){
                hasdown=values[1];
                handler.post(runable);
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer == DOWNLOAD_SUCCESS && file!=null){
                //下载成功后在监听中返回文件的路径
                dialog.cancel();
                listener.DownSuccessListener(file.getPath().toString());
            }
        }
    }

}

