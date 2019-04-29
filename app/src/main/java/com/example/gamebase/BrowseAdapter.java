package com.example.gamebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.List;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.BrowseViewHolder> {
    private String[] list;
    private List<GameTitle> titles;
    public static class BrowseViewHolder extends RecyclerView.ViewHolder{
        public TextView txt;
        public LinearLayout layout;
        public BrowseViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.textView);
        }
    }
    public BrowseAdapter(String[] inList){
        list=inList;
    }

    @NonNull
    @Override
    public BrowseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse_listitem, parent, false);
        BrowseViewHolder holder = new BrowseViewHolder(v);
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

    public void setData(List<GameTitle> newData){
        this.titles = newData;
        notifyDataSetChanged();
    }
}
