package com.tiago.finalyearproject.view;

import android.support.v7.app.AppCompatActivity;

import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.gcm.ServerMessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Tiago on 09/03/2016.
 */
public abstract class AppAbstractFragmentActivity extends AppCompatActivity {


    private List<ClientMessage> pendingClientMessages;



    public List<ClientMessage> getPendingClientMessages() {
        if (pendingClientMessages == null){
            pendingClientMessages = new ArrayList<ClientMessage>();
        }
        return pendingClientMessages;
    }

    public void setPendingClientMessages(List<ClientMessage> pendingClientMessages) {
        this.pendingClientMessages = pendingClientMessages;
    }

    public void handlePendingMessage(ServerMessage message){
        for(Iterator<ClientMessage> iterator = getPendingClientMessages().iterator(); iterator.hasNext();) {
            ClientMessage clientMessage = iterator.next();
            if (message.getMessageRepliedId().equals(clientMessage.getMessageId())) {
                treatValidMessage(message, clientMessage.getMessageType());
                iterator.remove();
            }
        }


//        for (ClientMessage clientMessage : getPendingClientMessages()) {
//            if (message.getMessageRepliedId().equals(clientMessage.getMessageId())){
//                treatValidMessage(message, clientMessage.getMessageType());
//                getPendingClientMessages().remove(clientMessage);
//            }

//        }
    }

    protected abstract void treatValidMessage(ServerMessage serverMessage, ClientMessage.ClientMessageType clientMessageType);

}
