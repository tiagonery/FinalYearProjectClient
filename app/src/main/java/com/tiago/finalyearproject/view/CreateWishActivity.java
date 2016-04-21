package com.tiago.finalyearproject.view;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
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
public class CreateWishActivity extends AppAbstractFragmentActivity {

    private EditText editWishName;
    private AppEvent.EventType eventType;
    private Button createWishButton;
    private EventTypeAdapter eventTypeAdapter;
    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_wish_activity);


        editWishName = (EditText) findViewById(R.id.enter_wish_name_edit_text);

        gridview = (GridView) findViewById(R.id.categories_grid_view);

        setEventTypeAdapter(new EventTypeAdapter(this));
        gridview.setAdapter(getEventTypeAdapter());

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                setEventType(AppEvent.EventType.valueOf(getEventTypeAdapter().mThumbIds[position].getNumber()));

                gridview.setItemChecked(position, true);
            }
        });


        createWishButton = (Button) findViewById(R.id.create_wish_button);
        createWishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



    public AppEvent.EventType getEventType() {
        return eventType;
    }

    public void setEventType(AppEvent.EventType eventType) {
        this.eventType = eventType;
    }

    public EditText getEditWishName() {
        return editWishName;
    }

    public void setEditWishName(EditText editWishName) {
        this.editWishName = editWishName;
    }

    public Button getCreateWishButton() {
        return createWishButton;
    }

    public void setCreateWishButton(Button createWishButton) {
        this.createWishButton = createWishButton;
    }

    public EventTypeAdapter getEventTypeAdapter() {
        return eventTypeAdapter;
    }

    public void setEventTypeAdapter(EventTypeAdapter eventTypeAdapter) {
        this.eventTypeAdapter = eventTypeAdapter;
    }


    @Override
    protected void treatValidMessage(ServerMessage serverMessage) {
        if(serverMessage.getServerMessageType()== ServerMessage.ServerMessageType.REPLY_SUCCES){
            Intent intent = new Intent(CreateWishActivity.this, HomeActivity.class);
            startActivity(intent);
        }else if(serverMessage.getServerMessageType()== ServerMessage.ServerMessageType.REPLY_ERROR){
            System.out.println("Could not create Event");
        }

    }
}
