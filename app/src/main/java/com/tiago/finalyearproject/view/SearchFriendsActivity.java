package com.tiago.finalyearproject.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.gcm.ServerMessage;
import com.tiago.finalyearproject.model.Core;
import com.tiago.finalyearproject.model.Friendship;
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
public class SearchFriendsActivity extends AppAbstractFragmentActivity {


    private SearchListAdapter theAdapter;
    private ListView theListView;
    private List<User> friendsFromFB;
    private EditText searchFriendEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_friends);


        searchFriendEditText = (EditText) findViewById(R.id.search_friend_EditText);
        final ImageView search = (ImageView) findViewById(R.id.search_friends_search_user_imageView);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                requestUserByName(searchFriendEditText.getText().toString());

            }
        });
    }

    public void setUsersPicturesFromFriendsList(final List<User> friendsList, final List<Friendship> friendshipList) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                for(User user: friendsList) {
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

                createActivitiesListView(friendsList, friendshipList);
            }
        }.execute(null, null, null);

    }

    public void acceptFriendship(Friendship friendship) {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.ACCEPT_FRIENDSHIP);
        clientRequestMessage.setFriendship(friendship);
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        getPendingClientMessages().add(clientRequestMessage);
    }

    public void refuseFriendship(Friendship friendship) {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.REFUSE_FRIENDSHIP);
        clientRequestMessage.setFriendship(friendship);
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        getPendingClientMessages().add(clientRequestMessage);
    }
    public void sendFriendRequest(List<String> listOfUsersIdsToAdd) {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.REQUEST_FRIENDSHIP_FB_IDS_LIST);
        clientRequestMessage.setFacebookIdsList(listOfUsersIdsToAdd);
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        getPendingClientMessages().add(clientRequestMessage);
    }


            public void requestUserByName(String userName) {
                ClientMessage clientRequestMessage = new ClientMessage();
                clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.SEARCH_USER);
                clientRequestMessage.setUserName(userName);
                String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
                clientRequestMessage.setMessageId(msgId);
                getPendingClientMessages().add(clientRequestMessage);

            }

    private void createActivitiesListView(final List<User> friendsList, final List<Friendship> friendshipList) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        theAdapter = new SearchListAdapter(SearchFriendsActivity.this, friendsList, friendshipList);

                        theListView = (ListView) findViewById(R.id.search_friends_list_ListView);

                        // Tells the ListView what data to use
                        theListView.setAdapter(theAdapter);

                    }
                });
    }

    @Override
    protected void treatValidMessage(ServerMessage serverMessage, ClientMessage.ClientMessageType clientMessageType) {
        ServerMessage.ServerMessageType serverMessageType = serverMessage.getServerMessageType();
        if (serverMessageType== ServerMessage.ServerMessageType.REPLY_SUCCES || serverMessageType== ServerMessage.ServerMessageType.REPLY_ERROR){
            switch (clientMessageType) {
                case SEARCH_USER:
                    if(serverMessage.getServerMessageType()== ServerMessage.ServerMessageType.REPLY_SUCCES){
                        List<User> usersList = serverMessage.getUsersList();
                        List<Friendship> friendshipList = serverMessage.getFriendshipList();
                        setUsersPicturesFromFriendsList(usersList, friendshipList);
                    }else if(serverMessage.getServerMessageType()== ServerMessage.ServerMessageType.REPLY_ERROR){
                        System.out.println("Reply ERROR from adding friends: "+serverMessage.getMessageError());
                    }
                    break;
                case ACCEPT_FRIENDSHIP:
                    if(serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES){
                        Intent intent = new Intent(SearchFriendsActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }else if(serverMessageType == ServerMessage.ServerMessageType.REPLY_ERROR){
//                        ((WishesFragment) mAdapter.getItem(2)).requestWishes();
                    }
                case REFUSE_FRIENDSHIP:
                    if(serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES){
                        Intent intent = new Intent(SearchFriendsActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }else if(serverMessageType == ServerMessage.ServerMessageType.REPLY_ERROR){
//                        ((WishesFragment) mAdapter.getItem(2)).requestWishes();
                    }
                    break;
                case REQUEST_FRIENDSHIP_FB_IDS_LIST:
                    if(serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES){
                        Intent intent = new Intent(SearchFriendsActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }else if(serverMessageType == ServerMessage.ServerMessageType.REPLY_ERROR){
//                        ((WishesFragment) mAdapter.getItem(2)).requestWishes();
                    }
                    break;
            }
        }
    }

}
