package com.to_do_list.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.to_do_list.DatabaseHelper;
import com.to_do_list.R;
import com.to_do_list.Record;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{

   public interface OnRecordClickListener{
        void onRecordClick(long recordId, int position);
        void onRemoveClick(long recordId, int position);
    }
    private final LayoutInflater inflater;
    private Cursor cursor;
    private final OnRecordClickListener onClickListener;
    public RecordAdapter(Context context, Cursor cursor, OnRecordClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.cursor = cursor;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordAdapter.ViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
            String text = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEXT));
            final long recordId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));

            holder.titleView.setText(title);
            holder.textView.setText(text);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onClickListener.onRecordClick(recordId, adapterPosition);
                    }
                }
            });

            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onClickListener.onRemoveClick(recordId, adapterPosition);
                    }
                }
            });
        }
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView titleView, textView;
        final Button removeButton;
        ViewHolder(View view){
            super(view);
            removeButton = itemView.findViewById(R.id.remove_button);
            titleView = view.findViewById(R.id.record_title);
            textView = view.findViewById(R.id.record_text);
        }
    }
}
