package com.ennbou.contact.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;


public class EditContactVM extends AndroidViewModel {

    private ContactRepository repo;
    private MutableLiveData<Contact> contact;

    public EditContactVM(@NonNull Application application) {
        super(application);
        repo = new ContactRepository(application);
        contact = new MutableLiveData<>();
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
