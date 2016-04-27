package com.tiago.finalyearproject.view;

import android.content.Context;
import android.content.Intent;
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
import com.tiago.finalyearproject.model.Wish;
import com.tiago.finalyearproject.model.UserWish;

import java.util.List;

/**
 * Created by Tiago on 15/03/2016.
 */

// We can create custom adapters
class WishesAdapter extends ArrayAdapter<Wish> {


    public WishesAdapter(Context context, List<Wish> activities){

        super(context, R.layout.add_friends_row_layout, activities);

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
        View theView = theInflater.inflate(R.layout.wishes_row_layout, parent, false);

        // We retrieve the text from the array
        final Wish wish = getItem(position);

        Profile profile = Profile.getCurrentProfile();

        if(wish.getWishOwner().getFacebookId().equals(profile.getId())) {
            theView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }



//        UserWish.UserWishState state = wish.getCurrentUserWishState(profile.getId());

        // Get the ImageView in the layout
        ImageView joinActivityImage = (ImageView) theView.findViewById(R.id.join_wish_ImageView);

        if(wish.getWishOwner().getFacebookId().equals(profile.getId())){
            joinActivityImage.setImageResource(R.drawable.view);
            joinActivityImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ManageWishActivity.class);
                    intent.putExtra("wish", wish);
                    getContext().startActivity(intent);
                }
            });
        }
        else if(!wish.getUserWishList().isEmpty()){
                joinActivityImage.setImageResource(R.drawable.joined);
                joinActivityImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
            else {
            joinActivityImage.setImageResource(R.drawable.not_joined);
            joinActivityImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        // Get the TextView we want to edit
        TextView userNameTextView = (TextView) theView.findViewById(R.id.userNameTextView);

        // Put the next TV Show into the TextView
        userNameTextView.setText(wish.getWishOwner().getName());

        // Get the TextView we want to edit
        TextView wishNameTextView = (TextView) theView.findViewById(R.id.wishNameTextView);

        // Put the next TV Show into the TextView
        wishNameTextView.setText(wish.getName());

        // Get the TextView we want to edit
        TextView wishDateTextView = (TextView) theView.findViewById(R.id.wishDateTextView);

        String hour = String.format("%02d", wish.getWishDateTime().getHours());
        String minute = String.format("%02d", wish.getWishDateTime().getMinutes());
        String day = String.format("%02d", wish.getWishDateTime().getDate());
        String month = String.format("%02d", wish.getWishDateTime().getMonth()+1);
        String year = String.format("%02d", wish.getWishDateTime().getYear()+1900);
        // Put the next TV Show into the TextView
        wishDateTextView.setText(hour + ":" + minute + " " + day + "/" + month + "/" + year);

        // Get the ImageView in the layout
        ImageView userPictureImageView = (ImageView) theView.findViewById(R.id.userPictureImageView);

        userPictureImageView.setImageBitmap(wish.getWishOwner().getProfilePicture());

        // Get the ImageView in the layout
        ImageView categoryImageView = (ImageView) theView.findViewById(R.id.wishCategoryImageView);


        // We can set a ImageView like this
        AppEvent.EventType eventType = wish.getEventType();
        categoryImageView.setImageResource(eventType.getSelectedImage());


        return theView;
    }



    }