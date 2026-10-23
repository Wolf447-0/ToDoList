package com.to_do_list.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;

import com.google.android.material.appbar.AppBarLayout;
import com.to_do_list.DatabaseHelper;
import com.to_do_list.R;
import com.to_do_list.Record;
import com.to_do_list.adapter.RecordAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentList extends Fragment {

    private Button addBtn;
    public DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor recordCursor;
    private RecyclerView recyclerView;
    private RecordAdapter recordAdapter;



    public FragmentList() {

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
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addBtn = view.findViewById(R.id.add_record);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick (View view){
                Navigation.findNavController(view).navigate(R.id.list_records_to_new_record);
            }
        });

        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        RecordAdapter.OnRecordClickListener recordClickListener = new RecordAdapter.OnRecordClickListener() {
            @Override
            public void onRecordClick(long recordId, int position) {
                recordCursor = db.rawQuery("select title, text from " + DatabaseHelper.TABLE +
                       " where _id = " + recordId, null);
                recordCursor.moveToFirst();
                Bundle bundle = new Bundle();
                bundle.putString(DatabaseHelper.COLUMN_TITLE,recordCursor.getString(0));
                bundle.putString(DatabaseHelper.COLUMN_TEXT,recordCursor.getString(1));
                bundle.putLong(DatabaseHelper.COLUMN_ID,recordId);
                Navigation.findNavController(view).navigate(R.id.list_records_to_edit_record,bundle);
            }

            @Override
            public void onRemoveClick(long recordId, int position){
                databaseHelper.deleteRecord(recordId);
                Cursor newCursor = databaseHelper.getAllRecords();
                recordAdapter.swapCursor(newCursor);
            }
        };

        recordCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE, null);
        recordAdapter = new RecordAdapter(getContext(),recordCursor,recordClickListener);


        recyclerView.setAdapter(recordAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
        recordCursor.close();
    }
}

