package com.ennbou.contact.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactRepository {

    private ContactDao dao;
    private LiveData<List<Contact>> contacts;
    private LiveData<Contact> contact;

    public ContactRepository(Application application) {
        dao = MyDataBase.getDataBase(application.getApplicationContext()).contactDao();
        contacts = dao.getAll();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return contacts;
    }

    public LiveData<Contact> getContact(Long id) {
        return dao.getById(id);
    }

    public void insertContact(Contact contact) {
        MyDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(contact);
            }
        });
    }


    public void updateContact(Contact contact) {
        MyDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.update(contact);
            }
        });
    }

    public void deleteContact(Contact contact) {
        MyDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(contact);
            }
        });
    }

}
