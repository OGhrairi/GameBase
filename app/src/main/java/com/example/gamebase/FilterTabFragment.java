package com.example.gamebase;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class FilterTabFragment extends Fragment {


    public FilterTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        List<String> testList = new ArrayList<String>();
        testList.add("test1");
        testList.add("test2");
        testList.add("test3");
        testList.add("test4");

        Spinner spinner = (Spinner) view.findViewById(R.id.platformSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,testList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(dataAdapter);

        Spinner spinner1 = (Spinner) view.findViewById(R.id.genreSpinner);
        spinner1.setAdapter(dataAdapter);

        Spinner spinner2 = (Spinner)view.findViewById(R.id.typeSpinner);
        spinner2.setAdapter(dataAdapter);

        return view;
    }
}
