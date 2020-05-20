package com.ennbou.contact.data;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {

    private ContactRepository repo;
    private LiveData<List<Contact>> contacts;
    private MutableLiveData<Contact> contact;

    public ContactViewModel(Application application) {

        super(application);
        repo = new ContactRepository(application);
        contacts = repo.getAllContacts();
        contact = new MutableLiveData<>();
        setContact(new Contact());
    }

    public LiveData<List<Contact>> getAllContacts() {
        return contacts;
    }

    public void deleteContact(Contact contact) {
        repo.deleteContact(contact);
    }

    public void insertContact(Contact contact) {
        repo.insertContact(contact);
    }


    public void setContact(Contact contact) {
        this.contact.setValue(contact);
    }

    public void saveContact(Contact contact) {
        repo.updateContact(contact);
    }

    public LiveData<Contact> getContact() {
        return contact;
    }
}
