package com.ennbou.contact.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Contact.class}, version = 3, exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {

    public abstract ContactDao contactDao();

    private static volatile MyDataBase dataBase;
    private static final String DB_NAME = "ContactDB";
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MyDataBase getDataBase(final Context context) {
        if (dataBase == null) {
            synchronized (MyDataBase.class) {
                if (dataBase == null) {
                    dataBase = Room.databaseBuilder(context.getApplicationContext(),
                            MyDataBase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }

        }
        return dataBase;
    }

}
