package com.tiago.finalyearproject.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.model.User;

import java.util.List;

/**
 * Created by Tiago on 15/03/2016.
 */

// We can create custom adapters
class InviteFriendsToNewEventAdapter extends ArrayAdapter<User> {


    CheckBox[] checkBoxArray;
    List<String> selectedFriendsId;

    public InviteFriendsToNewEventAdapter(Context context, List<User> users, List<String> selectedFriendsId){

        super(context, R.layout.add_friends_row_layout, users);
        checkBoxArray = new CheckBox[users.size()];
       this.selectedFriendsId = selectedFriendsId;


    }

    // Override getView which is responsible for creating the rows for our list
    // position represents the index we are in for the array.

    // convertView is a reference to the previous view that is available for reuse. As
    // the user scrolls the information is populated as needed to conserve memory.

    // A ViewGroup are invisible containers that hold a bunch of views and
    // define their layout properties.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // The LayoutInflator puts a layout into the right View
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        // inflate takes the resource to load, the parent that the resource may be
        // loaded into and true or false if we are loading into a parent view.
        View theView = theInflater.inflate(R.layout.add_friends_row_layout, parent, false);



        // We retrieve the text from the array
        User user = getItem(position);


        CheckBox cBox = (CheckBox) theView.findViewById(R.id.checkbox_add_friends);
        String fbId = user.getFacebookId();
        cBox.setTag(fbId); // set the tag so we can identify the correct row in the listener
        if(selectedFriendsId!=null){
            for (String id:selectedFriendsId) {
                if(fbId.equals(id)){
                    cBox.setChecked(true);
                }
            }
        }

        cBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxArray[position].setChecked(isChecked);
            }

        }); // set the listener

        checkBoxArray[position]=cBox;

        // Get the TextView we want to edit
        TextView theTextView = (TextView) theView.findViewById(R.id.friendNameTextView);

        // Put the next TV Show into the TextView
        theTextView.setText(user.generateFullName());

        // Get the ImageView in the layout
        ImageView theImageView = (ImageView) theView.findViewById(R.id.friendPictureImageView);

        // We can set a ImageView like this
        theImageView.setImageBitmap(user.getProfilePicture());

        return theView;
    }

    public CheckBox[] getCheckBoxArray() {
        return checkBoxArray;
    }



    }