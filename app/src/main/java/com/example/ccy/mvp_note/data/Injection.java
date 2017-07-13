package com.example.ccy.mvp_note.data;

import android.content.Context;

import com.example.ccy.mvp_note.data.local.NotesLocalDataSource;

/**
 * Created by ccy on 2017-07-13.
 * 提供NotesRespository
 */

public class Injection {
    public static NotesRepository provideRespository(Context context){
        return NotesRepository.getInstence(NotesLocalDataSource.getInstance(context));
    }
}
