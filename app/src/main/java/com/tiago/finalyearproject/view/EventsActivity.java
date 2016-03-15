package com.tiago.finalyearproject.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.tiago.finalyearproject.Constants;
import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ServerMessage;
import com.tiago.finalyearproject.model.Core;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tiago on 14/03/2016.
 */
public class EventsActivity extends AppAbstractFragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.events_activity);
        Profile profile = Profile.getCurrentProfile();
        TextView mTextDetails = (TextView) findViewById(R.id.textView1);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String regId = prefs.getString(Constants.KEY_REG_ID, null);

        mTextDetails.append(profile.getFirstName() + " " + profile.getLastName() + " " + profile.getId() + " regid: " + regId);


//        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
//                new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        Log.v("LoginActivity", response.toString());
//
//                        // Application code
//                        try {
//                            String friends = object.getString("user_friends");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "friends");
//        request.setParameters(parameters);
//        request.executeAsync();

    }
    @Override
    protected void treatValidMessage(ServerMessage serverMessage) {

    }
}
