package com.example.gamebase;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.List;

//fragment that handles the search filter on a tablet, inflating on page instead of into a dialog
public class FilterTabFragment extends Fragment {
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
    public FilterTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_filter, container, false);
        autoPlatform = view.findViewById(R.id.platformSelector);
        autoGenre = view.findViewById(R.id.genreSelector);
        ImageButton clearButton = view.findViewById(R.id.clearButton);
        //when on the page, clear button is not necessary, the cancel button takes its role
        clearButton.setVisibility(View.INVISIBLE);
        Button cancelButton = view.findViewById(R.id.filterCancel);
        //cancel button has different text based on its context, either 'clear' or 'cancel'
        //depending on whether it's on the page or in a dialog
        cancelButton.setText("Clear");
        final EditText yr1 = view.findViewById(R.id.filterYearFrom);
        final EditText yr2 = view.findViewById(R.id.filterYearTo);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yr1.setText("");
                yr2.setText("");
                autoGenre.setText("");
                autoPlatform.setText("");

            }
        });
        Button applyButton = view.findViewById(R.id.filterApply);
        applyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Activity a = getActivity();
                //apply filters to the activity variable
                if(a instanceof SearchActivity){
                    AutoCompleteTextView platform = view.findViewById(R.id.platformSelector);
                    AutoCompleteTextView genre = view.findViewById(R.id.genreSelector);
                    if(!TextUtils.isEmpty(platform.getText().toString())){
                        ((SearchActivity) a).setPlatformFilter(autoPlatform.getText().toString());
                    }
                    if(!TextUtils.isEmpty(genre.getText().toString())) {
                        ((SearchActivity) a).setGenreFilter(autoGenre.getText().toString());
                    }
                    if(!TextUtils.isEmpty(yr1.getText().toString())){
                        ((SearchActivity) a).setY1Filter(Integer.parseInt(yr1.getText().toString()));
                    }
                    if(!TextUtils.isEmpty(yr2.getText().toString())){
                        ((SearchActivity) a).setY2Filter(Integer.parseInt(yr2.getText().toString()));
                    }
                    ((SearchActivity) a).filterUpdate();
                }
            }
        });


        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //call asynctask to populate genre and platform autocompletes
        final getter g = new getter();
        g.execute();
    }
    public class getter extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            platform = GameDatabase.getGameDatabase(getContext()).PlatformDao().getAllPlatforms();
            genre = GameDatabase.getGameDatabase(getContext()).GenreDao().getAllGenres();
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
            ArrayAdapter<String> platAdapter = new ArrayAdapter<>(view.getContext(),android.R.layout.select_dialog_item, platformList);
            ArrayAdapter<String> genAdapter = new ArrayAdapter<>(view.getContext(),android.R.layout.select_dialog_item,genreList);

            autoPlatform.setAdapter(platAdapter);
            autoGenre.setAdapter(genAdapter);
        }
    }


}
