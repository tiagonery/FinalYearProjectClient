package com.tiago.finalyearproject.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.model.User;

import java.util.List;

/**
 * Created by Tiago on 15/03/2016.
 */

// We can create custom adapters
class MyAdapter extends ArrayAdapter<User> {

    public MyAdapter(Context context, List<User> users){

        super(context, R.layout.row_layout, users);

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
        View theView = theInflater.inflate(R.layout.row_layout, parent, false);

        // We retrieve the text from the array
        User user = getItem(position);

        // Get the TextView we want to edit
        TextView theTextView = (TextView) theView.findViewById(R.id.friendNameTextView);

        // Put the next TV Show into the TextView
        theTextView.setText(user.getFullName());

        // Get the ImageView in the layout
        ImageView theImageView = (ImageView) theView.findViewById(R.id.friendPictureImageView);

        // We can set a ImageView like this
        theImageView.setImageBitmap(user.getProfilePicture());

        return theView;

    }
}