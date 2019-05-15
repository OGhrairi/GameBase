package com.example.gamebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class ArtworkRecycleAdapter extends RecyclerView.Adapter<ArtworkRecycleAdapter.ArtworkViewHolder> {
    private List<String> mUrls = new ArrayList<>();
    private Context mContext;
    private List<BrowseResultsTable> titles;
    public ArtworkRecycleAdapter(List<String> urls, Context context) {
        mUrls = urls;
        mContext = context;
    }

    @NonNull
    @Override
    public ArtworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.artwork_listitem, parent, false);
        ArtworkViewHolder holder = new ArtworkViewHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ArtworkViewHolder holder, final int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(mUrls.get(position))
                .override(Target.SIZE_ORIGINAL)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mUrls.size();
    }
    public static class ArtworkViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        LinearLayout parentLayout;
        public ArtworkViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.imageView);
            parentLayout = view.findViewById(R.id.artworkLayout);
        }

    }
}
