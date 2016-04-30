package com.tiago.finalyearproject.view;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Tiago on 14/03/2016.
 */
public class CreateEventActivity extends AppAbstractFragmentActivity {


    private InviteFriendsToNewEventAdapter inviteFriendsAdapter;
    private ListView theListView;

    private EditText editName;
    private EditText editLocation;
    private TextView editDate;
    private TextView editTime;

    private Spinner activitiesSpinner;


    Calendar c = Calendar.getInstance();
    int startYear = c.get(Calendar.YEAR);
    int startMonth = c.get(Calendar.MONTH);
    int startDay = c.get(Calendar.DAY_OF_MONTH);
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);


    String month;
    String day;

//    private RadioGroup visualizationRadioGroup;
//    private RadioButton visualizationRadioButton;
//    private RadioButton invitedFriendsRadioButton;
//    private RadioButton allFriendsRadioButton;
//    private RadioGroup matchingRadioGroup;
//    private RadioButton matchingRadioButton;
//    private RadioButton disabledRadioButton;
//    private RadioButton friendsRadioButton;
//    private RadioButton anyoneRadioButton;

    private Button createEventButton;
    private Wish wish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_activity);

        editName = (EditText) findViewById(R.id.enter_event_name_edit_text);
        editLocation = (EditText) findViewById(R.id.enter_location_edit_text);
        editDate = (TextView) findViewById(R.id.enter_date_edit_text);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mTimePicker;
                mTimePicker =new DatePickerDialog(CreateEventActivity.this, new StartDatePicker(), startYear, startMonth, startDay);
                mTimePicker.setTitle("Select Date");
                mTimePicker.show();

            }
        });
        editTime = (TextView) findViewById(R.id.enter_time_edit_text);
        editTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editTime.setText(String.format("%02d", selectedHour)  + ":" +String.format("%02d", selectedMinute) );
                        hour = selectedHour;
                        minute = selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        activitiesSpinner = (Spinner) findViewById(R.id.activities_spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, AppEvent.EventType.names()); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitiesSpinner.setAdapter(spinnerArrayAdapter);





