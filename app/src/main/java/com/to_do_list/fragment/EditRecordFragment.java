package com.to_do_list.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.to_do_list.DatabaseHelper;
import com.to_do_list.R;


public class EditRecordFragment extends Fragment {

    private EditText title;
    private EditText text;

    public EditRecordFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.input_title);
        text = view.findViewById(R.id.input_text);

        title.setText(getArguments().getString(DatabaseHelper.COLUMN_TITLE));
        text.setText(getArguments().getString(DatabaseHelper.COLUMN_TEXT));

    }
}