package com.mobdeve.calaranan.k_ilagan.m.goks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseToRead extends SQLiteOpenHelper {
    private Context context;
    private static final String DB_NAME = "GOksToRead.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "ToRead";
    private static final String FIELD_ID = "_id";
    private static final String FIELD_BOOK = "_title";

    public DatabaseToRead(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + TABLE_NAME + " (" + FIELD_ID + " STRING PRIMARY KEY, " +
                FIELD_BOOK + " TEXT);";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void AddToRead(String bookID, String book){
        SQLiteDatabase db = this.getWritableDatabase(); // write to table
        ContentValues cv = new ContentValues(); // store data from application and pass to table

        cv.put(FIELD_ID, bookID);
        cv.put(FIELD_BOOK, book);

        if(!checkBook(bookID)){ // if book is not yet added in the to booklist category
            long result = db.insert(TABLE_NAME, null, cv);
            if(result == -1){
                Toast.makeText(context, "Error adding!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Book added in booklist!", Toast.LENGTH_SHORT).show();
            }
        } else Toast.makeText(context, "Book already added in booklist!", Toast.LENGTH_SHORT).show();
    }

    Cursor getToRead(){
        String retrieve = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(retrieve, null);   // contains all data
        } return cursor;
    }

    public void removeBook(String bookID){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{bookID});
        if(result == -1){
            Toast.makeText(context, "Error removing book in library!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor searchBook(String title){
        String search = "SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_BOOK + " LIKE '%" + title + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(search, null);   // contains all data
        } return cursor;
    }

    public boolean checkBook(String bookID) {
        String check = "SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_ID + " = \"" + bookID + "\";";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(check, null);
        if(cursor.getCount() == 1){ // book already in to read category
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
}
