package com.to_do_list.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.to_do_list.DatabaseHelper;
import com.to_do_list.R;
import com.to_do_list.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{

    private static class RecordItem {
        long id;
        String title, text, date;
        int important;
    }

    private List<RecordItem> itemsList = new ArrayList<>();

   public interface OnRecordClickListener{
        void onRecordClick(long recordId, int position);
        void onRemoveClick(long recordId, int position);
    }
    private final LayoutInflater inflater;
    private final OnRecordClickListener onClickListener;
    public RecordAdapter(Context context, Cursor cursor, OnRecordClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.inflater = LayoutInflater.from(context);
        swapCursor(cursor);
    }

    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordAdapter.ViewHolder holder, int position) {
        RecordItem item = itemsList.get(position);

        holder.titleView.setText(item.title);
        holder.textView.setText(item.text);
        holder.dateView.setText(item.date);

        if(item.important == 1){
            holder.imageButton.setVisibility(View.VISIBLE);
        } else {
            holder.imageButton.setVisibility(View.INVISIBLE);
        }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onClickListener.onRecordClick(item.id, adapterPosition);
                    }
                }
            });

            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onClickListener.onRemoveClick(item.id, adapterPosition);
                    }
                }
            });

    }

    public void swapCursor(Cursor newCursor) {
        itemsList.clear();
        if (newCursor != null && newCursor.moveToFirst()) {
            do {
                RecordItem item = new RecordItem();
                item.id = newCursor.getLong(newCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                item.title = newCursor.getString(newCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                item.text = newCursor.getString(newCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEXT));
                item.date = newCursor.getString(newCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                item.important = newCursor.getInt(newCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMPORTANT));
                itemsList.add(item);
            } while (newCursor.moveToNext());
            newCursor.close();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void moveItem(int fromPosition, int toPosition) {
        Collections.swap(itemsList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView titleView, textView, dateView;
        final Button removeButton;
        final ImageButton imageButton;
        ViewHolder(View view){
            super(view);
            removeButton = itemView.findViewById(R.id.remove_button);
            titleView = view.findViewById(R.id.record_title);
            textView = view.findViewById(R.id.record_text);
            imageButton = view.findViewById(R.id.important_star);
            dateView = view.findViewById(R.id.record_date);
        }
    }
}
