package com.example.android.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;



/**
 * Created by rirg9 on 3/21/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    Context mContext;
    /**
     * Constructs a new {@link NewsAdapter}
     *
     * @param context of the app
     * @param info    is the list of news, which is the data source of the data
     */
    public NewsAdapter(Context context, ArrayList<News> info) {
        super(context, 0, info);
        mContext = context;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        if (gridView == null) {
            gridView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        //Find the new at the given position in the list of news
        News currentNew = getItem(position);

        //Find the ImageView with the ID image
        ImageView imageView = (ImageView) gridView.findViewById(R.id.image_view);

        //Display the Image of the current new in that ImageView using Picasso.
        if(currentNew.getImageUrl() != null) {
            Picasso.with(mContext).load(currentNew.getImageUrl()).fit().centerCrop().into(imageView);
        }else {
            imageView.setImageResource(R.drawable.no_image);
        }

        //Find the TextView with the ID title
        TextView titleTextView = (TextView) gridView.findViewById(R.id.title_text_view);
        //Display the title of the current new in that TextView.
        titleTextView.setText(currentNew.getTitle());

        //Find the TextView with the ID date
        TextView descriptionTextView = (TextView) gridView.findViewById(R.id.description_text_view);
        //Display the title of the current new in that TextView.
        descriptionTextView.setText(currentNew.getDescription());

        //Return the grid item view that is now showing the appropriate date.
        return gridView;
    }

}
