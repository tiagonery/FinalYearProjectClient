package com.tiago.finalyearproject.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.tiago.finalyearproject.model.AppEvent;

/**
 * Created by Tiago on 15/03/2016.
 */

// We can create custom adapters
class EventTypeAdapter extends BaseAdapter {

    private Context mContext;

    // Keep all Images in array
    public AppEvent.EventType[] eventTypeList = AppEvent.EventType.values();

    // Constructor
    public EventTypeAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return eventTypeList.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(eventTypeList[position].getImage());
        return imageView;
    }




    }