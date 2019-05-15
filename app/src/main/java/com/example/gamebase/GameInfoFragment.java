package com.example.gamebase;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class GameInfoFragment extends Fragment {
   // private TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_info, container, false);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            String rating = new DecimalFormat("#.#").format(bundle.getDouble("rating"));
            int ratingCount = bundle.getInt("ratingCount");
            String title = bundle.getString("name");
            String desc = bundle.getString("summary");
            ArrayList<String> artworks = bundle.getStringArrayList("artworks");
            TextView titleView = view.findViewById(R.id.gameInfoTitle);
            titleView.setText(title);
            TextView descView = view.findViewById(R.id.gameInfoDesc);
            descView.setText(desc);
            TextView ratingView = view.findViewById(R.id.gameInfoExtra);
            ratingView.setText("Average rating of "+rating+", From a total of "+ratingCount+" reviews.");
            descView.setMovementMethod(new ScrollingMovementMethod());
            if(artworks.size()==0){
                TextView descr = view.findViewById(R.id.screenshotNotifcation);
                descr.setText("No Artwork Available");
            }
            RecyclerView recyclerView;
            RecyclerView.Adapter adapter = new ArtworkRecycleAdapter(artworks,getActivity().getApplicationContext());
            RecyclerView.LayoutManager layoutManager;
            recyclerView = (RecyclerView) view.findViewById(R.id.gameInfoArtworkRecycler);
            layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        }
        return view;
    }
}
