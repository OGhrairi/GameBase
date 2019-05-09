package com.example.gamebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//Main class, extends the ViewHolder adapter defined below
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {


    private List<String> mTitles = new ArrayList<>();
    private List<String> mBodies = new ArrayList<>();
    private List<String> mImages = new ArrayList<>();
    private Context mContext;

    //Constructor for recyclerview
    public FeedAdapter(List<String> titles, List<String> images, List<String> bodies, Context context){
        mTitles = titles;
        mBodies = bodies;
        mImages = images;
        mContext = context;
    }

    @NonNull
    @Override
    //Method responsible for inflating view
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_listitem,parent,false);
        FeedViewHolder holder = new FeedViewHolder(view);
        return holder;
    }

    @Override
    //Method that binds elements to the view
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {

        //use glide package to request image from url, provided in mImages array
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);

        //set the text values for the view from their respective input arrays
        holder.title.setText(mTitles.get(position));
        holder.body.setText(mBodies.get(position));
        //onclicklistener for view
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    //Class which holds items of each entry in a ViewHolder, for use by the recycler
    public class FeedViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        TextView body;
        LinearLayout parentLayout;
        //constructor binds views
        public FeedViewHolder(View view){
            super (view);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.feedTitleText);
            body = view.findViewById(R.id.feedBodyText);
            parentLayout = view.findViewById(R.id.feedBrowseLayout);
        }

    }
}
