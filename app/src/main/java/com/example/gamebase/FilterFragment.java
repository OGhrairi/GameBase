package com.example.gamebase;


import android.app.Dialog;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;


public class FilterFragment extends DialogFragment {
    public class gameCombo {
        private int id;
        private String name;

        public gameCombo(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
    List<PlatformTable> platform  = new ArrayList<>();
    List<GenreTable> genre = new ArrayList<>();

    List<String>platformList = new ArrayList<>();
    List<String>genreList = new ArrayList<>();
    AutoCompleteTextView autoPlatform;
    AutoCompleteTextView autoGenre;
    View view;
    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_filter,null);
        builder.setView(view);

        getter g = new getter();
        g.execute();
        ImageButton clearButton = view.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText yr1 = view.findViewById(R.id.filterYearFrom);
                EditText yr2 = view.findViewById(R.id.filterYearTo);
                yr1.setText("");
                yr2.setText("");
            }
        });


        return builder.create();
    }
    public class getter extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            platform = GameDatabase.getGameDatabase(getActivity().getApplicationContext()).PlatformDao().getAllPlatforms();
            genre = GameDatabase.getGameDatabase(getActivity().getApplicationContext()).GenreDao().getAllGenres();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            for(int i=0; i<platform.size();i++){
                platformList.add(platform.get(i).getName());
            }
            for(int j=0;j<genre.size();j++){
                genreList.add(genre.get(j).getName());
            }
            ArrayAdapter<String> platAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.select_dialog_item, platformList);
            ArrayAdapter<String> genAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.select_dialog_item,genreList);
            autoPlatform = view.findViewById(R.id.platformSelector);
            autoGenre = view.findViewById(R.id.genreSelector);
            autoPlatform.setAdapter(platAdapter);
            autoGenre.setAdapter(genAdapter);
        }
    }

}
