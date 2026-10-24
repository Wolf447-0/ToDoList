package com.to_do_list;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH;
    private static String DB_NAME = "toDoList.db";
    private static final int SCHEMA = 1;
    public static final String TABLE = "records";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_IMPORTANT = "important";
    public static final String COLUMN_DATE = "date";
    private Context myContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext=context;
        DB_PATH = context.getFilesDir().getPath() + "/" + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) { }

    public void create_db(){

        File file = new File(DB_PATH);
        if (!file.exists()) {

            try(InputStream myInput = myContext.getAssets().open(DB_NAME);

                OutputStream myOutput = new FileOutputStream(DB_PATH)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
            }
            catch(IOException ex){
                Log.d("DatabaseHelper", ex.getMessage());
            }
        }
    }

    public void deleteRecord(long recordId){
        create_db();
        SQLiteDatabase database = open();
        database.execSQL("DELETE FROM " + TABLE + " WHERE _id = " + recordId);
    }

    public Cursor getRecordByTitle(String title){
        create_db();
        SQLiteDatabase database = open();
        return database.rawQuery("select * from " + TABLE + " where title = ?", new String[]{title});
    }

    public Cursor getAllRecords(){
        create_db();
        SQLiteDatabase database = open();
        return database.rawQuery("select * from " + TABLE,null);
    }

    public Cursor SortByAsc(){
        create_db();
        SQLiteDatabase database = open();
        return database.rawQuery("SELECT * FROM " + TABLE + " ORDER BY title ASC",null);
    }

    public Cursor SortByDesc(){
        create_db();
        SQLiteDatabase database = open();
        return database.rawQuery("SELECT * FROM " + TABLE + " ORDER BY title DESC",null);
    }

    public Cursor SortByDateAsc(){
        create_db();
        SQLiteDatabase database = open();
        return database.rawQuery("SELECT * FROM " + TABLE +" ORDER BY date ASC;",null);
    }
    public Cursor SortByDateDesc(){
        create_db();
        SQLiteDatabase database = open();
        return database.rawQuery("SELECT * FROM " + TABLE +" ORDER BY date DESC;",null);
    }

    public Cursor getAllImportantRecords(){
        create_db();
        SQLiteDatabase database = open();
        return database.rawQuery("SELECT * FROM " + TABLE + " where important = 1",null);
    }
    public SQLiteDatabase open()throws SQLException {

        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}
