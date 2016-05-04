package com.tiago.finalyearproject.view;


import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.gcm.ServerMessage;
import com.tiago.finalyearproject.model.AppEvent;
import com.tiago.finalyearproject.model.Friendship;
import com.tiago.finalyearproject.model.User;
import com.tiago.finalyearproject.model.UserWish;
import com.tiago.finalyearproject.model.Wish;

import java.util.List;

/**
 * Created by Tiago on 14/03/2016.
 */
public class HomeActivity extends AppAbstractFragmentActivity implements ActionBar.TabListener {


    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Activities", "Events", "Friends" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);
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

        viewPager.setCurrentItem(0);
    }



    public TabsPagerAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected void treatValidMessage(ServerMessage serverMessage, ClientMessage.ClientMessageType clientMessageType) {
        ServerMessage.ServerMessageType serverMessageType = serverMessage.getServerMessageType();
        if (serverMessageType== ServerMessage.ServerMessageType.REPLY_SUCCES || serverMessageType== ServerMessage.ServerMessageType.REPLY_ERROR){
            switch (clientMessageType) {
                case REQUEST_EVENTS:
                    if(serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES){
                        List<AppEvent> eventsList = serverMessage.getEventsList();
                        ((EventsFragment) mAdapter.getItem(1)).createEventsListView(eventsList);
                    }else{
                    }
                    break;
                case REQUEST_WISHES:
                    if(serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES){
                        List<Wish> wishesList = serverMessage.getWishesList();
                        ((WishesFragment) mAdapter.getItem(0)).setUsersPicturesFromActivities(wishesList);
                    }else{
                    }
                    break;
                case JOIN_WISH:
                    if(serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES){
                        ((WishesFragment) mAdapter.getItem(0)).requestWishes();
                    }else if(serverMessageType == ServerMessage.ServerMessageType.REPLY_ERROR){
                        ((WishesFragment) mAdapter.getItem(0)).requestWishes();
                    }
                    break;
                case LEAVE_WISH:
                    if(serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES){
                        ((WishesFragment) mAdapter.getItem(0)).requestWishes();
                    }else if(serverMessageType == ServerMessage.ServerMessageType.REPLY_ERROR){
                        ((WishesFragment) mAdapter.getItem(0)).requestWishes();
                    }
                    break;
                case REQUEST_FRIENDS_LIST:
                    if(serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES){
                        List<User> friendsList = serverMessage.getUsersList();
                        List<Friendship> friendshipList = serverMessage.getFriendshipList();
                        ((FriendsFragment) mAdapter.getItem(2)).setUsersPicturesFromFriendsList(friendsList, friendshipList);
                    }else if(serverMessageType == ServerMessage.ServerMessageType.REPLY_ERROR){
//                        ((WishesFragment) mAdapter.getItem(2)).requestWishes();
                    }
                    break;
                case ACCEPT_FRIENDSHIP:
                    if(serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES){
                        ((FriendsFragment) mAdapter.getItem(2)).requestFriendsList();
                    }else if(serverMessageType == ServerMessage.ServerMessageType.REPLY_ERROR){
//                        ((WishesFragment) mAdapter.getItem(2)).requestWishes();
                    }
                case REFUSE_FRIENDSHIP:
                    if(serverMessageType == ServerMessage.ServerMessageType.REPLY_SUCCES){
                        ((FriendsFragment) mAdapter.getItem(2)).requestFriendsList();
                    }else if(serverMessageType == ServerMessage.ServerMessageType.REPLY_ERROR){
//                        ((WishesFragment) mAdapter.getItem(2)).requestWishes();
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
