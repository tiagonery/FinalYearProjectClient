package com.tiago.finalyearproject.view;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Profile;
import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.gcm.ServerMessage;
import com.tiago.finalyearproject.model.AppEvent;
import com.tiago.finalyearproject.model.Core;
import com.tiago.finalyearproject.model.User;
import com.tiago.finalyearproject.model.UserEvent;
import com.tiago.finalyearproject.model.UserWish;
import com.tiago.finalyearproject.model.Wish;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tiago on 14/03/2016.
 */
public class ManageWishActivity extends AppAbstractFragmentActivity {

    private TextView wishNameTextView;
    private ImageView wishManagementCategoryImageView;
    private ImageView deleteWishImageView;
    private Button inviteToEventButton;
    private InviteFriendsFromWishAdapter usersToInviteAdapter;
    private ListView usersToInviteListView;
    private Wish wish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_wish_activity);
        wish = (Wish)getIntent().getSerializableExtra("wish");
        wishManagementCategoryImageView = (ImageView) findViewById(R.id.wishManagementCategoryImageView);
        wishNameTextView = (TextView) findViewById(R.id.wish_name_text_view);
        deleteWishImageView = (ImageView) findViewById(R.id.delete_wish_imageview);
        inviteToEventButton = (Button) findViewById(R.id.invite_to_event_from_wish_button);


        wishNameTextView.setText(wish.getName());
        wishManagementCategoryImageView.setImageResource(wish.getEventType().getSelectedImage());

        deleteWishImageView.setImageResource(R.drawable.garbage);
        deleteWishImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWish(wish.getWishId());
            }
        });

        if(wish.getLinkedEvent()!=null){
            inviteToEventButton.setText("Invite to Event");
            inviteToEventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inviteToEvent();
                }
            });

        }else{

            inviteToEventButton.setText("Create an Event");
            inviteToEventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CheckBox[] checkBoxArray = usersToInviteAdapter.getCheckBoxArray();
                    List<String> listOfUsersIds = new ArrayList<String>();
                    for (CheckBox checkBox : checkBoxArray) {
                        if (checkBox.isChecked()) {
                            listOfUsersIds.add((String) checkBox.getTag());
                        }
                    }

                    Intent intent = new Intent(ManageWishActivity.this, CreateEventActivity.class);
                    intent.putExtra("wish", wish);
                    intent.putStringArrayListExtra("listOfUsers", (ArrayList<String>) listOfUsersIds);
                    startActivity(intent);
                }
            });
        }
        requestUsers();
    }


    private void inviteToEvent() {
        CheckBox[] checkBoxArray = usersToInviteAdapter.getCheckBoxArray();
        List<String> listOfUsersIds = new ArrayList<String>();
        for (CheckBox checkBox : checkBoxArray) {
            if (checkBox.isChecked()) {
                listOfUsersIds.add((String) checkBox.getTag());
            }
        }
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.INVITE_TO_EVENT);
        clientRequestMessage.setEventId(wish.getLinkedEvent().getEventId());
        clientRequestMessage.setFacebookIdsList(listOfUsersIds);
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        addMessageToPendingClientMessages(clientRequestMessage);
    }

    private void deleteWish(int wishId) {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.DELETE_WISH);
        clientRequestMessage.setWishId(wishId);
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        addMessageToPendingClientMessages(clientRequestMessage);
    }

    private void requestUsers() {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.REQUEST_USERS_WISH_LIST);
        clientRequestMessage.setWishId(wish.getWishId());
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        addMessageToPendingClientMessages(clientRequestMessage);
    }

    private void setUsersPictures(final List<User> usersList, final List<UserEvent> userEventList) {
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
                createListView(usersList, userEventList);
            }
        }.execute(null, null, null);

    }

    private void createListView(List<User> usersList, List<UserEvent> userEventList) {
        usersToInviteAdapter = new InviteFriendsFromWishAdapter(this, usersList, userEventList);

        usersToInviteListView = (ListView) findViewById(R.id.invite_friends_from_wish_list_view);


        // Tells the ListView what data to use
        usersToInviteListView.setAdapter(usersToInviteAdapter);
    }

    @Override
    public void treatValidMessage(ServerMessage serverMessage, ClientMessage.ClientMessageType clientMessageType) {
        ServerMessage.ServerMessageType serverMessageType = serverMessage.getServerMessageType();
        if (serverMessageType== ServerMessage.ServerMessageType.REPLY_SUCCES || serverMessageType== ServerMessage.ServerMessageType.REPLY_ERROR) {
            switch (clientMessageType) {
                case REQUEST_USERS_WISH_LIST:
                    if (serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES) {
                        List<User> usersList = serverMessage.getUsersList();
                        List<UserEvent> usersEventList = new ArrayList<UserEvent>();
                        if (wish.getLinkedEvent() != null) {
                            usersEventList = serverMessage.getUsersEventList();
                        }
                        setUsersPictures(usersList, usersEventList);
                    } else {
                    }
                    break;
                case DELETE_WISH:
                    if (serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES) {
                        Intent intent = new Intent(ManageWishActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                    }
                    break;
                case INVITE_TO_EVENT:
                    if (serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES) {
                        requestUsers();
                    } else {
                    }
                    break;

                default:
                    // Statements
            }
        }


    }
}
