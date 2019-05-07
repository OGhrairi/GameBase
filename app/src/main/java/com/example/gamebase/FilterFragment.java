package com.example.gamebase;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;


public class FilterFragment extends DialogFragment {
    public class platform{
        private int id;
        private String name;
        public platform(int id, String name){
            this.id=id;
            this.name=name;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }
    List<platform> platformList = new ArrayList<>();
    public FilterFragment() {
        // Required empty public constructor
    }
    private void adder(int id, String name){
        int platformId = id;
        String platformName = name;
        platformList.add(new platform(platformId,platformName));
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_filter,null);
        builder.setView(view);
        adder(14,"Mac");
        adder(3, "Linux");
        adder(6, "PC (Microsoft Windows)");
        adder(130,"Nintendo Switch");
        adder(48, "PlayStation 4");
        adder(49, "Xbox One");
        adder(9,"PlayStation 3");
        adder(12, "Xbox 360");


        Spinner spinner = (Spinner) view.findViewById(R.id.platformSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,testList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(dataAdapter);

        Spinner spinner1 = (Spinner) view.findViewById(R.id.genreSpinner);
        spinner1.setAdapter(dataAdapter);

        Spinner spinner2 = (Spinner)view.findViewById(R.id.typeSpinner);
        spinner2.setAdapter(dataAdapter);
        return builder.create();
    }
}
