package com.ennbou.contact.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM Contact WHERE id = :id")
    public LiveData<Contact> getById(Long id);

    @Query("SELECT * FROM Contact ORDER BY first_name COLLATE NOCASE ASC")
    public LiveData<List<Contact>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insert(Contact contact);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Contact> contacts);

    @Delete
    public void delete(Contact contact);

    @Update
    public void update(Contact contact);
}
