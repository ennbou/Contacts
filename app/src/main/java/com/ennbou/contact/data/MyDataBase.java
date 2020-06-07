package com.ennbou.contact.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
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
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }

        }
        return dataBase;
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            ContactDao dao = dataBase.contactDao();
            List<Contact> contacts = new ArrayList<>();
            contacts.add(new Contact(null, "Mohammed", "ziyani", "0634326787", "moh.ziyani@gmail.com", "etudiant", "mohammed casablanca, marchandises"));
            contacts.add(new Contact(null, "Mohammed", "khababi", "0612278899", "moh.khababi@gmail.com", "adjointe administrative", "khababi ecole rabat, idara"));
            contacts.add(new Contact(null, "Mohammed", "amar", "0766321812", "amar88@gmail.com", "enseignent", "ammar EHTP, mn rachidiya"));
            contacts.add(new Contact(null, "Fatima", "jabri", "0698326773", "fatima.jabri@gmail.com", "enseignente", "Algo c, EST sale"));
            contacts.add(new Contact(null, "Salma", "ouad", "0667123463", "salma.ouad@gmail.com", "etudiante", "EMI, electrique"));
            contacts.add(new Contact(null, "Sara", "fekri", "0678889211", "sara.fekri77@gmail.com", "etudiante", "EMI, mecanique"));
            contacts.add(new Contact(null, "Mouad", "fekri", "0623783129", "moaud.fekri77@gmail.com", "etudiant", "FST tanga, wald casa"));
            contacts.add(new Contact(null, "Hamza", "safir", "0788128201", "hamza.safir@gmail.com", "etudiant", "FST tanga, wald rabat"));
            contacts.add(new Contact(null, "Hamza", "el mouratbit", "0688102932", "hamza.mouratbit@gmail.com", "etudiant", "FST tanga, wald agadir"));
            contacts.add(new Contact(null, "Anass", "el mouratbit", "0612993029", "anass.mouratbit@gmail.com", "etudiant", "FST media, info,concoure cycle ingenieur"));
            contacts.add(new Contact(null, "Anass", "barakat", "0671983021", "anass.barakat@gmail.com", "etudiant", "EST oujda, info,concoure LP big data"));
            contacts.add(new Contact(null, "Karim", "zahri", "0612832109", "karim.zahri@gmail.com", "developpeur", "SQLI oujda, expert php, symfony"));
            contacts.add(new Contact(null, "Jawad", "noaami", "0667128892", "jawad.noaami@gmail.com", "developpeur", "SQLI oujda, expert php, symfony et laravel"));
            contacts.add(new Contact(null, "Farid", "zemmam", "0677238918", "farid.zemmam@gmail.com", "developpeur", "SQLI oujda, expert JS, TS, angular"));
            contacts.add(new Contact(null, "Naima", "kharbach", "0661827361", "naima.kharbach@gmail.com", "developpeuse", "SQLI oujda, expert JS, SCSS et SASS"));

            databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    dao.insertAll(contacts);
                }
            });
        }
    };
}
