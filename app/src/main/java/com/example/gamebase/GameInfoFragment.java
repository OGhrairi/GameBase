package com.example.gamebase;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class GameInfoFragment extends Fragment {
   // private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_info, container, false);
        Bundle bundle = this.getArguments();
        String title = bundle.getString("title");
        String desc = bundle.getString("desc");
        TextView titleView = view.findViewById(R.id.gameInfoTitle);
        titleView.setText(title);
        TextView descView = view.findViewById(R.id.gameInfoDesc);
        descView.setText(desc);
        return view;
    }
    public void setTitle(String title){
        String t = title;
       // textView.setText(t);
    }
}
