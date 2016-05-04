package com.tiago.finalyearproject.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.gcm.ServerMessage;
import com.tiago.finalyearproject.model.Core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Tiago on 09/03/2016.
 */
public abstract class AppAbstractFragmentActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private android.os.Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new android.os.Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message message) {
                Toast.makeText(AppAbstractFragmentActivity.this,"Could not connect to Server", Toast.LENGTH_LONG).show();
                getPendingClientMessages().clear();
            }

        };
    }

    public List<ClientMessage> getPendingClientMessages() {
        return Core.getInstance().getPendingClientMessages();
    }

    public void addMessageToPendingClientMessages(ClientMessage clientMessage) {
        getPendingClientMessages().add(clientMessage);
        if(!getProgressDialog().isShowing()) {
            getProgressDialog().show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(50000);
                        if(getProgressDialog().isShowing()){
                            getProgressDialog().dismiss();
                            Message msg = handler.obtainMessage();
                            handler.sendMessage(msg);

                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


    public void handlePendingMessage(ServerMessage serverMessage, ClientMessage.ClientMessageType clientMessageType){
        if(getPendingClientMessages().isEmpty()){
            getProgressDialog().dismiss();
        }
        treatValidMessage(serverMessage, clientMessageType);
    }

    public ProgressDialog getProgressDialog() {
        if(progressDialog==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Waiting");
            progressDialog.setMessage("Waiting for Server");
        }
        return progressDialog;
    }




        public abstract void treatValidMessage(ServerMessage serverMessage, ClientMessage.ClientMessageType clientMessageType);

}
