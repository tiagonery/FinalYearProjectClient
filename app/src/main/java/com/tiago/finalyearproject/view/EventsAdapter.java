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
import com.tiago.finalyearproject.model.AppEvent;
import com.tiago.finalyearproject.model.UserEvent;

import java.util.List;

/**
 * Created by Tiago on 15/03/2016.
 */

// We can create custom adapters
class EventsAdapter extends ArrayAdapter<AppEvent> {


    CheckBox[] checkBoxArray;

    public EventsAdapter(Context context, List<AppEvent> events){

        super(context, R.layout.add_friends_row_layout, events);
        checkBoxArray = new CheckBox[events.size()];


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
        View theView = theInflater.inflate(R.layout.events_row_layout, parent, false);

        theView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        // We retrieve the text from the array
        AppEvent event = getItem(position);

        Profile profile = Profile.getCurrentProfile();
        UserEvent.UserEventState state = event.getCurrentUserEventState(profile.getId());
        if(state == UserEvent.UserEventState.INVITED){
            theView.setBackgroundColor(Color.parseColor("#b3b3ff"));
        }
        else if(state == UserEvent.UserEventState.GOING){
            theView.setBackgroundColor(Color.parseColor("#b3ffcc"));
        }
        else if(state == UserEvent.UserEventState.IDLE){
            theView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        else if(state == UserEvent.UserEventState.NOT_GOING){
            theView.setBackgroundColor(Color.parseColor("#ffb3b3"));
        }
        else if(state == UserEvent.UserEventState.OWNER){
            theView.setBackgroundColor(Color.parseColor("#ffffb3"));
        }

        // Get the TextView we want to edit
        TextView theTextView = (TextView) theView.findViewById(R.id.eventNameTextView);

        // Put the next TV Show into the TextView
        theTextView.setText(event.getName());

        // Get the ImageView in the layout
        ImageView theImageView = (ImageView) theView.findViewById(R.id.eventCategoryImageView);

        // We can set a ImageView like this
        AppEvent.EventActivity activity = event.getActivity();
        if(activity == AppEvent.EventActivity.SPORTS) {
            theImageView.setImageResource(R.drawable.football);
        }else if (activity == AppEvent.EventActivity.DRINKS){
            theImageView.setImageResource(R.drawable.drink);
        }

        return theView;
    }

    public CheckBox[] getCheckBoxArray() {
        return checkBoxArray;
    }



    }