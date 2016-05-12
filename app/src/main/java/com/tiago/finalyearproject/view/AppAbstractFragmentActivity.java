package com.tiago.finalyearproject.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.tiago.finalyearproject.R;
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
                Toast.makeText(AppAbstractFragmentActivity.this,"Could not connect to Server. Try reloading", Toast.LENGTH_LONG).show();
                getPendingClientMessages().clear();
            }

        };
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Instructions \n \n " +
                        "Wishes - Show your friends what you feel like doing. You can either create a Wish or join Wishes created by your friends. The owner of each Wish will have acces to the name of every Friend who joined their Wish. From there users can create an Event which will be linked to that Wish, making it really easy to know who to Invite. \n \n " +
                        "Event - You can create an Event from a Wish or if you already know who to invite you can create an Event straight away. Be aware of Invites. You can choose if you accept it or not. \n \n " +
                        "Friends - See who are your friends and add new ones through Facebook or through their names. ")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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
                            Core.getInstance().showEventsRefreshButton();
                            Core.getInstance().showFriendsRefreshButton();
                            Core.getInstance().showWishesRefreshButton();

                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(getProgressDialog().isShowing()) {
            getProgressDialog().dismiss();
        }
        Core.getInstance().getPendingClientMessages().clear();

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
