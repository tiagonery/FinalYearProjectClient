package com.tiago.finalyearproject.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.model.AppEvent;
import com.tiago.finalyearproject.model.UserEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiago on 15/03/2016.
 */

// We can create custom adapters
class EventsAdapter extends ArrayAdapter<AppEvent> {



    public EventsAdapter(Context context, List<AppEvent> events){

        super(context, R.layout.events_row_layout, events);


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

        // We retrieve the text from the array
        final AppEvent event = getItem(position);

        // inflate takes the resource to load, the parent that the resource may be
        // loaded into and true or false if we are loading into a parent view.
        View theView = theInflater.inflate(R.layout.events_row_layout, parent, false);

        ImageView statusImageView= (ImageView) theView.findViewById(R.id.user_event_status_ImageView);







        Profile profile = Profile.getCurrentProfile();
        UserEvent.UserEventState state = event.getCurrentUserEventState(profile.getId());
        if(state == UserEvent.UserEventState.INVITED){
            statusImageView.setImageResource(R.drawable.envelope);
        }
        else if(state == UserEvent.UserEventState.GOING){
            statusImageView.setImageResource(R.drawable.joined);
        }
        else if(state == UserEvent.UserEventState.IDLE){
            statusImageView.setImageResource(R.drawable.view);
        }
        else if(state == UserEvent.UserEventState.NOT_GOING){
            statusImageView.setImageResource(R.drawable.delete);
        }
        else if(state == UserEvent.UserEventState.OWNER){
            statusImageView.setImageResource(R.drawable.crown);
        }

        statusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ManageEventActivity.class);
                intent.putExtra("event", event);
                getContext().startActivity(intent);
            }
        });

        // Get the TextView we want to edit
        TextView theTextView = (TextView) theView.findViewById(R.id.eventNameTextView);

        // Put the next TV Show into the TextView
        theTextView.setText(event.getName());

        // Get the ImageView in the layout
        ImageView theImageView = (ImageView) theView.findViewById(R.id.eventCategoryImageView);

        // We can set a ImageView like this
        AppEvent.EventType eventType = event.getEventType();
        theImageView.setImageResource(eventType.getSelectedImage());

        return theView;
    }




    }