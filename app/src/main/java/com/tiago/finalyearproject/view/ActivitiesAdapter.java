package com.tiago.finalyearproject.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.model.AppActivity;
import com.tiago.finalyearproject.model.AppEvent;
import com.tiago.finalyearproject.model.UserActivity;
import com.tiago.finalyearproject.model.UserEvent;

import java.util.List;

/**
 * Created by Tiago on 15/03/2016.
 */

// We can create custom adapters
class ActivitiesAdapter extends ArrayAdapter<AppActivity> {


    CheckBox[] checkBoxArray;

    public ActivitiesAdapter(Context context, List<AppActivity> activities){

        super(context, R.layout.add_friends_row_layout, activities);
        checkBoxArray = new CheckBox[activities.size()];


    }

    // Override getView which is responsible for creating the rows for our list
    // position represents the index we are in for the array.

    // convertView is a reference to the previous view that is available for reuse. As
    // the user scrolls the information is populated as needed to conserve memory.

    // A ViewGroup are invisible containers that hold a bunch of views and
    // define their layout properties.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // The LayoutInflator puts a layout into the right View
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        // inflate takes the resource to load, the parent that the resource may be
        // loaded into and true or false if we are loading into a parent view.
        View theView = theInflater.inflate(R.layout.activities_row_layout, parent, false);

        theView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        // We retrieve the text from the array
        AppActivity activity = getItem(position);

        Profile profile = Profile.getCurrentProfile();
        UserActivity.UserActivityState state = activity.getCurrentUserActivityState(profile.getId());

        // Get the ImageView in the layout
        ImageView joinActivityImage = (ImageView) theView.findViewById(R.id.join_activity_ImageView);

        if(state == UserActivity.UserActivityState.IN || state == UserActivity.UserActivityState.OWNER){
            joinActivityImage.setImageResource(R.drawable.joined);
        }
        else if(state == UserActivity.UserActivityState.OUT){
            joinActivityImage.setImageResource(R.drawable.not_joined);
        }

        // Get the TextView we want to edit
        TextView userNameTextView = (TextView) theView.findViewById(R.id.userNameTextView);

        // Put the next TV Show into the TextView
        userNameTextView.setText(activity.getActivityOwner().getName());

        // Get the TextView we want to edit
        TextView activityNameTextView = (TextView) theView.findViewById(R.id.activityNameTextView);

        // Put the next TV Show into the TextView
        activityNameTextView.setText(activity.getName());

        // Get the TextView we want to edit
        TextView activityDateTextView = (TextView) theView.findViewById(R.id.activityDateTextView);

        String hour = String.format("%02d", activity.getActivityDateTime().getHours());
        String minute = String.format("%02d", activity.getActivityDateTime().getMinutes());
        String day = String.format("%02d", activity.getActivityDateTime().getDate());
        String month = String.format("%02d", activity.getActivityDateTime().getMonth()+1);
        String year = String.format("%02d", activity.getActivityDateTime().getYear()+1900);
        // Put the next TV Show into the TextView
        activityDateTextView.setText(hour+":"+minute+" "+day+"/"+month+"/"+year);

        // Get the ImageView in the layout
        ImageView userPictureImageView = (ImageView) theView.findViewById(R.id.userPictureImageView);

        userPictureImageView.setImageBitmap(activity.getActivityOwner().getProfilePicture());

        // Get the ImageView in the layout
        ImageView categoryImageView = (ImageView) theView.findViewById(R.id.activityCategoryImageView);


        // We can set a ImageView like this
        AppActivity.ActivityType activityType = activity.getActivityType();
        if(activityType == AppActivity.ActivityType.SPORTS) {
            categoryImageView.setImageResource(R.drawable.football);
        }else if (activityType == AppActivity.ActivityType.DRINKS){
            categoryImageView.setImageResource(R.drawable.drink);
        }

        return theView;
    }

    public CheckBox[] getCheckBoxArray() {
        return checkBoxArray;
    }



    }