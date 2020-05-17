package com.ennbou.contact.data;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {

    private ContactRepository repo;
    private LiveData<List<Contact>> contacts;

    public ContactViewModel(Application application) {

        super(application);
        repo = new ContactRepository(application);
        contacts = repo.getAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return contacts;
    }

    public void deleteContact(Contact contact){
        repo.deleteContact(contact);
    }

    public void insertContact(Contact contact) {
        repo.insertContact(contact);
    }

}
