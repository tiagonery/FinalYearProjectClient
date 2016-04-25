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
import android.widget.ListView;

import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
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


        final Button button = (Button) thisView.findViewById(R.id.start_to_create_wish_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateWishActivity.class);
                startActivity(intent);
            }
        });

//
//        List<Wish> activitiesList = new ArrayList<Wish>();
//
//        Profile profile = Profile.getCurrentProfile();
//        User user = new User("regid123", profile.getId(), profile.getFirstName(), profile.getLastName());
//        List<UserWish> userWishList = new ArrayList<UserWish>();
//        userWishList.add(new UserWish(profile.getId(),1));
//        Date date = new Date();
//        activitiesList.add(new Wish(1,"Play Football",date, AppEvent.EventType.SPORTS, user, userWishList));
//
//        setUsersPicturesFromActivities(activitiesList);

        requestWishes();

        return rootView;
    }


    public void setUsersPicturesFromActivities(final List<Wish> wishesList) {
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


    private void requestWishes() {
        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.REQUEST_WISHES);
        String msgId = Core.getInstance().sendRequest((AppAbstractFragmentActivity) getActivity(), clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        ((AppAbstractFragmentActivity) getActivity()).getPendingClientMessages().add(clientRequestMessage);
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
}
