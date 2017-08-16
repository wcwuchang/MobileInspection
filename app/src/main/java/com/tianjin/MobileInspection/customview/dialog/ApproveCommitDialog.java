package com.tianjin.MobileInspection.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;

/**
 * Created by wuchang on 2017-8-11.
 */
public class ApproveCommitDialog extends Dialog{

    private View view;
    private TextView title;
    private EditText content;
    private Button cancel;
    private Button commit;
    private ApproveCommitListener listener;

    public interface ApproveCommitListener{
        public void ApproveCancel();
        public void ApproveCommit(String msg);
    }

    public void setListener(ApproveCommitListener listener){
        this.listener=listener;
    }

    public ApproveCommitDialog(Context context) {
        super(context);
        view= LayoutInflater.from(context).inflate(R.layout.dialog_approve_commit,null);
        title=(TextView)view.findViewById(R.id.tv_approve_title);
        content=(EditText)view.findViewById(R.id.edt_approve_content);
        cancel=(Button)view.findViewById(R.id.btn_approve_concal);
        commit=(Button)view.findViewById(R.id.btn_approve_commit);

        setContentView(view);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.ApproveCancel();
                }
            }
        });

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    String str=content.getText().toString();
                    listener.ApproveCommit(str);
                }
            }
        });
    }





}
