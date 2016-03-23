package com.tiago.finalyearproject.view;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.gcm.ServerMessage;

/**
 * Created by Tiago on 14/03/2016.
 */
public class CreateEventActivity extends AppAbstractFragmentActivity implements ActionBar.TabListener {


    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Feed", "Events", "Friends" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    protected void treatValidMessage(ServerMessage serverMessage) {
        ServerMessage.ServerMessageType serverMessageType = serverMessage.getServerMessageType();
        if (serverMessageType== ServerMessage.ServerMessageType.REPLY_SUCCES || serverMessageType== ServerMessage.ServerMessageType.REPLY_ERROR){
            ClientMessage.ClientMessageType clientMessageType = getPendingClientMessage().getMessageType();
            switch (clientMessageType) {
                case REQUEST_EVENTS:
                    if(serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES){
//                        List<AppEvent> eventsList = ServerMessage.getEventsList();
//                                ((EventsFragment) mAdapter.getItem(1)).createEventsListView();
                    }else{

                }
                    break;

                default:
                    // Statements
            }

        }else if(serverMessageType == ServerMessage.ServerMessageType.NOTIFY_FRIENDSHIP_REQUEST_RECEIVED){

        }

    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
}