//        visualizationRadioGroup = (RadioGroup) findViewById(R.id.visualization_settings_radio_group);
//        invitedFriendsRadioButton = (RadioButton) findViewById(R.id.invited_friends_radio_button);
//        allFriendsRadioButton = (RadioButton) findViewById(R.id.all_friends_radio_button);
//        matchingRadioGroup = (RadioGroup) findViewById(R.id.matching_settings_radio_group);
//        disabledRadioButton = (RadioButton) findViewById(R.id.none_radio_button);
//        friendsRadioButton = (RadioButton) findViewById(R.id.friends_radio_button);
//        anyoneRadioButton = (RadioButton) findViewById(R.id.anyone_radio_button);

        createEventButton = (Button) findViewById(R.id.create_event_button);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AppEvent newEvent = new AppEvent();


                CheckBox[] checkBoxArray = getInviteFriendsAdapter().getCheckBoxArray();
                List<String> listOfUsersIds = new ArrayList<String>();
                for (CheckBox checkBox : checkBoxArray) {
                    if (checkBox.isChecked()) {
                        listOfUsersIds.add((String) checkBox.getTag());
                    }
                }
                newEvent.setName(editName.getText().toString());
                newEvent.setLocation(editLocation.getText().toString());

                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date();
                try {
                    date = originalFormat.parse(startYear + month + day);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                date.setHours(hour);
                date.setMinutes(minute);
                newEvent.setEventDateTimeStart(date);

                newEvent.setEventType(AppEvent.EventType.valueOf(activitiesSpinner.getSelectedItem().toString()));

//                int selectedId = visualizationRadioGroup.getCheckedRadioButtonId();
//                visualizationRadioButton = (RadioButton) findViewById(selectedId);
//                newEvent.setEventVisualizationPrivacy(AppEvent.EventVisualizationPrivacy.valueOf(visualizationRadioButton.getTag().toString()));

//                int selectedMatchId = matchingRadioGroup.getCheckedRadioButtonId();
//                matchingRadioButton = (RadioButton) findViewById(selectedMatchId);
//                newEvent.setEventMatchingPrivacy(AppEvent.EventMatchingPrivacy.valueOf(matchingRadioButton.getTag().toString()));

                createEvent(newEvent, listOfUsersIds);

            }
        });
        setWish((Wish) getIntent().getSerializableExtra("wish"));
        if(getWish()!=null){

            editName.setText(wish.getName());
            editName.setEnabled(false);
            activitiesSpinner.setSelection(getIndexFromSpinner(activitiesSpinner, wish.getEventType().name()));
            activitiesSpinner.setEnabled(false);

        }


        requestUsers();

    }



    private void requestUsers() {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.REQUEST_USERS_NEW_EVENT_LIST);
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        getPendingClientMessages().add(clientRequestMessage);
    }


    //private method of your class
    private int getIndexFromSpinner(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    private void createEvent(AppEvent event, List<String> invitedUsersIds) {

        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.CREATE_EVENT);
//            User user= new User(null,profile.getRegId(),profile.getFirstName(),profile.getLastName());
        clientRequestMessage.setEvent(event);
        clientRequestMessage.setFacebookIdsList(invitedUsersIds);
        if(getWish()==null){
            clientRequestMessage.setWishId(-1);
        }else {
            clientRequestMessage.setWishId(getWish().getWishId());
        }
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        getPendingClientMessages().add(clientRequestMessage);

    }

    class StartDatePicker implements DatePickerDialog.OnDateSetListener {

//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // TODO Auto-generated method stub
//            // Use the current date as the default date in the picker
//            DatePickerDialog dialog = new DatePickerDialog(CreateEventActivity.this, this, startYear, startMonth, startDay);
//            return dialog;
//
//        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // Do something with the date chosen by the user
            startYear = year;
            month =  String.format("%02d", monthOfYear+1);
            day = String.format("%02d", dayOfMonth);
            updateDateDisplay();
        }
    }

    private void updateDateDisplay() {
        editDate.setText(day + "/" + month + "/" + startYear);
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


    private void createListView(List<User> usersList) {

        List<String> listOfUsersIds = getIntent().getStringArrayListExtra("listOfUsers");
        inviteFriendsAdapter = new InviteFriendsToNewEventAdapter(this, usersList, listOfUsersIds);

        theListView = (ListView) findViewById(R.id.invite_friends_list_view);


        // Tells the ListView what data to use
        theListView.setAdapter(inviteFriendsAdapter);


//        List<String> listOfUsersIds = getIntent().getStringArrayListExtra("listOfUsers");
//        List<Integer> positions = new ArrayList<Integer>();
//        if(listOfUsersIds!=null) {
//                for(int i=0; i<getInviteFriendsAdapter().getCount();i++){
//                    for (String id: listOfUsersIds) {
//                        if(getInviteFriendsAdapter().getItem(i).getFacebookId().equals(id)){
//                            positions.add(i);
//                        }
//                    }
//                }

            // Execute some code after 2 seconds have passed
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    List<String> listOfUsersIds = getIntent().getStringArrayListExtra("listOfUsers");
//                    CheckBox[] checkBoxArray = getInviteFriendsAdapter().getCheckBoxArray();
//                    for (String id: listOfUsersIds) {
//                        for (CheckBox checkBox : checkBoxArray) {
//                            if (checkBox.getTag().equals(id)) {
//                                checkBox.setChecked(true);
//                            }
//                        }
//                    }
//                }
//            }, 5000);
//            while (getInviteFriendsAdapter().getCheckBoxArray().)
//            CheckBox[] checkBoxArray = getInviteFriendsAdapter().getCheckBoxArray();
//            for (String id: listOfUsersIds) {
//                for (CheckBox checkBox : checkBoxArray) {
//                    if (checkBox.getTag().equals(id)) {
//                        checkBox.setChecked(true);
//                    }
//                }
//            }

//        }
    }

    public InviteFriendsToNewEventAdapter getInviteFriendsAdapter() {
        return inviteFriendsAdapter;
    }

    public void setInviteFriendsAdapter(InviteFriendsToNewEventAdapter inviteFriendsAdapter) {
        this.inviteFriendsAdapter = inviteFriendsAdapter;
    }

    public ListView getTheListView() {
        return theListView;
    }

    public void setTheListView(ListView theListView) {
        this.theListView = theListView;
    }

    public Wish getWish() {
        return wish;
    }

    public void setWish(Wish wish) {
        this.wish = wish;
    }


    @Override
    protected void treatValidMessage(ServerMessage serverMessage, ClientMessage.ClientMessageType clientMessageType) {
        ServerMessage.ServerMessageType serverMessageType = serverMessage.getServerMessageType();
        if (serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES || serverMessageType == ServerMessage.ServerMessageType.REPLY_ERROR) {
            switch (clientMessageType) {
                case CREATE_EVENT:
                    if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.REPLY_SUCCES) {
                        Intent intent = new Intent(CreateEventActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.REPLY_ERROR) {
                        System.out.println("Could not create Event");
                    }
                    break;
                case REQUEST_USERS_NEW_EVENT_LIST:
                    if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.REPLY_SUCCES) {
                        List<User> usersList = serverMessage.getUsersList();
                        List<UserEvent> usersEventList = new ArrayList<UserEvent>();
                        setUsersPictures(usersList);

                    } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.REPLY_ERROR) {
                        System.out.println("Could not create Event");
                    }
                    break;

            }
        }
    }
}
