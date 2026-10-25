package com.to_do_list.fragment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.to_do_list.DatabaseHelper;
import com.to_do_list.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EditRecordFragment extends Fragment {

    private EditText title;
    private EditText text;
    private Button changeBtn;
    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

    public EditRecordFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());
        databaseHelper.create_db();
        db = databaseHelper.open();
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
        changeBtn = view.findViewById(R.id.change_record);

        title.setText(getArguments().getString(DatabaseHelper.COLUMN_TITLE));
        text.setText(getArguments().getString(DatabaseHelper.COLUMN_TEXT));

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String newDate = formatter.format(new Date());
                long recordId = getArguments().getLong(DatabaseHelper.COLUMN_ID);
                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.COLUMN_TITLE,title.getText().toString());
                cv.put(DatabaseHelper.COLUMN_TEXT,text.getText().toString());
                cv.put(DatabaseHelper.COLUMN_DATE,newDate);
                db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + recordId, null);
                Snackbar.make(view, "Запись изменена", Snackbar.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.edit_record_to_list_records);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}