package com.tiago.finalyearproject.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.model.Friendship;
import com.tiago.finalyearproject.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiago on 15/03/2016.
 */

// We can create custom adapters
class SearchListAdapter extends ArrayAdapter<User> {

    private List<Friendship> friendshipList;

    public SearchListAdapter(Context context, List<User> users, List<Friendship> friendshipList){

        super(context, R.layout.friends_list_row_layout, users);
        this.friendshipList=friendshipList;

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
        View theView = theInflater.inflate(R.layout.friends_list_row_layout, parent, false);



        // We retrieve the text from the array
        final User user = getItem(position);


        // Get the TextView we want to edit
        TextView theTextView = (TextView) theView.findViewById(R.id.friend_list_name_ImageView);

        // Put the next TV Show into the TextView
        theTextView.setText(user.generateFullName());

        // Get the ImageView in the layout
        ImageView profilePictureImageView = (ImageView) theView.findViewById(R.id.friend_list_profile_picture_ImageView);

        // We can set a ImageView like this
        profilePictureImageView.setImageBitmap(user.getProfilePicture());


        ImageView statusImageView = (ImageView) theView.findViewById(R.id.friend_list_status_ImageView);
        final Friendship friendship = getFriendship(friendshipList, user.getFacebookId());

        if(friendship.getState().equals(Friendship.FriendshipState.REQUEST_ACCEPTED)){
            statusImageView.setImageResource(R.drawable.friends);
        }
        else if(friendship.getState().equals(Friendship.FriendshipState.REQUEST_UNANSWARED)&& friendship.getUser1Id().equals(user.getFacebookId())){
            statusImageView.setImageResource(R.drawable.friend_request);

            statusImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    ((SearchFriendsActivity)getContext()).acceptFriendship(friendship);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    ((SearchFriendsActivity)getContext()).refuseFriendship(friendship);
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Do you want to Add this User?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                }
            });

        }
        else if(friendship.getState().equals(Friendship.FriendshipState.REQUEST_UNANSWARED)&& friendship.getUser2Id().equals(user.getFacebookId())){
            statusImageView.setImageResource(R.drawable.add_friend_sent);

        }
        else if(friendship.getState()==null){
            statusImageView.setImageResource(R.drawable.add_friend);
            statusImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> usersIds = new ArrayList<String>();
                    usersIds.add(user.getFacebookId());
                    ((SearchFriendsActivity) getContext()).sendFriendRequest(usersIds);
                }
            });
        }

        else if(friendship.getState().equals(Friendship.FriendshipState.REQUEST_REFUSED)){
            statusImageView.setImageResource(R.drawable.add_friend_sent);

        }


        return theView;
    }



    private Friendship getFriendship (List<Friendship> friendshipList, String id) {
        Friendship result = new Friendship();

        for (Friendship friendship: friendshipList) {
            if(friendship.getUser1Id().equals(id)||friendship.getUser2Id().equals(id)){
                result = friendship;
            }
        }
        return result;
    }


    }