package com.tiago.finalyearproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.gcm.ServerMessage;
import com.tiago.finalyearproject.model.Core;
import com.tiago.finalyearproject.view.AppAbstractFragmentActivity;
import com.tiago.finalyearproject.view.EventsActivity;
import com.tiago.finalyearproject.view.LoginActivity;

import java.io.IOException;

public class MainActivity extends AppAbstractFragmentActivity implements View.OnClickListener {


    private ProfileTracker mProfileTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);




//        btnRegId = (Button) findViewById(R.id.btnGetRegId);
//        etRegId = (EditText) findViewById(R.id.etRegId);
//
//        btnRegId.setOnClickListener(this);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setCategory(ClientMessage.ClientMessageType.CREATE_USER);
        String msgId = Core.getInstance().sendRequest(this, clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        setPendingClientMessage(clientRequestMessage);
//        createRegId();
    }


    @Override
    protected void treatValidMessage(ServerMessage serverMessage) {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(Profile.getCurrentProfile() == null) {
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                    // profile2 is the new profile
                    Log.v("facebook - profile", profile2.getFirstName());
                    mProfileTracker.stopTracking();
                }
            };
            mProfileTracker.startTracking();
        }
        Profile profile = Profile.getCurrentProfile();

        if(profile == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);

        }else{
            Intent intent = new Intent(MainActivity.this, EventsActivity.class);
            startActivity(intent);
        }

    }
}
