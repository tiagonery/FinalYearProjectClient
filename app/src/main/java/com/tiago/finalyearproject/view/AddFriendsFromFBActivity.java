package com.tiago.finalyearproject.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.gcm.ServerMessage;
import com.tiago.finalyearproject.model.Core;
import com.tiago.finalyearproject.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiago on 14/03/2016.
 */
public class AddFriendsFromFBActivity extends AppAbstractFragmentActivity {


    private AddFriendsAdapter theAdapter;
    private ListView theListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friends_from_fb_activity);



        final Button button = (Button) findViewById(R.id.add_friends_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox[] checkBoxArray = getTheAdapter().getCheckBoxArray();
                List<String> listOfUsersIdsToAdd = new ArrayList<String>();
                for (CheckBox checkBox: checkBoxArray) {
                    if (checkBox.isChecked()){
                        listOfUsersIdsToAdd.add((String) checkBox.getTag());
                    }
                }
                addFriends(listOfUsersIdsToAdd);
            }
        });

        getFriendsFromFacebook();

//        ListAdapter theAdapter = new ArrayAdapter<User>(this, R.layout.add_friends_row_layout, R.id.textView1, friendsFromFB);


//        setContentView(R.layout.app_activity);
//        Profile profile = Profile.getCurrentProfile();
//        TextView mTextDetails = (TextView) findViewById(R.id.textView1);
//        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String regId = prefs.getString(Constants.KEY_REG_ID, null);
//
//        mTextDetails.append(profile.getFirstName() + " " + profile.getLastName() + " " + profile.getRegId() + " regid: " + regId);


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

    private void addFriends(List<String> listOfUsersIdsToAdd) {

        Profile profile = Profile.getCurrentProfile();

        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.REQUEST_FRIENDSHIP_FB);
//            User user= new User(null,profile.getRegId(),profile.getFirstName(),profile.getLastName());
        clientRequestMessage.setFacebookIdsList(listOfUsersIdsToAdd);
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        getPendingClientMessages().add(clientRequestMessage);

    }

    public void getFriendsFromFacebook() {
        final List<User> usersList = new ArrayList<User>();
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            if(response.getJSONObject()!=null) {
                                JSONArray data = (JSONArray) response.getJSONObject().get("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject userJson = data.getJSONObject(i);
                                    User user = new User();
                                    String id = (String) userJson.get("id");
                                    user.setFacebookId(id);
                                    user.setName((String) userJson.get("name"));


//                                user.setProfilePicture(getUserProfilePicture(id));
                                    usersList.add(user);

                                }
                                setUsersPictures(usersList);
                            }else{
                                Toast.makeText(AddFriendsFromFBActivity.this,"Could not connect to Facebook", Toast.LENGTH_LONG);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void createListView(List<User> usersList) {
        theAdapter = new AddFriendsAdapter(this, usersList);

        theListView = (ListView) findViewById(R.id.listView1);


        // Tells the ListView what data to use
        theListView.setAdapter(theAdapter);
    }


    private void setUsersPictures(final List<User> usersList) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                for(User user: usersList) {
                    try {
                        URL imageURL = new URL("https://graph.facebook.com/" + user.getFacebookId() + "/picture?type=large");
                        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                        user.setProfilePicture(bitmap);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return "";
            }
            @Override
            protected void onPostExecute(String msg) {
                createListView(usersList);
            }
        }.execute(null, null, null);

    }

    public void sendFriendsRequests() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            ClientMessage clientRequestMessage = new ClientMessage();
            clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.REQUEST_FRIENDSHIP_FB);
            clientRequestMessage.setFacebookIdsList(getIdsFromSelectedFriends());
            String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
            clientRequestMessage.setMessageId(msgId);
            getPendingClientMessages().add(clientRequestMessage);


//        createRegId();
    }

    private List<String> getIdsFromSelectedFriends() {
        List<String> result = new ArrayList<String>();
        CheckBox checkBox;
        for (int i = 0; i < theListView.getCount(); i++) {
            checkBox = (CheckBox) theListView.getChildAt(i).findViewById(R.id.checkbox_add_friends);
            if(checkBox.isChecked()) {
                result.add(((User)theAdapter.getItem(i)).getFacebookId());
            }
        }return result;
    }

    @Override
    protected void treatValidMessage(ServerMessage serverMessage, ClientMessage.ClientMessageType clientMessageType) {

        if(serverMessage.getServerMessageType()== ServerMessage.ServerMessageType.REPLY_SUCCES){
            Intent intent = new Intent(AddFriendsFromFBActivity.this, HomeActivity.class);
            startActivity(intent);
        }else if(serverMessage.getServerMessageType()== ServerMessage.ServerMessageType.REPLY_ERROR){
            System.out.println("Reply ERROR from adding friends: "+serverMessage.getMessageError());
        }
    }

    public AddFriendsAdapter getTheAdapter() {
        return theAdapter;
    }
}
