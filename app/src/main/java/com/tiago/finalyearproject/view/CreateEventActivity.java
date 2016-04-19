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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.gcm.ServerMessage;
import com.tiago.finalyearproject.model.AppActivity;
import com.tiago.finalyearproject.model.AppEvent;
import com.tiago.finalyearproject.model.Core;
import com.tiago.finalyearproject.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


    private AddFriendsAdapter inviteFriendsAdapter;
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

    private RadioGroup visualizationRadioGroup;
    private RadioButton visualizationRadioButton;
//    private RadioButton invitedFriendsRadioButton;
//    private RadioButton allFriendsRadioButton;
    private RadioGroup matchingRadioGroup;
    private RadioButton matchingRadioButton;
//    private RadioButton disabledRadioButton;
//    private RadioButton friendsRadioButton;
//    private RadioButton anyoneRadioButton;

    private Button createEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_activity);


        getFriendsFromFacebook();

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
                        editTime.setText( selectedHour + ":" + selectedMinute);
                        hour = selectedHour;
                        minute = selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        activitiesSpinner = (Spinner) findViewById(R.id.activities_spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, AppActivity.ActivityType.names()); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitiesSpinner.setAdapter(spinnerArrayAdapter);





        visualizationRadioGroup = (RadioGroup) findViewById(R.id.visualization_settings_radio_group);
//        invitedFriendsRadioButton = (RadioButton) findViewById(R.id.invited_friends_radio_button);
//        allFriendsRadioButton = (RadioButton) findViewById(R.id.all_friends_radio_button);
        matchingRadioGroup = (RadioGroup) findViewById(R.id.matching_settings_radio_group);
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
                    date = originalFormat.parse(startYear + month + day );
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                date.setHours(hour);
                date.setMinutes(minute);
                newEvent.setEventDateTimeStart(date);

                newEvent.setActivity(AppActivity.ActivityType.valueOf(activitiesSpinner.getSelectedItem().toString()));

                int selectedId = visualizationRadioGroup.getCheckedRadioButtonId();
                visualizationRadioButton = (RadioButton) findViewById(selectedId);
                newEvent.setEventVisualizationPrivacy(AppEvent.EventVisualizationPrivacy.valueOf(visualizationRadioButton.getTag().toString()));

                int selectedMatchId = matchingRadioGroup.getCheckedRadioButtonId();
                matchingRadioButton = (RadioButton) findViewById(selectedMatchId);
                newEvent.setEventMatchingPrivacy(AppEvent.EventMatchingPrivacy.valueOf(matchingRadioButton.getTag().toString()));

                createEvent(newEvent, listOfUsersIds);

            }
        });

    }

    private void createEvent(AppEvent event, List<String> invitedUsersIds) {

        Profile profile = Profile.getCurrentProfile();

        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.CREATE_EVENT);
//            User user= new User(null,profile.getRegId(),profile.getFirstName(),profile.getLastName());
        clientRequestMessage.setEvent(event);
        clientRequestMessage.setFacebookIdsList(invitedUsersIds);
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        setPendingClientMessage(clientRequestMessage);

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
            month =  String.format("%02d", monthOfYear);;
            day = String.format("%02d", dayOfMonth);
            updateDateDisplay();
        }
    }

    private void updateDateDisplay() {
        editDate.setText(day+"/"+month+"/"+startYear);
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
                                Toast.makeText(CreateEventActivity.this, "Could not connect to Facebook", Toast.LENGTH_LONG);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
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
        inviteFriendsAdapter = new AddFriendsAdapter(this, usersList);

        theListView = (ListView) findViewById(R.id.invite_friends_list_view);


        // Tells the ListView what data to use
        theListView.setAdapter(inviteFriendsAdapter);
    }

    public AddFriendsAdapter getInviteFriendsAdapter() {
        return inviteFriendsAdapter;
    }

    public void setInviteFriendsAdapter(AddFriendsAdapter inviteFriendsAdapter) {
        this.inviteFriendsAdapter = inviteFriendsAdapter;
    }

    public ListView getTheListView() {
        return theListView;
    }

    public void setTheListView(ListView theListView) {
        this.theListView = theListView;
    }

    @Override
    protected void treatValidMessage(ServerMessage serverMessage) {
        if(serverMessage.getServerMessageType()== ServerMessage.ServerMessageType.REPLY_SUCCES){
            Intent intent = new Intent(CreateEventActivity.this, HomeActivity.class);
            startActivity(intent);
        }else if(serverMessage.getServerMessageType()== ServerMessage.ServerMessageType.REPLY_ERROR){
            System.out.println("Could not create Event");
        }

    }
}
