package com.tiago.finalyearproject.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.model.AppEvent;
import com.tiago.finalyearproject.model.Core;
import com.tiago.finalyearproject.model.User;
import com.tiago.finalyearproject.model.UserEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiago on 21/03/2016.
 */
public class EventsFragment extends Fragment {

    private View thisView;
    private EventsAdapter eventsAdapter;
    private ListView eventsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        thisView = rootView;


        final Button button = (Button) thisView.findViewById(R.id.boredButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        List<AppEvent> eventsList = new ArrayList<AppEvent>();
        eventsList.add(new AppEvent("Friend's Football", AppEvent.EventActivity.SPORTS, UserEvent.UserEventState.GOING));
        eventsList.add(new AppEvent("Friend's Basketball", AppEvent.EventActivity.SPORTS, UserEvent.UserEventState.NOT_GOING));
        eventsList.add(new AppEvent("Drinks at Dylan's", AppEvent.EventActivity.DRINKS, UserEvent.UserEventState.IDLE));
        eventsList.add(new AppEvent("Football Championship", AppEvent.EventActivity.SPORTS, UserEvent.UserEventState.IDLE));
        eventsList.add(new AppEvent("Drinks at Pub", AppEvent.EventActivity.DRINKS, UserEvent.UserEventState.INVITED));

        createEventsListView(eventsList);

        return rootView;
    }
    public void createEventsListView(List<AppEvent> eventsList) {
        eventsAdapter = new EventsAdapter(getContext(), eventsList);

        eventsListView = (ListView) thisView.findViewById(R.id.eventsListView);


        // Tells the ListView what data to use
        eventsListView.setAdapter(eventsAdapter);
    }

}
