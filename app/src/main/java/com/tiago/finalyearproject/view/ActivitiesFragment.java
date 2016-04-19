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

import com.facebook.Profile;
import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.model.AppActivity;
import com.tiago.finalyearproject.model.AppEvent;
import com.tiago.finalyearproject.model.Core;
import com.tiago.finalyearproject.model.User;
import com.tiago.finalyearproject.model.UserActivity;
import com.tiago.finalyearproject.model.UserEvent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Tiago on 21/03/2016.
 */
public class ActivitiesFragment extends Fragment {

    private View thisView;
    private ActivitiesAdapter activitiesAdapter;
    private ListView activitiesListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_activities, container, false);
        thisView = rootView;


        final Button button = (Button) thisView.findViewById(R.id.start_to_create_activity_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(intent);
            }
        });


        List<AppActivity> activitiesList = new ArrayList<AppActivity>();

        Profile profile = Profile.getCurrentProfile();
        User user = new User("regid123", profile.getId(), profile.getFirstName(), profile.getLastName());
        List<UserActivity> userActivityList = new ArrayList<UserActivity>();
        userActivityList.add(new UserActivity(profile.getId(),1, UserActivity.UserActivityState.OWNER));
        Date date = new Date();
        activitiesList.add(new AppActivity(1,"Play Football",date, AppActivity.ActivityType.SPORTS, user, userActivityList));

        setUsersPicturesFromActivities(activitiesList);

//        requestActivities();

        return rootView;
    }


    private void setUsersPicturesFromActivities(final List<AppActivity> activitiesList) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                for(AppActivity activity: activitiesList) {
                    try {
                        User user = activity.getActivityOwner();
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

                createActivitiesListView(activitiesList);
            }
        }.execute(null, null, null);

    }


    private void requestActivities() {
        Profile profile = Profile.getCurrentProfile();

        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.REQUEST_ACTIVITIES);
//            User user= new User(null,profile.getRegId(),profile.getFirstName(),profile.getLastName());
        String msgId = Core.getInstance().sendRequest((AppAbstractFragmentActivity) getActivity(), clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        ((AppAbstractFragmentActivity) getActivity()).setPendingClientMessage(clientRequestMessage);
    }

    public void createActivitiesListView(final List<AppActivity> activitiesList) {


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activitiesAdapter = new ActivitiesAdapter(getActivity(), activitiesList);

                activitiesListView = (ListView) thisView.findViewById(R.id.activitiesListView);

                // Tells the ListView what data to use
                activitiesListView.setAdapter(activitiesAdapter);

            }
        });
    }
}
