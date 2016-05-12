package com.tiago.finalyearproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.facebook.Profile;
import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.model.AppEvent;
import com.tiago.finalyearproject.model.Core;

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


        final ImageView button = (ImageView) thisView.findViewById(R.id.start_to_create_event_button);
        button.setImageResource(R.drawable.plus_sign);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(intent);
            }
        });


//        List<AppEvent> eventsList = new ArrayList<AppEvent>();
//        eventsList.add(new AppEvent("Drinks at Pub", AppEvent.EventType.BAR, UserEvent.UserEventState.INVITED));
//        eventsList.add(new AppEvent("Friend's Football", AppEvent.EventType.SPORTS, UserEvent.UserEventState.GOING));
//        eventsList.add(new AppEvent("Drinks at Dylan's", AppEvent.EventType.BAR, UserEvent.UserEventState.IDLE));
//        eventsList.add(new AppEvent("Football Championship", AppEvent.EventType.SPORTS, UserEvent.UserEventState.IDLE));
//        eventsList.add(new AppEvent("Handball Match", AppEvent.EventType.SPORTS, UserEvent.UserEventState.IDLE));
//        eventsList.add(new AppEvent("Friend's Basketball", AppEvent.EventType.SPORTS, UserEvent.UserEventState.NOT_GOING));
//
//        createEventsListView(eventsList);

        requestEvents();

        return rootView;
    }



    private void requestEvents() {
        Profile profile = Profile.getCurrentProfile();

        ClientMessage clientRequestMessage = new ClientMessage();
        clientRequestMessage.setMessageType(ClientMessage.ClientMessageType.REQUEST_EVENTS);
//            User user= new User(null,profile.getRegId(),profile.getFirstName(),profile.getLastName());
        String msgId = Core.getInstance().sendRequest((AppAbstractFragmentActivity) getActivity(), clientRequestMessage);
        clientRequestMessage.setMessageId(msgId);
        ((AppAbstractFragmentActivity) getActivity()).addMessageToPendingClientMessages(clientRequestMessage);
    }

    public void createEventsListView(final List<AppEvent> eventsList) {


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                eventsAdapter = new EventsAdapter(getActivity(), eventsList);

                eventsListView = (ListView) thisView.findViewById(R.id.eventsListView);

                // Tells the ListView what data to use
                eventsListView.setAdapter(eventsAdapter);

            }
        });
    }

}
