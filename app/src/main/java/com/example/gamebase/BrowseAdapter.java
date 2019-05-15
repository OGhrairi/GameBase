package com.example.gamebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


//adapter for game list recyclerview, used in top 50 page and in search page
public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.BrowseViewHolder> {
    private String[] list;
    private List<BrowseResultsTable> titles;
    private OnBrowseListener mOnBrowseListener;
    //takes two args; an array of titles and a listener for onClick handling
    public BrowseAdapter(String[] inList, OnBrowseListener OnBrowseListener){
        list=inList;
        this.mOnBrowseListener = OnBrowseListener;
    }

    //Class which holds items of each entry in a ViewHolder, for use by the recycler
    public static class BrowseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txt;
        public LinearLayout layout;
        OnBrowseListener onBrowseListener;
        public BrowseViewHolder(@NonNull View itemView, OnBrowseListener onBrowseListener) {
            super(itemView);
            txt = itemView.findViewById(R.id.textView);
            this.onBrowseListener=onBrowseListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onBrowseListener.onBrowseClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public BrowseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse_listitem, parent, false);
        BrowseViewHolder holder = new BrowseViewHolder(v, mOnBrowseListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseViewHolder holder, int position) {
        holder.txt.setText(list[position]);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public void setData(List<BrowseResultsTable> newData){
        this.titles = newData;
        notifyDataSetChanged();
    }
    //interface that is called by this adapter when an item is tapped, and implemented by
    //whichever activity it is on
    public interface OnBrowseListener{
        void onBrowseClick(int position);
    }
}
