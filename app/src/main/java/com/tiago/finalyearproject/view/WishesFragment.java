package com.tiago.finalyearproject.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.model.Friendship;
import com.tiago.finalyearproject.model.Wish;
import com.tiago.finalyearproject.model.Core;
import com.tiago.finalyearproject.model.User;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Tiago on 21/03/2016.
 */
public class WishesFragment extends Fragment {

    private View thisView;
    private WishesAdapter wishesAdapter;
    private ListView activitiesListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wishes, container, false);
        thisView = rootView;

        final ImageView refresh = (ImageView) thisView.findViewById(R.id.refresh_wish_image_view);
        refresh.setVisibility(View.INVISIBLE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestWishes();
            }
        });

        final ImageView button = (ImageView) thisView.findViewById(R.id.start_to_create_wish_image_view);
        button.setImageResource(R.drawable.plus_sign);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateWishActivity.class);
                startActivity(intent);
            }
        });


        requestWishes();

        return rootView;
    }

    public void treatWishesListReceived(final List<Wish> wishesList) {
        final ImageView refresh = (ImageView) thisView.findViewById(R.id.refresh_wish_image_view);
        refresh.setVisibility(View.INVISIBLE);

        setUsersPicturesFromWishesList(wishesList);

    }

    public void setUsersPicturesFromWishesList(final List<Wish> wishesList) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                for(Wish activity: wishesList) {
                    try {
                        User user = activity.getWishOwner();
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

                createActivitiesListView(wishesList);
            }
        }.execute(null, null, null);

    }


    public void requestWishes() {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.REQUEST_WISHES);
        String msgId = Core.getInstance().sendRequest((AppAbstractFragmentActivity) getActivity(), clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        ((AppAbstractFragmentActivity) getActivity()).addMessageToPendingClientMessages(clientRequestMessage);
    }

    public void joinWish(int wishId) {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.JOIN_WISH);
        clientRequestMessage.setWishId(wishId);
        String msgId = Core.getInstance().sendRequest((AppAbstractFragmentActivity) getActivity(), clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        ((AppAbstractFragmentActivity) getActivity()).addMessageToPendingClientMessages(clientRequestMessage);
    }

    public void leaveWish(int wishId) {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.LEAVE_WISH);
        clientRequestMessage.setWishId(wishId);
        String msgId = Core.getInstance().sendRequest((AppAbstractFragmentActivity) getActivity(), clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        ((AppAbstractFragmentActivity) getActivity()).addMessageToPendingClientMessages(clientRequestMessage);
    }

    private void createActivitiesListView(final List<Wish> wishesList) {


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wishesAdapter = new WishesAdapter(getActivity(), wishesList);

                activitiesListView = (ListView) thisView.findViewById(R.id.wishesListView);

                // Tells the ListView what data to use
                activitiesListView.setAdapter(wishesAdapter);

            }
        });
    }

    public void setRefresh(final boolean b) {

        Handler mainHandler = new Handler(getActivity().getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                if (b) {
                    ((ImageView) thisView.findViewById(R.id.refresh_wish_image_view)).setVisibility(View.VISIBLE);
                } else {
                    ((ImageView) thisView.findViewById(R.id.refresh_wish_image_view)).setVisibility(View.INVISIBLE);
                }
            }
        };
        mainHandler.post(myRunnable);

    }

}
