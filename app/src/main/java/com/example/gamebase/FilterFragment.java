package com.example.gamebase;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;


public class FilterFragment extends DialogFragment {

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
        ImageButton clearButton = view.findViewById(R.id.clearButton);
        Button cancelButton = view.findViewById(R.id.filterCancel);
        cancelButton.setText("Cancel");
        builder.setView(view);
        final EditText yr1 = view.findViewById(R.id.filterYearFrom);
        final EditText yr2 = view.findViewById(R.id.filterYearTo);
        final AutoCompleteTextView platform = view.findViewById(R.id.platformSelector);
        final AutoCompleteTextView genre = view.findViewById(R.id.genreSelector);
        Bundle bundle = this.getArguments();
        if(!bundle.isEmpty()) {
            int y1 = bundle.getInt("y1",0);
            int y2 = bundle.getInt("y2",0);
           // yr1.setText(bundle.getInt("y1", 0));
            if(y1 != 0)yr1.setText(Integer.toString(y1));
            if(y2 != 0)yr2.setText(Integer.toString(y2));
           // yr2.setText(bundle.getInt("y2", 0));
            platform.setText(bundle.getString("platform", ""));
            genre.setText(bundle.getString("genre", ""));
        }else System.out.println("bundle is empty");
        getter g = new getter();
        g.execute();

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                yr1.setText("");
                yr2.setText("");
                platform.setText("");
                genre.setText("");

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button applyButton = view.findViewById(R.id.filterApply);
        applyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Activity a = getActivity();
                if(a instanceof SearchActivity){
                    if(!TextUtils.isEmpty(platform.getText().toString())) {
                        ((SearchActivity) a).setPlatformFilter(autoPlatform.getText().toString());
                    }else((SearchActivity) a).setPlatformFilter("");
                    if(!TextUtils.isEmpty(genre.getText().toString())) {
                        ((SearchActivity) a).setGenreFilter(autoGenre.getText().toString());
                    }else((SearchActivity) a).setGenreFilter("");
                    if(!TextUtils.isEmpty(yr1.getText().toString())){
                        ((SearchActivity) a).setY1Filter(Integer.parseInt(yr1.getText().toString()));
                    }else((SearchActivity)a).setY1Filter(0);
                    if(!TextUtils.isEmpty(yr2.getText().toString())){
                        ((SearchActivity) a).setY2Filter(Integer.parseInt(yr2.getText().toString()));
                    }else((SearchActivity)a).setY2Filter(0);
                    ((SearchActivity) a).filterUpdate();
                    dismiss();
                }
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
