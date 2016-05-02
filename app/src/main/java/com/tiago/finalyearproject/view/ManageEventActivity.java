package com.tiago.finalyearproject.view;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.tiago.finalyearproject.model.Wish;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiago on 14/03/2016.
 */
public class ManageEventActivity extends AppAbstractFragmentActivity {

    private TextView eventNameTextView;
    private TextView eventLocationTextView;
    private TextView eventDateTextView;
    private TextView usersListTextView;
    private TextView eventTimeTextView;
    private ImageView eventManagementCategoryImageView;
    private ImageView stateInEventImageView;
    private Button inviteToEventButton;
    private InviteFriendsFromEventAdapter usersToInviteAdapter;
    private ListView usersToInviteListView;
    private AppEvent event;
    private UserEvent.UserEventState userEventStateFromCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_event_activity);
        event = (AppEvent)getIntent().getSerializableExtra("event");
        eventManagementCategoryImageView = (ImageView) findViewById(R.id.eventManagementCategoryImageView);
        eventNameTextView = (TextView) findViewById(R.id.event_name_text_view);
        usersListTextView = (TextView) findViewById(R.id.event_users_list_text_view);
        eventLocationTextView = (TextView) findViewById(R.id.location_text_view);
        eventDateTextView = (TextView) findViewById(R.id.date_text_view);
        eventTimeTextView = (TextView) findViewById(R.id.event_time_text_view);
        stateInEventImageView = (ImageView) findViewById(R.id.delete_event_imageview);
        inviteToEventButton = (Button) findViewById(R.id.invite_to_event_button);

        eventNameTextView.setText(event.getName());
        eventLocationTextView.setText(event.getLocation());

        String hour = String.format("%02d", event.getEventDateTimeStart().getHours());
        String minute = String.format("%02d", event.getEventDateTimeStart().getMinutes());
        String day = String.format("%02d", event.getEventDateTimeStart().getDate());
        String month = String.format("%02d", event.getEventDateTimeStart().getMonth()+1);
        String year = String.format("%02d", event.getEventDateTimeStart().getYear()+1900);
        eventDateTextView.setText(day + "/" + month + "/" + year);
        eventTimeTextView.setText(hour + ":" + minute);

        eventManagementCategoryImageView.setImageResource(event.getEventType().getSelectedImage());


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
        clientRequestMessage.setEventId(event.getEventId());
        clientRequestMessage.setFacebookIdsList(listOfUsersIds);
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        getPendingClientMessages().add(clientRequestMessage);
    }

    private void deleteEvent() {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.DELETE_EVENT);
        clientRequestMessage.setEventId(event.getEventId());
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        getPendingClientMessages().add(clientRequestMessage);
    }

    private void leaveEvent() {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.JOIN_EVENT);
        clientRequestMessage.setEventId(event.getEventId());
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        getPendingClientMessages().add(clientRequestMessage);
    }

    private void joinEvent() {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.LEAVE_EVENT);
        clientRequestMessage.setEventId(event.getEventId());
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        getPendingClientMessages().add(clientRequestMessage);
    }


    private void requestUsers() {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.REQUEST_USERS_EVENT_LIST);
        clientRequestMessage.setEventId(event.getEventId());
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        getPendingClientMessages().add(clientRequestMessage);
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

        Profile profile = Profile.getCurrentProfile();
        UserEvent.UserEventState state = getUserEvent(userEventList, profile.getId()).getState();
        setUserEventStateFromCurrentUser(state);
        usersToInviteAdapter = new InviteFriendsFromEventAdapter(this, usersList, userEventList, state);

        usersToInviteListView = (ListView) findViewById(R.id.invite_friends_from_wish_list_view);


        // Tells the ListView what data to use
        usersToInviteListView.setAdapter(usersToInviteAdapter);
    }

    private UserEvent getUserEvent(List<UserEvent> userEventList, String id) {
        UserEvent result = new UserEvent();
        for (UserEvent userEvent: userEventList) {
            if(userEvent.getUserId().equals(id)){
                result = userEvent;
            }
        }
        return result;
    }



    public void setUserEventStateFromCurrentUser(UserEvent.UserEventState userEventStateFromCurrentUser) {
        this.userEventStateFromCurrentUser = userEventStateFromCurrentUser;
        if(userEventStateFromCurrentUser == UserEvent.UserEventState.OWNER){
            usersListTextView.setText("Friends to Invite");
            inviteToEventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inviteToEvent();
                }
            });

            stateInEventImageView.setImageResource(R.drawable.garbage);
            stateInEventImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                deleteEvent();
                }
            });
        }else if(userEventStateFromCurrentUser == UserEvent.UserEventState.GOING){
            inviteToEventButton.setVisibility(View.INVISIBLE);
            usersListTextView.setText("Friends Going");
            stateInEventImageView.setImageResource(R.drawable.joined);
            stateInEventImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                leaveEvent();
                }
            });
        }else if(userEventStateFromCurrentUser == UserEvent.UserEventState.INVITED){


            inviteToEventButton.setVisibility(View.INVISIBLE);
            usersListTextView.setText("Friends Going");
            stateInEventImageView.setImageResource(R.drawable.envelope);
            stateInEventImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                            joinEvent();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                            leaveEvent();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManageEventActivity.this);
                    builder.setMessage("Do you want to Join this Event?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                }
            });

        }else if(userEventStateFromCurrentUser == UserEvent.UserEventState.NOT_GOING){

            inviteToEventButton.setVisibility(View.INVISIBLE);
            usersListTextView.setText("Friends Going");
            stateInEventImageView.setImageResource(R.drawable.delete);
            stateInEventImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                joinEvent();
                }
            });

        }

    }
    @Override
    protected void treatValidMessage(ServerMessage serverMessage, ClientMessage.ClientMessageType clientMessageType) {
        ServerMessage.ServerMessageType serverMessageType = serverMessage.getServerMessageType();
        if (serverMessageType== ServerMessage.ServerMessageType.REPLY_SUCCES || serverMessageType== ServerMessage.ServerMessageType.REPLY_ERROR) {
            switch (clientMessageType) {
                case REQUEST_USERS_EVENT_LIST:
                    if (serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES) {
                        List<User> usersList = serverMessage.getUsersList();
                        List<UserEvent> usersEventList = serverMessage.getUsersEventList();
                        setUsersPictures(usersList, usersEventList);
                    } else {
                    }
                    break;
                case INVITE_TO_EVENT:
                    if (serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES) {
                        requestUsers();
                    } else {
                    }
                    break;
                case DELETE_EVENT:
                    if (serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES) {
                        Intent intent = new Intent(ManageEventActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                    }
                    break;
                case JOIN_EVENT:
                    if (serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES) {
                        requestUsers();
                    } else {
                    }
                    break;
                case LEAVE_EVENT:
                    if (serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES) {
                        Intent intent = new Intent(ManageEventActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                    }
                    break;

                default:
                    // Statements
            }
        }


    }
}
