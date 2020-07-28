package com.shashankbhat.musicplayer.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.utils.Constants;
import com.shashankbhat.musicplayer.utils.FileReadHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by SHASHANK BHAT on 28-Jul-20.
 */
@Database(entities = {Song.class},version = 1,exportSchema = false)
public abstract class  SongDatabase extends RoomDatabase {

    public abstract SongDao mSongDao();
    private static SongDatabase instance;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static SongDatabase getInstance(Context context){
        if(instance==null){
            synchronized (SongDatabase.class){
                instance = Room.databaseBuilder(context,SongDatabase.class, Constants.DATABASE_NAME)
                        .addCallback(getCallback(context))
                        .build();
            }
        }
        return instance;
    }

    public static Callback getCallback(Context context){

        RoomDatabase.Callback callback = new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                databaseWriteExecutor.execute(()->{
                    for(Song song : FileReadHelper.readSongsFromJSON(context)){
                        instance.mSongDao().insert(song);
                    }
                });
            }
        };

        return callback;
    }


}
