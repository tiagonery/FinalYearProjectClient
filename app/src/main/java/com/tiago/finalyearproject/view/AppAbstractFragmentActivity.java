package com.tiago.finalyearproject.view;

import android.support.v7.app.AppCompatActivity;

import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.gcm.ServerMessage;

/**
 * Created by Tiago on 09/03/2016.
 */
public abstract class AppAbstractFragmentActivity extends AppCompatActivity {


    private ClientMessage pendingClientMessage;



    public ClientMessage getPendingClientMessage() {
        return pendingClientMessage;
    }

    public void setPendingClientMessage(ClientMessage pendingClientMessage) {
        this.pendingClientMessage = pendingClientMessage;
    }

    public void handlePendingMessage(ServerMessage message){
        if (message.getMessageRepliedId().equals(pendingClientMessage.getMessageId())){
            treatValidMessage(message);
        }
    }

    protected abstract void treatValidMessage(ServerMessage serverMessage);

}
