package com.tiago.finalyearproject.view;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.Profile;
import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.gcm.ServerMessage;
import com.tiago.finalyearproject.model.Core;
import com.tiago.finalyearproject.model.User;

/**
 * Created by Tiago on 14/03/2016.
 */
public class LoginActivity extends AppAbstractFragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

    }

    public void createUser() {
//        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

//        if(prefs.getString(Constants.KEY_REG_ID, null) == null) {

            Profile profile = Profile.getCurrentProfile();

            ClientMessage clientRequestMessage = new ClientMessage();
            clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.CREATE_USER);
//            User user= new User(null,profile.getId(),profile.getFirstName(),profile.getLastName());
            clientRequestMessage.setFacebookId(profile.getId());
            clientRequestMessage.setUserName(profile.getFirstName());
            clientRequestMessage.setUserSurname(profile.getLastName());
            String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
            clientRequestMessage.setMessageId(msgId);
            setPendingClientMessage(clientRequestMessage);


//            Intent intent = new Intent(LoginActivity.this, EventsActivity.class);
//            startActivity(intent);
//        createRegId();
//        }
    }


    @Override
    protected void treatValidMessage(ServerMessage serverMessage) {
        if(serverMessage.getServerMessageType()== ServerMessage.ServerMessageType.REPLY_SUCCES){
            Intent intent = new Intent(LoginActivity.this, AddFriendsFromFBActivity.class);
            startActivity(intent);
        }else if(serverMessage.getServerMessageType()== ServerMessage.ServerMessageType.REPLY_ERROR){
            System.out.println("Reply ERROR from user registration: "+serverMessage.getMessageError());
        }

    }
}
