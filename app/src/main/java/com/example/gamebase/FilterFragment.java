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


    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_filter,null);
        builder.setView(view);
        List<String> testList = new ArrayList<String>();
        testList.add("test1");
        testList.add("test2");
        testList.add("test3");
        testList.add("test4");

        Spinner spinner = (Spinner) view.findViewById(R.id.platformSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,testList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(dataAdapter);
        spinner.setAdapter(dataAdapter);
        return builder.create();
    }
}
