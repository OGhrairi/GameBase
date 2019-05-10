package com.example.gamebase;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

//Main class, extends the ViewHolder adapter defined below
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    private List<String> mTitles = new ArrayList<>();
    private List<String> mBodies = new ArrayList<>();
    private List<String> mImages = new ArrayList<>();
    private List<String> mUrls = new ArrayList<>();
    private Context mContext;
    //Constructor for recyclerview
    public FeedAdapter(List<String> titles, List<String> images, List<String> bodies, List<String> Urls, Context context){
        mTitles = titles;
        mBodies = bodies;
        mImages = images;
        mContext = context;
        mUrls = Urls;
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
    public void onBindViewHolder(@NonNull FeedViewHolder holder, final int position) {

        //use glide package to request image from url, provided in mImages array
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);

        //set the text values for the view from their respective input arrays
        holder.title.setText(mTitles.get(position));
        holder.body.setText(mBodies.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mUrls.get(position);
                Intent intent = new Intent(mContext,WebViewActivity.class);
                intent.putExtra("url",url);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    //Class which holds items of each entry in a ViewHolder, for use by the recycler
    public class FeedViewHolder extends RecyclerView.ViewHolder {
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
    public interface FeedListener{
        void webOpener(int position);
    }
}
