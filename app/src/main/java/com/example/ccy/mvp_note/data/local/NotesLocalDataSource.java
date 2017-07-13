package com.example.ccy.mvp_note.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ccy.mvp_note.data.NoteBean;
import com.example.ccy.mvp_note.data.NoteDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccy on 2017-07-13.
 * 数据库操作类 （ Model 实现类）
 * 单例
 */

public class NotesLocalDataSource implements NoteDataSource {

    private static NotesLocalDataSource INSTANCE = null;
    private NoteDbHelper dbHelper;


    private NotesLocalDataSource(Context context) {
        dbHelper = new NoteDbHelper(context);
    }

    public static NotesLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NotesLocalDataSource(context);
        }
        return INSTANCE;
    }


    @Override
    public void getNote(String noteId, LoadNoteCallback callback) {
        NoteBean note = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String queryColums[] = new String[]{
                NoteDbHelper.COLUMN_ID,
                NoteDbHelper.COLUMN_TITLE,
                NoteDbHelper.COLUMN_CONTENT,
                NoteDbHelper.COLUMN_IS_ACTIVE
        };
        String selection = NoteDbHelper.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = {noteId};

        Cursor cursor = null;
        try {
            cursor = db.query(NoteDbHelper.TABLE_NAME, queryColums, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                String id = cursor.getString(cursor.getColumnIndex(NoteDbHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(NoteDbHelper.COLUMN_TITLE));
                String content = cursor.getString(cursor.getColumnIndex(NoteDbHelper.COLUMN_CONTENT));
                boolean isActive = cursor.getInt(cursor.getColumnIndex(NoteDbHelper.COLUMN_IS_ACTIVE)) == 1;
                note = new NoteBean(id, title, content, isActive);
            }
        } catch (Exception e) {
            callback.loadFailed();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        if(note == null){
            callback.loadFailed();
        }else {
            callback.loadSuccess(note);
        }

    }

    @Override
    public void getNotes(LoadNotesCallback callback) {
        List<NoteBean> notes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String queryColums[] = {
                NoteDbHelper.COLUMN_ID,
                NoteDbHelper.COLUMN_TITLE,
                NoteDbHelper.COLUMN_CONTENT,
                NoteDbHelper.COLUMN_IS_ACTIVE
        };

        Cursor cursor = null;
        try {
            cursor = db.query(NoteDbHelper.TABLE_NAME, queryColums, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(NoteDbHelper.COLUMN_ID));
                    String title = cursor.getString(cursor.getColumnIndex(NoteDbHelper.COLUMN_TITLE));
                    String content = cursor.getString(cursor.getColumnIndex(NoteDbHelper.COLUMN_CONTENT));
                    boolean isActive = cursor.getInt(cursor.getColumnIndex(NoteDbHelper.COLUMN_IS_ACTIVE)) == 1;
                    NoteBean bean = new NoteBean(id, title, content, isActive);
                    notes.add(bean);
                }
            }
        } catch (Exception e) {
            callback.loadFailed();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        callback.loadSucess(notes);

    }

    @Override
    public void saveNote(NoteBean note) {
        if(note == null){
            return;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteDbHelper.COLUMN_ID,note.id);
        values.put(NoteDbHelper.COLUMN_TITLE,note.title);
        values.put(NoteDbHelper.COLUMN_CONTENT,note.content);
        values.put(NoteDbHelper.COLUMN_IS_ACTIVE,note.isActive);

        db.insert(NoteDbHelper.TABLE_NAME,null,values);
        db.close();
    }

    @Override
    public void updateNote(NoteBean note) {
        if(note == null){
            return;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteDbHelper.COLUMN_TITLE,note.title);
        values.put(NoteDbHelper.COLUMN_CONTENT,note.content);
        values.put(NoteDbHelper.COLUMN_IS_ACTIVE,note.isActive);

        String selection = NoteDbHelper.COLUMN_ID+" LIKE ?";
        String[] selectionArgs = {note.id};

        db.update(NoteDbHelper.TABLE_NAME,values,selection,selectionArgs);

        db.close();
    }

    @Override
    public void markNote(NoteBean note, boolean isActive) {
        if(note == null){
            return;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteDbHelper.COLUMN_IS_ACTIVE,isActive);

        String selection = NoteDbHelper.COLUMN_ID+ " LIKE ?";
        String[] selectionArgs = {note.id};

        db.update(NoteDbHelper.TABLE_NAME,values,selection,selectionArgs);

        db.close();
    }

    @Override
    public void clearCompleteNotes() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = NoteDbHelper.COLUMN_IS_ACTIVE+" LIKE ?";
        String[] selectionArgs ={"0"};   //0即false

        db.delete(NoteDbHelper.TABLE_NAME,selection,selectionArgs);
        db.close();
    }

    @Override
    public void deleteAllNotes() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(NoteDbHelper.TABLE_NAME,null,null);
        db.close();
    }

    @Override
    public void deleteNote(String noteId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = NoteDbHelper.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = {noteId};

        db.delete(NoteDbHelper.TABLE_NAME,selection,selectionArgs);
        db.close();
    }

    @Override
    public void cacheEnable(boolean enable) {
        //无操作。改方法由NotesRepository管理
    }
}
