package com.tiago.finalyearproject.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.tiago.finalyearproject.Constants;
import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.gcm.ServerMessage;
import com.tiago.finalyearproject.model.Core;

import org.json.JSONException;
import org.json.JSONObject;

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
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(prefs.getString(Constants.KEY_REG_ID, null) == null) {

            ClientMessage clientRequestMessage = new ClientMessage();
            clientRequestMessage.setCategory(ClientMessage.ClientMessageType.CREATE_USER);
            String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
            clientRequestMessage.setMessageId(msgId);
            setPendingClientMessage(clientRequestMessage);


            Intent intent = new Intent(LoginActivity.this, EventsActivity.class);
            startActivity(intent);
//        createRegId();
        }
    }


    @Override
    protected void treatValidMessage(ServerMessage serverMessage) {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity", response.toString());

                        // Application code
                        try {
                            String friends = object.getString("user_friends");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "friends");
        request.setParameters(parameters);
        request.executeAsync();


        Intent intent = new Intent(LoginActivity.this, EventsActivity.class);
        startActivity(intent);
    }
}
