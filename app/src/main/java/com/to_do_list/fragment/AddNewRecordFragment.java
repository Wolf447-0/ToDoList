package com.to_do_list.fragment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.to_do_list.DatabaseHelper;
import com.to_do_list.R;
import com.to_do_list.adapter.RecordAdapter;


public class AddNewRecordFragment extends Fragment {

    private Button addBtn;
    public DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private EditText title;
    private EditText text;

    public AddNewRecordFragment() {}

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
        return inflater.inflate(R.layout.fragment_add_new_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        title = view.findViewById(R.id.input_title);
        text = view.findViewById(R.id.input_text);

        addBtn = view.findViewById(R.id.add_new_record);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String title_r = title.getText().toString();
             String text_r = text.getText().toString();
             save(title_r,text_r);
                Navigation.findNavController(view).navigate(R.id.new_record_to_list_records);
            }
        });

    }
    private void save(String title, String text){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_TITLE,title);
        cv.put(DatabaseHelper.COLUMN_TEXT,text);
        db.insert(DatabaseHelper.TABLE,null,cv);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}