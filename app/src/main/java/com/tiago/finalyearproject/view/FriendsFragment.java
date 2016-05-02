package com.tiago.finalyearproject.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.model.Core;
import com.tiago.finalyearproject.model.Friendship;
import com.tiago.finalyearproject.model.User;
import com.tiago.finalyearproject.model.Wish;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Tiago on 21/03/2016.
 */
public class FriendsFragment extends Fragment {


    private View friendsView;
    private FriendsListAdapter friendsAdapter;
    private ListView friendsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        friendsView = rootView;


        final ImageView addFacebookFriends = (ImageView) friendsView.findViewById(R.id.add_from_fb_ImageView);
        addFacebookFriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddFriendsFromFBActivity.class);
                startActivity(intent);
            }
        });

        final ImageView searchFriends = (ImageView) friendsView.findViewById(R.id.search_user_imageView);
        searchFriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchFriendsActivity.class);
                startActivity(intent);
            }
        });


        requestFriendsList();

        return rootView;
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


    public void requestFriendsList() {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.REQUEST_FRIENDS_LIST);
        String msgId = Core.getInstance().sendRequest((AppAbstractFragmentActivity) getActivity(), clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        ((AppAbstractFragmentActivity) getActivity()).getPendingClientMessages().add(clientRequestMessage);
    }

    public void acceptFriendship(Friendship friendship) {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.ACCEPT_FRIENDSHIP);
        clientRequestMessage.setFriendship(friendship);
        String msgId = Core.getInstance().sendRequest((AppAbstractFragmentActivity) getActivity(), clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        ((AppAbstractFragmentActivity) getActivity()).getPendingClientMessages().add(clientRequestMessage);
    }

    public void refuseFriendship(Friendship friendship) {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.REFUSE_FRIENDSHIP);
        clientRequestMessage.setFriendship(friendship);
        String msgId = Core.getInstance().sendRequest((AppAbstractFragmentActivity) getActivity(), clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        ((AppAbstractFragmentActivity) getActivity()).getPendingClientMessages().add(clientRequestMessage);
    }

    private void createActivitiesListView(final List<User> friendsList, final List<Friendship> friendshipList) {


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                friendsAdapter = new FriendsListAdapter(getActivity(), friendsList, friendshipList);

                friendsListView = (ListView) friendsView.findViewById(R.id.friends_list_ListView);

                // Tells the ListView what data to use
                friendsListView.setAdapter(friendsAdapter);

            }
        });
    }
}
